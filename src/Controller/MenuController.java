package Controller;


import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.Optional;
import java.util.ResourceBundle;

import Data.Data;
import Data.ProductData;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.geometry.Insets;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ScrollPane;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class MenuController implements Initializable {

  @FXML
  private Button buttonReceipt;

  @FXML
  private Button buttonRemove;

  @FXML
  private Button buttonTake;

  @FXML
  private AnchorPane menuPane;

  @FXML
  private TableColumn<ProductData, String> menu_col_productName;

  @FXML
  private TableColumn<ProductData, String> menu_col_quantity;

  @FXML
  private GridPane menu_gridPane;

  @FXML
  private ScrollPane menu_scrollPane;

  @FXML
  private TableView<ProductData> menu_tableView;

  private Alert alert;
  Connection connect;
  PreparedStatement statement;
  ResultSet result;

  private ObservableList<ProductData> cardListData = FXCollections.observableArrayList();

  public ObservableList<ProductData> menuGetData() throws SQLException {
    String sql = "SELECT * FROM product";

    ObservableList<ProductData> listData = FXCollections.observableArrayList();
    connect = DatabaseConnection.getConnection();

    try {
      statement = connect.prepareStatement(sql);
      result = statement.executeQuery();

      ProductData product;

      while (result.next()) {
        product = new ProductData(result.getInt("id"),
            result.getString("productID"),
            result.getString("productName"),
            result.getInt("productStock"),
            result.getString("image"),
            result.getString("location"));
        listData.add(product);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return listData;
  }

  public void menuDisplayCard() throws SQLException {
    cardListData.clear();
    cardListData.addAll(menuGetData());

    int row = 0;
    int column = 0;

    menu_gridPane.getRowConstraints().clear();
    menu_gridPane.getColumnConstraints().clear();

    for (int q = 0; q < cardListData.size(); q++) {

      try {
        FXMLLoader load = new FXMLLoader();
        load.setLocation(getClass().getResource("../Content/Menu/CardProduct.fxml"));
        AnchorPane pane = load.load();
        CardProductController cardC = load.getController();
        cardC.setData(cardListData.get(q));

        if (column == 3) {
          column = 0;
          row += 1;
        }
        menu_gridPane.add(pane, column++, row);

        GridPane.setMargin(pane, new Insets(10));
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public ObservableList<ProductData> menuGetOrder() throws SQLException {
    ObservableList<ProductData> listData = FXCollections.observableArrayList();

    String sql = "SELECT * FROM customer";

    connect = DatabaseConnection.getConnection();

    try {
      statement = connect.prepareStatement(sql);
      result = statement.executeQuery();

      ProductData product;
      while (result.next()) {
        product = new ProductData(result.getInt("id"),
            result.getString("productID"),
            result.getString("productName"),
            result.getInt("quantity"),
            result.getString("image"),
            result.getDate("date"),
            result.getString("location"));
        listData.add(product);
      }
    } catch (Exception e) {
      e.printStackTrace();
    }
    return listData;
  }

  public ObservableList<ProductData> menuOrderListData;

  public void menuShowOrderData() throws SQLException {
    menuOrderListData = menuGetOrder();

    menu_col_productName.setCellValueFactory(new PropertyValueFactory<>("productName"));
    menu_col_quantity.setCellValueFactory(new PropertyValueFactory<>("quantity"));

    menu_tableView.setItems(menuOrderListData);

  }

  public void handleButtonTake() throws SQLException {
    Alert alert = new Alert(AlertType.CONFIRMATION);
    alert.setTitle("Message");
    alert.setHeaderText(null);
    alert.setContentText("Are you sure?");
    Optional<ButtonType> option = alert.showAndWait();

    if (option.get().equals(ButtonType.OK)) {

      String selectCustomer = "SELECT * FROM customer";
      String insertOrder = "INSERT INTO custorder(username, productName, quantity, date, location) VALUES(?,?,?,?,?)";
      String insertReceipt = "INSERT INTO receipt(productName, quantity, date, location) VALUES(?,?,?,?)";
      String deleteAllCustomer = "DELETE FROM customer";

      try {
        connect = DatabaseConnection.getConnection();
        statement = connect.prepareStatement(selectCustomer);
        result = statement.executeQuery();

        PreparedStatement statementOrder = connect.prepareStatement(insertOrder);
        PreparedStatement statementReceipt = connect.prepareStatement(insertReceipt);

        while (result.next()) {
          String productName = result.getString("productName");
          int quantity = result.getInt("quantity");
          String location = result.getString("location");

          statementOrder.setString(1, Data.username);
          statementOrder.setString(2, productName);
          statementOrder.setInt(3, quantity);
          statementOrder.setDate(4, new java.sql.Date(new Date().getTime()));
          statementOrder.setString(5, location);
          statementOrder.executeUpdate();

          statementReceipt.setString(1, productName);
          statementReceipt.setInt(2, quantity);
          statementReceipt.setDate(3, new java.sql.Date(new Date().getTime()));
          statementReceipt.setString(4, location);
          statementReceipt.executeUpdate();
        }

        statement = connect.prepareStatement(deleteAllCustomer);
        statement.executeUpdate();

        alert = new Alert(AlertType.INFORMATION);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText("Please take it at the pick-up location");
        alert.showAndWait();

        menuShowOrderData();
      } catch (Exception e) {
        e.printStackTrace();

      }
    }
  }

  private int getid;

  public void menuSelectedOrder() {
    ProductData prod = menu_tableView.getSelectionModel().getSelectedItem();
    int num = menu_tableView.getSelectionModel().getSelectedIndex();

    if ((num - 1) < -1) {
      return;
    }
    getid = prod.getId();
  }

  public void handleButtonRemove() throws SQLException {
    if (getid == 0) {
      alert = new Alert(AlertType.ERROR);
      alert.setTitle("Message");
      alert.setHeaderText(null);
      alert.setContentText("Please first select the item you want to delete");
      alert.showAndWait();
    } else {
      String deleteData = "DELETE FROM customer WHERE id = ?";
      String updateProductStock = "UPDATE product SET productStock = productStock + ? WHERE productID = ?";

      connect = DatabaseConnection.getConnection();

      try {

        statement = connect.prepareStatement("SELECT * FROM customer WHERE id = ?");
        statement.setInt(1, getid);
        result = statement.executeQuery();

        if (result.next()) {
          String productID = result.getString("productID");
          int quantity = result.getInt("quantity");

          statement = connect.prepareStatement(deleteData);
          statement.setInt(1, getid);
          statement.executeUpdate();

          // Perbarui stok produk
          statement = connect.prepareStatement(updateProductStock);
          statement.setInt(1, quantity);
          statement.setString(2, productID);
          statement.executeUpdate();

          // Tampilkan ulang data pesanan
          menuShowOrderData();
        }
      } catch (Exception e) {
        e.printStackTrace();
      }
    }
  }

  public void handleButtonReceipt() throws IOException {
    FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/Menu/Receipt.fxml"));
    Stage stage = new Stage();
    stage.initModality(Modality.APPLICATION_MODAL);
    stage.setScene(new Scene(loader.load()));

    stage.show();

  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      menuDisplayCard();
      menuGetOrder();
      menuShowOrderData();

      menu_scrollPane.setContent(menu_gridPane);

      menu_scrollPane.setFitToWidth(true);
      menu_scrollPane.setFitToHeight(true);
      menu_scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.NEVER);
      menu_scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
