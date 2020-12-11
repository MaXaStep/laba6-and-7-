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
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.input.MouseEvent;
import javafx.stage.Stage;
import models.Order;
import models.OrderModel;
import models.RequestModel;
import models.ResponseModel;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.net.URL;
import java.util.ArrayList;
import java.util.ResourceBundle;

public class CustomerShowOrdersController implements Initializable {
    public TableColumn<OrderModel,String> column_status;
    public TableColumn<OrderModel,String> column_provider;
    public TableColumn<OrderModel,String> column_id;
    public TableView<OrderModel> tableview_orders;
    public TableColumn<OrderModel,String> column_customer;
    public Button button_add;
    public Button button_delete;
    public Button button_edit;
    public Button button_back;
    public Button button_report;
    PrintWriter out;
    ArrayList<Order> orders;

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
        column_status.setCellValueFactory(new PropertyValueFactory("Status"));
        column_provider.setCellValueFactory(new PropertyValueFactory<>("Provider"));
        column_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        column_customer.setCellValueFactory(new PropertyValueFactory<>("Customer"));
        RequestModel requestModel = new RequestModel();
        requestModel.setRequestMessage(String.valueOf(ClientSocket.getInstance().getUser().getfId()));
        requestModel.setRequestType(RequestType.OrderReport);
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
    public void Back(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/IntroductionScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
    public void Report(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/CustomerOrdersScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}
