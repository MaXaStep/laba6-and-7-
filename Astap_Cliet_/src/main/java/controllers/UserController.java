package controllers;

import Utils.ClientSocket;
import Utils.RequestType;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class UserController implements Initializable {
    public TableColumn<UserModel, String> column_password;
    public TableColumn<UserModel, String> column_login;
    public TableColumn<UserModel, String> column_id;
    public TableColumn<UserModel, String> column_role;
    public TableColumn<UserModel, String> column_name;
    public TableView<UserModel> tableview_users;
    public Button button_delete;
    public Button button_add;
    public Button button_edit;
    public Button button_back;
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

    public void Add(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/UserForm.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);

    }

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        column_password.setCellValueFactory(new PropertyValueFactory<>("Password"));
        column_login.setCellValueFactory(new PropertyValueFactory<>("Login"));
        column_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        column_role.setCellValueFactory(new PropertyValueFactory<>("Role"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("Username"));
        RequestModel requestModel = new RequestModel();
        requestModel.setRequestMessage("");
        requestModel.setRequestType(RequestType.ShowUsers);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        String answer = null;
        try {
            answer = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        users = new ArrayList<User>();
        ResponseModel responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
        Type listType = new TypeToken<ArrayList<User>>() {
        }.getType();
        users = ClientSocket.gson.fromJson(responseModel.getResponseMessage(), listType);
        ObservableList<UserModel> list = FXCollections.observableArrayList();
        for(int i = 0; i<users.size();i++) {
            UserModel model = new UserModel(users.get(i).getfId(), users.get(i).getfLogin(), users.get(i).getfPassword(), users.get(i).getfUsername(), users.get(i).getfRole());
            list.add(model);
        }
        tableview_users.setItems((ObservableList<UserModel>) list);
    }

    public void Delete(ActionEvent actionEvent) {
        RequestModel requestModel = new RequestModel();
        UserModel userModel = tableview_users.getSelectionModel().getSelectedItem();
        requestModel.setRequestMessage(ClientSocket.gson.toJson(new User(userModel.getLogin(),userModel.getPassword(),userModel.getUsername(),userModel.getRole(),userModel.getId())));
        requestModel.setRequestType(RequestType.DeleteUser);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        tableview_users.getItems().remove(userModel);
        button_edit.setDisable(true);
        button_delete.setDisable(true);
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(this.dialogStage);
        alert.setTitle("Отлично!");
        alert.setHeaderText("Операция прошла успешно!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("myDialog");
        alert.showAndWait();
    }

    public void Edit(ActionEvent actionEvent) throws IOException {
        UserModel userModel = tableview_users.getSelectionModel().getSelectedItem();
        ClientSocket.getInstance().setUserId(userModel.getId());
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/UserForm.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void Back(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/ManagerMenuScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void Clicked(MouseEvent mouseEvent) {
        if(tableview_users.getSelectionModel().getSelectedItem()!=null){
            button_delete.setDisable(false);
            button_edit.setDisable(false);
        }
        else {
            button_edit.setDisable(true);
            button_delete.setDisable(true);
        }
    }
}
