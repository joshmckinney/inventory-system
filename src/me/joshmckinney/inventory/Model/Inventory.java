package me.joshmckinney.inventory.Model;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

public class Inventory {
    // Declarations
    private static ObservableList<Part> allParts = FXCollections.observableArrayList();
    private static ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private static ObservableList<Part> lookupPart = FXCollections.observableArrayList();
    private static ObservableList<Product> lookupProduct = FXCollections.observableArrayList();

    // Setters
    public static void addPart(Part newPart) {
        allParts.add(newPart);
    }
    public static void addProduct(Product newProduct) {
        allProducts.add(newProduct);
    }

    // Functions
    public static Part lookupPart(int partId) {
        if(!(getLookupPart().isEmpty())) {
            getLookupPart().clear();
        }
        for(Part part : Inventory.getAllParts()) {
            if(part.getId() == partId) {
                lookupPart.add(part);
            }
            return part;
        }
        return null;
    }
    public static Product lookupProduct(int productId) {
        for(Product product : Inventory.getAllProducts()) {
            if(product.getId() == productId) {
                lookupProduct.add(product);
            }
            return product;
        }
        return null;
    }
    public static ObservableList<Part> lookupPart(String partName) {
        if (!(getLookupPart().isEmpty())) {
            getLookupPart().clear();
        }
        for (Part part : getAllParts()) {
            if (part.getName().toLowerCase().contains(partName.toLowerCase())) {
                getLookupPart().add(part);
            }
        }
        return getLookupPart();
    }
    public static ObservableList<Product> lookupProduct(String productName) {
        if (!(getLookupProduct().isEmpty())) {
            getLookupProduct().clear();
        }
        for (Product product : getAllProducts()) {
            if (product.getName().toLowerCase().contains(productName.toLowerCase())) {
                getLookupProduct().add(product);
            }
        }
        return getLookupProduct();
    }
    public static void updatePart(int index, Part selectedPart) {
        allParts.set(index, selectedPart);
        }
    public static void updateProduct(int index, Product newProduct) {
        allProducts.set(index, newProduct);
    }
    public static boolean deletePart(Part selectedPart) {
        for(Part part : Inventory.getAllParts()) {
            if(part.getId() == selectedPart.getId()) {
                Inventory.getAllParts().remove(selectedPart);
                return true;
            }
        }return false;
    }
    public static boolean deleteProduct(Product selectedProduct) {
        for(Product deleted : Inventory.getAllProducts()) {
            if(deleted.getId() == selectedProduct.getId()) {
                Inventory.getAllProducts().remove(selectedProduct);
                return true;
            }
        }
        return false;
    }

    // Getters
    public static ObservableList<Part> getAllParts() { return allParts; }
    public static ObservableList<Product> getAllProducts() { return allProducts; }
    public static ObservableList<Part> getLookupPart() { return lookupPart; }
    public static ObservableList<Product> getLookupProduct() { return lookupProduct; }
}