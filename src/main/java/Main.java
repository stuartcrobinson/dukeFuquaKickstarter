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


    //convert epoch timestamps to readable dates yearMoDay.  times NOT in millis! https://pypi.python.org/pypi/gender-guesser/
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
//    linesMaster.add("scr36055\t2410721\t1050467111\tMediacloud: Journalism by the people for the people (Canceled)\t\"Support the development of a self surveilling news website, based on grassroots journalism by the people for the people.\"\tOliver Schlumpf\t\t\tJournalism\tWeb\t42781.8631944444\t42841.8215277778\tcanceled\t80000\t0\t0\t0\thttps://www.kickstarter.com/projects/947656624/mediacloud-journalism-by-the-people-for-the-people\tEUR\t‰âÂ\t1\t42782.4180555556\t42812.0861111111");
//    linesMaster.add("scr29949\t2527881\t267703940\tVision Notes‰ã¢ - A Visualization Journal & Sketchbook\t\"Vision Notes‰ã¢ is a 7\\x10\\\"\" visualization journal & sketchbook featuring 150 pages of 70 lb. weight acid-free drawing paper.\"\"\"\t\"Future Mind Media, LLC\"\t\t\tPublishing\tArt Books\t42860\t42890\tdeleted\t24750\t0\t0\t0\thttps://www.kickstarter.com/projects/futuremindmedia/vision-notestm-a-visualization-journal-and-sketchb?ref=popular\tUSD\t$\t1\t42860.4173611111\t42890.0277777778");
//    linesMaster.add("scr4702\t2383061\t1364562319\tZero Patience Gaming Needs a Desktop\t\"My friend and I are about ready to launch our YouTube channel ,Zero Patience Gaming. The only thing we're missing is a Desktop for him!\"\tZero Patience Gaming\t\t\tGaming Hardware\t\t42761.9763888889\t42791.9763888889\tdeleted\t750\t0\t0\t0\thttps://www.kickstarter.com/projects/760155439/zero-patience-gaming-needs-a-desktop?ref=popular\tUSD\t$\t0\t42762.4590277778\t42791.9861111111");
//    linesMaster.add("scr6236\t2536811\t1918117328\tPRODUCIR CONTENIDO EROTICO PARA WEB DE PRODUCTORA GAY\tCampaÌ±a para ganar fondos y poder continuar produciendo videos pornograficos y fotos eroticas con modelos y actores gays para gays.\tIvan\t\t\tPhotography\tPeople\t42861.4236111111\t42921.4236111111\tdeleted\t1000\t10\t0.01\t1\thttps://www.kickstarter.com/projects/249017071/producir-contenido-erotico-para-web-de-productora?ref=popular\tEUR\t‰âÂ\t1\t42866.4180555556\t42921.4444444444");
//    linesMaster.add("scr28215\t2363391\t1858712793\tProtecting mask embedded with natural olfactory additives\t\"I am interested in developing respirator face masks embedded with an olfactory additive, such as menthol (like the menthol kleenex).\"\tTobias Foehr and Banan Al-Nasery\t\t\tTechnology\tWearables\t42747.9340277778\t42779.9576388889\tfailed\t20000\t0\t0\t0\thttps://www.kickstarter.com/projects/831231232/respirator-mask-protection-embedded-with-olfactory\tCHF\tFr \t1\t42749.4180555556\t42812.04375");
//    linesMaster.add("scr15342\t2354691\t619709625\tEscapsim\tAn upcoming indie movie about a girl who subconsciously and unknowingly creates a sentient entity of herself.\tMia Alexander\t1\t\tFilm & Video\t\t42742.8708333333\t42782.8708333333\tdeleted\t5000\t30\t0.006\t3\thttps://www.kickstarter.com/projects/1146476498/escapsim?ref=popular\tUSD\t$\t0\t42743.4583333333\t42782.9034722222");
//    linesMaster.add("scr6236\t2536811\t1918117328\tPRODUCIR CONTENIDO EROTICO PARA WEB DE PRODUCTORA GAY\tCampaÌ±a para ganar fondos y poder continuar produciendo videos pornograficos y fotos eroticas con modelos y actores gays para gays.\tIvan\t\t\tPhotography\tPeople\t42861.4236111111\t42921.4236111111\tdeleted\t1000\t10\t0.01\t1\thttps://www.kickstarter.com/projects/249017071/producir-contenido-erotico-para-web-de-productora?ref=popular\tEUR\t‰âÂ\t1\t42866.4180555556\t42921.4444444444");
//    linesMaster.add("scr2763\t2374401\t2135764341\tCrude humour youtube channel using video games as props\t\"A crude and immature adult humor channel using video games as a prop, focusing on interactions between adults in comedic scenarios.\"\tAHH! You're pissing me off...\t\t\tComedy\t\t42755.7409722222\t42785.7409722222\tdeleted\t500\t0\t0\t0\thttps://www.kickstarter.com/projects/ahhyourepissingmeoff/crude-humour-youtube-channel-using-video-games-as\tUSD\t$\t0\t42756.4583333333\t42812.05625");
//    linesMaster.add("scr7854\t2411991\t1316553835\tUse The HouseWife and forget the chore!\tThe HouseWife is an online modern company that helps everyone to find whatever they need for their household without leaving the house.\tGi\t\t\tWeb\t\t42774.8097222222\t42804.8097222222\tdeleted\t1500\t0\t0\t0\thttps://www.kickstarter.com/projects/thehousewife/use-the-housewife-and-forget-the-chore?ref=popular\tEUR\t‰âÂ\t0\t42783.4180555556\t42804.8201388889");
//    linesMaster.add("scr12685\t2587681\t880774022\t\"The Legend of Chip, A Novel\"\t\"I am publishing my first novel, The Legend of Chip: The Legend Begins. Please preorder to help me get started.\"\tStanley Campbell\t\t\tPublishing\tFiction\t42899.6173611111\t42919.6173611111\tfailed\t3000\t60\t0.02\t2\thttps://www.kickstarter.com/projects/1082403770/the-legend-of-chip-a-novel\tUSD\t42900.4173611111\t42919.6527777778");
//    linesMaster.add("scr33230\t2456971\t1100111323\tDAWSON HOLLOW\tThis music thing is so important. 15 years of touring and writing all for this moment.Let's change the world.\tDawson Hollow\t\t\tMusiIndie Rock\t42811.7125\t42845.6645833333\tdeleted\t40000\t20\t0.0005\t1\thttps://www.kickstarter.com/projects/1981246582/dawson-hollow?ref=popular\tUSD\t$\t1\t42814.4173611111\t42845.6944444444");
//    linesMaster.add("scr29949\t2527881\t267703940\tVision Notes‰ã¢ - A Visualization Journal & Sketchbook\t\"Vision Notes‰ã¢ is a 7\\x10\\\"\" visualization journal & sketchbook featuring 150 pages of 70 lb. weight acid-free drawing paper.\"\"\"\t\"Future Mind Media, LLC\"\t\t\tPublishing\tArt Books\t42860\t42890\tdeleted\t24750\t0\t0\t0\thttps://www.kickstarter.com/projects/futuremindmedia/vision-notestm-a-visualization-journal-and-sketchb?ref=popular\tUSD\t$\t1\t42860.4173611111\t42890.0277777778");
//    linesMaster.add("scr29190\t2625891\t1740389386\tMichl Heat pumps - the future of home heating\tMichl Heat Pumps are the latest type to supply a house with hot water and heating energy.\tSascha Michl\t\tTechnology\tTechnology\t42927.3770833333\t42972.3770833333\tfailed\t20000\t0\t0\t0\thttps://www.kickstarter.com/projects/1868386775/michl-heat-pumps-the-future-of-home-heating\tEUR\t‰âÂ\t1\t42928.4173611111\t42972.4027777778");
//    linesMaster.add("scr4702\t2383061\t1364562319\tZero Patience Gaming Needs a Desktop\t\"My friend and I are about ready to launch our YouTube channel ,Zero Patience Gaming. The only thing we're missing is a Desktop for him!\"\tZero Patience Gaming\t\t\tGaming Hardware\t\t42761.9763888889\t42791.9763888889\tdeleted\t750\t0\t0\t0\thttps://www.kickstarter.com/projects/760155439/zero-patience-gaming-needs-a-desktop?ref=popular\tUSD\t$\t0\t42762.4590277778\t42791.9861111111");
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
          System.out.println(scrId + " :)");
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
          }else if (source.contains("this project is no longer available")) {
            System.out.println("this project is no longer available");
            pitch = "";
          } else {
            pitch = getPitch(doc);
          }
          //this project is no longer available

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