package Controller;


import java.io.File;
import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import Data.Data1;
import Data.NewsData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class ManagingNewsController implements Initializable {

  @FXML
  private Button buttonAdd;

  @FXML
  private Button buttonClear;

  @FXML
  private Button buttonDelete;

  @FXML
  private Button buttonImport;

  @FXML
  private Button buttonUpdate;

  @FXML
  private TableColumn<NewsData, String> col_title;

  @FXML
  private ImageView imageView;

  @FXML
  private TextArea taContent;

  @FXML
  private TableView<NewsData> tableView;

  @FXML
  private TextField tfTitle;

  @FXML
  private AnchorPane managingNewsPane;

  @FXML
  private File file;
  private Image image;

  private Connection connect;
  private PreparedStatement statement;
  private ResultSet result;

  private Alert alert;

  private ObservableList<NewsData> newsListData;

  private Node currentContent;

  private void clearContent() {
    if (currentContent != null) {
      managingNewsPane.getChildren().remove(currentContent);
      currentContent = null;
    }
  }

  @FXML
  void handleButtonAdd(ActionEvent event) throws SQLException {
    if (tfTitle.getText().isEmpty() || taContent.getText().isEmpty() || Data1.path == null) {
      alert = new Alert(AlertType.ERROR);
      alert.setTitle("Message");
      alert.setHeaderText(null);
      alert.setContentText("Please fill in all the data");
      alert.showAndWait();
    } else {
      connect = DatabaseConnection.getConnection();

      try {
        String insertData = "INSERT INTO news (title, content, image) VALUES (?,?,?)";
        statement = connect.prepareStatement(insertData);

        statement.setString(1, tfTitle.getText());
        statement.setString(2, taContent.getText());

        String path = Data1.path;
        path = path.replace("\\", "\\\\");

        statement.setString(3, path);
        statement.executeUpdate();

        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText("Successfully added!");
        alert.showAndWait();

        showNewsData();
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @FXML
  void handleButtonImport(ActionEvent event) {
    FileChooser openFile = new FileChooser();
    openFile.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png", "*jpg"));

    file = openFile.showOpenDialog(null);

    if (file != null) {
      Data1.path = file.getAbsolutePath();
      image = new Image(file.toURI().toString(), 200, 200, false, true);
      imageView.setImage(image);
    }
  }

  @FXML
  void handleButtonClear(ActionEvent event) {
    tfTitle.setText("");
    taContent.setText("");
    Data1.path = "";
    Data1.id = 0;
    imageView.setImage(null);
  }

  @FXML
  void handleButtonDelete(ActionEvent event) throws SQLException {
    if (Data1.id == 0) {
      alert = new Alert(AlertType.ERROR);
      alert.setTitle("Message");
      alert.setHeaderText(null);
      alert.setContentText("Please select the data you want to delete");
      alert.showAndWait();
    } else {
      alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("Message");
      alert.setHeaderText(null);
      alert.setContentText("Are you sure you want to DELETE this data?");
      Optional<ButtonType> option = alert.showAndWait();

      if (option.isPresent() && option.get().equals(ButtonType.OK)) {
        connect = DatabaseConnection.getConnection(); // Add this line to get the database connection

        String deleteData = "DELETE FROM news WHERE id = " + Data1.id;
        try {
          statement = connect.prepareStatement(deleteData);
          statement.executeUpdate();

          alert = new Alert(AlertType.INFORMATION);
          alert.setTitle("Message");
          alert.setHeaderText(null);
          alert.setContentText("Successfully deleted!");
          alert.showAndWait();

          showNewsData();
        } catch (Exception e) {
          e.printStackTrace();
        } finally {
          try {
            if (statement != null) {
              statement.close();
            }
            if (connect != null) {
              connect.close();
            }
          } catch (SQLException e) {
            e.printStackTrace();
          }
        }
      }
    }
  }

  @FXML
  void handleButtonUpdate(ActionEvent event) throws SQLException {
    if (tfTitle.getText().isEmpty() || taContent.getText().isEmpty() || Data1.path == null || Data1.id == 0) {
      alert = new Alert(AlertType.ERROR);
      alert.setTitle("Message");
      alert.setHeaderText(null);
      alert.setContentText("Please fill in all the data");
      alert.showAndWait();
    } else {
      String path = Data1.path;
      path = path.replace("\\", "\\\\");

      String updateQuery = "UPDATE news SET title = ?, content = ?, image = ? WHERE id = ?";
      Connection connection = DatabaseConnection.getConnection();
      PreparedStatement statement = connection.prepareStatement(updateQuery);

      try {
        statement.setString(1, tfTitle.getText());
        statement.setString(2, taContent.getText());
        statement.setString(3, path);
        statement.setInt(4, Data1.id);

        Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
        confirmationAlert.setTitle("Message");
        confirmationAlert.setHeaderText(null);
        confirmationAlert.setContentText("Are you sure you want to update?");
        Optional<ButtonType> option = confirmationAlert.showAndWait();

        if (option.isPresent() && option.get().equals(ButtonType.OK)) {
          statement.executeUpdate();

          alert = new Alert(AlertType.INFORMATION);
          alert.setTitle("Message");
          alert.setHeaderText(null);
          alert.setContentText("Successfully updated!");
          alert.showAndWait();

          showNewsData();
        }
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }

  public ObservableList<NewsData> newsDataList() throws SQLException {
    ObservableList<NewsData> listData = FXCollections.observableArrayList();
    String sql = "SELECT * FROM news";
    connect = DatabaseConnection.getConnection();

    try {
      statement = connect.prepareStatement(sql);
      result = statement.executeQuery();

      while (result.next()) {
        NewsData newsData = new NewsData(result.getInt("id"),
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

  public void showNewsData() throws SQLException {
    newsListData = newsDataList();
    col_title.setCellValueFactory(new PropertyValueFactory<>("title"));
    tableView.setItems(newsListData);
    selectNewsData();
  }

  public void selectNewsData() {
    NewsData newsData = tableView.getSelectionModel().getSelectedItem();

    if (newsData != null) {
      tfTitle.setText(newsData.getTitle());
      taContent.setText(newsData.getContent());

      Data1.path = newsData.getImage();
      Data1.id = newsData.getId();

      String path = "File:" + newsData.getImage();
      image = new Image(path, 200, 200, false, true);
      imageView.setImage(image);
    }
  }

  @FXML
  public void handleButtonBack(ActionEvent event) {
    try {

      clearContent();
      FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/Menu/News.fxml"));
      AnchorPane loadedPane = loader.load();

      // Mengganti konten di accountPane dengan konten baru
      managingNewsPane.getChildren().setAll(loadedPane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    try {
      showNewsData();
    } catch (SQLException e) {
      e.printStackTrace();
    }

  }
}