import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

// Nama Kelompok : SnapTech
// Anggota : 
// 1. Prabawikan Azhar Himawan (22523307)
// 2. Hanif Nur Hidayat (22523067)
// 3. Daffa Al Fajri (2252327)
// 4. Farhan Gusti Pamungkas (22523091)
// 5. Kasyiful Kurobi Alqorrosyai (22523178)

public class Main extends Application { 
    @Override
    public void start(Stage stage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("Content/LoginRegister/SignIn.fxml"));

        Scene scene = new Scene(root);

        stage.setScene(scene);
        stage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        launch(args);
    }

}
