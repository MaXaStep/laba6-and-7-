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
import javafx.stage.Modality;
import javafx.stage.Stage;
import models.Operation;
import models.Order;
import models.RequestModel;
import models.ResponseModel;

import java.io.*;
import java.lang.reflect.Type;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class CustomerOrdersController {
    public Label label_message;
    @FXML
    public ChoiceBox choice_report;
    public Button button_ok;
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

    @FXML
    void initialize() throws IOException {
        RequestModel requestModel = new RequestModel();
        requestModel.setRequestMessage(String.valueOf(ClientSocket.getInstance().getUser().getfId()));
        requestModel.setRequestType(RequestType.OrderReport);
        out.println(ClientSocket.gson.toJson(requestModel));
        out.flush();
        String answer = in.readLine();
        orders = new ArrayList<Order>();
        ResponseModel responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
        Type listType = new TypeToken<ArrayList<Order>>() {
        }.getType();
        orders = ClientSocket.gson.fromJson(responseModel.getResponseMessage(), listType);
        choice_report.getItems().removeAll(choice_report.getItems());
        for (Order order :
                orders) {
            choice_report.getItems().add(order.getfId());
        }
    }
    public void Ok_Pressed(ActionEvent actionEvent) throws IOException {
        if (choice_report.getSelectionModel().getSelectedItem() != null) {
            label_message.setVisible(false);
            int orderId = (int) choice_report.getSelectionModel().getSelectedItem();
            RequestModel requestModel = new RequestModel();
            requestModel.setRequestMessage(String.valueOf(orderId));
            requestModel.setRequestType(RequestType.Report);
            out.println(ClientSocket.gson.toJson(requestModel));
            out.flush();
            String answer = in.readLine();
            ResponseModel responseModel = ClientSocket.gson.fromJson(answer, ResponseModel.class);
            Order order = ClientSocket.gson.fromJson(responseModel.getResponseMessage(), Order.class);
            File file = new File("Report_" + DateTimeFormatter.ofPattern("yyyy-MM-dd_HH_mm_ss").format(LocalDateTime.now())+".txt");
            FileOutputStream fileOutputStream = new FileOutputStream(file);
            String titleString = "Отчёт по заказу №" + order.getfId()+"\n";
            String customerName = "Имя заказчика:" + order.getfCustomer().getfName()+"\n";
            String providerName = "Имя поставщика" + order.getfProvider().getfName()+"\n";
            fileOutputStream.write(titleString.getBytes());
            fileOutputStream.write(customerName.getBytes());
            fileOutputStream.write(providerName.getBytes());
            String line = "---------------------------------------------"+"\n";
            fileOutputStream.write(line.getBytes());
            if (order.getfOperation().stream().anyMatch(x -> x.getfType().equals("Приход"))) {
                Operation operation = order.getfOperation().stream().filter(x -> x.getfType().equals("Приход")).findFirst().get();
                String arrival = operation.getfType() + " " + operation.getfProduct().getfName()+" "+ operation.getfProductQuantity() + " " + operation.getfDate()+"\n";
                fileOutputStream.write(arrival.getBytes());
                if (order.getfOperation().stream().anyMatch(x -> x.getfType().equals("Расход"))) {
                    Operation operationOutgo = order.getfOperation().stream().filter(x -> x.getfType().equals("Расход")).findFirst().get();
                    fileOutputStream.write(line.getBytes());
                    String outgo = operationOutgo.getfType() + " " + operationOutgo.getfProduct().getfName()+" "+ operationOutgo.getfProductQuantity() + " " + operationOutgo.getfDate()+"\n";
                    fileOutputStream.write(outgo.getBytes());
                }
            }
            fileOutputStream.close();
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(this.dialogStage);
            alert.setTitle("Создание отчета");
            alert.setHeaderText("Отчет создан успешно!");
            DialogPane dialogPane = alert.getDialogPane();
            dialogPane.getStyleClass().add("myDialog");
            alert.showAndWait();
        }
        else  {
            label_message.setVisible(true);
        }
    }


    public void Back(ActionEvent actionEvent) throws IOException {
        Stage stage = (Stage) button_ok.getScene().getWindow();
        Parent root = FXMLLoader.load(getClass().getResource("/CustomerShowOrdersScene.fxml"));
        Scene newScene = new Scene(root);
        stage.setScene(newScene);
    }
}
