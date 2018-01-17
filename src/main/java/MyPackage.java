public class MyPackage {
  public String scrId;
  public String name;
  public String blurb;
  public String goal;
  public String pledged;
  public String backers_count;
  public String state;
  public String currency_symbol;
  public String currency;
  public String country;
  public String creator;
  public String genderCode;
  public String creatorImgUrlMedium;
  public String created_at;
  public String launched_at;
  public String deadline;
  public String location_city;
  public String location_state;
  public String category;
  public String pitchLen;
  public String pitch;
  public String url;

  public MyPackage(String scrId, String name, String blurb, String goal, String pledged, String backers_count, String state, String currency_symbol, String currency, String country, String creator, String genderCode, String creatorImgUrlMedium, String created_at, String launched_at, String deadline, String location_city, String location_state, String category, String pitchLen, String pitch, String url) {

    this.scrId = scrId;
    this.name = name;
    this.blurb = blurb;
    this.goal = goal;
    this.pledged = pledged;
    this.backers_count = backers_count;
    this.state = state;
    this.currency_symbol = currency_symbol;
    this.currency = currency;
    this.country = country;
    this.creator = creator;
    this.genderCode = genderCode;
    this.creatorImgUrlMedium = creatorImgUrlMedium;
    this.created_at = created_at;
    this.launched_at = launched_at;
    this.deadline = deadline;
    this.location_city = location_city;
    this.location_state = location_state;
    this.category = category;
    this.pitchLen = pitchLen;
    this.pitch = pitch;
    this.url = url;

  }
}
