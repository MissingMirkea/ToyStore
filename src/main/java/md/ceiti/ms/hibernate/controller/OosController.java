package md.ceiti.ms.hibernate.controller;
import java.io.IOException;
import java.util.List;
import java.util.Optional;

import javafx.application.Platform;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Insets;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.layout.GridPane;
import javafx.stage.Stage;
import javafx.util.Pair;
import md.ceiti.ms.hibernate.model.dao.impl.ToysDAOImpl;

import md.ceiti.ms.hibernate.model.entity.Toys;
import md.ceiti.ms.hibernate.model.entity.ToysOutOfStock;
import md.ceiti.ms.hibernate.util.HibernateUtil;
import org.hibernate.Session;

public class OosController {
    @FXML
    private TableView oosTable;
    @FXML private TableColumn<ToysOutOfStock, Integer> oosId;
    @FXML private TableColumn<ToysOutOfStock, String> oosName;
    @FXML private TableColumn<ToysOutOfStock, String> oosCountry;
    @FXML private TableColumn<ToysOutOfStock, String> oosTT;
    @FXML private TableColumn<ToysOutOfStock, String> oosAge;
    @FXML private TableColumn<ToysOutOfStock, String> oosCantity;
    @FXML private TableColumn<ToysOutOfStock, String> oosPrice;
    @FXML
    private MenuItem oosItemMain;
    @FXML
    private MenuItem oosItemToys;
    @FXML
    private MenuItem oosItemOos;
    @FXML
    private MenuItem oosItemSales;
    @FXML
    private Button oosButtonSearch;
    @FXML
    private Button oosRst;
    @FXML
    private Button oosButtonInsert;
    @FXML
    private Button oosButtonDelete;
    @FXML
    private Button oosButtonUpdate;


    private final ToysDAOImpl toysDAO = new ToysDAOImpl();


    public OosController() {
    }

    @FXML
    public void initialize() {
        this.oosItemOos.setOnAction(this::loadPage);
        this.oosItemMain.setOnAction(this::loadPage);
        this.oosItemSales.setOnAction(this::loadPage);
        this.oosItemToys.setOnAction(this::loadPage);

        oosButtonInsert.setOnAction(this::showToyInputDialog);
        oosButtonDelete.setOnAction(this::deleteToy);
        oosButtonUpdate.setOnAction(this::updateToy);
        oosRst.setOnAction(actionEvent -> populateTable());
        oosButtonSearch.setOnAction(actionEvent ->searchToys());




//        buttonInsert.setOnAction(this::showToyInputDialog);
//        buttonReset.setOnAction(event -> populateTable());
//        buttonMD.setOnAction(event -> toysMd());
//        buttonBuy.setOnAction(this::buyToy);
//        buttonChange.setOnAction(this::updateToy);
//        buttonDelete.setOnAction(this::deleteToy);
//        buttonSearch.setOnAction(this::searchToys);
        insertFromOtherTable();
        populateTable();
    }

    private void loadPage(ActionEvent event) {
        String pageName = null;
        Stage currentStage = null;
        if (event.getSource() instanceof MenuItem) {
            MenuItem source = (MenuItem)event.getSource();
            pageName = (String)source.getUserData();
            currentStage = (Stage)this.oosItemMain.getParentPopup().getOwnerWindow();
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

    private void showToyInputDialog(ActionEvent event) {
        // Creează un dialog personalizat
        Dialog<String> dialog = new Dialog<>();
        dialog.setTitle("Introducere date jucării");
        dialog.setHeaderText("Te rog să completezi câmpurile pentru jucăria ta");

        // Creează un GridPane pentru a organiza câmpurile
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);

        // Creează câmpurile de text pentru fiecare atribut
        TextField nameField = new TextField();
        nameField.setPromptText("Nume");

        TextField countryField = new TextField();
        countryField.setPromptText("Țara");

        TextField toyTypeField = new TextField();
        toyTypeField.setPromptText("Tip jucărie");

        TextField ageField = new TextField();
        ageField.setPromptText("Vârstă");

        TextField quantityField = new TextField();
        quantityField.setPromptText("Cantitate");

        TextField priceField = new TextField();
        priceField.setPromptText("Preț");

        // Adaugă câmpurile în GridPane
        grid.add(nameField, 0, 0);
        grid.add(countryField, 0, 1);
        grid.add(toyTypeField, 0, 2);
        grid.add(ageField, 0, 3);
        grid.add(quantityField, 0, 4);
        grid.add(priceField, 0, 5);

        // Adaugă GridPane în dialog
        dialog.getDialogPane().setContent(grid);

        // Setează butoanele pentru dialog
        dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

        // Așteaptă să fie apăsat un buton și preia textul
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == ButtonType.OK) {
                try {
                    // Creează un obiect Toys pe baza datelor introduse
                    String name = nameField.getText();
                    String country = countryField.getText();
                    String toyType = toyTypeField.getText();
                    int age = Integer.parseInt(ageField.getText());
                    int quantity = Integer.parseInt(quantityField.getText());
                    float price = Float.parseFloat(priceField.getText());

                    // Creează obiectul Toys și setează datele
                    Toys toy = new Toys();
                    toy.setName(name);
                    toy.setCountry(country);
                    toy.setToyType(toyType);
                    toy.setAge(age);
                    toy.setCantity(quantity);
                    toy.setPrice(price);

                    // Salvează în baza de date
                    toysDAO.insert(toy);

                    // Creează obiectul ToysOutOfStock și copiază datele
                    ToysOutOfStock outOfStockToy = new ToysOutOfStock();
                    outOfStockToy.setId(toy.getId());
                    outOfStockToy.setName(name);
                    outOfStockToy.setCountry(country);
                    outOfStockToy.setToyType(toyType);
                    outOfStockToy.setAge(age);
                    outOfStockToy.setCantity(quantity);
                    outOfStockToy.setPrice(price);

                    // Salvează și în tabelul ToysOutOfStock

                    return "Jucăria a fost adăugată în ambele tabele!";
                } catch (Exception e) {
                    e.printStackTrace();
                    return "Eroare la adăugare!";
                }
            }
            return null;  // Dacă se apasă Cancel
        });

        // Afișează dialogul
        dialog.showAndWait().ifPresent(result -> {
            System.out.println(result); // Afișează mesajul de succes sau eroare
        });

        populateTable(); // Actualizează tabelul cu noile date
    }

    public void insertFromOtherTable() {
        try (Session session = HibernateUtil.getSessionFactory().openSession()) {
            session.beginTransaction();

            String sql = """
            INSERT INTO Toys ( name, country, toyType,age, cantity, price)
            SELECT  name, country, toyType,age, cantity, price 
            FROM ToysOutOfStock
            WHERE NOT EXISTS (
                SELECT 1 FROM Toys WHERE Toys.id = ToysOutOfStock.id
            )
        """;

            session.createNativeQuery(sql).executeUpdate();
            session.getTransaction().commit();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void populateTable() {
        List<Toys> toysList = toysDAO.findAllOos();
        ObservableList<Toys> observableList = FXCollections.observableList(toysList);
        oosTable.setItems(observableList);
        oosId.setCellValueFactory(new PropertyValueFactory<>("id"));
        oosName.setCellValueFactory(new PropertyValueFactory<>("Name"));
        oosCountry.setCellValueFactory(new PropertyValueFactory<>("Country"));
        oosTT.setCellValueFactory(new PropertyValueFactory<>("ToyType"));
        oosAge.setCellValueFactory(new PropertyValueFactory<>("Age"));
        oosCantity.setCellValueFactory(new PropertyValueFactory<>("Cantity"));
        oosPrice.setCellValueFactory(new PropertyValueFactory<>("Price"));

    }



    private void updateToy(ActionEvent event) {
        // Preluăm selecția din tabel
        Toys selectedToy = (Toys) oosTable.getSelectionModel().getSelectedItem();

        if (selectedToy != null) {
            // Afișăm un dialog pentru a modifica datele jucăriei
            Dialog<Toys> dialog = new Dialog<>();
            dialog.setTitle("Actualizare date jucărie");
            dialog.setHeaderText("Completează câmpurile pentru a actualiza jucăria");

            // Creează un GridPane pentru a organiza câmpurile
            GridPane grid = new GridPane();
            grid.setHgap(10);
            grid.setVgap(10);

            // Creează câmpurile de text pentru fiecare atribut
            TextField nameField = new TextField(selectedToy.getName());
            nameField.setPromptText("Nume");

            TextField countryField = new TextField(selectedToy.getCountry());
            countryField.setPromptText("Țara");

            TextField toyTypeField = new TextField(selectedToy.getToyType());
            toyTypeField.setPromptText("Tip jucărie");

            TextField ageField = new TextField(String.valueOf(selectedToy.getAge()));
            ageField.setPromptText("Vârstă");

            TextField quantityField = new TextField(String.valueOf(selectedToy.getCantity()));
            quantityField.setPromptText("Cantitate");

            TextField priceField = new TextField(String.valueOf(selectedToy.getPrice()));
            priceField.setPromptText("Preț");

            // Adaugă câmpurile în GridPane
            grid.add(nameField, 0, 0);
            grid.add(countryField, 0, 1);
            grid.add(toyTypeField, 0, 2);
            grid.add(ageField, 0, 3);
            grid.add(quantityField, 0, 4);
            grid.add(priceField, 0, 5);

            // Adaugă GridPane în dialog
            dialog.getDialogPane().setContent(grid);

            // Setează butoanele pentru dialog
            dialog.getDialogPane().getButtonTypes().addAll(ButtonType.OK, ButtonType.CANCEL);

            // Așteaptă să fie apăsat un buton și preia datele
            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == ButtonType.OK) {
                    // Actualizează obiectul Toys cu noile date
                    selectedToy.setName(nameField.getText());
                    selectedToy.setCountry(countryField.getText());
                    selectedToy.setToyType(toyTypeField.getText());
                    selectedToy.setAge(Integer.parseInt(ageField.getText()));
                    selectedToy.setCantity(Integer.parseInt(quantityField.getText()));
                    selectedToy.setPrice(Float.parseFloat(priceField.getText()));

                    // Returnează obiectul actualizat
                    return selectedToy;
                }
                return null;  // Dacă se apasă Cancel
            });

            // Afișează dialogul
            dialog.showAndWait().ifPresent(updatedToy -> {
                try {
                    // Apelăm metoda update() din ToysDAOImpl pentru a actualiza datele în baza de date
                    toysDAO.update(updatedToy);

                    // Reîncărcăm tabela pentru a reflecta modificările
                    populateTable();

                    // Mesaj de confirmare
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Confirmare");
                    alert.setHeaderText("Jucăria a fost actualizată cu succes!");
                    alert.setContentText("Datele jucăriei au fost actualizate.");
                    alert.showAndWait();
                } catch (Exception e) {
                    e.printStackTrace();
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    alert.setTitle("Eroare");
                    alert.setHeaderText("Actualizare eșuată");
                    alert.setContentText("A apărut o eroare la actualizarea jucăriei.");
                    alert.showAndWait();
                }
            });
        } else {
            // Dacă nu este selectată nici o jucărie
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atenție");
            alert.setHeaderText("Nicio jucărie selectată");
            alert.setContentText("Te rog să selectezi o jucărie din tabel pentru a o actualiza.");
            alert.showAndWait();
        }
        populateTable();
    }

    private void deleteToy(ActionEvent event) {
        // Preluăm selecția din tabel
        Toys selectedToy = (Toys) oosTable.getSelectionModel().getSelectedItem();

        if (selectedToy != null) {
            // Confirmare înainte de ștergere
            Alert confirmAlert = new Alert(Alert.AlertType.CONFIRMATION);
            confirmAlert.setTitle("Confirmare ștergere");
            confirmAlert.setHeaderText("Sigur vrei să ștergi această jucărie?");
            confirmAlert.setContentText("Jucăria selectată va fi ștearsă din baza de date.");

            // Așteptăm răspunsul utilizatorului
            confirmAlert.showAndWait().ifPresent(response -> {
                if (response == ButtonType.OK) {
                    try {
                        // Apelăm metoda delete() din ToysDAOImpl pentru a șterge jucăria
                        toysDAO.delete(selectedToy);

                        // Reîncărcăm tabelul pentru a reflecta modificările
                        populateTable();

                        // Mesaj de confirmare
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Ștergere reușită");
                        alert.setHeaderText("Jucăria a fost ștearsă");
                        alert.setContentText("Jucăria selectată a fost ștearsă cu succes.");
                        alert.showAndWait();
                    } catch (Exception e) {
                        e.printStackTrace();
                        // Mesaj de eroare în cazul în care apare o problemă
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        alert.setTitle("Eroare");
                        alert.setHeaderText("Ștergere eșuată");
                        alert.setContentText("A apărut o eroare la ștergerea jucăriei.");
                        alert.showAndWait();
                    }
                }
            });
        } else {
            // Dacă nu este selectată nici o jucărie
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Atenție");
            alert.setHeaderText("Nicio jucărie selectată");
            alert.setContentText("Te rog să selectezi o jucărie din tabel pentru a o șterge.");
            alert.showAndWait();
        }
    }

    private void searchToys() {
        // Creăm un dialog pentru introducerea țării și numelui
        Dialog<Pair<String, String>> dialog = new Dialog<>();
        dialog.setTitle("Căutare Jucării");
        dialog.setHeaderText("Introdu țara și/sau numele jucăriei:");

        // Setăm butoanele OK și Cancel
        ButtonType searchButtonType = new ButtonType("Caută", ButtonBar.ButtonData.OK_DONE);
        dialog.getDialogPane().getButtonTypes().addAll(searchButtonType, ButtonType.CANCEL);

        // Creăm câmpurile de text
        GridPane grid = new GridPane();
        grid.setHgap(10);
        grid.setVgap(10);
        grid.setPadding(new Insets(20, 150, 10, 10));

        TextField countryField = new TextField();
        countryField.setPromptText("Țara");

        TextField nameField = new TextField();
        nameField.setPromptText("Numele jucăriei");

        grid.add(new Label("Țara:"), 0, 0);
        grid.add(countryField, 1, 0);
        grid.add(new Label("Numele:"), 0, 1);
        grid.add(nameField, 1, 1);

        dialog.getDialogPane().setContent(grid);

        // Focus pe primul câmp
        Platform.runLater(() -> countryField.requestFocus());

        // Procesăm datele introduse
        dialog.setResultConverter(dialogButton -> {
            if (dialogButton == searchButtonType) {
                return new Pair<>(countryField.getText().trim(), nameField.getText().trim());
            }
            return null;
        });

        Optional<Pair<String, String>> result = dialog.showAndWait();

        result.ifPresent(searchParams -> {
            String country = searchParams.getKey();
            String name = searchParams.getValue();

            // Căutăm jucăriile și actualizăm tabela
            List<Toys> toysList = toysDAO.searchOos(country, name);
            oosTable.getItems().setAll(toysList);
        });
    }





}
