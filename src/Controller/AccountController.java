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
  import javafx.scene.control.Button;
  import javafx.scene.layout.AnchorPane;
  import javafx.scene.text.Text;

  public class AccountController implements Initializable {

    @FXML
    private Text temail;

    @FXML
    private Text talamat;

    @FXML
    private Text tnamaLengkap;

    @FXML
    private Text tnoTelepon;

    @FXML
    private Text tpassword;

    @FXML
    private Text tusername;
    @FXML
    private Button buttonEdit;
    @FXML
    public AnchorPane accountPane;

    Connection connect;
    PreparedStatement statement;
    ResultSet result;

    private Node currentContent;

    private void clearContent() {
      if (currentContent != null) {
        accountPane.getChildren().remove(currentContent);
        currentContent = null;
      }
    }

    @FXML
    private void handleButtonEdit(ActionEvent event) {
      try {
        clearContent();

        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/Menu/EditAccount.fxml"));
        AnchorPane loadedPane = loader.load();

        accountPane.getChildren().setAll(loadedPane);
        currentContent = loadedPane;
      } catch (IOException e) {
        e.printStackTrace();
      }
    }

    public void showAccount() throws SQLException {
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

        tusername.setText(username);
        tpassword.setText(password);
        temail.setText(email);
        tnoTelepon.setText(noTelepon);
        talamat.setText(alamat);
        tnamaLengkap.setText(namaLengkap);
      }
    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {

      try {
        showAccount();
      } catch (SQLException e) {
        e.printStackTrace();
      }
    }
  }
