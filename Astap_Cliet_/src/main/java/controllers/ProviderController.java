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

public class ProviderController implements Initializable {
    public TableColumn<ProviderModel,String> column_id;
    public TableColumn<ProviderModel,String> column_name;
    public TableColumn<ProviderModel,String> column_address;
    public TableColumn<ProviderModel,String> column_phonenumber;
    public TableColumn<ProviderModel,String> column_email;
    public TableView<ProviderModel> tableview_providers;
    public Button button_add;
    public Button button_delete;
    public Button button_edit;
    public Button button_back;
    PrintWriter out;
    ArrayList<Provider> providers;
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

    @Override
    public void initialize(URL location, ResourceBundle resources) {
        column_email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        column_address.setCellValueFactory(new PropertyValueFactory<>("Address"));
        column_phonenumber.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        column_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        RequestModel requestModel = new RequestModel();
        requestModel.setRequestMessage("");
        requestModel.setRequestType(RequestType.ShowProviders);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        String answer = null;
        try {
            answer = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        providers = new ArrayList<Provider>();
        ResponseModel responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
        Type listType = new TypeToken<ArrayList<Provider>>() {
        }.getType();
        providers = ClientSocket.gson.fromJson(responseModel.getResponseMessage(), listType);
        ObservableList<ProviderModel> list = FXCollections.observableArrayList();
        for (int i = 0; i < providers.size(); i++) {
            ProviderModel model = new ProviderModel(providers.get(i).getfId(), providers.get(i).getfName(), providers.get(i).getfPhonenumber(),providers.get(i).getfEmail(),providers.get(i).getfAddress());
            list.add(model);
        }
        tableview_providers.setItems((ObservableList<ProviderModel>) list);
    }
    public void Add(ActionEvent actionEvent)  throws IOException {
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/ProviderForm.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void Delete(ActionEvent actionEvent) {
        RequestModel requestModel = new RequestModel();
        ProviderModel providerModel = tableview_providers.getSelectionModel().getSelectedItem();
        requestModel.setRequestMessage(ClientSocket.gson.toJson(new Provider(providerModel.getName(),providerModel.getAddress(),providerModel.getPhoneNumber(),providerModel.getEmail(),providerModel.getId())));
        requestModel.setRequestType(RequestType.DeleteProvider);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        tableview_providers.getItems().remove(providerModel);
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
    public void Clicked(MouseEvent mouseEvent) {
        if(tableview_providers.getSelectionModel().getSelectedItem()!=null){
            button_delete.setDisable(false);
            button_edit.setDisable(false);
        }
        else {
            button_edit.setDisable(true);
            button_delete.setDisable(true);
        }
    }
    public void Edit(ActionEvent actionEvent)  throws IOException {
        ProviderModel providerModel = tableview_providers.getSelectionModel().getSelectedItem();
        ClientSocket.getInstance().setProviderId(providerModel.getId());
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/ProviderForm.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void Back(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/ManagerMenuScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}
