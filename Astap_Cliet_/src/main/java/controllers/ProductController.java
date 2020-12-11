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

public class ProductController implements Initializable {
    public TableColumn<ProductModel, String> column_id;
    public TableView<ProductModel> tableview_products;
    public Button button_add;
    public Button button_delete;
    public Button button_edit;
    public Button button_back;
    public TableColumn<ProductModel, String> column_type;
    public TableColumn<ProductModel, String> column_name;
    PrintWriter out;
    ArrayList<Product> products;
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
        column_type.setCellValueFactory(new PropertyValueFactory<ProductModel, String>("Type"));
        column_name.setCellValueFactory(new PropertyValueFactory<>("Name"));
        column_id.setCellValueFactory(new PropertyValueFactory<>("Id"));
        RequestModel requestModel = new RequestModel();
        requestModel.setRequestMessage("");
        requestModel.setRequestType(RequestType.ShowProducts);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        String answer = null;
        try {
            answer = in.readLine();
        } catch (IOException e) {
            e.printStackTrace();
        }
        products = new ArrayList<Product>();
        ResponseModel responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
        Type listType = new TypeToken<ArrayList<Product>>() {
        }.getType();
        products = ClientSocket.gson.fromJson(responseModel.getResponseMessage(), listType);
        ObservableList<ProductModel> list = FXCollections.observableArrayList();
        for (int i = 0; i < products.size(); i++) {
            ProductModel model = new ProductModel(products.get(i).getfId(), products.get(i).getfName(), products.get(i).getfType());
            list.add(model);
        }
        tableview_products.setItems((ObservableList<ProductModel>) list);
    }
    public void Clicked(MouseEvent mouseEvent) {
        if(tableview_products.getSelectionModel().getSelectedItem()!=null){
            button_delete.setDisable(false);
            button_edit.setDisable(false);
        }
        else {
            button_edit.setDisable(true);
            button_delete.setDisable(true);
        }
    }
    public void Add(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/ProductForm.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }

    public void Delete(ActionEvent actionEvent) {
        RequestModel requestModel = new RequestModel();
        ProductModel productModel = tableview_products.getSelectionModel().getSelectedItem();
        requestModel.setRequestMessage(ClientSocket.gson.toJson(new Product(productModel.getName(),productModel.getType(),productModel.getId())));
        requestModel.setRequestType(RequestType.DeleteProduct);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        tableview_products.getItems().remove(productModel);
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
        ProductModel productModel = tableview_products.getSelectionModel().getSelectedItem();
        ClientSocket.getInstance().setProductId(productModel.getId());
        Stage stage = (Stage) button_back.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/ProductForm.fxml"));
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
