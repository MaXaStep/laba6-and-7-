package controllers;

import Utils.ClientSocket;
import Utils.RequestType;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.control.DialogPane;
import javafx.stage.Stage;
import models.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class OrderFormController {
    public Button button_back;
    public Button button_ok;
    public ChoiceBox choicebox_provider;
    public ChoiceBox choicebox_customer;
    public ChoiceBox choicebox_status;
    PrintWriter out;
    int OrderId;
    ArrayList<Customer> customers;
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

    @FXML
    public void initialize() throws IOException {
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
        requestModel = new RequestModel();
        requestModel.setRequestMessage("");
        requestModel.setRequestType(RequestType.ShowCustomers);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        answer = null;
        try {
            answer = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        customers = new ArrayList<Customer>();
        responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
        listType = new TypeToken<ArrayList<Customer>>() {
        }.getType();
        customers = ClientSocket.gson.fromJson(responseModel.getResponseMessage(), listType);
        choicebox_status.getItems().removeAll(choicebox_status.getItems());
        choicebox_customer.getItems().removeAll(choicebox_customer.getItems());
        choicebox_provider.getItems().removeAll(choicebox_provider.getItems());
        for (Customer tempCustomer :
                customers) {
            choicebox_customer.getItems().add(tempCustomer.getfName());
        }
        for (Provider tempProvider:
             providers) {
            choicebox_provider.getItems().add(tempProvider.getfName());
        }
        choicebox_provider.getItems().addAll();
        choicebox_status.getItems().addAll("Ожидается поставка", "Товары на складе", "Товары отгружены со склада");
        if (ClientSocket.getInstance().getOrderId() != -1) {
            requestModel = new RequestModel();
            Order prevOrder = new Order();
            prevOrder.setfId(ClientSocket.getInstance().getOrderId());
            requestModel.setRequestMessage(ClientSocket.gson.toJson(prevOrder));
            requestModel.setRequestType(RequestType.GetOrder);
            out.println(ClientSocket.gson.toJson(requestModel));
            out.flush();
            answer = in.readLine();
            responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
            Order order = ClientSocket.gson.fromJson(responseModel.getResponseMessage(), Order.class);
            choicebox_customer.getSelectionModel().select((order.getfCustomer().getfName()));
            choicebox_provider.getSelectionModel().select(order.getfProvider().getfName());
            choicebox_status.getSelectionModel().select(order.getfStatus());
            OrderId = order.getfId();
        }

    }

    public void Ok_Pressed(ActionEvent actionEvent) throws IOException {
        if(choicebox_provider.getSelectionModel().getSelectedItem()== null||choicebox_customer.getSelectionModel().getSelectedItem() == null||choicebox_status.getSelectionModel().getSelectedItem() == null) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.dialogStage);
            alert.setTitle("Запись не добавлена");
            alert.setHeaderText("Заполните пустые поля!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStyleClass().add("myDialog");
            alert.showAndWait();
        }

        else {try {
            int customerId = customers.get(choicebox_customer.getSelectionModel().getSelectedIndex()).getfId();
            int providerId = providers.get(choicebox_provider.getSelectionModel().getSelectedIndex()).getfId();
            Order order = new Order(providerId,customerId,choicebox_status.getSelectionModel().getSelectedItem().toString());
            RequestModel requestModel = new RequestModel();
            if (ClientSocket.getInstance().getOrderId() == -1)
                requestModel.setRequestType(RequestType.CreateOrder);
            else {
                order.setfId(OrderId);
                requestModel.setRequestType(RequestType.UpdateOrder);
            }
            requestModel.setRequestMessage(ClientSocket.gson.toJson(order));
            out.println(ClientSocket.gson.toJson(requestModel));
            out.flush();
            Stage stage = (Stage) button_back.getScene().getWindow();
            Parent root = FXMLLoader.load(getClass().getResource("/OrderScene.fxml"));
            Scene newScene = new Scene(root);
            stage.setScene(newScene);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(this.dialogStage);
            alert.setTitle("Отлично!");
            alert.setHeaderText("Операция прошла успешно!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStyleClass().add("myDialog");
            alert.showAndWait();
        }
        catch (Exception e){

        }}
    }

    public void Back_Pressed(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/OrderScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}
