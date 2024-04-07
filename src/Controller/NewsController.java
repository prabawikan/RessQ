package Controller;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Data.NewsData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ScrollPane;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;

public class NewsController implements Initializable {

    @FXML
    private Button buttonManageNews;

    @FXML
    private ScrollPane news_scrollPane;

    @FXML
    private GridPane newsGridPane;

    @FXML
    private AnchorPane newsPane;

    private Node currentContent;

    Connection connect;
    PreparedStatement statement;
    ResultSet result;

    private void clearContent() {
        if (currentContent != null) {
            newsPane.getChildren().remove(currentContent);
            currentContent = null;
        }
    }

    @FXML
    void handleButtonManageNews(ActionEvent event) {
        try {
            clearContent();

            // Memuat konten dari file FXML terpisah
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/Menu/ManagingNews.fxml"));
            AnchorPane loadedPane = loader.load();

            // Mengganti konten di contentPane dengan konten baru
            newsPane.getChildren().setAll(loadedPane);
            currentContent = loadedPane;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public ObservableList<NewsData> menuGetData() throws SQLException {
        String sql = "SELECT * FROM news";

        ObservableList<NewsData> listData = FXCollections.observableArrayList();
        connect = DatabaseConnection.getConnection();

        try {
            statement = connect.prepareStatement(sql);
            result = statement.executeQuery();

            NewsData newsData;

            while (result.next()) {
                newsData = new NewsData(result.getInt("id"),
                        result.getString("title"),
                        result.getString("content"),
                        result.getString("image"));
                listData.add(newsData);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return listData;
    }

    private ObservableList<NewsData> cardListData = FXCollections.observableArrayList();

    private void menuDisplayCard() throws SQLException {
        cardListData.clear();
        cardListData.addAll(menuGetData());

        int row = 0;
        int column = 0;

        newsGridPane.getRowConstraints().clear();
        newsGridPane.getColumnConstraints().clear();

        for (int q = 0; q < cardListData.size(); q++) {
            final int index = q; // Tambahkan variabel final index untuk mengakses q dalam ekspresi lambda

            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/Menu/CardNews.fxml"));
                AnchorPane pane = loader.load();
                CardNewsController cardC = loader.getController();
                cardC.setData(cardListData.get(index));

                cardC.buttonRead.setOnAction(event -> {
                    try {
                        FXMLLoader contentLoader = new FXMLLoader(
                                getClass().getResource("../Content/Menu/NewsContent.fxml"));
                        AnchorPane contentPane = contentLoader.load();

                        NewsContentController contentController = contentLoader.getController();
                        contentController.setSelectedNews(cardListData.get(index));
                        contentController.showData();

                        newsPane.getChildren().setAll(contentPane);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                });

                if (column == 2) {
                    column = 0;
                    row += 1;
                }
                newsGridPane.add(pane, column++, row);

                GridPane.setMargin(pane, new Insets(20));
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        try {
            menuDisplayCard();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
