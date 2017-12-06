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
import java.time.Instant;
import java.time.ZoneId;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class Main {
  public static void main(String[] asdf) throws IOException, InterruptedException, XPathExpressionException {

    //convert epoch timestamps to readable dates yearMoDay.  times NOT in millis!

    downloadJsonFiles();

    System.exit(0);
//
//    new File("../beccaKickstarterOutputSuccess").mkdir();
//    new File("../beccaKickstarterOutputFail").mkdir();

    Map<String, JsonObject> map_new_scrId_jo = getNewMap();
    Map<String, String> map_old_scrId_line = getOldMap();

    String oldHeader = getOldHeader();
    String newHeader = getNewHeader();

    String megaFileStr = oldHeader + newHeader + "\n";
    String updatedOld = oldHeader + "pitch\n";
    String onlyNewStuff = newHeader + "\n";
    String tiny = "";

    // goal, pledge, creator, creatorImageUrl,

    /*
    Ask amount, pledge amount, whether goal was met
We have to code the gender of he entrepreneur manually
     */

    for (String scrId : map_old_scrId_line.keySet()) {

    }
  }

  private static String getDate(String epochSeconds) {
    return Instant.ofEpochSecond(Long.parseLong(epochSeconds)).atZone(ZoneId.systemDefault()).toLocalDate().toString();
  }
    /*
    now read json files and write to single tab delimited text file

    "id":2114275778,
"photo":{
   "key":"assets/011/516/963/e2a7be
   "full":"https://ksr-ugc.imgix.ne
   "ed":"https://ksr-ugc.imgix.net/
   "med":"https://ksr-ugc.imgix.net
   "little":"https://ksr-ugc.imgix.
   "small":"https://ksr-ugc.imgix.n
   "thumb":"https://ksr-ugc.imgix.n
   "1024x576":"https://ksr-ugc.imgi
   "1536x864":"https://ksr-ugc.imgi
},
"name":"The Keyboard Waffle Iron",
"blurb":"The Keyboard Waffle Iron I
"goal":50000.0,
"pledged":66685.0,
"state":"successful",
"slug":"the-keyboard-waffle-iron",
"disable_communication":false,
"country":"US",
"currency":"USD",
"currency_symbol":"$",
"currency_trailing_code":true,
"deadline":1419483660,
"state_changed_at":1419483660,
"created_at":1367463424,
"launched_at":1416919663,
"staff_pick":true,
"is_starrable":false,
"backers_count":850,
"static_usd_rate":1.0,
"usd_pledged":"66685.0",
"converted_pledged_amount":66685,
"fx_rate":1.0,
"current_currency":"USD",
"usd_type":"domestic",
"creator":{
   "id":1304060422,
   "name":"Chris Dimino, Designer",
   "is_registered":true,
   "chosen_currency":null,
   "avatar":{
      "thumb":"https://ksr-ugc.imgi
      "small":"https://ksr-ugc.imgi
      "medium":"https://ksr-ugc.img

 "location":{
      "id":12589335,
      "name":"Brooklyn",
      "slug":"brooklyn-ny",
      "short_name":"Brooklyn, NY",
      "displayable_name":"Brooklyn, NY",
      "localized_name":"Brooklyn",
      "country":"US",
      "state":"NY",

        },
   "category":{
      "id":28,
      "name":"Product Design",

 "urls":{
      "web":{
         "project":"https:/
     */


  private static void downloadJsonFiles() throws IOException {

//    String csvFile = "/Users/stuart.robinson/beccakickstartblubs/2017_clean1.txt";
    String csvFile = "2017_clean1.txt";

    List<String> linesMaster = Files.readAllLines(new File(csvFile).toPath());

//    FileUtils.deleteDirectory(new File("../beccaKickstarterOutputSuccess"));
    FileUtils.deleteDirectory(new File("../beccaKickstarterOutputFail"));

    new File("../beccaKickstarterOutputSuccess").mkdir();
    new File("../beccaKickstarterOutputFail").mkdir();

    File[] successes = new File("../beccaKickstarterOutputSuccess").listFiles();

    List<String> succeededScrIds = Arrays.stream(successes).map(File::getName).collect(Collectors.toList());
//    System.out.println(succeededScrIds);

//    System.exit(0);

    System.out.println(linesMaster.size());
    System.out.println(succeededScrIds.size());
//        System.exit(0);







    new Thread(() -> {
      try {
        saveProjectsToJsonFiles(linesMaster.subList(0,14000), succeededScrIds);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (XPathExpressionException e) {
        e.printStackTrace();
      }
    }).start();


    new Thread(() -> {
      try {
        saveProjectsToJsonFiles(linesMaster.subList(14000,22000), succeededScrIds);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (XPathExpressionException e) {
        e.printStackTrace();
      }
    }).start();


    new Thread(() -> {
      try {
        saveProjectsToJsonFiles(linesMaster.subList(22000,39000), succeededScrIds);
      } catch (IOException e) {
        e.printStackTrace();
      } catch (InterruptedException e) {
        e.printStackTrace();
      } catch (XPathExpressionException e) {
        e.printStackTrace();
      }
    }).start();

//    System.exit(0);
//
//
//
//
//
//
//    try {
//      saveProjectsToJsonFiles(linesMaster, succeededScrIds);
//    } catch (InterruptedException e) {
//      System.out.println("failed " + e);
//      e.printStackTrace();
//    } catch (XPathExpressionException e) {
//      System.out.println("failed " + e);
//      e.printStackTrace();
//    }
//
//    int count = 0;
//    for (List<String> lines : Lists.partition(linesMaster, 500)) {
//
//      System.out.println("parition: " + count++);
////      for (String line : lines) {
////        count++;
////      }
////      saveProjectsToJsonFiles(lines);
//
//      new Thread(() -> {
//        try {
//          saveProjectsToJsonFiles(lines, succeededScrIds);
//        } catch (IOException e) {
//          e.printStackTrace();
//        } catch (InterruptedException e) {
//          e.printStackTrace();
//        } catch (XPathExpressionException e) {
//          e.printStackTrace();
//        }
//      }).start();
//    }
//    System.out.println(count);

  }

  private static void saveProjectsToJsonFiles(List<String> lines, List<String> succeededScrIds) throws IOException, InterruptedException, XPathExpressionException {

    int c = -1;
    for (String lineStr : lines) {

      boolean skipThisOne = false;
      c += 1;
//      if (c == 0) {
//        continue;
//      }
//
//      if (c < 136) {
//        continue;
//      }
//      if (c > 1000) {
//        break;
//      }

      String[] line = lineStr.split("\t");

//      System.out.println(lineStr);

      String scrId = line[0];

      try {

        if (succeededScrIds.contains(scrId)) {
//          System.out.println("returning, already downloaded " + scrId);
//          System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
          continue;
        }
        String _title = line[3];
        String _blurb = line[4];
        String url = line[17];


        System.out.println("here1 " + scrId);
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

          System.out.println("here2");
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
          System.out.println("------------------------------------------------------------------------------------------------------------------------------------");


        } catch (ArrayIndexOutOfBoundsException fefew) {
          System.out.println("failed for " + _title + ", " + _blurb + ", " + url + ", " + bingSearchUrl);
          fefew.printStackTrace();
          FileUtils.writeStringToFile(new File("../beccaKickstarterOutputFail/" + scrId), "", StandardCharsets.UTF_8);
          System.out.println("------------------------------------------------------------------------------------------------------------------------------------");

        }
      } catch (Exception e) {

        System.out.println("failed for line " + lineStr);
        e.printStackTrace();
        FileUtils.writeStringToFile(new File("../beccaKickstarterOutputFail/" + scrId), "", StandardCharsets.UTF_8);
        System.out.println("------------------------------------------------------------------------------------------------------------------------------------");

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

  public static Map<String, JsonObject> getNewMap() throws IOException {

    File[] successes = new File("../beccaKickstarterOutputSuccess").listFiles();

    Map<String, JsonObject> map_new_scrId_line = new HashMap();

    for (File successFile : successes) {
      String json = FileUtils.readFileToString(successFile, StandardCharsets.UTF_8);

      JsonParser jsonParser = new JsonParser();
      JsonObject jo = (JsonObject) jsonParser.parse(json);

      String scrId = jo.get("scrId").getAsString();
      String name = jo.get("name").getAsString();
      String blurb = jo.get("blurb").getAsString();
      String goal = jo.get("goal").getAsString();
      String pledged = jo.get("pledged").getAsString();
      String backers_count = jo.get("backers_count").getAsString();
      String state = jo.get("state").getAsString();
      String currency_symbol = jo.get("currency_symbol").getAsString();
      String currency = jo.get("currency").getAsString();
      String country = jo.get("country").getAsString();
      String creator = jo.get("creator").getAsJsonObject().get("name").getAsString();
      String creatorImgUrlMedium = jo.get("creator").getAsJsonObject().get("avatar").getAsJsonObject().get("medium").getAsString();
      String created_at = getDate(jo.get("created_at").getAsString());
      String launched_at = getDate(jo.get("launched_at").getAsString());
      String deadline = getDate(jo.get("deadline").getAsString());
      String location_city = jo.get("location").getAsJsonObject().get("localized_name").getAsString();
      String location_state = jo.get("location").getAsJsonObject().get("state").getAsString();
      String category = jo.get("category").getAsJsonObject().get("name").getAsString();
      String pitch = jo.get("pitch").getAsString().replaceAll("\\n", " ").replaceAll("  ", " ").replaceAll("  ", " ").replaceAll("  ", " ");

      String line = name + "\t" +
                    blurb + "\t" +
                    goal + "\t" +
                    pledged + "\t" +
                    backers_count + "\t" +
                    state + "\t" +
                    currency_symbol + "\t" +
                    currency + "\t" +
                    country + "\t" +
                    creator + "\t" +
                    creatorImgUrlMedium + "\t" +
                    created_at + "\t" +
                    launched_at + "\t" +
                    deadline + "\t" +
                    location_city + "\t" +
                    location_state + "\t" +
                    category + "\t" +
                    pitch;

      map_new_scrId_line.put(scrId, jo);
    }
    return map_new_scrId_line;
  }

  public static Map<String, String> getOldMap() throws IOException {

    String csvFile = "2017_clean1.txt";

    List<String> linesOld = Files.readAllLines(new File(csvFile).toPath());

    Map<String, String> map_new_scrId_line = new HashMap();

    for (String lineStr : linesOld) {

      String[] lineAr = lineStr.split("\t");

      System.out.println(lineStr);

      String scrId = lineAr[0];

      String lineWithoutScrId = "";

      for (int i = 1; i < lineAr.length; i++) {
        lineWithoutScrId += lineAr[i] + "\t";
      }

      map_new_scrId_line.put(scrId, lineWithoutScrId);
    }

    return map_new_scrId_line;
  }

  public static String getNewHeader() {
    return "name" + "\t" +
           "blurb" + "\t" +
           "goal" + "\t" +
           "pledged" + "\t" +
           "backers_count" + "\t" +
           "state" + "\t" +
           "currency_symbol" + "\t" +
           "currency" + "\t" +
           "country" + "\t" +
           "creator" + "\t" +
           "creatorImgUrlMedium" + "\t" +
           "created_at" + "\t" +
           "launched_at" + "\t" +
           "deadline" + "\t" +
           "location_city" + "\t" +
           "location_state" + "\t" +
           "category" + "\t" +
           "pitch\t";
  }

  public static String getOldHeader() {
    return "id\tkickstarter_id\tname\tblurb\tcreator\tfemale1male0\tNot sure\tmain_category\tsub_category\tlaunched_at\tdeadline\tstate\tgoal\tpledged\tpercentage_funded\tbackers_count\tweb_url\tcurrency\tcurrency_symbol\thas_accurate_category\tcreated_at\tupdated_at\t";
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