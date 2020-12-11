package controllers;

import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;

public class ManagerMenuController {
    public Button button_createoperation;
    public Button button_showoperations;
    public Button button_createreport;
    public Button button_showdiagrams;
    public Button button_menu_provider;
    public Button button_menu_customer;
    public Button button_menu_order;
    public Button button_menu_user;
    public Button button_logout;
    public Button button_menu_products;

    public void CreateOperation(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_logout.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/OperationScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);

    }

    public void ShowOperations(ActionEvent actionEvent) throws IOException {

        Stage stage = (Stage) button_logout.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/OperationsInfoScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void CreateReport(ActionEvent actionEvent) throws IOException {

        Parent root = FXMLLoader.load(getClass().getResource("/ReportDialog.fxml"));
        Stage stage = new Stage();
        stage.setScene(new Scene(root));
        Stage parentStage = (Stage) button_logout.getScene().getWindow();
        stage.initOwner(parentStage);
        stage.initModality(Modality.APPLICATION_MODAL);
        stage.show();

    }

    public void ShowDiagrams(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_logout.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/DiagramScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void MenuProvider(ActionEvent actionEvent) throws IOException {

        Stage stage = (Stage) button_logout.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/ProviderScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void MenuCustomer(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_logout.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/CustomerScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);

    }

    public void MenuOrder(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_logout.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/OrderScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);

    }

    public void MenuUser(ActionEvent actionEvent) throws IOException {

        Stage stage = (Stage) button_logout.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/UsersScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void Logout(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_logout.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/IntroductionScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void MenuProducts(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_logout.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/ProductScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}
