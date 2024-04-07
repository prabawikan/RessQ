package Controller;

import java.io.IOException;
import java.net.URL;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ResourceBundle;

import Data.Data;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;

public class EditAccountController implements Initializable {

  @FXML
  private TextField tfAlamat;

  @FXML
  private TextField tfEmail;

  @FXML
  private TextField tfNamaLengkap;

  @FXML
  private TextField tfNoTelepon;

  @FXML
  private TextField tfPassword;

  @FXML
  private Label tfUsername;

  @FXML
  private Button buttonBack;

  @FXML
  private Button buttonConfirm;

  @FXML
  private AnchorPane editAccountPane;

  Alert alert;

  Connection connect;
  PreparedStatement statement;
  ResultSet result;

  private Node currentContent;

  private void clearContent() {
    if (currentContent != null) {
      editAccountPane.getChildren().remove(currentContent);
      currentContent = null;
    }
  }

  private void showData() throws SQLException {
    connect = DatabaseConnection.getConnection();
    String sql = "SELECT * FROM produsen_account WHERE username = ?";
    statement = connect.prepareStatement(sql);

    statement.setString(1, Data.username);

    result = statement.executeQuery();

    while (result.next()) {
      String namaLengkap = result.getString("namaLengkap");
      String email = result.getString("email");
      String noTelepon = result.getString("noTelepon");
      String alamat = result.getString("alamat");
      String username = result.getString("username");
      String password = result.getString("password");

      tfUsername.setText(username);
      tfEmail.setText(email);
      tfNamaLengkap.setText(namaLengkap);
      tfPassword.setText(password);
      tfAlamat.setText(alamat);
      tfNoTelepon.setText(noTelepon);
    }
  }

  @FXML
  void handleButtonBack(ActionEvent event) {
    try {
      clearContent();

      FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/Menu/Account.fxml"));
      AnchorPane loadedPane = loader.load();

      editAccountPane.getChildren().setAll(loadedPane);
    } catch (IOException e) {
      e.printStackTrace();
    }
  }

  @FXML
  void handleButtonConfirm(ActionEvent event) throws SQLException, IOException {
    if (tfAlamat.getText().isEmpty()
        || tfEmail.getText().isEmpty()
        || tfNamaLengkap.getText().isEmpty()
        || tfNoTelepon.getText().isEmpty()
        || tfPassword.getText().isEmpty()
        || tfUsername.getText().isEmpty()) {

      alert = new Alert(AlertType.ERROR);
      alert.setTitle("Message");
      alert.setHeaderText(null);
      alert.setContentText("Please fill in all the data");
      alert.showAndWait();
    } else {
      String email = tfEmail.getText();
      String namaLengkap = tfNamaLengkap.getText();
      String noTelepon = tfNoTelepon.getText();
      String alamat = tfAlamat.getText();
      String username = tfUsername.getText();
      String password = tfPassword.getText();

      connect = DatabaseConnection.getConnection();

      String sql = "UPDATE produsen_account SET email = ?, namaLengkap = ?, noTelepon = ?, alamat = ?, username = ?, password = ? WHERE username = ?";
      statement = connect.prepareStatement(sql);

      statement.setString(1, email);
      statement.setString(2, namaLengkap);
      statement.setString(3, noTelepon);
      statement.setString(4, alamat);
      statement.setString(5, username);
      statement.setString(6, password);
      statement.setString(7, username);

      statement.executeUpdate();

      alert = new Alert(AlertType.INFORMATION);
      alert.setTitle("Message");
      alert.setHeaderText(null);
      alert.setContentText("Successfully update");
      alert.showAndWait();
    }
  }

  @Override
  public void initialize(URL location, ResourceBundle resources) {
    try {
      showData();
    } catch (SQLException e) {
      e.printStackTrace();
    }
  }

}
