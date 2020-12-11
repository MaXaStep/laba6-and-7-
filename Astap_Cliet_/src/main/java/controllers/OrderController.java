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

public class OrderController implements Initializable {
    public TableColumn<OrderModel,String> column_status;
    public TableColumn<OrderModel,String> column_provider;
    public TableColumn<OrderModel,String> column_id;
    public TableView<OrderModel> tableview_orders;
    public TableColumn<OrderModel,String> column_customer;
    public Button button_add;
    public Button button_delete;
    public Button button_edit;
    public Button button_back;
    PrintWriter out;
    ArrayList<Order> orders;
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
    public void Clicked(MouseEvent mouseEvent) {
        if(tableview_orders.getSelectionModel().getSelectedItem()!=null){
            button_delete.setDisable(false);
            button_edit.setDisable(false);
        }
        else {
            button_edit.setDisable(true);
            button_delete.setDisable(true);
        }
    }
    @Override
    public void initialize(URL location, ResourceBundle resources) {
        column_status.setCellValueFactory(new PropertyValueFactory("Status"));
        column_provider.setCellValueFactory(new PropertyValueFactory<>("Provider"));
        column_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        column_customer.setCellValueFactory(new PropertyValueFactory<>("Customer"));
        RequestModel requestModel = new RequestModel();
        requestModel.setRequestMessage("");
        requestModel.setRequestType(RequestType.ShowOrders);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        String answer = null;
        try {
            answer = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        orders = new ArrayList<Order>();
        ResponseModel responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
        Type listType = new TypeToken<ArrayList<Order>>() {
        }.getType();
        orders = ClientSocket.gson.fromJson(responseModel.getResponseMessage(), listType);
        ObservableList<OrderModel> list = FXCollections.observableArrayList();
        for (int i = 0; i < orders.size(); i++) {
            OrderModel model = new OrderModel(orders.get(i).getfId(), orders.get(i).getfStatus(), orders.get(i).getfProvider().getfName(),orders.get(i).getfCustomer().getfName());
            list.add(model);
        }
        tableview_orders.setItems((ObservableList<OrderModel>) list);
    }
    public void Add(ActionEvent actionEvent)  throws IOException {
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/OrderForm.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void Delete(ActionEvent actionEvent) {
        RequestModel requestModel = new RequestModel();
        OrderModel orderModel = tableview_orders.getSelectionModel().getSelectedItem();
        requestModel.setRequestMessage(ClientSocket.gson.toJson(new Order(orderModel.getStatus(),orderModel.getId())));
        requestModel.setRequestType(RequestType.DeleteOrder);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        tableview_orders.getItems().remove(orderModel);
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

    public void Edit(ActionEvent actionEvent)  throws IOException {
        OrderModel orderModel = tableview_orders.getSelectionModel().getSelectedItem();
        ClientSocket.getInstance().setOrderId(orderModel.getId());
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/OrderForm.fxml"));
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
