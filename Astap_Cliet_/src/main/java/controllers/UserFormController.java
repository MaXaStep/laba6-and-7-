package controllers;

import Utils.ClientSocket;
import Utils.RequestType;
import Utils.ResponseStatus;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.Customer;
import models.RequestModel;
import models.ResponseModel;
import models.User;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.util.ArrayList;

public class UserFormController {

    public TextField textfield_password;
    public TextField textfield_name;
    public TextField textfield_login;
    public Button button_ok;
    public ChoiceBox checkbox_role;
    public Button button_back;
    public TextField textfield_address;
    public TextField textfield_email;
    public TextField textfield_phonenumber;
    public Label label_message;
    private int UserId;
    PrintWriter out;
    ArrayList<User> users;
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
        checkbox_role.getItems().removeAll(checkbox_role.getItems());
        checkbox_role.getItems().addAll("Заказчик", "Менеджер");
        if (ClientSocket.getInstance().getUserId() != -1) {
            RequestModel requestModel = new RequestModel();
            User prevUser = new User();
            prevUser.setfId(ClientSocket.getInstance().getUserId());
            requestModel.setRequestMessage(ClientSocket.gson.toJson(prevUser));
            requestModel.setRequestType(RequestType.GetUser);
            out.println(ClientSocket.gson.toJson(requestModel));
            out.flush();
            String answer;
            answer = in.readLine();
            ResponseModel responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
            User user = ClientSocket.gson.fromJson(responseModel.getResponseMessage(), User.class);
            checkbox_role.getSelectionModel().select(user.getfRole());
            if (user.getfCustomer() != null) {
                textfield_phonenumber.setText(user.getfCustomer().getfPhoneNumber());
                textfield_address.setText(user.getfCustomer().getfAddress());
                textfield_email.setText(user.getfCustomer().getfEmail());
            }
            textfield_name.setText(user.getfUsername());
            textfield_login.setText(user.getfLogin());
            textfield_password.setText(user.getfPassword());
            UserId = user.getfId();
        }

    }

    public void Ok_Pressed(ActionEvent actionEvent) throws IOException {
        if (textfield_password.getText().equals("") || textfield_login.getText().equals("") || textfield_name.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.dialogStage);
            alert.setTitle("Запись не добавлена");
            alert.setHeaderText("Заполните пустые поля!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStyleClass().add("myDialog");
            alert.showAndWait();
        }
            else try {
                label_message.setVisible(false);
                User user = new User();
                user.setfPassword(textfield_password.getText());
                user.setfLogin(textfield_login.getText());
                user.setfRole(checkbox_role.getSelectionModel().getSelectedItem().toString());
                user.setfUsername(textfield_name.getText());
                Customer customer = null;
                if (checkbox_role.getSelectionModel().getSelectedItem().toString().equals("Заказчик")) {
                    customer = new Customer();
                    customer.setfAddress(textfield_address.getText());
                    customer.setfEmail(textfield_email.getText());
                    customer.setfName(textfield_name.getText());
                    customer.setfPhoneNumber(textfield_phonenumber.getText());
                    customer.setfUser(user);
                }
                RequestModel requestModel = new RequestModel();
                if (ClientSocket.getInstance().getUserId() == -1)
                    requestModel.setRequestType(RequestType.CreateUser);
                else {
                    user.setfId(UserId);
                    requestModel.setRequestType(RequestType.UpdateUser);
                }
                requestModel.setRequestMessage(ClientSocket.gson.toJson(user));

                out.println(ClientSocket.gson.toJson(requestModel));
                out.flush();
                String answer;
                answer = in.readLine();
                ResponseModel responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
                if (responseModel.getResponseStatus() == ResponseStatus.Error) {
                    label_message.setVisible(true);
                    label_message.setText(responseModel.getResponseMessage());
                    return;
                }
                if (checkbox_role.getSelectionModel().getSelectedItem().toString().equals("Заказчик")) {
                    requestModel.setRequestMessage(ClientSocket.gson.toJson(customer));
                    if (ClientSocket.getInstance().getUserId() == -1) {
                        requestModel.setRequestType(RequestType.CreateCustomer);
                        out.println(ClientSocket.gson.toJson(requestModel));
                        out.flush();
                    }
                }


                Stage stage = (Stage) button_back.getScene().getWindow();
                Parent root = FXMLLoader.load(getClass().getResource("/ManagerMenuScene.fxml"));
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

    public void Back(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/UsersScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void Role(ActionEvent actionEvent) {
        if (checkbox_role.getSelectionModel().getSelectedItem().toString().equals("Заказчик")) {
            textfield_email.setDisable(false);
            textfield_address.setDisable(false);
            textfield_phonenumber.setDisable(false);
        } else {
            textfield_email.setDisable(true);
            textfield_address.setDisable(true);
            textfield_phonenumber.setDisable(true);
        }
    }
}
