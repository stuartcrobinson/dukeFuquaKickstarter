import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.google.gson.stream.JsonReader;

import org.apache.commons.io.FileUtils;
import org.json.JSONObject;

import java.io.File;
import java.io.IOException;
import java.nio.charset.StandardCharsets;

public class JsonFilesUpdater {
  static Genderer g = new Genderer();

  public static void main(String[] asdf) throws IOException {
//String firstName = "hi-my-name-is";
//
//    firstName = firstName.split("-")[0];
//
//    System.out.println(firstName);
//    System.exit(0);

    File jsonsDir = new File("/Users/stuart.robinson/beccakickstartblubs/beccaKickstarterOutputSuccess");
//    File jsonsDir = new File("/Users/stuart.robinson/beccakickstartblubs/beccaKickstarterOutputSuccessPrepared");

    String jsonDirNewFileStr = "/Users/stuart.robinson/beccakickstartblubs/beccaKickstarterOutputSuccessPrepared";
    File jsonsDirNew = new File(jsonDirNewFileStr);

    jsonsDirNew.mkdir();

    File[] jsonFiles = jsonsDir.listFiles();

    int count = 0;
    for (File file : jsonFiles) {

      System.out.println(count++ + "  ##");
      String filename = file.getName();
      if (filename.startsWith(".")){
        continue;
      }

      String fileStr = FileUtils.readFileToString(file, StandardCharsets.UTF_8);
      System.out.println(filename);

//      JsonReader.setLenient(true);
      JsonParser jsonParser = new JsonParser();

      JsonObject jo = (JsonObject) jsonParser.parse(fileStr);

      jo = updateJsonWithGendersAndPitchLength(jo);

      FileUtils.writeStringToFile(new File(jsonDirNewFileStr + "/" + filename), new JSONObject(jo.toString()).toString(2), StandardCharsets.UTF_8);


    }

  }

  private static JsonObject updateJsonWithGendersOfHyphenatedNames(JsonObject jo) {


    String firstName = jo.get("creator").getAsJsonObject().get("name").getAsString().split(" ")[0];

    firstName = firstName.split("-")[0];
    String genderCode = g.getGender(firstName).toString();

    jo.remove("genderCode");
    jo.addProperty("genderCode", genderCode);

    return jo;
  }

  private static JsonObject updateJsonWithGendersAndPitchLength(JsonObject jo) {


    String firstName = jo.get("creator").getAsJsonObject().get("name").getAsString().split(" ")[0];

    System.out.println(firstName);

    firstName = firstName.split("-")[0];


    System.out.println(firstName);

    String genderCode = g.getGender(firstName).toString();
    jo.addProperty("genderCode", genderCode);

    String pitch = jo.get("pitch").getAsString();

    pitch = removeNewlines(pitch);

    int pitchLen = pitch.split(" ").length;
    jo.addProperty("pitchWordCount", pitchLen);

//      FileUtils.writeStringToFile(new JSONObject(jo.toString()).toString(2), StandardCharsets.UTF_8);
    System.out.println(genderCode);
    System.out.println(pitchLen);

    return jo;
  }

  private static String removeNewlines(String pitch) {
    return pitch.replaceAll("\n", " ").replaceAll("  ", " ").replaceAll("  ", " ").replaceAll("  ", " ");
  }

//
//  import json
//  from pprint import pprint
//
//  from os import listdir
//  from os.path import isfile, join
//
//import gender_guesser.detector as gender
//
//      d = gender.Detector()
//  print(d.get_gender(u"Bob"))
//
//  mypath = '.'
//
//  onlyfiles = [f for f in listdir(mypath) if isfile(join(mypath, f))]
//
//      for f in listdir(mypath):
//  data = json.load(open(f))
//  pitch = data['pitch'].replace("\n", " ").replace("  ", " ").replace("  ", " ")
//  pitchLen = len(pitch.split(" "))
//  print("pitchLen")
//  print(pitchLen)
//  data['pitchWordCount'] = pitchLen
//      firstName = data['creator']['name'].split(" ")[0]
//  gender = d.get_gender(firstName)
//  data['genderGuess'] = gender
//  with open('data.txt', 'w') as outfile:
//      json.dump(data,  f)
//  print(pitchLen, firstName, gender)
//
//

}
