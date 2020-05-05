package InventorySystem.ViewControllers;

import InventorySystem.Constants.HelperFunctions;
import InventorySystem.Constants.ScreenPaths;
import InventorySystem.Constants.UIStringConstants;
import InventorySystem.Errors.ProductEmptyFieldsException;
import InventorySystem.Errors.ProductNotValidException;
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
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static InventorySystem.ViewControllers.MainScreenController.getSelectedProduct;
import static InventorySystem.ViewControllers.MainScreenController.getSelectedProductIndex;

/**
 * ModifyProductScreenController class
 *
 * @author Nicholas Hartman
 */
public class ModifyProductScreenController implements Initializable, ScreenPaths, UIStringConstants {
    Product product = new Product();
    private Product selectedProduct;


    @FXML
    private TextField txtModifyProductID;

    @FXML
    private TextField txtModifyProductName;

    @FXML
    private TextField txtModifyProductInv;

    @FXML
    private TextField txtModifyProductPrice;

    @FXML
    private TextField txtModifyProductMax;

    @FXML
    private TextField txtModifyProductMin;

    @FXML
    private TextField txtSearchParts;

    @FXML
    private TableView<Part> tvPartsInStock;

    @FXML
    private TableColumn<Part, Integer> partIDCol;

    @FXML
    private TableColumn<Part, String> partNameCol;

    @FXML
    private TableColumn<Part, Integer> partInStockCol;

    @FXML
    private TableColumn<Part, Double> partPriceCol;

    @FXML
    private TableView<Part> tvAssociatedParts;

    @FXML
    private TableColumn<Part, Integer> associatedPartIDCol;

    @FXML
    private TableColumn<Part, String> associatedPartNameCol;

    @FXML
    private TableColumn<Part, Integer> associatedPartInStockCol;

    @FXML
    private TableColumn<Part, Double> associatedPartPriceCol;

    @FXML
    private int productID;

    @FXML
    private String productValidError = new String();

    @FXML
    private String productFieldError = new String();

    @FXML
    public static ObservableList<Part> currentAssocParts = FXCollections.observableArrayList();

    @FXML
    public void handleSearchAction(ActionEvent event) {
        String searchPartIDString = txtSearchParts.getText();
        if (searchPartIDString.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(partSearchWarningTitle);
            alert.setHeaderText(partNotFoundHeader);
            alert.setContentText(partNotEnteredForSearch);
            alert.showAndWait();
        } else {
            boolean found = false;
            try {
                Part searchPart = MainScreenController.inventory.lookupPart(Integer.parseInt(searchPartIDString));
                if (searchPart != null) {
                    ObservableList<Part> filteredPartsList = FXCollections.observableArrayList();
                    filteredPartsList.add(searchPart);
                    tvPartsInStock.setItems(filteredPartsList);
                } else {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(partSearchWarningTitle);
                    alert.setHeaderText(partNotFoundHeader);
                    alert.setContentText(partNotMatchedContent);
                    alert.showAndWait();
                }
            } catch (NumberFormatException e) {
                for (Part p : MainScreenController.inventory.getAllParts()) {
                    if (p.getName().equals(searchPartIDString)) {
                        found = true;
                        ObservableList<Part> filteredPartsList = FXCollections.observableArrayList();
                        filteredPartsList.add(p);
                        tvPartsInStock.setItems(filteredPartsList);
                    }
                }
                if (found == false) {
                    Alert alert = new Alert(Alert.AlertType.WARNING);
                    alert.setTitle(partSearchWarningTitle);
                    alert.setHeaderText(partNotFoundHeader);
                    alert.setContentText(partNotMatchNameContent);
                    alert.showAndWait();
                }
            }
        }
    }

    @FXML
    public void handleAddAction(ActionEvent event) {
        Part part = tvPartsInStock.getSelectionModel().getSelectedItem();
        if (part == null) {
            Alert nullPartAlert = new Alert(Alert.AlertType.ERROR);
            nullPartAlert.setTitle(productPartAdditionErrorTitle);
            nullPartAlert.setHeaderText(partAdditionWarningHeader);
            nullPartAlert.setContentText(partNotSelectedContent);
            nullPartAlert.showAndWait();
        } else {
            product.addAssociatedPart(part);
            tvAssociatedParts.setItems(product.getAllAssociatedParts());
        }
    }

    @FXML
    public void handleDeleteAction(ActionEvent event) {
        Alert deletePartAlert = new Alert(Alert.AlertType.CONFIRMATION);
        deletePartAlert.setTitle(productPartDeleteTitle);
        deletePartAlert.setHeaderText(productPartDeleteQuestion);
        deletePartAlert.setContentText(productPartDeleteInstructions);
        deletePartAlert.showAndWait();

        if (deletePartAlert.getResult() == ButtonType.OK) {
            try {
                Part part = tvAssociatedParts.getSelectionModel().getSelectedItem();
                product.deleteAssociatedPart(part);
            } catch (NullPointerException e) {
                Alert noPartSelectedAlert = new Alert(Alert.AlertType.ERROR);
                noPartSelectedAlert.setTitle(productPartDeletionErrorTitle);
                noPartSelectedAlert.setHeaderText(productPartNotDeletedHeader);
                noPartSelectedAlert.setContentText(partNotSelectedContent);
                noPartSelectedAlert.showAndWait();
            }
        } else {
            deletePartAlert.close();
        }
    }

    @FXML
    public void handleSaveAction(ActionEvent event) throws IOException {
        String name = txtModifyProductName.getText();
        String inStock = txtModifyProductInv.getText();
        String price = txtModifyProductPrice.getText();
        String max = txtModifyProductMax.getText();
        String min = txtModifyProductMin.getText();
        currentAssocParts = tvAssociatedParts.getItems();

        try {
            HelperFunctions.getEmptyFields(name, inStock, price, max, min);
        } catch (ProductEmptyFieldsException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(productNotAddedTitle);
            alert.setHeaderText(productNotAddedHeader);
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
        if (currentAssocParts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(productNotSavedTitle);
            alert.setHeaderText(productNotSavedHeader);
            alert.setContentText(productPartNotSelectedContent);
            alert.showAndWait();
        } else {
            selectedProduct.setProductID(productID);
            selectedProduct.setName(name);
            selectedProduct.setInStock(Integer.parseInt(inStock));
            selectedProduct.setPrice(Double.parseDouble(price));
            selectedProduct.setMax(Integer.parseInt(max));
            selectedProduct.setMin(Integer.parseInt(min));
            try {
                MainScreenController.inventory.updateProduct(getSelectedProductIndex(), selectedProduct);
            } catch (ProductNotValidException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(productNotAddedTitle);
                alert.setHeaderText(productNotAddedHeader);
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
            FXMLLoader loader = new FXMLLoader(getClass().getResource(MainScreenPath));
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage winMainScreen = (Stage) ((Node) event.getSource()).getScene().getWindow();
            winMainScreen.setTitle(mainScreenTitle);
            winMainScreen.setScene(scene);
            winMainScreen.show();
        }
    }

    @FXML
    public void handleCancelAction(ActionEvent event) throws IOException {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(exitToMainScreen);
            alert.setHeaderText(exitQuestion);
            alert.setContentText(exitInstruction);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                Parent root = FXMLLoader.load(getClass().getResource(MainScreenPath));
                Scene scene = new Scene(root);
                Stage winMainScreen = (Stage) ((Node) event.getSource()).getScene().getWindow();
                winMainScreen.setTitle(mainScreenTitle);
                winMainScreen.setScene(scene);
                winMainScreen.show();
            } else {
                alert.close();
            }
        } catch (IOException e) {
        }
    }

    /**
     * Initializes the controller class.
     *
     * @param url
     * @param rb
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedProduct = getSelectedProduct();
        productID = getSelectedProduct().getProductID();
        txtModifyProductID.setText("Auto Gen: " + productID);
        txtModifyProductName.setText(selectedProduct.getName());
        txtModifyProductInv.setText(Integer.toString(selectedProduct.getInStock()));
        txtModifyProductPrice.setText(Double.toString(selectedProduct.getPrice()));
        txtModifyProductMax.setText(Integer.toString(selectedProduct.getMax()));
        txtModifyProductMin.setText(Integer.toString(selectedProduct.getMin()));

        currentAssocParts = product.getAllAssociatedParts();

        partIDCol.setCellValueFactory(new PropertyValueFactory<>("partID"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInStockCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartIDCol.setCellValueFactory(new PropertyValueFactory<>("partID"));
        associatedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInStockCol.setCellValueFactory(new PropertyValueFactory<>("inStock"));
        associatedPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));
        updateAssociatedPartsTV();
    }

    public void updateAssociatedPartsTV() {
        tvAssociatedParts.setItems(currentAssocParts);
    }
}

