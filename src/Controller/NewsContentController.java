package Controller;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.util.ResourceBundle;

import Data.NewsData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class NewsContentController implements Initializable {

  @FXML
  private Text textTitle;

  @FXML
  private Text textContent;

  @FXML
  private ImageView imageView;

  @FXML
  private Button buttonBack;

  @FXML
  private AnchorPane newsContentPane;

  @FXML
  private AnchorPane newsContent;

  @FXML
  private ScrollPane scrollPane;

  private NewsData selectedNews;

  Connection connect;
  PreparedStatement statement;
  ResultSet result;
  private Node currentContent;

  private void clearContent() {
    if (currentContent != null) {
      newsContentPane.getChildren().remove(currentContent);
      currentContent = null;
    }
  }

  @FXML
  void handleButtonBack(ActionEvent event) {
    try {

      clearContent();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/Menu/News.fxml"));
      AnchorPane loadedPane = loader.load();

      // Mengganti konten di accountPane dengan konten baru
      newsContentPane.getChildren().setAll(loadedPane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  public void setSelectedNews(NewsData newsData) {
    this.selectedNews = newsData;
  }

  public void showData() {
    textTitle.setText(selectedNews.getTitle());
    textContent.setText(selectedNews.getContent());

    String path = "File:" + selectedNews.getImage();
    Image image = new Image(path);
    imageView.setImage(image);
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    scrollPane.setContent(textContent);

    scrollPane.setFitToWidth(true);
    scrollPane.setFitToHeight(true);
    scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
    scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
  }
}
