import com.google.common.collect.Lists;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.FileUtils;
import org.apache.commons.lang3.StringEscapeUtils;
import org.json.JSONObject;
import org.w3c.dom.Document;
import org.w3c.dom.Node;

import java.io.File;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.URLEncoder;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.util.List;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class Main {
  public static void main(String[] asdf) throws IOException, InterruptedException, XPathExpressionException {

//    String csvFile = "/Users/stuart.robinson/beccakickstartblubs/2017_clean1.txt";
    String csvFile = "2017_clean1.txt";

    List<String> linesMaster = Files.readAllLines(new File(csvFile).toPath());

    FileUtils.deleteDirectory(new File("../beccaKickstarterOutputSuccess"));
    FileUtils.deleteDirectory(new File("../beccaKickstarterOutputFail"));

    new File("../beccaKickstarterOutputSuccess").mkdir();
    new File("../beccaKickstarterOutputFail").mkdir();


    for (List<String> lines : Lists.partition(linesMaster, 1000)) {

//      saveProjectsToJsonFiles(lines);

      new Thread(() -> {
        try {
          saveProjectsToJsonFiles(lines);
        } catch (IOException e) {
          e.printStackTrace();
        } catch (InterruptedException e) {
          e.printStackTrace();
        } catch (XPathExpressionException e) {
          e.printStackTrace();
        }
      }).start();
    }

  }

  private static void saveProjectsToJsonFiles(List<String> lines) throws IOException, InterruptedException, XPathExpressionException {

    int c = -1;
    for (String lineStr : lines) {
      System.out.println("------------------------------------------------------------------------------------------------------------------------------------");

      boolean skipThisOne = false;
      c += 1;
      if (c == 0) {
        continue;
      }

      if (c < 136) {
        continue;
      }
      if (c > 1000) {
        break;
      }

      String[] line = lineStr.split("\t");

      System.out.println(lineStr);

      String scrId = line[0];
      String _title = line[3];
      String _blurb = line[4];
      String url = line[17];

      System.out.println(url);

      String source = null;
      Document doc = null;
      String bingSearchUrl = null;
      try {
        source = HttpDownloadUtility.getPageSource(url);
        doc = HttpDownloadUtility.getWebpageDocument_fromSource(source);
        System.out.println("Succeeded first time");
      } catch (MalformedURLException e) {

        System.out.println("in catch");
        //search bing to get actual url

        int wordsToRemove = 0;

        boolean failed = false;
        do {
          try {
            bingSearchUrl = getBingUrl(_title, _blurb, wordsToRemove++);

            System.out.println("_title:");
            System.out.println(_title);
            System.out.println("bingSearchUrl:");
            System.out.println(bingSearchUrl);

            String bingResultsSource = HttpDownloadUtility.getPageSource(bingSearchUrl);

            bingResultsSource = bingResultsSource.replace("Search Results\"><li class=\"b_algo\"><h2><a href=\"", "stuartreplacer12345");

            url = bingResultsSource.split("stuartreplacer12345")[1].split("\"")[0];

            System.out.println("replacement url: " + url);
            if (url.equals("https://www.kickstarter.com/")) {
              System.out.println("wtf url: " + url);
              skipThisOne = true;
            }
            source = HttpDownloadUtility.getPageSource(url);
            doc = HttpDownloadUtility.getWebpageDocument_fromSource(source);
            failed = false;
          } catch (ArrayIndexOutOfBoundsException aioube) {
            System.out.println("ArrayIndexOutOfBoundsException !!");

            failed = true;
          } catch (JustGiveUpException e1) {
            System.out.println("JustGiveUpException !!");

            failed = false; //of course it actually failed, but we want to stop looping and just fail on this one.
            skipThisOne = true;
          }
        } while (failed);

      }
      if (skipThisOne) {
        System.out.println("Skipping");
        continue;
      }

      try {
        String test = source;
        test = test.split("window.current_project = \"")[1];
        test = test.split("}\";")[0].trim() + "}";
//    System.out.println(test);
        test = StringEscapeUtils.unescapeHtml4(test);

        test = test.replaceAll("\\\\\"", "\\\\\\\\\"");
//      System.out.println(test);

        JsonParser jsonParser = new JsonParser();
        JsonObject jo = (JsonObject) jsonParser.parse(test);

//      jo.addProperty("pitch", pitch);

        System.out.println(url);

        System.out.println(jo.get("id").getAsString());
        System.out.println(jo.get("name").getAsString());
        System.out.println(jo.get("blurb").getAsString());
        System.out.println(jo.get("goal").getAsString());
        System.out.println(jo.get("pledged").getAsString());
        System.out.println("doc:" + doc);
        String pitch = getPitch(doc);

        jo.addProperty("pitch", pitch);
        jo.addProperty("scrId", scrId);

        System.out.println(jo.toString());
        FileUtils.writeStringToFile(new File("../beccaKickstarterOutputSuccess/" + scrId), new JSONObject(jo.toString()).toString(2), StandardCharsets.UTF_8);


      } catch (ArrayIndexOutOfBoundsException fefew) {
        System.out.println("failed for " + _title + ", " + _blurb + ", " + url + ", " +  bingSearchUrl);
        fefew.printStackTrace();
        FileUtils.writeStringToFile(new File("../beccaKickstarterOutputFail/" + scrId), "", StandardCharsets.UTF_8);
      }
    }

  }

  private static String getBingUrl(String title, String blurb, int wordsToRemove) throws UnsupportedEncodingException, JustGiveUpException {
    String prefix = "https://www.bing.com/search?q=site:www.kickstarter.com+";
    String suffix = title.trim() + " " + blurb.trim();
    suffix = suffix.replaceAll("\\p{P}", " ").replaceAll("  ", " ").replaceAll("  ", " ").replaceAll("  ", " ").replaceAll("  ", " ");

    System.out.println("wordsToRemove:" + wordsToRemove);

    if (wordsToRemove > 0) {

      String[] result = removeUpToNBadCharacterWords(suffix, wordsToRemove);

      suffix = result[0];
      int removed = Integer.parseInt(result[1]);

      if (removed < wordsToRemove) {

        String[] suffixAr = suffix.split(" ");

        if (wordsToRemove >= suffixAr.length) {
          System.out.println("Just Giving Up");
          throw new JustGiveUpException();
        }

        suffix = "";

        for (int i = 0; i < suffixAr.length - wordsToRemove; i++) {
          suffix += suffixAr[i] + " ";
        }
      }
    }

    suffix = URLEncoder.encode(suffix, "UTF-8");

    return prefix + suffix;
  }

  private static String[] removeUpToNBadCharacterWords(String suffix, int maxToRemove) {

    String[] suffixAr = suffix.split(" ");

    String suffix2 = "";

    int removed = 0;

    for (String s : suffixAr) {
      if (removed < maxToRemove && s.matches(".*\\W.*")) {
        removed++;
      } else {
        suffix2 += s + " ";
      }
    }
    return new String[]{suffix2, removed + ""};
  }

  static XPath xPath = XPathFactory.newInstance().newXPath();

  private static String getPitch(Document doc) throws XPathExpressionException {
    String pitchXpath = "//div[@class='col col-8 description-container']";
    String pitch = getText(doc, pitchXpath);

    String[] pitchAr = pitch.split("\n");
    pitch = "";

    for (String line : pitchAr) {
      if (!line.trim().isEmpty()) {
        pitch += line + "\n";
      }
    }
    return pitch;
  }


  private static String getText(Document doc, String xpath) throws XPathExpressionException {
    Node node = (Node) xPath.evaluate(xpath, doc, XPathConstants.NODE);
    String text = StringEscapeUtils.unescapeHtml4(node.getTextContent().trim());

    text = text.replaceAll("\\n\\n\\n", "\n\n").replaceAll("\\n\\n\\n", "\n\n").replaceAll("\\n\\n\\n", "\n\n").replaceAll("\\n\\n\\n", "\n\n").replaceAll("\\n\\n\\n", "\n\n");
    return text;
  }

}



/*


  private static String getBingUrl(String title, String blurb, int wordsToRemove) throws UnsupportedEncodingException, JustGiveUpException {
    String prefix = "https://www.bing.com/search?q=site:www.kickstarter.com+";
    String suffix = title.trim() + " " + blurb.trim();
    suffix = suffix.replaceAll("\\p{P}", " ").replaceAll("  ", " ").replaceAll("  ", " ").replaceAll("  ", " ").replaceAll("  ", " ");

    System.out.println("wordsToRemove:" + wordsToRemove);

    if (wordsToRemove > 0) {

      String[] suffixAr = suffix.split(" ");
//
      if (wordsToRemove >= suffixAr.length) {
        System.out.println("Just Giving Up");
        throw new JustGiveUpException();
      }
//      String oldSuffix = suffix;

      suffix = "";

      int removed = 0;
      for (String s : suffixAr) {
        if (s.matches(".*\\W.*")) {
          removed++;
          if (removed == wordsToRemove){
            wordsToRemove = 0;
            break;
          }
          System.out.println("removed " + s);
        } else {
          suffix += s + " ";
        }
      }


      if (wordsToRemove > 0) {
        suffixAr = suffix.split(" ");

        suffix = "";

        for (int i = 0; i < suffixAr.length - wordsToRemove; i++) {
          suffix += suffixAr[i] + " ";
        }
      }
    }

    suffix = URLEncoder.encode(suffix, "UTF-8");

    return prefix + suffix;
  }

 */

/*

    System.exit(0);

    String[] testUrls = new String[]{
        "https://www.kickstarter.com/projects/1602977937/division-0?ref=popular",
        "https://www.kickstarter.com/projects/joehertler/joe-hertler-and-the-rainbow-seekers-pluto",
        "https://www.kickstarter.com/projects/1304060422/the-keyboard-waffle-iron?ref=discovery",
        "https://www.kickstarter.com/projects/1939243466/yeehaw-wand-experience-the-future-of-design?ref=city",
        "https://www.kickstarter.com/projects/2107389976/the-escape-floating-restaurant"};

    for (String url : testUrls) {

      String source = HttpDownloadUtility.getPageSource(url);
      Document doc = HttpDownloadUtility.getWebpageDocument_fromSource(source);

      String test = source;
      test = test.split("window.current_project = \"")[1].split("}\";")[0].trim() + "}";
//    System.out.println(test);
      test = StringEscapeUtils.unescapeHtml4(test);

      test = test.replaceAll("\\\\\"", "\\\\\\\\\"");
      System.out.println(test);

      JsonParser jsonParser = new JsonParser();
      JsonObject jo = (JsonObject) jsonParser.parse(test);

//      jo.addProperty("pitch", pitch);

      System.out.println(url);

      System.out.println(jo.get("id").getAsString());
      System.out.println(jo.get("name").getAsString());
      System.out.println(jo.get("blurb").getAsString());
      System.out.println(jo.get("goal").getAsString());
      System.out.println(jo.get("pledged").getAsString());
      String pitch = getPitch(doc);

      jo.addProperty("pitch", pitch);

      System.out.println(jo.toString());

    }
 */