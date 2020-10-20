package me.joshmckinney.inventory.View_Controller;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.geometry.Pos;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.RadioButton;
import javafx.scene.control.TextField;
import javafx.scene.layout.VBox;
import javafx.scene.text.Text;
import javafx.stage.Stage;
import me.joshmckinney.inventory.Model.InHouse;
import me.joshmckinney.inventory.Model.Inventory;
import me.joshmckinney.inventory.Model.Outsourced;
import me.joshmckinney.inventory.Model.Part;

import java.io.IOException;
import java.util.Optional;

public class AddPartController {

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
    private TextField tfIdentifier;
    @FXML
    private Text selectedPart;
    @FXML
    private RadioButton rbInhouse;
    @FXML
    private RadioButton rbOutsourced;

    @FXML
    private void outsourcedSelected(ActionEvent event) {
        selectedPart.setText("Company Name");
        tfIdentifier.setPromptText("Company Name");
    }
    @FXML
    private void inhouseSelected(ActionEvent event) {
        selectedPart.setText("Machine ID");
        tfIdentifier.setPromptText("Machine ID");
    }

    @FXML private javafx.scene.control.Button saveButton;
    @FXML
    private void saveButtonClick(ActionEvent event) throws NumberFormatException {

        try {
            // Get textfields
            int id = Integer.parseInt(tfId.getText());
            String name = tfName.getText();
            int stock = Integer.parseInt(tfStock.getText());
            double price = Double.parseDouble(tfPrice.getText());
            int max = Integer.parseInt(tfMax.getText());
            int min = Integer.parseInt(tfMin.getText());
            boolean partExists = false;

            // Check that ID does not match any existing
            for (Part part : Inventory.getAllParts()) {
                if (part.getId() == id) {
                    System.out.println("Part ID exists!");
                    partExists = true;
                    break;
                } else {
                    partExists = false;
                }
            }

            if (rbInhouse.isSelected()) {
                // InHouse part selected, set machineId
                int machineId = Integer.parseInt(tfIdentifier.getText());
                // Part Validation
                try {
                    if (partExists) { // Check if part exists
                        System.out.println("Invalid part entered, not saved.");
                        VBox v = new VBox();
                        v.setAlignment(Pos.CENTER);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        Stage addpart = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        alert.initOwner(addpart);
                        alert.setTitle("Invalid Part");
                        alert.setHeaderText("Part ID already exists");
                        alert.setContentText("Please check ID and try again.");
                        alert.showAndWait();
                    } else if (max < min) { // Check if max is less than min value
                        System.out.println("Invalid part entered, not saved.");
                        VBox v = new VBox();
                        v.setAlignment(Pos.CENTER);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        Stage addpart = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        alert.initOwner(addpart);
                        alert.setTitle("Invalid Part");
                        alert.setHeaderText("Max value must be above min.");
                        alert.setContentText("Please check all min/max fields and try again.");
                        alert.showAndWait();
                    } else if ((stock < min) || (stock > max)) { // Check if stock is less than max and more than min
                        System.out.println("Invalid part entered, not saved.");
                        VBox v = new VBox();
                        v.setAlignment(Pos.CENTER);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        Stage addpart = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        alert.initOwner(addpart);
                        alert.setTitle("Invalid Part");
                        alert.setHeaderText("Stock value must be between min and max.");
                        alert.setContentText("Please check all fields and try again.");
                        alert.showAndWait();
                    } else { // All checks out, add the part
                        System.out.println("Part '" + name + "' added.");
                        Inventory.addPart(new InHouse(id, name, price, stock, min, max, machineId));
                        Stage close1 = (Stage) saveButton.getScene().getWindow();
                        close1.close();
                    }
                } catch (NumberFormatException ex) { // Catch invalid characters (letters in number fields)
                    System.out.println("Invalid part entered, not saved.");
                    VBox v = new VBox();
                    v.setAlignment(Pos.CENTER);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    Stage addpart = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    alert.initOwner(addpart);
                    alert.setTitle("Invalid Part");
                    alert.setHeaderText("Part has missing or invalid data");
                    alert.setContentText("Please check all part fields and try again.");
                    alert.showAndWait();
                }
            }
            if (rbOutsourced.isSelected()) {
                // Outsourced part selected, set companyName
                String companyName = tfIdentifier.getText();
                try {
                    if (partExists) { // Check if part exists
                        System.out.println("Invalid part entered, not saved.");
                        VBox v = new VBox();
                        v.setAlignment(Pos.CENTER);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        Stage addpart = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        alert.initOwner(addpart);
                        alert.setTitle("Invalid Part");
                        alert.setHeaderText("Part ID already exists");
                        alert.setContentText("Please check ID and try again.");
                        alert.showAndWait();
                    } else if (max < min) { // Check if max is less than min value
                        System.out.println("Invalid part entered, not saved.");
                        VBox v = new VBox();
                        v.setAlignment(Pos.CENTER);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        Stage addpart = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        alert.initOwner(addpart);
                        alert.setTitle("Invalid Part");
                        alert.setHeaderText("Max value must be above min.");
                        alert.setContentText("Please check all min/max fields and try again.");
                        alert.showAndWait();
                    } else if ((stock < min) || (stock > max)) { // Check if stock is less than max and more than min
                        System.out.println("Invalid part entered, not saved.");
                        VBox v = new VBox();
                        v.setAlignment(Pos.CENTER);
                        Alert alert = new Alert(Alert.AlertType.ERROR);
                        Stage addpart = (Stage) ((Node) event.getSource()).getScene().getWindow();
                        alert.initOwner(addpart);
                        alert.setTitle("Invalid Part");
                        alert.setHeaderText("Stock value must be between min and max.");
                        alert.setContentText("Please check all fields and try again.");
                        alert.showAndWait();
                    } else { // All checks out, add the part
                        System.out.println("Part '" + name + "' added.");
                        Inventory.addPart(new Outsourced(id, name, price, stock, min, max, companyName));
                        Stage close1 = (Stage) saveButton.getScene().getWindow();
                        close1.close();
                    }
                } catch (NumberFormatException ex) { // Catch invalid characters (letters in number fields)
                    System.out.println("Invalid part entered, not saved.");
                    VBox v = new VBox();
                    v.setAlignment(Pos.CENTER);
                    Alert alert = new Alert(Alert.AlertType.ERROR);
                    Stage addpart = (Stage) ((Node) event.getSource()).getScene().getWindow();
                    alert.initOwner(addpart);
                    alert.setTitle("Invalid Part");
                    alert.setHeaderText("Part has missing or invalid data");
                    alert.setContentText("Please check all part fields and try again.");
                    alert.showAndWait();
                }
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid part entered, not saved.");
            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Stage addpart = (Stage) ((Node) event.getSource()).getScene().getWindow();
            alert.initOwner(addpart);
            alert.setTitle("Invalid Part");
            alert.setHeaderText("Part has missing or invalid data");
            alert.setContentText("Please check all part fields and try again.");
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
        alert.setTitle("Cancel Add Part");
        alert.setHeaderText("Are you sure you wish to cancel?");
        alert.setContentText("All unsaved data will be lost.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            Stage primaryStage = (Stage) cancelButton.getScene().getWindow();
            primaryStage.close();
        }
    }

    @FXML
    private void initialize() {
        tfId.setDisable(true);
        tfId.setText(Integer.toString(genId()));
    }

    // Get size of array and add the next ID
    private int genId() {
         int id = 0;
         for(Part part : Inventory.getAllParts()) {
             if(part.getId() > id) {
                 id = part.getId();
             }
         }
         id++;
         return id;
    }
}
