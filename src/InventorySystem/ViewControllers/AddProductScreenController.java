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

/**
 * FXML Controller class
 *
 * @author Nicholas Hartman
 */
public class AddProductScreenController implements Initializable, ScreenPaths, UIStringConstants {
    Product product = new Product(); //instantiation of new product to add
    /**
     * Start of UI Elements
     **/
    @FXML
    private TextField txtAddProductID;

    @FXML
    private TextField txtAddProductName;

    @FXML
    private TextField txtAddProductInv;

    @FXML
    private TextField txtAddProductPrice;

    @FXML
    private TextField txtAddProductMax;

    @FXML
    private TextField txtAddProductMin;

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
    private String errProductValid = new String();

    @FXML
    private String errProductField = new String();
    // End of UI Elements

    /**
     * Start of Event Handlers
     **/
    @FXML
    public void handleSearchAction(ActionEvent event) {
        String searchPartIDString = txtSearchParts.getText();
        if (searchPartIDString.equals("")) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(partSearchWarningTitle);
            alert.setHeaderText(partNotFoundHeader);
            //TODO: double check this
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
                    alert.setContentText(partNotMatchedContent);
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
        Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
        alert.setTitle(productPartDeletionErrorTitle);
        alert.setHeaderText(productPartDeleteQuestion);
        alert.setContentText(productPartDeleteInstructions);
        alert.showAndWait();

        if (alert.getResult() == ButtonType.OK) {
            try {
                Part part = tvAssociatedParts.getSelectionModel().getSelectedItem();
                product.deleteAssociatedPart(part);
            } catch (NullPointerException e) {
                Alert nullPartAlert = new Alert(Alert.AlertType.ERROR);
                nullPartAlert.setTitle(productPartDeletionErrorTitle);
                nullPartAlert.setHeaderText(productPartNotDeletedHeader);
                nullPartAlert.setContentText(partNotSelectedContent);
                nullPartAlert.showAndWait();
            }
        } else {
            alert.close();
        }
    }

    @FXML
    public void handleSaveAction(ActionEvent event) throws IOException {
        String name = txtAddProductName.getText();
        String stock = txtAddProductInv.getText();
        String price = txtAddProductPrice.getText();
        String max = txtAddProductMax.getText();
        String min = txtAddProductMin.getText();
        ObservableList<Part> parts = tvAssociatedParts.getItems();

        try {
            HelperFunctions.getEmptyFields(name, stock, price, max, min);
        } catch (ProductEmptyFieldsException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(productNotAddedTitle);
            alert.setHeaderText(productNotAddedHeader);
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }

        if (parts.isEmpty()) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle(productNotSavedTitle);
            alert.setHeaderText(productNotSavedHeader);
            alert.setContentText(productPartNotSelectedContent);
            alert.showAndWait();
        } else {

            product.setProductID(productID);
            product.setName(name);
            product.setInStock(Integer.parseInt(stock));
            product.setPrice(Double.parseDouble(price));
            product.setMax(Integer.parseInt(max));
            product.setMin(Integer.parseInt(min));

            try {
                MainScreenController.inventory.addProduct(product);
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
    public void handleCancelAction(ActionEvent eCancel) throws IOException {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(exitToMainScreen);
            alert.setHeaderText(exitQuestion);
            alert.setContentText(exitInstruction);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                Parent root = FXMLLoader.load(getClass().getResource(MainScreenPath));
                Scene scene = new Scene(root);
                Stage winMainScreen = (Stage) ((Node) eCancel.getSource()).getScene().getWindow();
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
     * sets up productID with maybe a unique partID
     * sets up tables
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        partIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        partNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        partInStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        partPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        associatedPartIDCol.setCellValueFactory(new PropertyValueFactory<>("id"));
        associatedPartNameCol.setCellValueFactory(new PropertyValueFactory<>("name"));
        associatedPartInStockCol.setCellValueFactory(new PropertyValueFactory<>("stock"));
        associatedPartPriceCol.setCellValueFactory(new PropertyValueFactory<>("price"));

        productID = HelperFunctions.generateIDs();
        txtAddProductID.setText("Auto Gen: " + productID);
    }

}

