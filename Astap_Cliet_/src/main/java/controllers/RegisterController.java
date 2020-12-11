package controllers;

import Utils.ClientSocket;
import Utils.RequestType;
import Utils.ResponseStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.PasswordField;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import models.RegisterModel;
import models.RequestModel;
import models.ResponseModel;
import models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;

public class RegisterController {
    @FXML
    TextField textfield_username;
    @FXML
    PasswordField textfield_password;
    @FXML
    PasswordField textfield_confirmpassword;
    @FXML
    Label label_message;
    @FXML
    Button button_signup;
    @FXML
    Button button_back;
    @FXML
    TextField textfield_login;
    @FXML
    TextField textfield_phonenumber;
    @FXML
    TextField textfield_address;
    @FXML
    TextField textfield_email;

    @FXML
    void Signup(ActionEvent actionEvent) throws IOException {
        label_message.setVisible(false);
        PrintWriter out = new PrintWriter(ClientSocket.getInstance().getSocket().getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(ClientSocket.getInstance().getSocket().getInputStream()));
        RegisterModel registerModel = new RegisterModel(textfield_login.getText(), textfield_username.getText(), textfield_password.getText(), textfield_confirmpassword.getText(), textfield_email.getText(), textfield_phonenumber.getText(), textfield_address.getText());
        if (!registerModel.getPassword().equals(registerModel.getConfirmPassword())) {
            label_message.setText("Пароли не совпадают");
            label_message.setVisible(true);
            return;
        }
        if (registerModel.getPassword().equals("") || registerModel.getAddress().equals("") || registerModel.getConfirmPassword().equals("") || registerModel.getEmail().equals("") || registerModel.getPhonenumber().equals("") || registerModel.getUsername().equals("")) {
            label_message.setText("Не все поля заполнены.");
            label_message.setVisible(true);
            return;
        }
        RequestModel requestModel = new RequestModel();
        requestModel.setRequestMessage(ClientSocket.gson.toJson(registerModel));
        requestModel.setRequestType(RequestType.SignUp);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        String answer = in.readLine();
        ResponseModel responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            label_message.setVisible(false);
            ClientSocket.getInstance().setUser(ClientSocket.gson.fromJson(responseModel.getResponseMessage(), User.class));
            Stage stage = (Stage) button_back.getScene().getWindow();
            Parent root;
            if (ClientSocket.getInstance().getUser().getfRole().equals("Customer"))
                root = FXMLLoader.load(getClass().getResource("/CustomerOrdersScene.fxml"));
            else
                root = FXMLLoader.load(getClass().getResource("/ManagerMenuScene.fxml"));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        } else {
            label_message.setText("Пользователь с таким логином уже существует.");
            label_message.setVisible(true);
        }
    }

    @FXML
    void Back(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/IntroductionScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}
