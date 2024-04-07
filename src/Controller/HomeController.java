package Controller;

import javafx.scene.control.Label;
import java.io.IOException;
import java.net.URL;
import java.util.Optional;
import java.util.ResourceBundle;

import Data.Data;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.MenuItem;
import javafx.scene.layout.AnchorPane;
import javafx.scene.text.Text;
import javafx.stage.Modality;
import javafx.stage.Stage;

public class HomeController implements Initializable {

    @FXML
    private Label labelNama;

    @FXML
    private Text textNamaLengkap;

    @FXML
    private Button buttonNews;

    @FXML
    private Button buttonMenu;

    @FXML
    private Button buttonCustomerOrders;

    @FXML
    private Button buttonInventory;

    @FXML
    private Button buttonChart;

    @FXML
    private MenuItem buttonLogout;

    @FXML
    private MenuItem buttonAccount;

    @FXML
    private AnchorPane contentPane;

    @FXML
    private Label labelTitle;

    @FXML
    private Alert alert;

    private Node currentContent;

    private void clearContent() {
        if (currentContent != null) {
            contentPane.getChildren().remove(currentContent);
            currentContent = null;
        }
    }

    @FXML
    private void handleButtonNews(ActionEvent event) {
        buttonNews.setStyle("-fx-background-color: #303030;");
        buttonMenu.setStyle("");
        buttonInventory.setStyle("");
        buttonCustomerOrders.setStyle("");
        buttonChart.setStyle("");
        labelTitle.setText("News");

        try {
            clearContent();

            // Memuat konten dari file FXML terpisah
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/Menu/News.fxml"));
            AnchorPane loadedPane = loader.load();

            // Mengganti konten di contentPane dengan konten baru
            contentPane.getChildren().setAll(loadedPane);
            currentContent = loadedPane;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonMenu(ActionEvent event) {
        buttonMenu.setStyle("-fx-background-color:  #303030;");
        buttonNews.setStyle("-fx-text-fill: #00ffb7");
        buttonInventory.setStyle("");
        buttonCustomerOrders.setStyle("");
        buttonChart.setStyle("");
        labelTitle.setText("Menu");

        try {
            clearContent();

            // Memuat konten dari file FXML terpisah
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/Menu/Menu.fxml"));
            AnchorPane loadedPane = loader.load();

            // Mengganti konten di contentPane dengan konten baru
            contentPane.getChildren().setAll(loadedPane);
            currentContent = loadedPane;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonInventory(ActionEvent event) {
        buttonInventory.setStyle("-fx-background-color:  #303030;");
        buttonNews.setStyle("-fx-text-fill: #00ffb7");
        buttonMenu.setStyle("");
        buttonCustomerOrders.setStyle("");
        buttonChart.setStyle("");
        labelTitle.setText("Inventory");

        try {
            // Menghapus konten yang ada
            clearContent();

            // Memuat konten dari file FXML terpisah
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/Menu/Inventory.fxml"));
            AnchorPane loadedPane = loader.load();

            // Mengganti konten di contentPane dengan konten baru
            contentPane.getChildren().setAll(loadedPane);
            currentContent = loadedPane;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonChart() {
        buttonChart.setStyle("-fx-background-color:  #303030;");
        buttonCustomerOrders.setStyle("");
        buttonInventory.setStyle("");
        buttonNews.setStyle("-fx-text-fill: #00ffb7");
        buttonMenu.setStyle("");
        labelTitle.setText("Chart");

        try {
            // Menghapus konten yang ada
            clearContent();

            // Memuat konten dari file FXML terpisah
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/Menu/Chart.fxml"));
            AnchorPane loadedPane = loader.load();

            // Mengganti konten di contentPane dengan konten baru
            contentPane.getChildren().setAll(loadedPane);
            currentContent = loadedPane;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonCustomerOrders() {
        buttonCustomerOrders.setStyle("-fx-background-color:  #303030;");
        buttonInventory.setStyle("");
        buttonNews.setStyle("-fx-text-fill: #00ffb7");
        buttonMenu.setStyle("");
        buttonChart.setStyle("");
        labelTitle.setText("Customer Order");

        try {
            // Menghapus konten yang ada
            clearContent();

            // Memuat konten dari file FXML terpisah
            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/Menu/CustomerOrder.fxml"));
            AnchorPane loadedPane = loader.load();

            // Mengganti konten di contentPane dengan konten baru
            contentPane.getChildren().setAll(loadedPane);
            currentContent = loadedPane;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    public void handleButtonAccount(ActionEvent event) throws IOException {
        labelTitle.setText("Account Information");
        try {
            clearContent();

            FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/Menu/Account.fxml"));
            AnchorPane loadedPane = loader.load();

            contentPane.getChildren().setAll(loadedPane);
            currentContent = loadedPane;
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleButtonLogout(ActionEvent event) throws IOException {
        try {

            alert = new Alert(AlertType.CONFIRMATION);
            alert.setTitle("Message");
            alert.setHeaderText(null);
            alert.setContentText("Are you sure you want to logout?");
            Optional<ButtonType> option = alert.showAndWait();

            if (option.get().equals(ButtonType.OK)) {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("../Content/LoginRegister/SignIn.fxml"));
                Stage stage = new Stage();
                stage.initModality(Modality.APPLICATION_MODAL);
                stage.setScene(new Scene(loader.load()));
                stage.show();

                Stage loginStage = (Stage) buttonLogout.getParentPopup().getOwnerWindow();
                loginStage.close();
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void displayNamalengkap() {
        String namaLengkap = Data.namaLengkap;
        textNamaLengkap.setText(namaLengkap);
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        displayNamalengkap();
        handleButtonNews(new ActionEvent());
    }

}
