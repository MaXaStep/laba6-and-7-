package controllers;

import Utils.ClientSocket;
import Utils.RequestType;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.Product;
import models.RequestModel;
import models.ResponseModel;
import models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class ProductFormController {
    public Button button_ok;
    public Button button_back;
    public TextField textfield_type;
    public TextField textfield_name;
    PrintWriter out;
    ArrayList<User> users;
    int ProductId;
    private Stage dialogStage;

    {
        try {
            out = new PrintWriter(ClientSocket.getInstance().getSocket().getOutputStream());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    BufferedReader in;

    {
        try {
            in = new BufferedReader(new InputStreamReader(ClientSocket.getInstance().getSocket().getInputStream()));
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    public void Ok_Pressed(ActionEvent actionEvent) throws IOException {
        if( textfield_name.getText().equals("")||textfield_type.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.dialogStage);
            alert.setTitle("Запись не добавлена");
            alert.setHeaderText("Заполните пустын поля!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStyleClass().add("myDialog");
            alert.showAndWait();

        }
        else try {
            Product product = new Product(textfield_name.getText(), textfield_type.getText());
            RequestModel requestModel = new RequestModel();
            if (ClientSocket.getInstance().getProductId() == -1)
                requestModel.setRequestType(RequestType.CreateProduct);
            else {
                product.setfId(ProductId);
                requestModel.setRequestType(RequestType.UpdateProduct);
            }
            requestModel.setRequestMessage(ClientSocket.gson.toJson(product));
            out.println(ClientSocket.gson.toJson(requestModel));
            out.flush();
            Stage stage = (Stage) button_back.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/ProductScene.fxml"));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(this.dialogStage);
            alert.setTitle("Отлично!");
            alert.setHeaderText("Операция прошла успешно!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStyleClass().add("myDialog");
            alert.showAndWait();
        } catch (Exception e) {

        }
    }
    @FXML
    public void initialize() throws IOException {
        if (ClientSocket.getInstance().getProductId() != -1) {
            RequestModel requestModel = new RequestModel();
            Product prevProduct = new Product();
            prevProduct.setfId(ClientSocket.getInstance().getProductId());
            requestModel.setRequestMessage(ClientSocket.gson.toJson(prevProduct));
            requestModel.setRequestType(RequestType.GetProduct);
            out.println(ClientSocket.gson.toJson(requestModel));
            out.flush();
            String answer;
            answer = in.readLine();
            ResponseModel responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
            Product product = ClientSocket.gson.fromJson(responseModel.getResponseMessage(), Product.class);
            textfield_name.setText(product.getfName());
            textfield_type.setText(product.getfType());
            ProductId = product.getfId();
        }

    }
    public void Back(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/ProductScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}
