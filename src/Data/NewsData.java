package Data;

public class NewsData {
  private Integer id;
  private String title;
  private String content;
  private String image;

  public NewsData(Integer id, String title, String content, String image) {
    this.title = title;
    this.content = content;
    this.image = image;
    this.id = id;
  }

  public String getTitle() {
    return title;
  }

  public String getImage() {
    return image;
  }

  public String getContent() {
    return content;
  }

  public Integer getId() {
    return id;
  }
}
