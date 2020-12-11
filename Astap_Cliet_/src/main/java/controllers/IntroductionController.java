package controllers;

import Utils.ClientSocket;
import Utils.RequestType;
import Utils.ResponseStatus;
import com.google.gson.internal.$Gson$Preconditions;
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
import models.RequestModel;
import models.ResponseModel;
import models.User;

import java.io.*;
import java.net.Socket;

public class IntroductionController {
    public Label label_message;
    @FXML
    PasswordField textfield_password;
    @FXML
    TextField textfield_username;
    @FXML
    Button button_register;
    @FXML
    Button button_login;

    @FXML
    private void Login(ActionEvent event) throws IOException {
        Socket socket = ClientSocket.getInstance().getSocket();
        PrintWriter out = new PrintWriter(socket.getOutputStream());
        BufferedReader in = new BufferedReader(new InputStreamReader(socket.getInputStream()));
        User user = new User();
        user.setfLogin(textfield_username.getText());
        user.setfPassword(textfield_password.getText());
        RequestModel requestModel = new RequestModel();
        requestModel.setRequestMessage(ClientSocket.gson.toJson(user));
        requestModel.setRequestType(RequestType.Login);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        String answer = in.readLine();
        ResponseModel responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
        if (responseModel.getResponseStatus() == ResponseStatus.OK) {
            label_message.setVisible(false);
            ClientSocket.getInstance().setUser(ClientSocket.gson.fromJson(responseModel.getResponseMessage(), User.class));
            Stage stage = (Stage) button_login.getScene().getWindow();
            Parent root;
            if (ClientSocket.getInstance().getUser().getfRole().equals("Заказчик"))
                root = FXMLLoader.load(getClass().getResource("/CustomerShowOrdersScene.fxml"));
            else
                root = FXMLLoader.load(getClass().getResource("/ManagerMenuScene.fxml"));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
        } else {
            label_message.setVisible(true);
        }
    }

    @FXML
    private void Register(ActionEvent event) throws IOException {
        Stage stage = (Stage) button_login.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/RegisterScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}
