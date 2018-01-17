import java.io.File;
import java.nio.file.Files;
import java.text.Normalizer;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

public class Genderer {

  private Map<String, Float> map_maleName_rate;
  private Map<String, Float> map_femaleName_rate;

  private Set<String> maleNames;
  private Set<String> femaleNames;

  public static void main(String[] asdf) {
    Genderer g = new Genderer();
    System.out.println(g.getGender("tom"));
    System.out.println(g.getGender("sarah"));
    System.out.println(g.getGender("pauley"));
    System.out.println(g.getGender("jordan"));
    System.out.println(g.getGender("chris"));
    System.out.println(g.getGender("Renée"));
    System.out.println(g.getGender("Zoë"));
    System.out.println(g.getGender("Ruairí"));
    System.out.println(g.getGender("Mathéo"));
    System.out.println(g.getGender("Mátyás"));
  }

  public static String clean(String string){

    string = Normalizer.normalize(string, Normalizer.Form.NFD);
    string = string.replaceAll("[^\\p{ASCII}]", "");
    string = string.toLowerCase();

return string;
  }

  public Genderer() {

    try {

      map_maleName_rate = new HashMap<>();
      map_femaleName_rate = new HashMap<>();

      List<String> maleNamesRates = Files.readAllLines(new File("maleNamesAndRates.txt").toPath());
      List<String> femaleNamesRates = Files.readAllLines(new File("femaleNamesAndRates.txt").toPath());
      maleNames = new HashSet(Files.readAllLines(new File("maleNames.txt").toPath()));
      femaleNames = new HashSet(Files.readAllLines(new File("femaleNames.txt").toPath()));

      maleNames = maleNames.stream().map(name -> clean(name)).collect(Collectors.toSet());
      femaleNames = femaleNames.stream().map(name -> clean(name)).collect(Collectors.toSet());

      for (String line : maleNamesRates) {
        String[] lineAr = line.split("\t");
        String name = lineAr[0].toLowerCase();
        Float rate = Float.parseFloat(lineAr[1]);
        map_maleName_rate.put(name, rate);
      }

      for (String line : femaleNamesRates) {
        String[] lineAr = line.split("\t");
        String name = lineAr[0].toLowerCase();
        Float rate = Float.parseFloat(lineAr[1]);
        map_femaleName_rate.put(name, rate);
      }
    } catch (Exception e) {
      throw new RuntimeException(e);
    }
  }

  public static enum GenderCode {M, F, PM, PF, U}


  public GenderCode getGender(String firstName) {
    firstName = clean(firstName);

//    System.out.println("-------------------------------------------------");
//
//    System.out.println(firstName);
//    System.out.println(firstName);
    boolean isMale = maleNames.contains(firstName);
    boolean isFemale = femaleNames.contains(firstName);

    Float maleRate = null, femaleRate = null;

    if (map_maleName_rate.containsKey(firstName)) {
      maleRate = map_maleName_rate.get(firstName);
    }
    if (map_femaleName_rate.containsKey(firstName)) {
      femaleRate = map_femaleName_rate.get(firstName);
    }
//
//    System.out.println(isMale);
//    System.out.println(isFemale);
//    System.out.println(maleRate);
//    System.out.println(femaleRate);

    if (maleRate == null && femaleRate == null) {
      if (isMale && !isFemale) {
        return GenderCode.M;
      }
      if (!isMale && isFemale) {
        return GenderCode.F;
      }
      return GenderCode.U;
    }
    if (maleRate != null && femaleRate == null) {
      return GenderCode.M;
    }
    if (maleRate == null && femaleRate != null) {
      return GenderCode.F;
    }
    if (maleRate != null && femaleRate != null) {
      if (maleRate > femaleRate) {
        return GenderCode.PM;
      }
      if (maleRate < femaleRate) {
        return GenderCode.PF;
      }
      if (maleRate.equals(femaleRate)) {
        return GenderCode.U;
      }
    }
    throw new RuntimeException("shouldn't have gotten here");
  }
}
