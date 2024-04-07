package Controller;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class SignUpController {

    @FXML
    private Button buttonSignIn;

    @FXML
    private Button buttonSignUp;

    @FXML
    private Label notifAlamat;

    @FXML
    private Label notifEmail;

    @FXML
    private Label notifNamaLengkap;

    @FXML
    private Label notifNoTelepon;

    @FXML
    private Label notifPassword;

    @FXML
    private Label notifPassword1;

    @FXML
    private Label notifUsername;

    @FXML
    private TextField tfAlamat;

    @FXML
    private TextField tfEmail;

    @FXML
    private TextField tfNamaLengkap;

    @FXML
    private TextField tfNoTelepon;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private PasswordField tfPassword1;

    @FXML
    private TextField tfUsername;

    @FXML
    void handleButtonSignUp(ActionEvent event) throws IOException {
        String namaLengkap = tfNamaLengkap.getText();
        String username = tfUsername.getText();
        String password = tfPassword.getText();
        String confirmPassword = tfPassword1.getText();
        String alamat = tfAlamat.getText();
        String email = tfEmail.getText();
        String noTelepon = tfNoTelepon.getText();

        // Periksa apakah password dan konfirmasi password cocok
        if (!password.equals(confirmPassword)) {
            notifPassword1.setText("Password harus sama.");
            return;
        } else {
            notifPassword1.setText("");
        }

        // Periksa apakah semua input terisi
        boolean isInputValid = true;

        if (namaLengkap.isEmpty()) {
            notifNamaLengkap.setText("Nama lengkap harus diisi.");
            isInputValid = false;
        } else {
            notifNamaLengkap.setText("");
        }

        if (username.isEmpty()) {
            notifUsername.setText("Username harus diisi.");
            isInputValid = false;
        } else {
            notifUsername.setText("");
        }

        if (password.isEmpty()) {
            notifPassword.setText("Password harus diisi.");
            isInputValid = false;
        } else {
            notifPassword.setText("");
        }

        if (confirmPassword.isEmpty()) {
            notifPassword1.setText("Konfirmasi password harus diisi.");
            isInputValid = false;
        } else {
            notifPassword1.setText("");
        }

        if (alamat.isEmpty()) {
            notifAlamat.setText("Alamat harus diisi.");
            isInputValid = false;
        } else {
            notifAlamat.setText("");
        }

        if (noTelepon.isEmpty()) {
            notifNoTelepon.setText("No Telepon harus diisi.");
            isInputValid = false;
        } else {
            notifNoTelepon.setText("");
        }
        if (email.isEmpty()) {
            notifEmail.setText("Email harus diisi.");
            isInputValid = false;
        } else {
            notifEmail.setText("");
        }

        if (!isInputValid) {
            return;
        }

        try (Connection conn = DatabaseConnection.getConnection()) {
            String checkQuery = "SELECT COUNT(*) FROM produsen_account WHERE username = ?";
            PreparedStatement checkStatement = conn.prepareStatement(checkQuery);
            checkStatement.setString(1, username);
            ResultSet checkResult = checkStatement.executeQuery();
            checkResult.next();
            int count = checkResult.getInt(1);
            if (count > 0) {
                notifUsername.setText("Username sudah digunakan.");
                return;
            }

            String query = "INSERT INTO produsen_account (email, namaLengkap, noTelepon, alamat, username, password) VALUES (?,?,?,?,?,?)";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, email);
            statement.setString(2, namaLengkap);
            statement.setString(3, noTelepon);
            statement.setString(4, alamat);
            statement.setString(5, username);
            statement.setString(6, password);

            int rowsInserted = statement.executeUpdate();
            if (rowsInserted > 0) {
                Alert alert = new Alert(AlertType.INFORMATION);
                alert.setTitle("Registrasi");
                alert.setHeaderText(null);
                alert.setContentText("REGISTRASI BERHASIL!");

                alert.showAndWait();
            } else {
                System.out.println("Gagal menyimpan data pengguna.");
            }
        } catch (SQLException e) {
            e.printStackTrace();
            System.out.println("Terjadi kesalahan saat menyimpan data.");
        }
    }

    @FXML
    private void handleButtonSignIn(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/LoginRegister/SignIn.fxml"));
        Stage newStage = new Stage();
        newStage.setScene(new Scene(loader.load()));

        Stage currentStage = (Stage) buttonSignUp.getScene().getWindow();
        currentStage.close();

        newStage.show();
    }
}
