package me.joshmckinney.inventory.View_Controller;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.scene.image.Image;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.joshmckinney.inventory.Main.InventoryProgram;
import me.joshmckinney.inventory.Model.*;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Optional;

import static me.joshmckinney.inventory.View_Controller.MainScreenController.isNumeric;

public class AddProductController {

    // GUI Controls (FX:ID)
    @FXML
    private TextField tfId;
    @FXML
    private TextField tfName;
    @FXML
    private TextField tfStock;
    @FXML
    private TextField tfPrice;
    @FXML
    private TextField tfMax;
    @FXML
    private TextField tfMin;
    @FXML
    private TextField tfPart;

    // Parts Table
    @FXML
    public TableView<Part> partTableView;
    public TableColumn<Part, Integer> partIdCol;
    public TableColumn<Part, String> partNameCol;
    public TableColumn<Part, Double> partPriceCol;
    public TableColumn<Part, Integer> partStockCol;

    // Associated Parts Table
    @FXML
    public TableView<Part> associatedPartTableView;
    public TableColumn<Part, Integer> apartIdCol;
    public TableColumn<Part, String> apartNameCol;
    public TableColumn<Part, Double> apartPriceCol;
    public TableColumn<Part, Integer> apartStockCol;

    private ObservableList<Part> productParts = FXCollections.observableArrayList();

    @FXML
    private void searchButtonClick(ActionEvent event) throws IOException {
        String search = tfPart.getText();
        if(!(isNumeric(search))) {
            String name = search;
            partTableView.setItems(Inventory.lookupPart(name));
            partTableView.getSortOrder().add(partIdCol);
            tfPart.clear();
        } else {
            int id = Integer.parseInt(search);
            Inventory.lookupPart(id);
            partTableView.getSelectionModel().clearSelection();
            partTableView.setItems(Inventory.getAllParts());
            partTableView.getItems().stream().filter(item -> item.getId()==id).findAny();
            partTableView.getItems().stream()
                    .filter(item -> item.getId() == id)
                    .findAny()
                    .ifPresent(item -> {
                        partTableView.getSelectionModel().select(item);
                        partTableView.scrollTo(item);
                    });
            tfPart.clear();
        }
    }

    // Enable search query on return keypress
    @FXML
    public void returnQuery(KeyEvent event) {
        String search = tfPart.getText();
        if(!(isNumeric(search))) {
            String name = search;
            if(event.getCode() == KeyCode.ENTER) {
                partTableView.setItems(Inventory.lookupPart(name));
                partTableView.getSortOrder().add(partIdCol);
                tfPart.clear();
            }
        } else {
            int id = Integer.parseInt(search);
            if (event.getCode() == KeyCode.ENTER) {
                Inventory.lookupPart(id);
                partTableView.getSelectionModel().clearSelection();
                partTableView.setItems(Inventory.getAllParts());
                partTableView.getItems().stream().filter(item -> item.getId() == id).findAny();
                partTableView.getItems().stream()
                        .filter(item -> item.getId() == id)
                        .findAny()
                        .ifPresent(item -> {
                            partTableView.getSelectionModel().select(item);
                            partTableView.scrollTo(item);
                        });
                tfPart.clear();
            }
        }
    }

    @FXML
    private void addAssociatedPart(ActionEvent event) throws IOException {
        if (!(partTableView.getSelectionModel().getSelectedItem() == null)) {
            Part selectedPart = partTableView.getSelectionModel().getSelectedItem();
            if(!productParts.contains(selectedPart)) {
                productParts.add(selectedPart);
                associatedPartTableView.setItems(productParts);
                associatedPartTableView.refresh();
            } else {
                System.out.println("Added '" + selectedPart.getName() + "'");
                VBox v = new VBox();
                v.setAlignment(Pos.CENTER);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                Stage addproduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
                alert.initOwner(addproduct);
                alert.setTitle("Add Part");
                alert.setHeaderText("Part already associated.");
                alert.setContentText("Can't add duplicate parts.");
                alert.showAndWait();
            }
        } else {
            System.out.println("No part to add");
            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Stage addproduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
            alert.initOwner(addproduct);
            alert.setTitle("Add Part");
            alert.setHeaderText("No part to add");
            alert.setContentText("Please select a part.");
            alert.showAndWait();
        }
    }

    @FXML
    private void deleteButtonClick(ActionEvent event) throws IOException {
        Part delPart = associatedPartTableView.getSelectionModel().getSelectedItem();
        if(delPart == null) {
            System.out.println("No part selected!");
            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Stage delpart = (Stage) ((Node) event.getSource()).getScene().getWindow();
            alert.initOwner(delpart);
            alert.setTitle("Delete Part");
            alert.setHeaderText("No part selected");
            alert.setContentText("Select part and try again.");
            alert.showAndWait();
        } else {
            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage delpart = (Stage) ((Node) event.getSource()).getScene().getWindow();
            alert.initOwner(delpart);
            alert.setTitle("Delete Part");
            alert.setHeaderText("Please confirm part deletion");
            alert.setContentText("Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                try {
                    productParts.remove(delPart);
                    System.out.println("Part deleted!!!");
                } catch (NullPointerException ex) {
                    System.out.println("ID does not exist to delete!!!");
                }
            }
        }
    }

    @FXML
    private void saveButtonClick(ActionEvent event) throws IOException {
        try {
            // Get textfields
            int id = Integer.parseInt(tfId.getText());
            String name = tfName.getText();
            int stock = Integer.parseInt(tfStock.getText());
            double price = Double.parseDouble(tfPrice.getText());
            int max = Integer.parseInt(tfMax.getText());
            int min = Integer.parseInt(tfMin.getText());
            boolean productExists = false;

            // Check that ID does not match any existing
            for (Product product : Inventory.getAllProducts()) {
                if (product.getId() == id) {
                    System.out.println("Product ID exists!");
                    productExists = true;
                    break;
                } else {
                    productExists = false;
                }
            }
            try {
                if (productExists) { // Check if part exists
                    System.out.println("Invalid product entered, not saved.");
                    VBox v = new VBox();
                    v.setAlignment(Pos.CENTER);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    Stage addproduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    alert.initOwner(addproduct);
                    alert.setTitle("Invalid Product");
                    alert.setHeaderText("Product ID already exists");
                    alert.setContentText("Please check ID and try again.");
                    alert.showAndWait();
                } else if (max < min) { // Check if max is less than min value
                    System.out.println("Invalid product entered, not saved.");
                    VBox v = new VBox();
                    v.setAlignment(Pos.CENTER);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    Stage addproduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    alert.initOwner(addproduct);
                    alert.setTitle("Invalid Product");
                    alert.setHeaderText("Max value must be above min.");
                    alert.setContentText("Please check all min/max fields and try again.");
                    alert.showAndWait();
                } else if ((stock < min) || (stock > max)) { // Check if stock is less than max and more than min
                    System.out.println("Invalid product entered, not saved.");
                    VBox v = new VBox();
                    v.setAlignment(Pos.CENTER);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    Stage addproduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    alert.initOwner(addproduct);
                    alert.setTitle("Invalid Product");
                    alert.setHeaderText("Stock value must be between min and max.");
                    alert.setContentText("Please check all fields and try again.");
                    alert.showAndWait();
                } else if (associatedPartTableView.getItems().isEmpty()) { // Check that product has a min of one associated part
                    System.out.println("Invalid product entered, not saved.");
                    VBox v = new VBox();
                    v.setAlignment(Pos.CENTER);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    Stage addproduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    alert.initOwner(addproduct);
                    alert.setTitle("Invalid Product");
                    alert.setHeaderText("Product must have at least one associated part.");
                    alert.setContentText("Please add an associated part and try again.");
                    alert.showAndWait();
                } else { // All checks out, add the product
                    Product product = new Product(id,name,stock,price,min,max);
                    for(Part part : productParts) {
                        product.addAssociatedPart(part);
                    }
                    Inventory.addProduct(product);
                    Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
                    primaryStage.close();
                    Parent root = FXMLLoader.load(getClass().getResource("/me/joshmckinney/inventory/View_Controller/MainScreen.fxml"));
                    primaryStage.setTitle("Inventory Management System");
                    primaryStage.setScene(new Scene(root, 1200, 600));
                    primaryStage.getIcons().add(new Image(InventoryProgram.class.getResourceAsStream("/icon.png")));
                    primaryStage.show();
                }
            } catch (NumberFormatException ex) {
                System.out.println("Invalid product entered, not saved.");
                VBox v = new VBox();
                v.setAlignment(Pos.CENTER);
                Alert alert = new Alert(Alert.AlertType.INFORMATION);
                Stage addproduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
                alert.initOwner(addproduct);
                alert.setTitle("Invalid Product");
                alert.setHeaderText("Product has missing or invalid data");
                alert.setContentText("Check product fields and try again.");
                alert.showAndWait();
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid product entered, not saved.");
            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Stage addproduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
            alert.initOwner(addproduct);
            alert.setTitle("Invalid Product");
            alert.setHeaderText("Product has missing or invalid data");
            alert.setContentText("Check product fields and try again.");
            alert.showAndWait();
        }
    }

    @FXML private javafx.scene.control.Button cancelButton;
    @FXML
    private void cancelButtonClick(ActionEvent event) throws IOException {
        VBox v = new VBox();
        v.setAlignment(Pos.CENTER);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage exit = (Stage) ((Node) event.getSource()).getScene().getWindow();
        alert.initOwner(exit);
        alert.setTitle("Cancel Add Product");
        alert.setHeaderText("Are you sure you wish to cancel?");
        alert.setContentText("All unsaved data will be lost.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
            primaryStage.close();
            Parent root = FXMLLoader.load(getClass().getResource("/me/joshmckinney/inventory/View_Controller/MainScreen.fxml"));
            primaryStage.setTitle("Inventory Management System");
            primaryStage.setScene(new Scene(root, 1200, 600));
            primaryStage.show();
        }

    }

    @FXML
    public void initialize() {
        // Disable id field
        tfId.setDisable(true);
        // Set generated ID
        tfId.setText(Integer.toString(genId()));
        // Set part table
        partTableView.setItems(Inventory.getAllParts());
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        partTableView.getSortOrder().add(partNameCol);
        NumberFormat formatPartPrice = NumberFormat.getCurrencyInstance();
        partPriceCol.setCellFactory(tc -> new TableCell<Part, Double>() {

            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(formatPartPrice.format(price));
                }
            }
        });
        // Set associated parts table
        apartIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        apartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        apartStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        apartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        associatedPartTableView.getSortOrder().add(partNameCol);
        NumberFormat formataPartPrice = NumberFormat.getCurrencyInstance();
        apartPriceCol.setCellFactory(tc -> new TableCell<Part, Double>() {

            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(formataPartPrice.format(price));
                }
            }
        });
    }

    // Get size of array and add the next ID
    private int genId() {
        int id = 0;
        for(Product product : Inventory.getAllProducts()) {
            if(product.getId() > id) {
                id = product.getId();
            }
        }
        id++;
        return id;
    }
}
