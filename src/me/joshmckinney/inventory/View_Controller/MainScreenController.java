package me.joshmckinney.inventory.View_Controller;

import javafx.application.Platform;
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
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.VBox;
import javafx.stage.Stage;
import me.joshmckinney.inventory.Main.InventoryProgram;
import me.joshmckinney.inventory.Model.*;

import java.io.IOException;
import java.text.NumberFormat;
import java.util.Optional;

public class MainScreenController {

    @FXML
    private TextField tfPart;
    @FXML
    private TextField tfProduct;

    // Parts Table
    @FXML
    public TableView<Part> partTableView;
    public TableColumn<Part, Integer> partIdCol;
    public TableColumn<Part, String> partNameCol;
    public TableColumn<Part, Double> partPriceCol;
    public TableColumn<Part, Integer> partStockCol;

    // Products Table
    @FXML
    public TableView<Product> productTableView;
    public TableColumn<Product, Integer> productIdCol;
    public TableColumn<Product, String> productNameCol;
    public TableColumn<Product, Double> productPriceCol;
    public TableColumn<Product, Integer> productStockCol;

    @FXML
    protected void addPartButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/me/joshmckinney/inventory/View_Controller/AddPart.fxml"));
        Scene addPartScene = new Scene(root);
        Stage addPartStage = new Stage();
        addPartStage.setScene((addPartScene));
        addPartStage.setTitle("Add Part");
        addPartStage.getIcons().add(new Image(InventoryProgram.class.getResourceAsStream("/icon.png")));
        addPartStage.show();
        addPartStage.requestFocus();
        addPartStage.setOnHiding(event1 -> {
            partTableView.setItems(Inventory.getAllParts());
            partTableView.getSortOrder().add(partIdCol);
            partTableView.refresh();
        });
    }

    @FXML
    protected void addProductButtonClick(ActionEvent event) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/me/joshmckinney/inventory/View_Controller/AddProduct.fxml"));
        Scene addProductScene = new Scene(root);
        Stage addProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
        addProductStage.setScene((addProductScene));
        addProductStage.setTitle("Add Product");
        addProductStage.getIcons().add(new Image(InventoryProgram.class.getResourceAsStream("/icon.png")));
        addProductStage.show();
        addProductStage.setOnHiding(event1 -> {
            productTableView.setItems(Inventory.getAllProducts());
            productTableView.getSortOrder().add(productIdCol);
            productTableView.refresh();
        });
    }

    @FXML
    protected void modifyPartButtonClick(ActionEvent event) throws IOException {
        int partIndex = partTableView.getSelectionModel().getSelectedIndex();
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/me/joshmckinney/inventory/View_Controller/ModifyPart.fxml"));
            Parent root = loader.load();
            ModifyPartController controller = loader.getController();
            controller.setPartIndex(partIndex);
            if(partTableView.getSelectionModel().getSelectedItem() instanceof InHouse) {
                controller.setInPart((InHouse) (partTableView.getSelectionModel().getSelectedItem()));
            } else {
                controller.setOutPart((Outsourced) (partTableView.getSelectionModel().getSelectedItem()));
            }
            Scene modifyPartScene = new Scene(root);
            Stage modifyPartStage = new Stage();
            modifyPartStage.setScene((modifyPartScene));
            modifyPartStage.setTitle("Modify Part");
            modifyPartStage.getIcons().add(new Image(InventoryProgram.class.getResourceAsStream("/icon.png")));
            modifyPartStage.show();
            modifyPartStage.requestFocus();
            modifyPartStage.setOnHiding(event1 -> {
                partTableView.setItems(Inventory.getAllParts());
                partTableView.getSortOrder().add(partIdCol);
                partTableView.refresh();
            });
        } catch (NullPointerException e) {
            System.out.println("No part was selected!");
            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Stage modifypart = (Stage) ((Node) event.getSource()).getScene().getWindow();
            alert.initOwner(modifypart);
            alert.setTitle("Modify Part");
            alert.setHeaderText("No part selected");
            alert.setContentText("Select part and try again.");
            alert.showAndWait();
        }
    }

    @FXML
    protected void modifyProductButtonClick(ActionEvent event) throws IOException, NullPointerException {
        try {
            Product product = productTableView.getSelectionModel().getSelectedItem();
            int productIndex = productTableView.getSelectionModel().getSelectedIndex();
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/me/joshmckinney/inventory/View_Controller/ModifyProduct.fxml"));
            Parent root = loader.load();
            Scene modifyProductScene = new Scene(root);
            Stage modifyProductStage = (Stage) ((Node) event.getSource()).getScene().getWindow();
            modifyProductStage.getIcons().add(new Image(InventoryProgram.class.getResourceAsStream("/icon.png")));
            ModifyProductController controller = loader.getController();
            controller.setProduct(product);
            controller.initialize(product);
            controller.setProductIndex(productIndex);
            modifyProductStage.setTitle("Modify Product");
            modifyProductStage.show();
            modifyProductStage.setScene((modifyProductScene));
            modifyProductStage.setOnHiding(event1 -> {
                productTableView.setItems(Inventory.getAllProducts());
                productTableView.getSortOrder().add(productIdCol);
                productTableView.refresh();
            });
        } catch (NullPointerException e) {
            System.out.println("No product was selected!");
            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Stage modifyproduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
            alert.initOwner(modifyproduct);
            alert.setTitle("Modify Product");
            alert.setHeaderText("No product selected");
            alert.setContentText("Select product and try again.");
            alert.showAndWait();
        }
    }

    @FXML
    protected void searchPartButtonClick(ActionEvent event) throws IOException {
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

    @FXML
    protected void searchProductButtonClick(ActionEvent event) throws IOException {
        String search = tfProduct.getText();
        if(!(isNumeric(search))) {
            String name = search;
            productTableView.setItems(Inventory.lookupProduct(name));
            productTableView.getSortOrder().add(productIdCol);
            tfProduct.clear();
        } else {
            int id = Integer.parseInt(search);
            Inventory.lookupProduct(id);
            productTableView.getSelectionModel().clearSelection();
            productTableView.setItems(Inventory.getAllProducts());
            productTableView.getItems().stream().filter(item -> item.getId()==id).findAny();
            productTableView.getItems().stream()
                    .filter(item -> item.getId() == id)
                    .findAny()
                    .ifPresent(item -> {
                        productTableView.getSelectionModel().select(item);
                        productTableView.scrollTo(item);
                    });
            tfProduct.clear();
        }

    }

    // Check focus then search query on return key
    @FXML
    public void returnQuery(KeyEvent event) {
        if (tfPart.isFocused()) {
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
                if(event.getCode() == KeyCode.ENTER) {
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
        }
        if (tfProduct.isFocused()) {
            String search = tfProduct.getText();
            if(!(isNumeric(search))) {
                String name = search;
                if(event.getCode() == KeyCode.ENTER) {
                    productTableView.setItems(Inventory.lookupProduct(name));
                    productTableView.getSortOrder().add(productIdCol);
                    tfProduct.clear();
                }
            } else {
                int id = Integer.parseInt(search);
                if(event.getCode() == KeyCode.ENTER) {
                    Inventory.lookupProduct(id);
                    productTableView.getSelectionModel().clearSelection();
                    productTableView.setItems(Inventory.getAllProducts());
                    productTableView.getItems().stream().filter(item -> item.getId()==id).findAny();
                    productTableView.getItems().stream()
                            .filter(item -> item.getId() == id)
                            .findAny()
                            .ifPresent(item -> {
                                productTableView.getSelectionModel().select(item);
                                productTableView.scrollTo(item);
                            });
                    tfProduct.clear();
                }
            }
        }
    }

    @FXML
    protected void deletePartButtonClick(ActionEvent event) throws IOException {
        Part delPart = partTableView.getSelectionModel().getSelectedItem();
        Stage delpart = (Stage) ((Node) event.getSource()).getScene().getWindow();
        if (delPart == null) {
            System.out.println("No part selected!");
            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.initOwner(delpart);
            alert.setTitle("Delete Part");
            alert.setHeaderText("No part selected");
            alert.setContentText("Select part and try again.");
            alert.showAndWait();
        } else {
            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.initOwner(delpart);
            alert.setTitle("Delete Part");
            alert.setHeaderText("Please confirm part deletion");
            alert.setContentText("Are you sure you want to delete this part?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK) {
                boolean partExists = false;
                for (Product product : Inventory.getAllProducts()) {
                    if (product.getAllAssociatedParts().contains(delPart)) {
                        partExists = true;
                        break;
                    }
                }
                if(partExists) {
                    VBox v1 = new VBox();
                    v.setAlignment(Pos.CENTER);
                    Alert alert1 = new Alert(Alert.AlertType.WARNING);
                    alert1.initOwner(delpart);
                    alert1.setTitle("Delete Part");
                    alert1.setHeaderText("Part is still associated with a product!");
                    alert1.setContentText("Please remove from all products.");
                    alert1.showAndWait();
                } else {
                    if(Inventory.deletePart(delPart)) {
                        System.out.println("Part '" + delPart + "' Deleted");
                    } else {
                        System.out.println("Part delete failed!");
                    }
                }
            }
        }
    }

    @FXML
    protected void deleteProductButtonClick(ActionEvent event) throws IOException {
        Product delProduct = productTableView.getSelectionModel().getSelectedItem();
        if(delProduct == null) {
            System.out.println("No product selected!");
            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            Stage delproduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
            alert.initOwner(delproduct);
            alert.setTitle("Delete Product");
            alert.setHeaderText("No product selected");
            alert.setContentText("Select product and try again.");
            alert.showAndWait();
        } else {
            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            Stage delproduct = (Stage) ((Node) event.getSource()).getScene().getWindow();
            alert.initOwner(delproduct);
            alert.setTitle("Delete Part");
            alert.setHeaderText("Please confirm product deletion");
            alert.setContentText("Are you sure you want to delete this product?");
            Optional<ButtonType> result = alert.showAndWait();
            if (result.get() == ButtonType.OK){
                Inventory.deleteProduct(delProduct);
            }
        }
    }

    @FXML
    public void exitButtonClick(ActionEvent event) {
        VBox v = new VBox();
        v.setAlignment(Pos.CENTER);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage exit = (Stage) ((Node) event.getSource()).getScene().getWindow();
        alert.initOwner(exit);
        alert.setTitle("Exit");
        alert.setHeaderText("Are you sure you wish to exit?");
        alert.setContentText("You may lose unsaved data.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Platform.exit();
        }
    }

    public static boolean isNumeric(String str) {
        return str.matches("-?\\d+(\\.\\d+)?");  //match a number with optional '-' and decimal.
    }

    @FXML
    public void initialize() {
        // Set Parts and Products in tables
        partTableView.setItems(Inventory.getAllParts());
        productTableView.setItems(Inventory.getAllProducts());
        // Assign partTableView columns
        partIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        // Convert price to formatted currency ($1.00)
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
        // Assign productTableView columns
        productIdCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        productNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        productStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        // Convert price to formatted currency ($1.00)
        NumberFormat formatProductPrice = NumberFormat.getCurrencyInstance();
        productPriceCol.setCellFactory(tc -> new TableCell<Product, Double>() {

            @Override
            protected void updateItem(Double price, boolean empty) {
                super.updateItem(price, empty);
                if (empty) {
                    setText(null);
                } else {
                    setText(formatProductPrice.format(price));
                }
            }
        });
        // Sort by ID
        partTableView.getSortOrder().add(partIdCol);
        productTableView.getSortOrder().add(productIdCol);
        // Inhouse/outsourced parts table clicks to STDOUT
        partTableView.setOnMouseClicked((MouseEvent event) -> {
            try {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    String name = partTableView.getSelectionModel().getSelectedItem().getName();
                    String part = partTableView.getSelectionModel().getSelectedItem().getClass().getSimpleName();
                    System.out.println(part + " part '" + name + "' selected.");
                }
            } catch (NullPointerException e) {
                System.out.println("Nothing selected.");
            }
        });
        // Product table clicks to STDOUT
        productTableView.setOnMouseClicked((MouseEvent event) -> {
            try {
                if (event.getButton().equals(MouseButton.PRIMARY)) {
                    String name = productTableView.getSelectionModel().getSelectedItem().getName();
                    System.out.println("Product '" + name + "' selected.");
                }
            } catch (NullPointerException e) {
                System.out.println("Nothing selected.");
            }
        });
    }
}