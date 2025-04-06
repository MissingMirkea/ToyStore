package md.ceiti.ms.hibernate.controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import md.ceiti.ms.hibernate.model.dao.impl.SalesDAOImpl;
import md.ceiti.ms.hibernate.model.entity.Sales;

import java.io.IOException;
import java.util.List;

public class SalesController {
    @FXML private MenuItem salesItemMain;
    @FXML private MenuItem salesItemToys;
    @FXML private MenuItem salesItemSales;
    @FXML private MenuItem salesItemOoS;
    @FXML private Button salesButtonTopSales;
    @FXML private Button salesButtonTodaySales;
    @FXML private Button salesReset;
    @FXML private MenuBar salesMenuBar;
    @FXML private TableView<Sales> salesTable;
    @FXML private TableColumn<Sales, String> salesColumnID;
    @FXML private TableColumn<Sales, String> salesColumnToysName;
    @FXML private TableColumn<Sales, Integer> salesColumnQuantity;
    @FXML private TableColumn<Sales, String> salesColumnDate;
    @FXML private TableColumn<Sales, Double> salesColumnTotal;

    private final SalesDAOImpl salesDAO = new SalesDAOImpl();

    @FXML
    public void initialize() {
        salesItemMain.setOnAction(this::loadPage);
        salesItemToys.setOnAction(this::loadPage);
        salesItemSales.setOnAction(this::loadPage);
        salesItemOoS.setOnAction(this::loadPage);
        salesButtonTodaySales.setOnAction(event -> loadTodaySales());
        salesButtonTopSales.setOnAction(event -> loadTopSales());
        salesReset.setOnAction(event -> populateTable());
        populateTable();
    }

    private void loadPage(ActionEvent event) {
        String pageName = null;
        Stage currentStage = null;
        if (event.getSource() instanceof MenuItem source) {
            pageName = (String) source.getUserData();
            currentStage = (Stage) salesMenuBar.getScene().getWindow();
        }

        if (pageName != null && currentStage != null) {
            try {
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/md/ceiti/ms/hibernate/view/" + pageName + "-page.fxml"));
                Parent root = loader.load();
                Scene scene = new Scene(root);
                currentStage.setScene(scene);
                currentStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void populateTable() {
        List<Sales> salesList = salesDAO.getAll();
        ObservableList<Sales> observableList = FXCollections.observableArrayList(salesList);
        salesTable.setItems(observableList);
        salesColumnID.setCellValueFactory(new PropertyValueFactory<>("id"));
        salesColumnToysName.setCellValueFactory(new PropertyValueFactory<>("toysName"));
        salesColumnQuantity.setCellValueFactory(new PropertyValueFactory<>("cantity"));
        salesColumnDate.setCellValueFactory(new PropertyValueFactory<>("transaction_date"));
        salesColumnTotal.setCellValueFactory(new PropertyValueFactory<>("total"));

    }

    private void loadTodaySales() {
        List<Sales> todaySalesList = salesDAO.todaySales();
        salesTable.setItems(FXCollections.observableArrayList(todaySalesList));
    }

    private void loadTopSales() {
        List<Sales> topSalesList = salesDAO.topSales();
        salesTable.setItems(FXCollections.observableArrayList(topSalesList));
    }
}
