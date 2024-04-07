package Controller;


import java.net.URL;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Data.Data2;
import Data.CustomerData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;

public class CustomerOrderController implements Initializable {

  @FXML
  private TableColumn<CustomerData, String> col_date;

  @FXML
  private TableColumn<CustomerData, String> col_productName;

  @FXML
  private TableColumn<CustomerData, String> col_quantity;

  @FXML
  private TableColumn<CustomerData, String> col_username;

  @FXML
  private TableColumn<CustomerData, String> col_status;

  @FXML
  private TableColumn<CustomerData, String> col_location;

  @FXML
  private ComboBox<String> boxStatus;

  @FXML
  private TableView<CustomerData> tableView;

  @FXML
  private Label labelDate;

  @FXML
  private Label labelProductName;

  @FXML
  private Label labelQuantity;

  @FXML
  private Label labelUsername;

  @FXML
  private Button buttonSearch;

  @FXML
  private Button buttonUpdate;

  @FXML
  private TextField tfUsername;

  Connection connect;
  PreparedStatement statement;
  ResultSet result;

  public ObservableList<CustomerData> customerDataList() throws SQLException {
    ObservableList<CustomerData> listData = FXCollections.observableArrayList();

    String sql = "SELECT * FROM custorder";
    connect = DatabaseConnection.getConnection();

    try {
      statement = connect.prepareStatement(sql);
      result = statement.executeQuery();
      CustomerData cData;

      while (result.next()) {
        int id = result.getInt("id");
        String username = result.getString("username");
        String productName = result.getString("productName");
        int quantity = result.getInt("quantity");
        Date date = result.getDate("date");
        String status = result.getString("status");
        String location = result.getString("location");

        cData = new CustomerData(id, username, productName, quantity, date, status, location);
        listData.add(cData);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }

    return listData;
  }

  public void handleButtonUpdate() {
    CustomerData customerData = tableView.getSelectionModel().getSelectedItem();
    String updatedStatus = boxStatus.getValue();

    String updateData = "UPDATE custorder SET status = ? WHERE id = ?";

    try {
      connect = DatabaseConnection.getConnection();
      statement = connect.prepareStatement(updateData);
      statement.setString(1, updatedStatus);
      statement.setInt(2, customerData.getID());
      statement.executeUpdate();

      Alert alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Message");
      alert.setHeaderText(null);
      alert.setContentText("Successfully update");
      alert.showAndWait();

      customerShowData();

      ReceiptController receiptController = new ReceiptController();
      if (updatedStatus.equals("SUDAH DIAMBIL")) {
        receiptController.updateReceiptStatus(customerData.getProductName(), customerData.getQuantity(),
            customerData.getDate(), updatedStatus);
      } else if (updatedStatus.equals("BELUM DIAMBIL")) {
        receiptController.updateReceiptStatus(customerData.getProductName(), customerData.getQuantity(),
            customerData.getDate(), updatedStatus);
      }
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

  public void handleButtonSearch() {
    String searchUsername = tfUsername.getText();

    if (searchUsername.isEmpty()) {
      // Mengembalikan semua data ke TableView
      tableView.setItems(customerListData);

      // Kosongkan label dan ComboBox yang menampilkan data terpilih sebelumnya
      labelUsername.setText("");
      labelProductName.setText("");
      labelQuantity.setText("");
      labelDate.setText("");
      boxStatus.setValue(null);

      return;
    } else {
      try {
        ObservableList<CustomerData> searchResultList = FXCollections.observableArrayList();

        for (CustomerData customerData : customerListData) {
          if (customerData.getUsername().equalsIgnoreCase(searchUsername)) {
            searchResultList.add(customerData);
          }
        }

        if (searchResultList.isEmpty()) {

          Alert alert = new Alert(AlertType.INFORMATION);
          alert.setTitle("Message");
          alert.setHeaderText(null);
          alert.setContentText("Username not found");
          alert.showAndWait();
        } else {
          tableView.setItems(searchResultList);

          labelUsername.setText("");
          labelProductName.setText("");
          labelQuantity.setText("");
          labelDate.setText("");
          boxStatus.setValue(null);
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  private ObservableList<CustomerData> customerListData;

  public void customerShowData() throws SQLException {
    customerListData = customerDataList();

    col_username.setCellValueFactory(new PropertyValueFactory<>("username"));
    col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
    col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));
    col_location.setCellValueFactory(new PropertyValueFactory<>("location"));
    col_date.setCellValueFactory(new PropertyValueFactory<>("date"));
    col_status.setCellValueFactory(new PropertyValueFactory<>("status"));

    tableView.setItems(customerListData);
  }

  public void customerOrderSelectData() {
    CustomerData customerData = tableView.getSelectionModel().getSelectedItem();

    labelUsername.setText(customerData.getUsername());
    labelProductName.setText(customerData.getProductName());
    labelQuantity.setText(String.valueOf(customerData.getQuantity()));
    labelDate.setText(String.valueOf(customerData.getDate()));
    boxStatus.setValue(customerData.getStatus());

    Data2.id = customerData.getID();
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      customerShowData();
    } catch (SQLException e) {
      // TODO Auto-generated catch block
      e.printStackTrace();
    }

    ObservableList<String> itemList = FXCollections.observableArrayList();
    itemList.add("BELUM DIAMBIL");
    itemList.add("SUDAH DIAMBIL");

    boxStatus.setItems(itemList);
  }

}
