package InventorySystem.ViewControllers;


import InventorySystem.Constants.ScreenPaths;
import InventorySystem.Constants.UIStringConstants;
import InventorySystem.Models.Inventory;
import InventorySystem.Models.Part;
import InventorySystem.Models.Product;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Alert.AlertType;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
/**
 * MainScreenController Class
 * @author Nicholas Hartman
 */
public class MainScreenController implements Initializable, ScreenPaths, UIStringConstants {
    static Inventory inventory = new Inventory();
    /**
     * Start of UI Elements
     */
    @FXML
    private TextField txtSearchParts;

    @FXML
    private TextField txtSearchProducts;

    @FXML
    private TableView<Part> tvParts;

    @FXML
    private TableColumn<Part, Integer> partIDCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partInStockCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TableView<Product> tvProducts;

    @FXML
    private TableColumn<Product, Integer> productIDCol;

    @FXML
    private TableColumn<Product, String> productNameCol;

    @FXML
    private TableColumn<Product, Integer> productInventoryCol;

    @FXML
    private TableColumn<Product, Double> productPriceCol;
// End of UI Elements

    /**
     * UI Event Handlers
     *
     */
    @FXML
    public void addPartsAction (ActionEvent eAddParts) throws IOException {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource(AddPartScreenPath));
            //loader.setController();
            Parent addPartScreen = loader.load();
            Scene addPartScene = new Scene(addPartScreen);
            Stage winAddPart = (Stage)((Node)eAddParts.getSource()).getScene().getWindow();
            winAddPart.setTitle(addPartScreenTitle);
            winAddPart.setScene(addPartScene);
            winAddPart.show();
        }
        catch (IOException e) {}
    }

    @FXML
    public void modifyPartsAction (ActionEvent eModifyParts) throws IOException {
        selectedPart = tvParts.getSelectionModel().getSelectedItem();
        selectedPartIndex = inventory.getAllParts().indexOf(selectedPart);
        if (selectedPart == null) {
            Alert nullPartAlert = new Alert(AlertType.ERROR);
            //TODO: fix this raw text
            nullPartAlert.setTitle("Part Modification Error");
            nullPartAlert.setHeaderText("The part is NOT able to be modified!");
            nullPartAlert.setContentText("There was no part selected!");
            nullPartAlert.showAndWait();
        }
        else {
            try {
                Parent modifyPartScreen = FXMLLoader.load(getClass().getResource(ModifyPartScreenPath));
                Scene modifyPartScene = new Scene(modifyPartScreen);
                Stage winModifyPart = (Stage)((Node)eModifyParts.getSource()).getScene().getWindow();
                winModifyPart.setTitle(modifyPartScreenTitle);
                winModifyPart.setScene(modifyPartScene);
                winModifyPart.show();
            }
            catch (IOException e) {}
        }
    }

    @FXML
    public void deletePartsAction(ActionEvent event) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(partDeletionTitle);
        alert.setHeaderText(partDeletionQuestion);
        alert.setContentText(partDeletionInstruction);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            try {
                Part part = tvParts.getSelectionModel().getSelectedItem();
                inventory.deletePart(part);
            }
            catch (NullPointerException e) {
                Alert nullPartAlert = new Alert(AlertType.ERROR);
                nullPartAlert.setTitle(partDeletionErrorTitle);
                nullPartAlert.setHeaderText(partDeletionErrorHeader);
                nullPartAlert.setContentText(partNotSelectedContent);
                nullPartAlert.showAndWait();
            }
        }
        else {
            alert.close();
        }
    }

    @FXML
    public void searchPartsAction(ActionEvent event) {
        String searchPartIDString = txtSearchParts.getText();
        if (searchPartIDString.equals("")){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle(partSearchWarningTitle);
            alert.setHeaderText(partNotFoundHeader);
            alert.setContentText(partNotEnteredForSearch);
            alert.showAndWait();
        }
        else {
            boolean found=false;
            try {
                Part searchPart = inventory.lookupPart(Integer.parseInt(searchPartIDString));
                if (searchPart != null) {
                    ObservableList<Part> filteredPartsList = FXCollections.observableArrayList();
                    filteredPartsList.add(searchPart);
                    tvParts.setItems(filteredPartsList);
                }
                else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle(partSearchWarningTitle);
                    alert.setHeaderText(partNotFoundHeader);
                    alert.setContentText(partNotMatchedContent);
                    alert.showAndWait();
                }
            }
            catch (NumberFormatException e) {
                for (Part p : inventory.getAllParts()) {
                    if (p.getName().equals(searchPartIDString)) {
                        found=true;
                        ObservableList<Part> filteredPartsList = FXCollections.observableArrayList();
                        filteredPartsList.add(p);
                        tvParts.setItems(filteredPartsList);
                    }
                }
                if (found==false) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle(partSearchWarningTitle);
                    alert.setHeaderText(partNotFoundHeader);
                    alert.setContentText(partNotMatchedContent);
                    alert.showAndWait();
                }
            }
        }
    }

    @FXML
    public void addProductsAction (ActionEvent eAddProducts) throws IOException {
        try {
            Parent addProductScreen = FXMLLoader.load(getClass().getResource(AddProductScreenPath));
            Scene addProductScene = new Scene(addProductScreen);
            Stage winAddProduct = (Stage)((Node)eAddProducts.getSource()).getScene().getWindow();
            winAddProduct.setTitle(addProductScreenTitle);
            winAddProduct.setScene(addProductScene);
            winAddProduct.show();
        }
        catch (IOException e) {}
    }

    @FXML
    public void modifyProductsAction (ActionEvent eModifyProducts) throws IOException {
        selectedProduct = tvProducts.getSelectionModel().getSelectedItem();
        selectedProductIndex = inventory.getAllProducts().indexOf(selectedProduct);
        if (selectedProduct == null) {
            Alert nullProductAlert = new Alert(AlertType.ERROR);
            nullProductAlert.setTitle(productModificationErrorTitle);
            nullProductAlert.setHeaderText(productModificationErrorHeader);
            nullProductAlert.setContentText(productNotSelectedContent);
            nullProductAlert.showAndWait();
        }
        else {
            try {
                Parent modifyProductScreen = FXMLLoader.load(getClass().getResource(ModifyProductScreenPath));
                Scene modifyProductScene = new Scene(modifyProductScreen);
                Stage winModifyProduct = (Stage)((Node)eModifyProducts.getSource()).getScene().getWindow();
                winModifyProduct.setTitle(modifyProductScreenTitle);
                winModifyProduct.setScene(modifyProductScene);
                winModifyProduct.show();
            }
            catch (IOException e) {}
        }
    }

    @FXML
    public void deleteProductsAction(ActionEvent event) {
        Alert alert = new Alert(AlertType.CONFIRMATION);
        alert.setTitle(productDeletionTitle);
        alert.setHeaderText(productDeletionQuestion);
        alert.setContentText(productDeletionInstruction);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            try {
                Product product = tvProducts.getSelectionModel().getSelectedItem();
                inventory.deleteProduct(product);
            }
            catch (NullPointerException e) {
                Alert nullProductAlert = new Alert(AlertType.ERROR);
                nullProductAlert.setTitle(productDeletionErrorTitle);
                nullProductAlert.setHeaderText(productDeletionErrorHeader);
                nullProductAlert.setContentText(productNotSelectedContent);
                nullProductAlert.showAndWait();
            }
        }
        else {
            alert.close();
        }
    }

    @FXML
    public void searchProductsAction(ActionEvent event) {
        String searchProductIDString = txtSearchProducts.getText();
        if (searchProductIDString.equals("")){
            Alert alert = new Alert(AlertType.WARNING);
            alert.setTitle(productSearchWarningTitle);
            alert.setHeaderText(productSearchWarningHeader);
            alert.setContentText(productNotEnteredForSearch);
            alert.showAndWait();
        }
        else {
            boolean found=false;
            try {
                Product searchProduct = inventory.lookupProduct(Integer.parseInt(searchProductIDString));
                if (searchProduct != null) {
                    ObservableList<Product> filteredProductsList = FXCollections.observableArrayList();
                    filteredProductsList.add(searchProduct);
                    tvProducts.setItems(filteredProductsList);
                }
                else {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle(productSearchWarningTitle);
                    alert.setHeaderText(productSearchWarningHeader);
                    alert.setContentText(productNotMatchedContent);
                    alert.showAndWait();
                }
            }
            catch (NumberFormatException e) {
                for (Product p : inventory.getAllProducts()) {
                    if (p.getName().equals(searchProductIDString)) {
                        found=true;
                        ObservableList<Product> filteredProductsList = FXCollections.observableArrayList();
                        filteredProductsList.add(p);
                        tvProducts.setItems(filteredProductsList);
                    }
                }
                if (found==false) {
                    Alert alert = new Alert(AlertType.WARNING);
                    alert.setTitle("Product Search Warning");
                    alert.setHeaderText("There were no products found!");
                    alert.setContentText("The search term entered does not match any product names!");
                    alert.showAndWait();
                }
            }
        }
    }
    @FXML
    public void updatePartsTV() {
        tvParts.setItems(inventory.getAllParts());
    }
    @FXML
    public void updateProductsTV() {
        tvProducts.setItems(inventory.getAllProducts());
    }

    @FXML
    public void exitButtonAction (ActionEvent eExitButton) {
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(exitInventoryManagementSystem);
        alert.setHeaderText(exitQuestion);
        alert.setContentText(exitInstruction);
        alert.showAndWait();
        if (alert.getResult() == ButtonType.OK) {
            Stage winMainScreen = (Stage)((Node)eExitButton.getSource()).getScene().getWindow();
            winMainScreen.close();
        }
        else {
            alert.close();
        }
    }

    private static Part selectedPart;

    private static int selectedPartIndex;

    public static Part getSelectedPart() {
        return selectedPart;
    }

    public static int getSelectedPartIndex() {
        return selectedPartIndex;
    }

    private static Product selectedProduct;

    private static int selectedProductIndex;

    public static Product getSelectedProduct() {
        return selectedProduct;
    }

    public static int getSelectedProductIndex() {
        return selectedProductIndex;
    }


    /**
     * Initializes the controller class.
     *
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        updatePartsTV();
        productIDCol.setCellValueFactory(new PropertyValueFactory("productID"));
        productNameCol.setCellValueFactory(new PropertyValueFactory("name"));
        productInventoryCol.setCellValueFactory(new PropertyValueFactory("inStock"));
        productPriceCol.setCellValueFactory(new PropertyValueFactory("price"));
        updateProductsTV();
    }
}

