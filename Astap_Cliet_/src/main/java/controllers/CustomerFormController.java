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
import models.Customer;
import models.Provider;
import models.RequestModel;
import models.ResponseModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class CustomerFormController {
    public TextField textfield_address;
    public TextField textfield_name;
    public TextField textfield_phonenumber;
    public TextField textfield_email;
    public Button button_ok;
    public Button button_back;
    PrintWriter out;
    int CustomerId;
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

    @FXML
    public void initialize() throws IOException {
        if (ClientSocket.getInstance().getCustomerId() != -1) {
            RequestModel requestModel = new RequestModel();
            Customer prevCustomer = new Customer();
            prevCustomer.setfId(ClientSocket.getInstance().getCustomerId());
            requestModel.setRequestMessage(ClientSocket.gson.toJson(prevCustomer));
            requestModel.setRequestType(RequestType.GetCustomer);
            out.println(ClientSocket.gson.toJson(requestModel));
            out.flush();
            String answer;
            answer = in.readLine();
            ResponseModel responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
            Customer customer = ClientSocket.gson.fromJson(responseModel.getResponseMessage(), Customer.class);
            textfield_name.setText(customer.getfName());
            textfield_address.setText(customer.getfAddress());
            textfield_phonenumber.setText(customer.getfPhoneNumber());
            textfield_email.setText(customer.getfEmail());
            CustomerId = customer.getfId();
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
        else try{
        Customer customer = new Customer(textfield_name.getText(),textfield_address.getText(),textfield_phonenumber.getText(),textfield_email.getText());
        RequestModel requestModel = new RequestModel();
        if (ClientSocket.getInstance().getCustomerId() == -1)
            requestModel.setRequestType(RequestType.CreateCustomer);
        else {
            customer.setfId(CustomerId);
            requestModel.setRequestType(RequestType.UpdateCustomer);
        }
        requestModel.setRequestMessage(ClientSocket.gson.toJson(customer));
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/CustomerScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(this.dialogStage);
            alert.setTitle("Отлично!");
            alert.setHeaderText("Операция прошла успешно!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStyleClass().add("myDialog");
            alert.showAndWait();}
        catch (Exception e) {

        }
    }

    public void Back(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/CustomerScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}
