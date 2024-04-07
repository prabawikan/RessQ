package Controller;

import java.io.IOException;
import java.net.ConnectException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;

import Data.NewsData;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class CardNewsController {

    @FXML
    private AnchorPane cardNews;

    @FXML
    private Text TextTitle;

    @FXML
    public Button buttonRead;

    @FXML
    private ImageView imageView;

    private String content;
    private NewsData newsData;

    private Image image;
    Connection connect;
    PreparedStatement statement;
    ResultSet result;

    public void setData(NewsData newsData) {
        this.newsData = newsData;

        TextTitle.setText(newsData.getTitle());
        content = newsData.getContent();

        String path = "File:" + newsData.getImage();
        image = new Image(path, 155, 230, false, true);
        imageView.setImage(image);
    }

    private Node currentContent;

    private void clearContent() {
        if (currentContent != null) {
            cardNews.getChildren().remove(currentContent);
            currentContent = null;
        }
    }

    @FXML
    private void handleButtonRead(ActionEvent event) {
        try {
            clearContent();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/Menu/NewsContent.fxml"));
            AnchorPane loadedPane = loader.load();

            cardNews.getChildren().setAll(loadedPane);
            currentContent = loadedPane;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
