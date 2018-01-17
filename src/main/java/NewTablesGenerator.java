import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import org.apache.commons.io.FileUtils;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.time.Instant;
import java.time.ZoneId;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NewTablesGenerator {


  public static void main(String[] asdf) throws IOException {

    Map<String, MyPackage> map_new_scrId_mypack = getNewMap();
    Map<String, String> map_old_scrId_line = getOldMap();


    /*

    You'll need an HTML5 capable browser to see this content. Play Replay with sound Play with sound 00:00 00:00
     */
    //check for missing values in map_new_scrId_mypack
    for (String scrId : map_old_scrId_line.keySet()) {
      if (!map_new_scrId_mypack.containsKey(scrId)) {
        System.out.println("missing! " + scrId);
      }
    }

    Map<String, String> map_scrId_beccaId = get_map_scrId_beccaId(map_old_scrId_line);

    //map_scrId_kickstartId
    Map<String, String> map_scrId_kickstartId = get_map_scrId_kickstartId(map_old_scrId_line);

    //map_scrId_kickstartId

    String oldHeader = getOldHeader();
    String newHeader = "id_\t" + "kickstarterId_\t" + getNewHeader();
    String newNoPitchHeader = "id_\t" + "kickstarterId_\t" + getNewNoPitchHeader();

    //id is unique.

    String megaFileHeader = oldHeader + newHeader;
    String megaFileNoPitchHeader = oldHeader + newNoPitchHeader;
    String updatedOldHeader = oldHeader + "pitchWC\tpitch";
    String onlyNewStuffHeader = newHeader;
    String tinyHeader = "id\tgoal\tpledge\tcreator\tgenderGuess\tpitchWC";

    List<String> megaLines = new ArrayList<>();
    List<String> megaNoPitchLines = new ArrayList<>();
    List<String> updatedOldLines = new ArrayList<>();
    List<String> onlyNewStuffLines = new ArrayList<>();
    List<String> onlyNewStuffNoPitchLines = new ArrayList<>();
    List<String> tinyLines = new ArrayList<>();

    megaLines.add(megaFileHeader);
    megaNoPitchLines.add(megaFileNoPitchHeader);
    updatedOldLines.add(updatedOldHeader);
    onlyNewStuffLines.add(onlyNewStuffHeader);
    onlyNewStuffNoPitchLines.add(newNoPitchHeader);
    tinyLines.add(tinyHeader);

    // goal, pledge, creator, creatorImageUrl,

    /*
    Ask amount, pledge amount, whether goal was met
We have to code the gender of he entrepreneur manually
     */

    int count = 0;
    for (String scrId : map_old_scrId_line.keySet()) {
      System.out.println(count++ + scrId);

      MyPackage p = map_new_scrId_mypack.get(scrId);

      String oldLine = map_old_scrId_line.get(scrId);

      String newNoPitchLine = "", newLine = "", pitchLen = "", pitch = "", goal = "", pledged = "", creator = "", genderCode = "";

      String beccaId = map_scrId_beccaId.get(scrId);
      String kickstartId = map_scrId_kickstartId.get(scrId);


      newLine = beccaId + "\t" + kickstartId + "\t" + getNewDataLine(p);
      newNoPitchLine = beccaId + "\t" + kickstartId + "\t" +  getNewNoPitchDataLine(p);

      if (p != null) {
        pitchLen = p.pitchLen;
        pitch = p.pitch;
        goal = p.goal;
        pledged = p.pledged;
        creator = p.creator;
        genderCode = p.genderCode;
      }

      String megaLine = oldLine +  newLine;

      String megaNoPitchLine = oldLine +  newNoPitchLine;

      String updateOldLine = oldLine + pitchLen + "\t" + pitch;

      String onlyNewStuffLine = newLine;

      String tinyLine =
          beccaId + "\t" +
          goal + "\t" +
          pledged + "\t" +
          creator + "\t" +
          genderCode + "\t" +
          pitchLen;

      megaLines.add(megaLine);
      megaNoPitchLines.add(megaNoPitchLine);
      updatedOldLines.add(updateOldLine);
      onlyNewStuffLines.add(onlyNewStuffLine);
      onlyNewStuffNoPitchLines.add(newNoPitchLine);
      tinyLines.add(tinyLine);
    }

    File mega = new File("/Users/stuart.robinson/beccakickstartblubs/finalData/mega.txt");
    File megaNoPitch = new File("/Users/stuart.robinson/beccakickstartblubs/finalData/megaNoPitch.txt");
    File updatedOld = new File("/Users/stuart.robinson/beccakickstartblubs/finalData/updatedOld.txt");
    File onlyNew = new File("/Users/stuart.robinson/beccakickstartblubs/finalData/onlyNew.txt");
    File onlyNewNoPitch = new File("/Users/stuart.robinson/beccakickstartblubs/finalData/onlyNewNoPitch.txt");
    File tiny = new File("/Users/stuart.robinson/beccakickstartblubs/finalData/tiny.txt");

    new File("/Users/stuart.robinson/beccakickstartblubs/finalData").mkdirs();

    Files.write(mega.toPath(), megaLines);
    Files.write(megaNoPitch.toPath(), megaNoPitchLines);
    Files.write(updatedOld.toPath(), updatedOldLines);
    Files.write(onlyNew.toPath(), onlyNewStuffLines);
    Files.write(onlyNewNoPitch.toPath(), onlyNewStuffNoPitchLines);
    Files.write(tiny.toPath(), tinyLines);
  }

  private static Map<String,String> get_map_scrId_kickstartId(Map<String, String> map_old_scrId_line) {

    Map<String, String> map_scrId_beccaId = new HashMap();

    for (String scrId : map_old_scrId_line.keySet()) {
      String line = map_old_scrId_line.get(scrId);
      String beccaId = line.split("\t")[1];

      map_scrId_beccaId.put(scrId, beccaId);
    }

    return map_scrId_beccaId;
  }

  private static Map<String, String> get_map_scrId_beccaId(Map<String, String> map_old_scrId_line) {
    Map<String, String> map_scrId_beccaId = new HashMap();

    for (String scrId : map_old_scrId_line.keySet()) {
      String line = map_old_scrId_line.get(scrId);
      String beccaId = line.split("\t")[0];

      map_scrId_beccaId.put(scrId, beccaId);
    }

    return map_scrId_beccaId;
  }

  public static Map<String, String> getOldMap() throws IOException {

    String csvFile = "2017_clean1.txt";

    List<String> linesOld = Files.readAllLines(new File(csvFile).toPath());

    Map<String, String> map_new_scrId_line = new HashMap();

    for (String lineStr : linesOld) {

      String[] lineAr = lineStr.split("\t");

//      System.out.println(lineStr);

      String scrId = lineAr[0];

      String lineWithoutScrId = "";

      for (int i = 1; i < lineAr.length; i++) {
        lineWithoutScrId += lineAr[i] + "\t";
      }

      map_new_scrId_line.put(scrId, lineWithoutScrId);
    }

    return map_new_scrId_line;
  }

  public static Map<String, MyPackage> getNewMap() throws IOException {

    String jsonDirNewFileStr = "/Users/stuart.robinson/beccakickstartblubs/beccaKickstarterOutputSuccessPrepared";

    File[] successes = new File(jsonDirNewFileStr).listFiles();

    Map<String, MyPackage> map_new_scrId_line = new HashMap();

    System.out.println("reading and parsing prepared json files");

    int count = 0;
    for (File successFile : successes) {
      System.out.println(count++ + ". " + successFile.getName());
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
      String genderCode = jo.get("genderCode").getAsString();
      String creatorImgUrlMedium = jo.get("creator").getAsJsonObject().get("avatar").getAsJsonObject().get("medium").getAsString();
      String url = jo.get("urls").getAsJsonObject().get("web").getAsJsonObject().get("project").getAsString();
      String created_at = getDate(jo.get("created_at").getAsString());
      String launched_at = getDate(jo.get("launched_at").getAsString());
      String deadline = getDate(jo.get("deadline").getAsString());
      String location_city = jo.get("location").getAsJsonObject().get("localized_name").getAsString();
      String location_state = get(jo.get("location").getAsJsonObject().get("state"));
      String category = jo.get("category").getAsJsonObject().get("name").getAsString();
      String pitchLen = jo.get("pitchWordCount").getAsString();
      String pitch = jo.get("pitch").getAsString().replaceAll("\\n", " ").replaceAll("  ", " ").replaceAll("  ", " ").replaceAll("  ", " ");

      MyPackage p = new MyPackage(scrId, name, blurb, goal, pledged, backers_count, state, currency_symbol, currency, country, creator, genderCode, creatorImgUrlMedium, created_at, launched_at, deadline, location_city, location_state, category, pitchLen, pitch, url);

      map_new_scrId_line.put(scrId, p);
    }
    return map_new_scrId_line;
  }

  private static String get(JsonElement jsonElement) {

    try {
      return jsonElement.getAsString();
    } catch (UnsupportedOperationException e) {
      return "";
    }


  }

  private static String getNewDataLine(MyPackage p) {
    if (p == null) {
      return "";
    }
//
    String line = p.name + "\t" +
                  p.blurb + "\t" +
                  p.goal + "\t" +
                  p.pledged + "\t" +
                  p.backers_count + "\t" +
                  p.state + "\t" +
                  p.currency + "\t" +
                  p.country + "\t" +
                  p.location_city + "\t" +
                  p.location_state + "\t" +
                  p.creator + "\t" +
                  p.genderCode + "\t" +
                  p.creatorImgUrlMedium + "\t" +
                  p.url + "\t" +
                  p.created_at + "\t" +
                  p.launched_at + "\t" +
                  p.deadline + "\t" +
                  p.category + "\t" +
                  p.pitchLen + "\t" +
                  p.pitch;

    return line;
  }

  private static String getNewNoPitchDataLine(MyPackage p) {
    if (p == null) {
      return "";
    }
    String line = p.name + "\t" +
                  p.blurb + "\t" +
                  p.goal + "\t" +
                  p.pledged + "\t" +
                  p.backers_count + "\t" +
                  p.state + "\t" +
                  p.currency + "\t" +
                  p.country + "\t" +
                  p.location_city + "\t" +
                  p.location_state + "\t" +
                  p.creator + "\t" +
                  p.genderCode + "\t" +
                  p.creatorImgUrlMedium + "\t" +
                  p.url + "\t" +
                  p.created_at + "\t" +
                  p.launched_at + "\t" +
                  p.deadline + "\t" +
                  p.category + "\t" +
                  p.pitchLen + "\t";
    return line;
  }

  public static String getNewHeader() {
    return
        "name_" + "\t" +
        "blurb_" + "\t" +
        "goal_" + "\t" +
        "pledged_" + "\t" +
        "backers_count_" + "\t" +
        "state_" + "\t" +
        "currency_" + "\t" +
        "country_" + "\t" +
        "location_city_" + "\t" +
        "location_state_" + "\t" +
        "creator_" + "\t" +
        "genderGuess_" + "\t" +
        "creatorImgUrlMedium_" + "\t" +
        "url_" + "\t" +
        "created_at_" + "\t" +
        "launched_at_" + "\t" +
        "deadline_" + "\t" +
        "category_" + "\t" +
        "pitchWC_" + "\t" +
        "pitch_\t";
  }

  public static String getOldHeader() {
    return "id\tkickstarter_id\tname\tblurb\tcreator\tfemale1male0\tNot sure\tmain_category\tsub_category\tlaunched_at\tdeadline\tstate\tgoal\tpledged\tpercentage_funded\tbackers_count\tweb_url\tcurrency\tcurrency_symbol\thas_accurate_category\tcreated_at\tupdated_at\t";
  }

  private static String getDate(String epochSeconds) {
    return Instant.ofEpochSecond(Long.parseLong(epochSeconds)).atZone(ZoneId.systemDefault()).toLocalDate().toString();
  }

  public static String getNewNoPitchHeader() {
    return
        "name_" + "\t" +
        "blurb_" + "\t" +
        "goal_" + "\t" +
        "pledged_" + "\t" +
        "backers_count_" + "\t" +
        "state_" + "\t" +
        "currency_" + "\t" +
        "country_" + "\t" +
        "location_city_" + "\t" +
        "location_state_" + "\t" +
        "creator_" + "\t" +
        "genderGuess_" + "\t" +
        "creatorImgUrlMedium_" + "\t" +
        "url_" + "\t" +
        "created_at_" + "\t" +
        "launched_at_" + "\t" +
        "deadline_" + "\t" +
        "category_" + "\t" +
        "pitchWC_" + "\t";
  }
}
