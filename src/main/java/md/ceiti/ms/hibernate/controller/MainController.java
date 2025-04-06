package md.ceiti.ms.hibernate.controller;


import java.io.IOException;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.MenuItem;
import javafx.stage.Stage;
import md.ceiti.ms.hibernate.model.report.ReportExporter;

public class MainController {
    @FXML
    private Button buttonToys;
    @FXML
    private Button buttonOoS;
    @FXML
    private Button buttonSales;
    @FXML
    private MenuItem menuMain;
    @FXML
    private MenuItem menuToys;
    @FXML
    private MenuItem menuSales;
    @FXML
    private MenuItem menuOoS;
    private Stage stage;

    public MainController() {
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void initialize() {
        this.buttonToys.setOnAction(this::loadPage);
        this.buttonOoS.setOnAction(this::loadPage);
        this.buttonSales.setOnAction(this::loadPage);
        this.menuMain.setOnAction(this::loadPage);
        this.menuToys.setOnAction(this::loadPage);
        this.menuSales.setOnAction(this::loadPage);
        this.menuOoS.setOnAction(this::loadPage);
    }

    public void loadPage(ActionEvent event) {
        String pageName = null;
        Stage currentStage = null;
        if (event.getSource() instanceof Button) {
            Button source = (Button)event.getSource();
            pageName = (String)source.getUserData();
            currentStage = (Stage)source.getScene().getWindow();
        } else if (event.getSource() instanceof MenuItem) {
            MenuItem source = (MenuItem)event.getSource();
            pageName = (String)source.getUserData();
            currentStage = (Stage)this.menuMain.getParentPopup().getOwnerWindow();
        }

        if (pageName != null && currentStage != null) {
            try {
                System.out.println("Loading page: " + pageName + "-page.fxml");
                FXMLLoader loader = new FXMLLoader(getClass().getResource("/md/ceiti/ms/hibernate/view/" + pageName + "-page.fxml"));
                Parent root = (Parent)loader.load();
                Scene scene = new Scene(root);
                currentStage.setScene(scene);
                currentStage.show();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    @FXML
    public void generateRaport(){
        ReportExporter.generatePdfReport();
    }
}
