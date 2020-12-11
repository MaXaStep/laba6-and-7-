package controllers;

import Utils.ClientSocket;
import Utils.RequestType;
import com.google.gson.reflect.TypeToken;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextArea;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import models.*;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.lang.reflect.Type;
import java.util.ArrayList;

public class OperationsInfoController {
    public Button button_back;
    public TableColumn<OperationModel,String> column_date;
    public TableColumn<OperationModel,String> column_quantity;
    public TableColumn<OperationModel,String> column_order;
    public TableColumn<OperationModel,String> column_type;
    public TableColumn<OperationModel,String> column_user;
    public TableColumn<OperationModel,String> column_product;
    public TableColumn<OperationModel,String> column_id;
    public TableView<OperationModel> tableview;
    PrintWriter out;
    ArrayList<Operation> operations;
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
    void initialize() {
        column_type.setCellValueFactory(new PropertyValueFactory("Type"));
        column_date.setCellValueFactory(new PropertyValueFactory<>("Date"));
        column_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        column_order.setCellValueFactory(new PropertyValueFactory<>("Order"));
        column_user.setCellValueFactory(new PropertyValueFactory<>("User"));
        column_product.setCellValueFactory(new PropertyValueFactory<>("Product"));
        column_quantity.setCellValueFactory(new PropertyValueFactory<>("Quantity"));
        RequestModel requestModel = new RequestModel();
        requestModel.setRequestMessage("");
        requestModel.setRequestType(RequestType.ShowOperations);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        String answer = null;
        try {
            answer = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        operations = new ArrayList<Operation>();
        ResponseModel responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
        Type listType = new TypeToken<ArrayList<Operation>>() {
        }.getType();
        operations = ClientSocket.gson.fromJson(responseModel.getResponseMessage(), listType);
        ObservableList<OperationModel> list = FXCollections.observableArrayList();
        for (int i = 0; i < operations.size(); i++) {
            OperationModel model = new OperationModel(operations.get(i).getfId(), operations.get(i).getfDate().toString(), String.valueOf(operations.get(i).getfOrder().getfId()) ,operations.get(i).getfProduct().getfName(),String.valueOf(operations.get(i).getfProductQuantity()),operations.get(i).getfUser().getfLogin(),operations.get(i).getfType());
            list.add(model);
        }
        tableview.setItems((ObservableList<OperationModel>) list);
        String operationString = "";

    }

    public void Back(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/ManagerMenuScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}
