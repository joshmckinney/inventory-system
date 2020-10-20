package me.joshmckinney.inventory.View_Controller;

import com.sun.javafx.image.IntPixelGetter;
import javafx.beans.binding.NumberBinding;
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

public class ModifyPartController {
    private boolean inhouse;
    private boolean outsourced;

    private Part part;

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
        outsourced = true;
        selectedPart.setText("Company Name");
    }

    @FXML
    private void inhouseSelected(ActionEvent event) {
        inhouse = true;
        selectedPart.setText("Machine ID");
    }

    private int index;
    public int setPartIndex(int partIndex) {
        return index = partIndex;
    }

    @FXML
    private void saveButtonClick(ActionEvent event) throws IOException {
        int id = Integer.parseInt(tfId.getText());
        String name = tfName.getText();
        int stock = Integer.parseInt(tfStock.getText());
        double price = Double.parseDouble(tfPrice.getText());
        int max = Integer.parseInt(tfMax.getText());
        int min = Integer.parseInt(tfMin.getText());
        part.setId(id);
        part.setName(name);
        part.setStock(stock);
        part.setPrice(price);
        part.setMin(min);
        part.setMax(max);
        try {
            if (part instanceof InHouse && outsourced == true) {
                // Save identifier as string
                String identifier = tfIdentifier.getText();
                Inventory.deletePart(part);
                Inventory.addPart(new Outsourced(id, name, price, stock, min, max, identifier));
                System.out.println("InHouse part changed to Outsourced");
            } else if (part instanceof Outsourced && inhouse == true) {
                int identifier = Integer.parseInt(tfIdentifier.getText());
                Inventory.deletePart(part);
                Inventory.addPart(new InHouse(id, name, price, stock, min, max, identifier));
                System.out.println("Outsourced part changed to InHouse");
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
            } else {
                // Bug fix for update by index, does not work in filtered view
                if (index < Inventory.getAllParts().size()) {
                    for (Part part : Inventory.getAllParts()) {
                        if (part == this.part) {
                            Inventory.deletePart(part);
                            Inventory.addPart(part);
                            break;
                        }
                    }
                } else {
                    Inventory.updatePart(index, part);
                    System.out.println("Part updated");
                }
            }
        } catch (NumberFormatException ex) {
            System.out.println("Invalid part entered, not saved.");
            VBox v = new VBox();
            v.setAlignment(Pos.CENTER);
            Alert alert = new Alert(Alert.AlertType.ERROR);
            Stage modifyPart = (Stage) ((Node) event.getSource()).getScene().getWindow();
            alert.initOwner(modifyPart);
            alert.setTitle("Invalid Part");
            alert.setHeaderText("Part has missing or invalid data");
            alert.setContentText("Please check all part fields and try again.");
            alert.showAndWait();
            modifyPart.close();
        }
    }

    @FXML
    private void cancelButtonClick(ActionEvent event) throws IOException {
        VBox v = new VBox();
        v.setAlignment(Pos.CENTER);
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        Stage exit = (Stage) ((Node) event.getSource()).getScene().getWindow();
        alert.initOwner(exit);
        alert.setTitle("Cancel Modify Part");
        alert.setHeaderText("Are you sure you wish to cancel?");
        alert.setContentText("All unsaved data will be lost.");
        Optional<ButtonType> result = alert.showAndWait();
        if (result.get() == ButtonType.OK){
            exit.close();
        }
    }

    public void setInPart(InHouse part) {
        this.part = part;
        tfId.setText(new Integer(part.getId()).toString());
        tfName.setText(part.getName());
        tfStock.setText(new Integer(part.getStock()).toString());
        tfPrice.setText(new Double(part.getPrice()).toString());
        tfMax.setText(new Integer(part.getMax()).toString());
        tfMin.setText(new Integer(part.getMin()).toString());
        tfIdentifier.setText(new Integer(part.getMachineId()).toString());
        rbInhouse.setSelected(true);
    }

    public void setOutPart(Outsourced part) {
        this.part = part;
        tfId.setText(new Integer(part.getId()).toString());
        tfName.setText(part.getName());
        tfStock.setText(new Integer(part.getStock()).toString());
        tfPrice.setText(new Double(part.getPrice()).toString());
        tfMax.setText(new Integer(part.getMax()).toString());
        tfMin.setText(new Integer(part.getMin()).toString());
        tfIdentifier.setText(part.getCompanyName());
        selectedPart.setText("Company Name");
        rbOutsourced.setSelected(true);
    }
}
