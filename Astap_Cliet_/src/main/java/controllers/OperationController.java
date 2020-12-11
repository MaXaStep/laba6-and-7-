package controllers;

import Utils.ClientSocket;
import Utils.RequestType;
import com.google.gson.reflect.TypeToken;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;
import models.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.sql.Date;
import java.util.ArrayList;

public class OperationController {
    public ChoiceBox choicebox_type;
    public DatePicker datepicker_date;
    public ChoiceBox choicebox_product;
    public Button button_ok;
    public TextField textfield_quantity;
    public ChoiceBox choicebox_order;
    public Label label_message;
    PrintWriter out;
    ArrayList<Product> products;
    ArrayList<Order> orders;
    private Stage dialogStage;

    public static boolean isInt(String x) throws NumberFormatException
    {
        try {
            Integer.parseInt(x);
            return true;
        } catch(Exception e) {
            return false;
        }
    }

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
    public void initialize() {
        choicebox_type.getItems().removeAll(choicebox_type.getItems());
        choicebox_type.getItems().addAll("Приход", "Расход");
    }

    public void Ok_Pressed(ActionEvent actionEvent) throws IOException {
        label_message.setVisible(false);
        RequestModel requestModel = new RequestModel();
        Operation operation = new Operation();
        Product product = null;
        Order order = null;
        if (choicebox_product.getSelectionModel().getSelectedItem() == null || datepicker_date.getValue()==null||choicebox_order.getSelectionModel().getSelectedItem() == null
                || choicebox_type.getSelectionModel().getSelectedItem() == null ||
                textfield_quantity.getText().equals("")) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.dialogStage);
            alert.setTitle("Ошибка ввода");
            alert.setHeaderText("Не все поля заполнены!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStyleClass().add("myDialog");
            alert.showAndWait();
            return;
        }
        if (isInt(textfield_quantity.getText())){
        for (Order tempOrder :
                orders) {
            if (choicebox_order.getSelectionModel().getSelectedItem().equals(tempOrder.getfId()))
                order = tempOrder;
        }

        for (Product tempProduct :
                products) {
            if (choicebox_product.getSelectionModel().getSelectedItem().equals(tempProduct.getfName()))
                product = tempProduct;
        }
        operation.setfOrder(order);
        operation.setfType(choicebox_type.getSelectionModel().getSelectedItem().toString());
        operation.setfProductQuantity(Integer.parseInt(textfield_quantity.getText()));
        operation.setfUser(ClientSocket.getInstance().getUser());
        operation.setfProduct(product);
        operation.setfDate(Date.valueOf(datepicker_date.getValue()));
        requestModel.setRequestMessage(ClientSocket.gson.toJson(operation));
        requestModel.setRequestType(RequestType.CreateOperation);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        if (choicebox_type.getSelectionModel().getSelectedItem().equals("Приход"))
            order.setfStatus("Товары на складе");
        else {
            order.setfStatus("Товары отгружены со склада");
        }
        requestModel.setRequestType(RequestType.UpdateOrder);
        requestModel.setRequestMessage(ClientSocket.gson.toJson(order));
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        Stage stage = (Stage) button_ok.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/ManagerMenuScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.initOwner(this.dialogStage);
        alert.setTitle("Регистрация операции");
        alert.setHeaderText("Операция зарегистирована!");
        DialogPane dialogPane = alert.getDialogPane();
        dialogPane.getStyleClass().add("myDialog");
        alert.showAndWait();}
        else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.initOwner(this.dialogStage);
            alert.setTitle("Ошибка ввода");
            alert.setHeaderText("Количество товаров должно быть числом!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStyleClass().add("myDialog");
            alert.showAndWait();
            return;
        }
    }

    public void CheckBox(ActionEvent actionEvent) throws IOException {
        boolean isArrival;
        if (choicebox_type.getSelectionModel().getSelectedItem().equals("Приход")) {
            isArrival = true;
        } else {
            isArrival = false;
        }
        RequestModel requestModel = new RequestModel();
        requestModel.setRequestMessage("");
        requestModel.setRequestType(RequestType.ShowOrders);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        String answer = in.readLine();
        orders = new ArrayList<Order>();
        ResponseModel responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
        Type listType = new TypeToken<ArrayList<Order>>() {
        }.getType();
        orders = ClientSocket.gson.fromJson(responseModel.getResponseMessage(), listType);
        choicebox_order.getItems().removeAll(choicebox_order.getItems());
        for (Order order :
                orders) {
            if (isArrival) {
                if (order.getfStatus().equals("Ожидается поставка"))
                    choicebox_order.getItems().add(order.getfId());
            } else {
                if (order.getfStatus().equals("Товары на складе"))
                    choicebox_order.getItems().add(order.getfId());
            }
        }
        requestModel = new RequestModel();
        requestModel.setRequestMessage("");
        requestModel.setRequestType(RequestType.ShowProducts);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        answer = in.readLine();
        products = new ArrayList<Product>();
        responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
        listType = new TypeToken<ArrayList<Product>>() {
        }.getType();
        products = ClientSocket.gson.fromJson(responseModel.getResponseMessage(), listType);
        choicebox_product.getItems().removeAll(choicebox_product.getItems());
        for (Product product :
                products) {
            choicebox_product.getItems().add(product.getfName());
        }
          choicebox_order.setDisable(false);
        textfield_quantity.setDisable(false);
        choicebox_product.setDisable(false);
        datepicker_date.setDisable(false);
    }

    public void Back(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_ok.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/ManagerMenuScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}
