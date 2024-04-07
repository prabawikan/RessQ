package Controller;


import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Optional;
import java.util.ResourceBundle;

import Data.ReceiptData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

public class ReceiptController implements Initializable {

  @FXML
  private TableColumn<ReceiptData, String> col_date;

  @FXML
  private TableColumn<ReceiptData, String> col_productname;
  @FXML
  private TableColumn<ReceiptData, String> col_status;

  @FXML
  private TableColumn<ReceiptData, String> col_quantity;

  @FXML
  private TableColumn<ReceiptData, String> col_location;

  @FXML
  private Button buttonRemove;

  @FXML
  private TableView<ReceiptData> tableReceipt;
  Connection connect;
  PreparedStatement statement;
  ResultSet result;

  public ObservableList<ReceiptData> inventoryDataList() throws SQLException {

    ObservableList<ReceiptData> listData = FXCollections.observableArrayList();
    String sql = "SELECT * FROM receipt";
    connect = DatabaseConnection.getConnection();

    try {

      statement = connect.prepareStatement(sql);
      result = statement.executeQuery();

      while (result.next()) {
        String productName = result.getString("productName");
        int quantity = result.getInt("quantity");
        Date date = result.getDate("date");
        String location = result.getString("location");
        String status = result.getString("status");

        ReceiptData product = new ReceiptData(productName, quantity, date, location, status);
        listData.add(product);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return listData;
  }

  private ObservableList<ReceiptData> inventoryListData;

  public void inventoryShowData() throws SQLException {

    inventoryListData = inventoryDataList();

    col_productname.setCellValueFactory(new PropertyValueFactory<>("productName"));
    col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    col_location.setCellValueFactory(new PropertyValueFactory<>("location"));
    col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
    col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

    tableReceipt.setItems(inventoryListData);

    for (ReceiptData receiptData : inventoryListData) {
      if (receiptData.getStatus().equals("SUDAH DIAMBIL")) {
        receiptData.setStatus("SUDAH DIAMBIL");
      }
    }
  }

  public void handleButtonRemove() {
    ReceiptData selectedData = tableReceipt.getSelectionModel().getSelectedItem();
    if (selectedData != null) {
      Alert confirmationAlert = new Alert(AlertType.CONFIRMATION);
      confirmationAlert.setTitle("Message");
      confirmationAlert.setHeaderText(null);
      confirmationAlert.setContentText("Are you sure you want to delete?");

      Optional<ButtonType> option = confirmationAlert.showAndWait();
      if (option.isPresent() && option.get() == ButtonType.OK) {
        try {
          connect = DatabaseConnection.getConnection();
          String sql = "DELETE FROM receipt WHERE productName = ? AND quantity = ? AND date = ?";
          statement = connect.prepareStatement(sql);
          statement.setString(1, selectedData.getProductName());
          statement.setInt(2, selectedData.getQuantity());
          statement.setDate(3, selectedData.getDate());
          statement.executeUpdate();

          // Menghapus data dari TableView
          tableReceipt.getItems().remove(selectedData);
        } catch (SQLException e) {
          e.printStackTrace();
        }
      }
    }
  }

  void updateReceiptStatus(String productName, int quantity, Date date, String status) {
    try {
      connect = DatabaseConnection.getConnection();
      String sql = "UPDATE receipt SET status = ? WHERE productName = ? AND quantity = ? AND date = ?";
      statement = connect.prepareStatement(sql);
      statement.setString(1, status);
      statement.setString(2, productName);
      statement.setInt(3, quantity);
      statement.setDate(4, date);
      statement.executeUpdate();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      inventoryShowData();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }
  }

}
