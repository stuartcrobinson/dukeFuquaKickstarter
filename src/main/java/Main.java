import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.JsonSyntaxException;

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
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class Main {
  public static void main(String[] asdf) throws IOException, InterruptedException, XPathExpressionException, JustGiveUpException {

    //convert epoch timestamps to readable dates yearMoDay.  times NOT in millis!
    //word count
//
//
//    String titleandblurb = " Cognito Jr | Fidget Spinner | by., 1 2 3 4 ! @ # $ % ^ & * ( SD,DF.sd g.sdf LMS Gear A fidget toy that is a compact reimagining of the original Cognito Made from high quality materials with quality finishes in the USA ";
//System.out.println(    prepareSuffix(titleandblurb, "", 0));
//    System.exit(0);
//

    //TODO make sure replacement URL is a kickstarter link.  get the first kickstarter link from bing results.  ?
    downloadJsonFiles();
    System.exit(0);
//
//    new File("../beccaKickstarterOutputSuccess").mkdir();
//    new File("../beccaKickstarterOutputFail").mkdir();

    Map<String, JsonObject> map_new_scrId_jo = getNewMap();
    Map<String, String> map_old_scrId_line = getOldMap();

    String oldHeader = getOldHeader();
    String newHeader = getNewHeader();

    //id is unique.

    String megaFileStr = oldHeader + newHeader + "\n";
    String updatedOld = oldHeader + "pitch\n";
    String onlyNewStuff = newHeader + "\n";
    String tiny = "id\tgoal\tcreator\tcreatorImageUrl\tgender\tgenderGuess\tgenderConfidence\tpitch\n";

    // goal, pledge, creator, creatorImageUrl,

    /*
    Ask amount, pledge amount, whether goal was met
We have to code the gender of he entrepreneur manually
     */

    for (String scrId : map_old_scrId_line.keySet()) {

      JsonObject jo = map_new_scrId_jo.get(scrId);
      String oldLine = map_old_scrId_line.get(scrId);

//      String scrId = jo.get("scrId").getAsString();
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
      String gender = getGenderTerm(creator);
      String genderEstimage = getGenderEstimate(creator);
      String genderConfidence = getGenderConfidence(creator);
      String creatorImgUrlMedium = jo.get("creator").getAsJsonObject().get("avatar").getAsJsonObject().get("medium").getAsString();
      String created_at = getDate(jo.get("created_at").getAsString());
      String launched_at = getDate(jo.get("launched_at").getAsString());
      String deadline = getDate(jo.get("deadline").getAsString());
      String location_city = jo.get("location").getAsJsonObject().get("localized_name").getAsString();
      String location_state = jo.get("location").getAsJsonObject().get("state").getAsString();
      String category = jo.get("category").getAsJsonObject().get("name").getAsString();
      String pitch = jo.get("pitch").getAsString().replaceAll("\\n", " ").replaceAll("  ", " ").replaceAll("  ", " ").replaceAll("  ", " ");

      String newFull = name + "\t" +
                       blurb + "\t" +
                       goal + "\t" +
                       pledged + "\t" +
                       backers_count + "\t" +
                       state + "\t" +
                       currency_symbol + "\t" +
                       currency + "\t" +
                       country + "\t" +
                       creator + "\t" +
                       gender + "\t" +
                       genderEstimage + "\t" +
                       genderConfidence + "\t" +
                       creatorImgUrlMedium + "\t" +
                       created_at + "\t" +
                       launched_at + "\t" +
                       deadline + "\t" +
                       location_city + "\t" +
                       location_state + "\t" +
                       category + "\t" +
                       pitch;

      megaFileStr += newFull + "\n";
      updatedOld += pitch + "\n";
      onlyNewStuff += newFull + "\n";

      String beccaId = oldLine.split("\t")[0];
      tiny += beccaId + "\t" +
              goal + "\t" +
              creator + "\t" +
              gender + "\t" +
              genderEstimage + "\t" +
              genderConfidence + "\t" +
              pitch;

      //TODO start here

      /*
      nohup mvn exec:java >/dev/null 2>&1 &
       */
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

//    List<String> linesMaster = new ArrayList();
//    linesMaster.add("scr473\t2418111\t2041162158\tPexel - Greenlight Phase\t\"Pexel is an endless RPG Dungeon Crawler with a heavy focus on Challenging Gameplay, Combats, Exploration, Trading, Stories and more...\"\tPulse Studios\t\t\tVideo Games\t\t42787.75\t42805.75\tdeleted\t100\t80\t0.8\t7\thttps://www.kickstarter.com/projects/pulsestudio/pexel?ref=popular\tEUR\t‰âÂ\t0\t42788.7784722222\t42805.7777777778");
//    linesMaster.add("scr668\t2647671\t1583074097\tPhantom Beats (Canceled)\t\"Hello, all. My name is Oladapo Olawuyi, born in Washington D.C., a Morgan State undergraduate student, and I want to create tunes.\"\tOladapo\t\t\tMusic\tMusic\t42944.5583333333\t42974.5583333333\tcanceled\t100\t65\t0.65\t3\thttps://www.kickstarter.com/projects/744604503/music-goals\tUSD\t$\t1\t42945.4173611111\t42974.5694444444");
//    linesMaster.add("scr966\t2488451\t1114904072\tDOGOLSHOOT frenetic shooter (FREE on Android)\tTotally FREE shoot em up for ANDROID.(and no microtransactions ! )The particularity of this game is that player control 8 planes!!!\tCholoco\t\t\tGames\tVideo Games\t42833.5923611111\t42850.5\tfailed\t180\t36\t0.2\t3\thttps://www.kickstarter.com/projects/957008919/dogolshoot-frenetic-shooter-free-on-android\tEUR\t‰âÂ\t1\t42834.4173611111\t42850.5284722222");
//    linesMaster.add("scr2055\t2722941\t1075671036\tStudio Ghibli Christmas Snow Globe Enamel Pins\t\"Spirited Away, Princess Mononoke, Howls Moving Castle, Kikis Delivery Service, Totoro Christmas Snow Globe Pins!\"\tSammy Stami\t\t\tFashion\tFashion\t43000.9534722222\t43020.9534722222\tlive\t300\t593\t1.97667\t42\thttps://www.kickstarter.com/projects/stami/studio-ghibli-christmas-snow-globe-enamel-pins?ref=popular\tGBP\tå£\t1\t43000.9659722222\t43011.4173611111");
//    linesMaster.add("scr8902\t2390991\t365593613\tMXRKED A Collection Of Inked Illustrations By Robin Holstein\tA collection of inked illustrations pulled from my imagination and marked onto paper for you to hold in your hands and enjoy!\tRobin Holstein\t\t\tComics\tComics\t42767.6979166667\t42802.6979166667\tsuccessful\t1900\t3417\t1.79842\t100\thttps://www.kickstarter.com/projects/1360224290/mxrked-a-collection-of-inked-illustrations-by-robi\tUSD\t$\t1\t42767.7777777778\t42812.0770833333");
//    linesMaster.add(
//        "scr12741\t2658871\t1770116980\tSome Place Far From Here - Watercolor Artbook\t\"A collection of the different worlds, feelings, and lessons that came from deciding to give 100% in a career in art.\"\tCindy Duong\t\t\tArt\tIllustration\t42953.5534722222\t42983.5534722222\tsuccessful\t3000\t14778\t4.926\t221\t\"\\\"\"title_for_backing_tier\\\"\":\\\"\"Early Supporter ($50)\\\"\"\"\t\"\\\"\"title\\\"\":\\\"\"Early Supporter\\\"\"\"\t\"\\\"\"starts_at\\\"\":1502031600\"\t\"\\\"\"ends_at\\\"\":1502686800\"\t\"\\\"\"shipping_enabled\\\"\":true\"\t\"\\\"\"shipping_preference\\\"\":\\\"\"unrestricted\\\"\"\"");
//    linesMaster.add(
//        "scr13507\t2454311\t1703596104\tCognito Jr | Fidget Spinner | by LMS Gear\t\"A fidget toy that is a compact reimagining of the original Cognito. Made from high quality materials, with quality finishes in the USA.\"\tLingua Machining Solutions\t\t\tDesign\tProduct Design\t42811.9513888889\t42842\tsuccessful\t3500\t56505\t16.14429\t563\t\"\\\"\"title\\\"\":\\\"\"Cognito Jr Double Pack\\\"\"\"\t\"\\\"\"starts_at\\\"\":0\"\t\"\\\"\"ends_at\\\"\":0\"\t\"\\\"\"shipping_enabled\\\"\":true\"\t\"\\\"\"shipping_preference\\\"\":\\\"\"unrestricted\\\"\"\"\t\"\\\"\"shipping_summary\\\"\":\\\"\"Ships anywhere in the world\\\"\"\"");
//    linesMaster.add(
//        "scr14659\t2662041\t1906910891\tGramatik | Coffee Shop Selection On Vinyl\tCoffee Shop Selection is finally being released on vinyl! We're making this a one time only run!\tGramatik\t\t\tMusic\tElectronic Music\t42955.5583333333\t42976.5583333333\tsuccessful\t4000\t27380\t6.845\t665\t\"\\\"\"title\\\"\":\\\"\"Coffeeshop Selection Double Vinyl\\\"\"\"\t\"\\\"\"starts_at\\\"\":0\"\t\"\\\"\"ends_at\\\"\":0\"\t\"\\\"\"shipping_enabled\\\"\":true\"\t\"\\\"\"shipping_preference\\\"\":\\\"\"unrestricted\\\"\"\"\t\"\\\"\"shipping_summary\\\"\":\\\"\"Ships anywhere in the world\\\"\"\"");
//    linesMaster.add(
//        "scr12741\t2658871\t1770116980\tSome Place Far From Here - Watercolor Artbook\t\"A collection of the different worlds, feelings, and lessons that came from deciding to give 100% in a career in art.\"\tCindy Duong\t\t\tArt\tIllustration\t42953.5534722222\t42983.5534722222\tsuccessful\t3000\t14778\t4.926\t221\t\"\\\"\"title_for_backing_tier\\\"\":\\\"\"Early Supporter ($50)\\\"\"\"\t\"\\\"\"title\\\"\":\\\"\"Early Supporter\\\"\"\"\t\"\\\"\"starts_at\\\"\":1502031600\"\t\"\\\"\"ends_at\\\"\":1502686800\"\t\"\\\"\"shipping_enabled\\\"\":true\"\t\"\\\"\"shipping_preference\\\"\":\\\"\"unrestricted\\\"\"\"");
//    linesMaster.add("");
//    linesMaster.add("");
//    linesMaster.add("");
//    linesMaster.add("");
//    linesMaster.add("");
//    linesMaster.add("");
//    linesMaster.add("");
//    linesMaster.add("");
//    linesMaster.add("");
//    linesMaster.add("");

//    FileUtils.deleteDirectory(new File("../beccaKickstarterOutputSuccess"));
    FileUtils.deleteDirectory(new File("../beccaKickstarterOutputFail"));

    new File("../beccaKickstarterOutputSuccess").mkdir();
    new File("../beccaKickstarterOutputFail").mkdir();

    Set<String> succeededScrIds = getSucceededScrIds();
//    System.out.println(succeededScrIds);

//    System.exit(0);

    System.out.println("lines: " + linesMaster.size());
    System.out.println("fnshd: " + succeededScrIds.size());

    Collections.shuffle(linesMaster);

    try {
      saveProjectsToJsonFiles(linesMaster);
    } catch (InterruptedException e) {
      System.out.println("failed " + e);
      e.printStackTrace();
    } catch (XPathExpressionException e) {
      System.out.println("failed " + e);
      e.printStackTrace();
    } catch (Exception e) {
      System.out.println("failed " + e);
      e.printStackTrace();
    }

  }

  private static void saveProjectsToJsonFiles(List<String> lines) throws IOException, InterruptedException, XPathExpressionException {

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

        if (getSucceededScrIds().contains(scrId)) {
//          System.out.println("returning, already downloaded " + scrId);
//          System.out.println("------------------------------------------------------------------------------------------------------------------------------------");
          continue;
        }
        String _title = line[3];
        String _blurb = line[4];
        String url = line[17];

        System.out.println("here1 " + scrId);
        System.out.println(url);

        String[] urlAndSourceAndBingUrl = getUrlAndSource(url, _title, _blurb);

        url = urlAndSourceAndBingUrl[0];
        String source = urlAndSourceAndBingUrl[1];
        String bingSearchUrl = urlAndSourceAndBingUrl[2];

        if (source.isEmpty()) {
          System.out.println("Skipping");
          new JustGiveUpException().printStackTrace();
          continue;
        }

        Document doc = HttpDownloadUtility.getWebpageDocument_fromSource(source);

        try {
          JsonObject jo = getJsonObjectFromSource(source);
//      jo.addProperty("pitch", pitch);

          System.out.println("here2");
          System.out.println(url);

          System.out.println(jo.get("id").getAsString());
          System.out.println(jo.get("name").getAsString());
          System.out.println(jo.get("blurb").getAsString());
          System.out.println(jo.get("goal").getAsString());
          System.out.println(jo.get("pledged").getAsString());
          System.out.println("doc:" + doc);

          String pitch;
          if (source.contains("subject of an intellectual property dispute and is currently unavailable")) {
            System.out.println("suspended for copyright dispute");
            pitch = "";
          } else if (source.contains("project has been removed from visibility at the request of the creator. It will remain permanently out of view")) {
            System.out.println("cancelled by creator");
            pitch = "";
          } else {
            pitch = getPitch(doc);
          }

          jo.addProperty("pitch", pitch);
          jo.addProperty("scrId", scrId);

          System.out.println(jo.toString());
          FileUtils.writeStringToFile(new File("../beccaKickstarterOutputSuccess/" + scrId), new JSONObject(jo.toString()).toString(2), StandardCharsets.UTF_8);
          System.out.println("------------------------------------------------------------------------------------------------------------------------------------");


        } catch (Exception fefew) {
          System.out.println("failed for line " + lineStr);
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

  private static String[] getUrlAndSource(String url, String _title, String _blurb) throws IOException, InterruptedException {

    String source = null;
    String bingSearchUrl = "";
    boolean skipThisOne = false;
    try {
      source = HttpDownloadUtility.getPageSource(url);
      System.out.println("Succeeded first time");
    } catch (MalformedURLException e) {

      System.out.println("in catch - failed to download for url=" + url);
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

          url = getReplacementURl(bingSearchUrl);

          System.out.println("replacement url: " + url);

          if (url.equals("https://www.kickstarter.com/")) {
            System.out.println("wtf url: " + url);
            skipThisOne = true;
            break;
          }
          source = HttpDownloadUtility.getPageSource(url);

          url = getMainPageUrl(source);

          source = HttpDownloadUtility.getPageSource(url);
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
      source = "";
    }
    return new String[]{url, source, bingSearchUrl};
  }

  private static JsonObject getJsonObjectFromSource(String source) {
    String test = source;
    test = test.split("window.current_project = \"")[1];
    test = test.split("}\";")[0].trim() + "}";
//    System.out.println(test);
    test = StringEscapeUtils.unescapeHtml4(test);

    test = test
        .replaceAll("\\\\\"", "\\\\\\\\\"")
        .replaceAll("\\\\\\\\\\\\\\\\\\\"", "\\\\\\\\\\\\\\\"");  //for https://www.kickstarter.com/projects/1360224290/mxrked-a-collection-of-inked-illustrations-by-robi

//      System.out.println(test);

    JsonParser jsonParser = new JsonParser();
    try {
      JsonObject jo = (JsonObject) jsonParser.parse(test);
      return jo;

    } catch (JsonSyntaxException mje) {
      System.out.println("failed parsing json. string:");
      System.out.println(test);
      throw mje;
    }
  }

  private static String getMainPageUrl(String source) {

    JsonObject jo = getJsonObjectFromSource(source);

    return jo.get("urls").getAsJsonObject().get("web").getAsJsonObject().get("project").getAsString();
  }

  private static String getReplacementURl(String bingSearchUrl) throws IOException, InterruptedException {

    String bingResultsSource = HttpDownloadUtility.getPageSource(bingSearchUrl);

    bingResultsSource = bingResultsSource.replace("Search Results\"><li class=\"b_algo\"><h2><a href=\"", "stuartreplacer12345");

    String url = bingResultsSource.split("stuartreplacer12345")[1].split("\"")[0];

    return url;
  }

  private static String getBingUrl(String title, String blurb, int wordsToRemove) throws UnsupportedEncodingException, JustGiveUpException {
    String prefix = "https://www.bing.com/search?q=site:www.kickstarter.com+";

    String suffix = prepareSuffix(title, blurb, wordsToRemove);

    suffix = URLEncoder.encode(suffix, "UTF-8");

    return prefix + suffix;
  }

  private static String prepareSuffix(String title, String blurb, int wordsToRemove) throws JustGiveUpException {
    String suffix = title.trim() + " " + blurb.trim();
    suffix = suffix.replaceAll("\\p{P}", " ").replaceAll("\\|", " ").replaceAll("\\^", " ").replaceAll("  ", " ").replaceAll("  ", " ").replaceAll("  ", " ").replaceAll("  ", " ");

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
    return suffix;
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
    String nodeTextContent = node.getTextContent();
//    System.out.println("nodeTextContent:");
//    System.out.println(nodeTextContent);
    String text = StringEscapeUtils.unescapeHtml4(nodeTextContent.trim());

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
      String gender = getGenderTerm(creator);
      String genderEstimage = getGenderEstimate(creator);
      String genderConfidence = getGenderConfidence(creator);
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

  /*
  gender stuff - use lists of boys and girls names and rates, and compare

  http://answers.google.com/answers/threadview/id/107201.html
  http://scrapmaker.com/view/names/male-names.txt
  https://www.ssa.gov/oact/babynames/limits.html
  https://names.mongabay.com/male_names_alpha.htm
  https://www.cs.cmu.edu/Groups/AI/areas/nlp/corpora/names/male.txt

  just use this - https://pypi.python.org/pypi/SexMachine/ - do in python - add values to json files
   */

  private static String getGenderConfidence(String creator) {
    return null;

  }

  private static String getGenderEstimate(String name) {
    return null;
  }

  private static String getGenderTerm(String name) {
    return null;
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
           "gender" + "\t" +
           "genderGuess" + "\t" +
           "genderConfidence" + "\t" +
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

  public static Set<String> getSucceededScrIds() {
    File[] successes = new File("../beccaKickstarterOutputSuccess").listFiles();
    Set<String> succeededScrIds = Arrays.stream(successes).map(File::getName).collect(Collectors.toSet());
    return succeededScrIds;
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