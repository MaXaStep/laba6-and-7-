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
import models.Provider;
import models.RequestModel;
import models.ResponseModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class ProviderFormController {
    public Button button_ok;
    public TextField textfield_address;
    public TextField textfield_name;
    public TextField textfield_phonenumber;
    public TextField textfield_email;
    public Button button_back;
    PrintWriter out;
    int ProviderId;
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
       if( textfield_address.getText().equals("")||textfield_name.getText().equals("")||textfield_phonenumber.getText().equals("")
                ||textfield_email.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.dialogStage);
            alert.setTitle("Запись не добавлена");
            alert.setHeaderText("Заполните пустые поля!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStyleClass().add("myDialog");
            alert.showAndWait();

        }
             else try {

                Provider provider = new Provider(textfield_name.getText(), textfield_address.getText(), textfield_phonenumber.getText(), textfield_email.getText());
                RequestModel requestModel = new RequestModel();
                if (ClientSocket.getInstance().getProviderId() == -1)
                    requestModel.setRequestType(RequestType.CreateProvider);
                else {
                    provider.setfId(ProviderId);
                    requestModel.setRequestType(RequestType.UpdateProvider);
                }
                requestModel.setRequestMessage(ClientSocket.gson.toJson(provider));
                out.println(ClientSocket.gson.toJson(requestModel));
                out.flush();
                Stage stage = (Stage) button_back.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/ProviderScene.fxml"));
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
        if (ClientSocket.getInstance().getProviderId() != -1) {
            RequestModel requestModel = new RequestModel();
            Provider prevProduct = new Provider();
            prevProduct.setfId(ClientSocket.getInstance().getProviderId());
            requestModel.setRequestMessage(ClientSocket.gson.toJson(prevProduct));
            requestModel.setRequestType(RequestType.GetProvider);
            out.println(ClientSocket.gson.toJson(requestModel));
            out.flush();
            String answer;
            answer = in.readLine();
            ResponseModel responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
            Provider provider = ClientSocket.gson.fromJson(responseModel.getResponseMessage(), Provider.class);
            textfield_name.setText(provider.getfName());
            textfield_address.setText(provider.getfAddress());
            textfield_phonenumber.setText(provider.getfPhonenumber());
            textfield_email.setText(provider.getfEmail());
            ProviderId = provider.getfId();
        }

    }

    public void Back(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/ProviderScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}
