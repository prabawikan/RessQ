package Controller;


import java.io.IOException;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Data.Data;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class SignInController {

    @FXML
    private Button buttonLogin;

    @FXML
    private Button buttonSignUp;

    @FXML
    private Label notif;

    @FXML
    private PasswordField tfPassword;

    @FXML
    private TextField tfUsername;

    @FXML
    void handleButtonLogin(ActionEvent event) throws IOException {
        String username = tfUsername.getText();
        String password = tfPassword.getText();

        Data.username = username;

        // Validasi data login
        boolean isDataValid = validateLoginData(username, password);

        if (isDataValid) {
            // Data login valid, tampilkan jendela Home
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Home.fxml"));
            Stage stage = new Stage();
            stage.setScene(new Scene(loader.load()));
            stage.show();

            Stage loginStage = (Stage) buttonLogin.getScene().getWindow();
            loginStage.close();
        } else {
            // Data login tidak valid, tampilkan pesan kesalahan
            notif.setText("Username atau password salah.");
        }
    }

    @FXML
    void handleButtonSignUp(ActionEvent event) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/LoginRegister/SignUp.fxml"));
        Stage stage = new Stage();
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.setScene(new Scene(loader.load()));

        stage.show();

        Stage signUpStage = (Stage) buttonSignUp.getScene().getWindow();
        signUpStage.close();
    }

    private String getNamaLengkap(String username) {
        try (Connection connect = DatabaseConnection.getConnection()) {
            String query = "SELECT namaLengkap FROM produsen_account WHERE username = ?";
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1, username);

            ResultSet result = statement.executeQuery();
            if (result.next()) {
                return result.getString("namaLengkap");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return null;
    }

    private boolean validateLoginData(String username, String password) {
        try (Connection connect = DatabaseConnection.getConnection()) {
            String query = "SELECT * FROM produsen_account WHERE username = ? AND password = ?";
            PreparedStatement statement = connect.prepareStatement(query);
            statement.setString(1, username);
            statement.setString(2, password);

            ResultSet result = statement.executeQuery();

            if (result.next()) {
                String namaLengkap = getNamaLengkap(username);
                if (namaLengkap != null) {
                    // Set nilai namaLengkap ke variabel Data
                    Data.namaLengkap = namaLengkap;
                    return true;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
}
