package InventorySystem.ViewControllers;

import InventorySystem.Constants.HelperFunctions;
import InventorySystem.Constants.ScreenPaths;
import InventorySystem.Constants.UIStringConstants;
import InventorySystem.Errors.PartEmptyFieldsException;
import InventorySystem.Errors.PartNotValidException;
import InventorySystem.Models.InHouse;
import InventorySystem.Models.OutSourced;
import InventorySystem.Models.Part;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.stage.Stage;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;

import static InventorySystem.ViewControllers.MainScreenController.getSelectedPart;
import static InventorySystem.ViewControllers.MainScreenController.getSelectedPartIndex;

/**
 * ModifyPartScreenController
 *
 * @author Nicholas Hartman
 */
public class ModifyPartScreenController implements Initializable, ScreenPaths, UIStringConstants {
    /**
     * Start of UI Elements
     */
    @FXML
    private RadioButton rbInHouse;

    @FXML
    private ToggleGroup tgSource;

    @FXML
    private RadioButton rbOutsourced;

    @FXML
    private Label lblModifyPartDyn;

    @FXML
    private TextField txtModifyPartID;

    @FXML
    private TextField txtModifyPartName;

    @FXML
    private TextField txtModifyPartInv;

    @FXML
    private TextField txtModifyPartPrice;

    @FXML
    private TextField txtModifyPartMax;

    @FXML
    private TextField txtModifyPartDyn;

    @FXML
    private TextField txtModifyPartMin;

    @FXML
    private int partID;

    @FXML
    private boolean bOutSourced;

    private Part selectedPart;
    private Part modifiedPart;
    // End of UI Elements


    @FXML
    public void getSourceAction(ActionEvent event) throws Exception {
        if (tgSource.getSelectedToggle() == rbInHouse) {
            lblModifyPartDyn.setText(machineIDText);
            txtModifyPartDyn.setPromptText(machineIDPrompt);
        } else {
            lblModifyPartDyn.setText(companyNameText);
            txtModifyPartDyn.setPromptText(companyPrompt);
            bOutSourced = true;
        }
    }

    @FXML
    public void handleSaveAction(ActionEvent event) {
        String name = txtModifyPartName.getText();
        String inStock = txtModifyPartInv.getText();
        String price = txtModifyPartPrice.getText();
        String min = txtModifyPartMin.getText();
        String max = txtModifyPartMax.getText();
        String partDyn = txtModifyPartDyn.getText();

        try {
            HelperFunctions.getPartEmptyFields(name, inStock, price, max, min, partDyn);
        } catch (PartEmptyFieldsException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(partModificationWarningTitle);
            alert.setHeaderText(partModificationWarningHeader);
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
        selectedPart.setId(partID);
        selectedPart.setName(name);
        selectedPart.setStock(Integer.parseInt(inStock));
        selectedPart.setPrice(Double.parseDouble(price));
        selectedPart.setMax(Integer.parseInt(max));
        selectedPart.setMin(Integer.parseInt(min));


        if (bOutSourced == true) {
            OutSourced outSourced = (OutSourced) selectedPart;
            outSourced.setCompanyName(partDyn);
            modifiedPart = outSourced;
        } else {
            InHouse inHouse = (InHouse) selectedPart;
            inHouse.setMachineID(Integer.parseInt(partDyn));
            modifiedPart = inHouse;
        }
        try {
            MainScreenController.inventory.updatePart(getSelectedPartIndex(), modifiedPart);
        } catch (PartNotValidException ex) {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(partModificationWarningTitle);
            alert.setHeaderText(partModificationWarningHeader);
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }

        FXMLLoader loader = new FXMLLoader(getClass().getResource(MainScreenPath));
        try {
            Parent root = loader.load();
            Scene scene = new Scene(root);
            Stage winMainScreen = (Stage) ((Node) event.getSource()).getScene().getWindow();
            winMainScreen.setTitle(mainScreenTitle);
            winMainScreen.setScene(scene);
            winMainScreen.show();
        } catch (IOException ex){}
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
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        selectedPart = getSelectedPart();
        partID = getSelectedPart().getId();
        txtModifyPartID.setText("Auto Gen: " + partID);
        txtModifyPartName.setText(selectedPart.getName());
        txtModifyPartInv.setText(Integer.toString(selectedPart.getStock()));
        txtModifyPartPrice.setText(Double.toString(selectedPart.getPrice()));
        txtModifyPartMax.setText(Integer.toString(selectedPart.getMax()));
        txtModifyPartMin.setText(Integer.toString(selectedPart.getMin()));
        if (selectedPart instanceof InHouse) {
            tgSource.selectToggle(rbInHouse);
            txtModifyPartDyn.setText(Integer.toString(((InHouse) selectedPart).getMachineID()));
            bOutSourced = false;
        } else {
            tgSource.selectToggle(rbOutsourced);
            txtModifyPartDyn.setText(((OutSourced) selectedPart).getCompanyName());
            lblModifyPartDyn.setText(companyNameText);
            txtModifyPartDyn.setPromptText(companyPrompt);
            bOutSourced = true;
        }
    }

}
