package Controller;


import java.sql.Statement;

import java.io.File;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;

import Data.Data;
import Data.ProductData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.stage.FileChooser;
import javafx.stage.FileChooser.ExtensionFilter;

public class InventoryController implements Initializable {

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
  private AnchorPane contentPane;

  @FXML
  private TableView<ProductData> tableViewProduct;

  @FXML
  private TableColumn<ProductData, String> productID;

  @FXML
  private TableColumn<ProductData, String> productName;

  @FXML
  private TableColumn<ProductData, String> productStock;

  @FXML
  private TableColumn<ProductData, String> location;

  @FXML
  private ImageView imageView;

  @FXML
  private TextField tfProductName;

  @FXML
  private TextField tfProductID;

  @FXML
  private TextField tfProductStock;

  @FXML
  private TextField tfLocation;

  @FXML
  private File file;
  private Image image;

  Connection connect;
  PreparedStatement statement;
  Statement st;
  ResultSet result;

  Alert alert;

  private ObservableList<ProductData> inventoryListData;

  @FXML
  void handleButtonAdd(ActionEvent event) throws SQLException {
    if (tfProductID.getText().isEmpty()
        || tfProductName.getText().isEmpty()
        || tfProductStock.getText().isEmpty()
        || Data.path == null) {

      alert = new Alert(AlertType.ERROR);
      alert.setTitle("Message");
      alert.setHeaderText(null);
      alert.setContentText("Please fill in all the data");
      alert.showAndWait();
    } else {
      String checkProductID = "SELECT productID FROM product WHERE productID = '"
          + tfProductID.getText() + "'";

      connect = DatabaseConnection.getConnection();

      try {
        st = connect.createStatement();
        result = st.executeQuery(checkProductID);

        if (result.next()) {
          alert = new Alert(AlertType.ERROR);
          alert.setTitle("Message");
          alert.setHeaderText(null);
          alert.setContentText("id" + tfProductID.getText() + " already used");
          alert.showAndWait();
        } else {
          String insertData = "INSERT INTO product (productID, productName, productStock,image, location) VALUES (?,?,?,?,?)";
          statement = connect.prepareStatement(insertData);

          statement.setString(1, tfProductID.getText());
          statement.setString(2, tfProductName.getText());
          statement.setString(3, tfProductStock.getText());

          String path = Data.path;
          path = path.replace("\\", "\\\\");
          statement.setString(4, path);

          statement.setString(5, tfLocation.getText());
          statement.executeUpdate();

          alert = new Alert(AlertType.INFORMATION);
          alert.setTitle("Message");
          alert.setHeaderText(null);
          alert.setContentText("Successfully added!");
          alert.showAndWait();

          inventoryShowData();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  @FXML
  void handleButtonClear(ActionEvent event) {
    tfProductID.setText("");
    tfProductName.setText("");
    tfProductStock.setText("");
    tfLocation.setText("");
    Data.path = "";
    Data.id = 0;

    imageView.setImage(null);
  }

  @FXML
  void handleButtonDelete(ActionEvent event) {
    if (Data.id == 0) {

      alert = new Alert(AlertType.ERROR);
      alert.setTitle("Message");
      alert.setHeaderText(null);
      alert.setContentText("Please select the data you want to delete");
      alert.showAndWait();

    } else {
      alert = new Alert(AlertType.CONFIRMATION);
      alert.setTitle("Message");
      alert.setHeaderText(null);
      alert.setContentText("Are you sure you want to DELETE Product ID: " + tfProductID.getText() + "?");
      Optional<ButtonType> option = alert.showAndWait();

      if (option.get().equals(ButtonType.OK)) {
        String deleteData = "DELETE FROM product WHERE id = " + Data.id;
        try {
          statement = connect.prepareStatement(deleteData);
          statement.executeUpdate();

          alert = new Alert(AlertType.INFORMATION);
          alert.setTitle("Message");
          alert.setHeaderText(null);
          alert.setContentText("successfully Deleted!");
          alert.showAndWait();

          inventoryShowData();

        } catch (Exception e) {
          e.printStackTrace();
        }
      }
    }
  }

  @FXML
  void handleButtonImport(ActionEvent event) {
    FileChooser openFile = new FileChooser();
    openFile.getExtensionFilters().add(new ExtensionFilter("Open Image File", "*png", "*jpg", "*jpeg"));

    file = openFile.showOpenDialog(null);

    if (file != null) {

      Data.path = file.getAbsolutePath();
      image = new Image(file.toURI().toString(), 140, 140, false, true);

      imageView.setImage(image);
    }
  }

  @FXML
  void handleButtonUpdate(ActionEvent event) throws SQLException {
    if (tfProductID.getText().isEmpty()
        || tfProductName.getText().isEmpty()
        || tfProductStock.getText().isEmpty()
        || Data.path == null || Data.id == 0) {

      alert = new Alert(AlertType.ERROR);
      alert.setTitle("Message");
      alert.setHeaderText(null);
      alert.setContentText("Please fill in all the data");
      alert.showAndWait();
    } else {
      String path = Data.path;
      path = path.replace("\\", "\\\\");

      String updateData = "UPDATE product SET productID = ?, productName = ?, productStock = ?, image = ?, location = ? WHERE id = ?";

      connect = DatabaseConnection.getConnection();

      try {

        alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText("Are you sure you want to update ID : " + tfProductID.getText() + "?");
        Optional<ButtonType> option = alert.showAndWait();

        if (option.get().equals(ButtonType.OK)) {
          statement = connect.prepareStatement(updateData);
          statement.setString(1, tfProductID.getText());
          statement.setString(2, tfProductName.getText());
          statement.setString(3, tfProductStock.getText());
          statement.setString(4, path);
          statement.setString(5, tfLocation.getText());
          statement.setInt(6, Data.id);

          statement.executeUpdate();

          alert = new Alert(AlertType.INFORMATION);
          alert.setTitle("Message");
          alert.setHeaderText(null);
          alert.setContentText("Successfully updated!");
          alert.showAndWait();

          // TO UPDATE YOUR TABLE VIEW
          inventoryShowData();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public ObservableList<ProductData> inventoryDataList() throws SQLException {

    ObservableList<ProductData> listData = FXCollections.observableArrayList();
    String sql = "SELECT * FROM product";
    connect = DatabaseConnection.getConnection();

    try {

      statement = connect.prepareStatement(sql);
      result = statement.executeQuery();

      ProductData prodData;
      while (result.next()) {
        prodData = new ProductData(result.getInt("id"),
            result.getString("productID"),
            result.getString("productName"),
            result.getInt("productStock"),
            result.getString("image"),
            result.getString("location"));

        listData.add(prodData);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return listData;
  }

  public void inventoryShowData() throws SQLException {
    inventoryListData = inventoryDataList();

    productID.setCellValueFactory(new PropertyValueFactory<>("productID"));
    productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
    productStock.setCellValueFactory(new PropertyValueFactory<>("productStock"));
    location.setCellValueFactory(new PropertyValueFactory<>("location"));

    tableViewProduct.setItems(inventoryListData);
  }

  public void inventorySelectData() {
    ProductData prodData = tableViewProduct.getSelectionModel().getSelectedItem();

    if (prodData != null) {
      tfProductID.setText(prodData.getProductID());
      tfProductName.setText(prodData.getProductName());
      tfProductStock.setText(String.valueOf(prodData.getProductStock()));
      tfLocation.setText(prodData.getLocation());

      Data.path = prodData.getImage();
      Data.id = prodData.getId();

      String path = "File:" + prodData.getImage();

      image = new Image(path, 120, 127, false, true);
      imageView.setImage(image);
    } else {
      // Tidak ada data yang dipilih, berikan tindakan yang sesuai
      // Misalnya, kosongkan isian atau tampilkan pesan kepada pengguna
      tfProductID.setText("");
      tfProductName.setText("");
      tfProductStock.setText("");
      tfLocation.setText("");

      Data.path = "";
      Data.id = 0;

      imageView.setImage(null);
    }
  }

  @Override
  public void initialize(URL url, ResourceBundle rb) {
    try {
      inventoryShowData();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }
}
