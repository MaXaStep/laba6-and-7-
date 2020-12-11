import Utils.ClientSocket;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

public class MainFx extends Application {

    @Override
    public void start(Stage primaryStage) throws Exception{
        ClientSocket.getInstance().getSocket();
        Parent root = FXMLLoader.load(getClass().getResource("IntroductionScene.fxml"));
        primaryStage.setScene(new Scene(root));
        primaryStage.setTitle("Logistics");

        primaryStage.show();
    }


    public static void main(String[] args) {
        launch(args);
    }
}
