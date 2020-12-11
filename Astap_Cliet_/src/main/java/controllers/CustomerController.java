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

public class CustomerController implements Initializable {
    public TableView<CustomerModel> tableview_customers;
    public TableColumn<CustomerModel,String> column_name;
    public TableColumn<CustomerModel,String> column_id;
    public TableColumn<CustomerModel,String> column_address;
    public TableColumn<CustomerModel,String> column_phonenumber;
    public TableColumn<CustomerModel,String> column_email;
    public Button button_add;
    public Button button_edit;
    public Button button_delete;
    public Button button_back;
    private Stage dialogStage;

    public void Delete(ActionEvent actionEvent) {
        RequestModel requestModel = new RequestModel();
        CustomerModel customerModel = tableview_customers.getSelectionModel().getSelectedItem();
        requestModel.setRequestMessage(ClientSocket.gson.toJson(new Customer(customerModel.getName(),customerModel.getAddress(),customerModel.getPhoneNumber(),customerModel.getEmail(),customerModel.getId())));
        requestModel.setRequestType(RequestType.DeleteCustomer);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        tableview_customers.getItems().remove(customerModel);
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

    public void Add(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/CustomerForm.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);

    }
    PrintWriter out;
    ArrayList<Customer> customers;

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
        column_phonenumber.setCellValueFactory(new PropertyValueFactory<>("PhoneNumber"));
        column_address.setCellValueFactory(new PropertyValueFactory<>("Address"));
        column_email.setCellValueFactory(new PropertyValueFactory<>("Email"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        column_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        RequestModel requestModel = new RequestModel();
        requestModel.setRequestMessage("");
        requestModel.setRequestType(RequestType.ShowCustomers);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        String answer = null;
        try {
            answer = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        customers = new ArrayList<Customer>();
        ResponseModel responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
        Type listType = new TypeToken<ArrayList<Customer>>() {
        }.getType();
        customers = ClientSocket.gson.fromJson(responseModel.getResponseMessage(), listType);
        ObservableList<CustomerModel> list = FXCollections.observableArrayList();
        for (int i = 0; i < customers.size(); i++) {
            CustomerModel model = new CustomerModel(customers.get(i).getfId(), customers.get(i).getfName(), customers.get(i).getfPhoneNumber(),customers.get(i).getfEmail(),customers.get(i).getfAddress());
            list.add(model);
        }
        tableview_customers.setItems((ObservableList<CustomerModel>) list);
    }
    public void Clicked(MouseEvent mouseEvent) {
        if(tableview_customers.getSelectionModel().getSelectedItem()!=null){
            button_delete.setDisable(false);
            button_edit.setDisable(false);
        }
        else {
            button_edit.setDisable(true);
            button_delete.setDisable(true);
        }
    }
    public void Edit(ActionEvent actionEvent)  throws IOException {
        CustomerModel customerModel = tableview_customers.getSelectionModel().getSelectedItem();
        ClientSocket.getInstance().setCustomerId(customerModel.getId());
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/CustomerForm.fxml"));
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
