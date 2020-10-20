package me.joshmckinney.inventory.Main;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.image.Image;
import javafx.stage.Stage;
import me.joshmckinney.inventory.Model.InHouse;
import me.joshmckinney.inventory.Model.Inventory;
import me.joshmckinney.inventory.Model.Outsourced;
import me.joshmckinney.inventory.Model.Product;


public class InventoryProgram extends Application {

    public static void main(String[] args) {
        // Populate Example Data
        // *********************

        // Example Parts
        InHouse inpart1 = new InHouse(1,"Dough",3.00,50,26,100,1);
        Inventory.addPart(inpart1);
        InHouse inpart2 = new InHouse(2,"Tomato Sauce",2.00,41,25,100,2);
        Inventory.addPart(inpart2);
        InHouse inpart3 = new InHouse(3,"Buffalo Sauce",1.00,37,10,40,3);
        Inventory.addPart(inpart3);
        InHouse inpart4 = new InHouse(4,"Cheese",1.25,75,50,150,4);
        Inventory.addPart(inpart4);
        InHouse inpart5 = new InHouse(5,"Garlic Spread",0.75,43,15,50,5);
        Inventory.addPart(inpart5);
        InHouse inpart6 = new InHouse(6,"Dressing",0.50,15,10,20,6);
        Inventory.addPart(inpart6);
        Outsourced outpart1 = new Outsourced(7,"Pepperoni",1.25,35,20,40,"Meat Co");
        Inventory.addPart(outpart1);
        Outsourced outpart2 = new Outsourced(8,"Chicken",7.99,40,20,60,"Meat Co");
        Inventory.addPart(outpart2);
        Outsourced outpart3 = new Outsourced(9,"Lettuce",3.99,15,10,30,"Farm Fresh");
        Inventory.addPart(outpart3);
        Outsourced outpart4 = new Outsourced(10,"Veggies",1.25,18,1,6,"Farm Fresh");
        Inventory.addPart(outpart4);
        Outsourced outpart5 = new Outsourced(11,"Sausage Crumbles",1.25,25,10,40,"Meat Co");
        Inventory.addPart(outpart5);
        // Example Products
        Product product1 = new Product(1,"Cheese Pizza",9,13.99,1,10);
        Inventory.addProduct(product1);
        Product product2 = new Product(2,"Pepperoni Pizza",6,14.99,1,10);
        Inventory.addProduct(product2);
        Product product3 = new Product(3,"Combo Pizza",7,15.99,1,10);
        Inventory.addProduct(product3);
        Product product4 = new Product(4,"Garlic Bread",15,6.99,10,25);
        Inventory.addProduct(product4);
        Product product5 = new Product(5,"Chicken Wings",33,9.99,15,45);
        Inventory.addProduct(product5);
        Product product6 = new Product(6,"House Salad",11,7.99,5,15);
        Inventory.addProduct(product6);


        // Generate Associated Parts lists and assign to each product

        // Product 1 'Cheese Pizza'
        product1.addAssociatedPart(inpart1);
        product1.addAssociatedPart(inpart2);
        product1.addAssociatedPart(inpart4);
        // Product 2 'Pepperoni Pizza'
        product2.addAssociatedPart(inpart1);
        product2.addAssociatedPart(inpart2);
        product2.addAssociatedPart(inpart4);
        product2.addAssociatedPart(outpart1);
        // Product 3 'Combo Pizza'
        product3.addAssociatedPart(inpart1);
        product3.addAssociatedPart(inpart2);
        product3.addAssociatedPart(outpart4);
        product3.addAssociatedPart(inpart4);
        product3.addAssociatedPart(outpart1);
        product3.addAssociatedPart(outpart5);
        // Product 4 'Garlic Bread'
        product4.addAssociatedPart(inpart1);
        product4.addAssociatedPart(inpart5);
        // Product 5 'Chicken Wings'
        product5.addAssociatedPart(outpart2);
        product5.addAssociatedPart(inpart3);
        // Product 6 'House Salad'
        product6.addAssociatedPart(outpart3);
        product6.addAssociatedPart(inpart6);

        launch(args);
    }

    @Override
    public void start(Stage primaryStage) throws Exception {
        Parent root = FXMLLoader.load(getClass().getResource("/me/joshmckinney/inventory/View_Controller/MainScreen.fxml"));
        primaryStage.setTitle("Inventory Management System");
        primaryStage.setScene(new Scene(root, 1200, 600));
        primaryStage.getIcons().add(new Image(InventoryProgram.class.getResourceAsStream("/icon.png")));
        primaryStage.show();
        primaryStage.requestFocus();
    }
}