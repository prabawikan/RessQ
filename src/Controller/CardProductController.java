package Controller;

import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Date;
import java.util.ResourceBundle;

import Data.Data;
import Data.ProductData;
import javafx.fxml.FXML;
import javafx.fxml.Initializable;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Spinner;
import javafx.scene.control.SpinnerValueFactory;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;

public class CardProductController implements Initializable {

  @FXML
  private AnchorPane cardForm;

  @FXML
  private Button productButtonAdd;

  @FXML
  private ImageView productImageView;

  @FXML
  private Label productName;

  @FXML
  private Label labelStock;

  @FXML
  private Text textLocation;

  @FXML
  private Spinner<Integer> productSpinner;

  private SpinnerValueFactory<Integer> spin;

  private String prodID;
  private String prodImage;
  private String prodDate;
  private int quantity;
  private ProductData productData;
  private Image image;
  private String location;

  Alert alert;

  Connection connect;
  PreparedStatement statement;
  ResultSet result;

  public void setData(ProductData productData) {
    this.productData = productData;

    prodImage = productData.getImage();
    prodID = productData.getProductID();
    prodDate = String.valueOf(productData.getDate());
    productName.setText(productData.getProductName());
    labelStock.setText(String.valueOf(productData.getProductStock()));
    textLocation.setText(productData.getLocation());
    location = productData.getLocation();


    String path = "File:" + productData.getImage();
    image = new Image(path, 250, 200, false, true);
    productImageView.setImage(image);
  }

  public void setQuantity() {
    spin = new SpinnerValueFactory.IntegerSpinnerValueFactory(0, 100, 0);

    productSpinner.setValueFactory(spin);
  }

  public void buttonAdd() throws SQLException {
    quantity = productSpinner.getValue();
    connect = DatabaseConnection.getConnection();

    try {
      int checkStock = 0;

      String checkStockQuery = "SELECT productStock FROM product WHERE productID = ?";
      statement = connect.prepareStatement(checkStockQuery);
      statement.setString(1, prodID);

      result = statement.executeQuery();

      if (result.next()) {
        checkStock = result.getInt("productStock");
      }

      if (checkStock == 0) {

        String updateStock = "UPDATE product SET productName = '"
            + productName.getText() + "', productStock = 0, image = '"
            + prodImage + "' WHERE productID = '"
            + prodID + "'";
        statement = connect.prepareStatement(updateStock);
        statement.executeUpdate();
      }

      if (quantity == 0) {
        alert = new Alert(AlertType.ERROR);
        alert.setTitle("Message");
        alert.setHeaderText(null);
        alert.setContentText("Please fill in the quantity");
        alert.showAndWait();
      } else {

        if (checkStock == 0) {
          alert = new Alert(AlertType.ERROR);
          alert.setTitle("Message");
          alert.setHeaderText(null);
          alert.setContentText("Sold out");
          alert.showAndWait();
        } else if (checkStock < quantity) {
          alert = new Alert(AlertType.ERROR);
          alert.setTitle("Message");
          alert.setHeaderText(null);
          alert.setContentText("Quantity exceeds stock");
          alert.showAndWait();
        } else {
          String insertData = "INSERT INTO customer(username, productID, productName, quantity, date, location) VALUES (?,?,?,?,?,?)";

          statement = connect.prepareStatement(insertData);
          statement.setString(1, Data.username);
          statement.setString(2, prodID);
          statement.setString(3, productName.getText());
          statement.setString(4, String.valueOf(quantity));

          Date date = new Date();
          java.sql.Date sqlDate = new java.sql.Date(date.getTime());
          statement.setString(5, String.valueOf(sqlDate));

          statement.setString(6, location);

          statement.executeUpdate();

          prodImage = prodImage.replace("\\", "\\\\");

          int upStock = checkStock - quantity;

          String updateStock = "UPDATE product SET productName = '"
              + productName.getText() + "', productStock = " + upStock + ", image = '"
              + prodImage + "' WHERE productID = '"
              + prodID + "'";

          statement = connect.prepareStatement(updateStock);
          statement.executeUpdate();

          alert = new Alert(AlertType.INFORMATION);
          alert.setTitle("Message");
          alert.setHeaderText(null);
          alert.setContentText("Successfully added");
          alert.showAndWait();
        }
      }

    } catch (Exception e) {
      e.printStackTrace();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    setQuantity();
  }
}
