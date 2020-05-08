package InventorySystem.ViewControllers;

import InventorySystem.Constants.HelperFunctions;
import InventorySystem.Constants.ScreenPaths;
import InventorySystem.Constants.UIStringConstants;
import InventorySystem.Errors.PartEmptyFieldsException;
import InventorySystem.Errors.PartNotValidException;
import InventorySystem.Models.InHouse;
import InventorySystem.Models.OutSourced;
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

/**
 * FXML Controller class
 *
 * @author Nicholas Hartman
 *
 *
 */
public class AddPartScreenController implements Initializable, ScreenPaths, UIStringConstants {

    /**
     * Start of UI Elements
     */
    @FXML
    private RadioButton rbAddPartInHouse;

    @FXML
    private ToggleGroup tgSource;

    @FXML
    private Label lblAddPartDyn;

    @FXML
    private TextField txtAddPartID;

    @FXML
    private TextField txtAddPartName;

    @FXML
    private TextField txtAddPartInv;

    @FXML
    private TextField txtAddPartPrice;

    @FXML
    private TextField txtAddPartMax;

    @FXML
    private TextField txtAddPartDyn;

    @FXML
    private TextField txtAddPartMin;

    @FXML
    private boolean bOutSourced;

    @FXML
    private int partID;
// End of UI Elements

    /**
     * Start of EventHandlers
     *
     */
    @FXML
    void handleAddPartCancel(ActionEvent event) throws IOException {
        try {
            Alert alert = new Alert(Alert.AlertType.CONFIRMATION);
            alert.setTitle(exitToMainScreen);
            alert.setHeaderText(exitQuestion);
            alert.setContentText(exitInstruction);
            alert.showAndWait();

            if (alert.getResult() == ButtonType.OK) {
                Parent root = FXMLLoader.load(getClass().getResource(MainScreenPath));
                Scene scene = new Scene(root);
                Stage winMainScreen = (Stage)((Node)event.getSource()).getScene().getWindow();
                winMainScreen.setTitle(mainScreenTitle);
                winMainScreen.setScene(scene);
                winMainScreen.show();
            }
            else {
                alert.close();
            }
        }
        catch (IOException e) {}
    }

    @FXML
    void handleAddPartSave(ActionEvent event) throws IOException {
        String name = txtAddPartName.getText();
        String inStock = txtAddPartInv.getText();
        String price = txtAddPartPrice.getText();
        String min = txtAddPartMin.getText();
        String max = txtAddPartMax.getText();
        String partDyn = txtAddPartDyn.getText();

        try {
            HelperFunctions.getPartEmptyFields(name, inStock, price, max, min,partDyn);
        } catch (PartEmptyFieldsException ex){
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle(partAdditionWarningHeader);
            alert.setHeaderText(partAdditionWarningHeader);
            alert.setContentText(ex.getMessage());
            alert.showAndWait();
        }
        if (bOutSourced == true) {
            OutSourced outSourced = new OutSourced(partID,
                    name,
                    Double.parseDouble(price),
                    Integer.parseInt(inStock),
                    Integer.parseInt(min),
                    Integer.parseInt(max),
                    partDyn);
            try {
                MainScreenController.inventory.addPart(outSourced);
            } catch (PartNotValidException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(partAdditionWarningTitle);
                alert.setHeaderText(partAdditionWarningHeader);
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        } else {
            InHouse inHouse = new InHouse(partID,
                    name,
                    Double.parseDouble(price),
                    Integer.parseInt(inStock),
                    Integer.parseInt(min),
                    Integer.parseInt(max),
                    Integer.parseInt(partDyn));
            try {
                MainScreenController.inventory.addPart(inHouse);
            } catch (PartNotValidException ex) {
                Alert alert = new Alert(Alert.AlertType.WARNING);
                alert.setTitle(partAdditionWarningTitle);
                alert.setHeaderText(partAdditionWarningHeader);
                alert.setContentText(ex.getMessage());
                alert.showAndWait();
            }
        }
        FXMLLoader loader = new FXMLLoader(getClass().getResource(MainScreenPath));
        Parent root = loader.load();
        Scene scene = new Scene(root);
        Stage winMainScreen = (Stage) ((Node) event.getSource()).getScene().getWindow();
        winMainScreen.setTitle(mainScreenTitle);
        winMainScreen.setScene(scene);
        winMainScreen.show();
    }



    /**
     * Changes when InHouse and Outsourced is changed.
     *
     */

    @FXML
    void setTGSource(ActionEvent event) {
        if (tgSource.getSelectedToggle() == rbAddPartInHouse) {
            lblAddPartDyn.setText(machineIDText);
            txtAddPartDyn.setPromptText(machineIDPrompt);
        }
        else {
            lblAddPartDyn.setText(companyNameText);
            txtAddPartDyn.setPromptText(companyPrompt);
            bOutSourced = true;
        }
    }

    /**
     * Initializes the controller class.
     * setup hopefully unique partID
     */
    @Override
    public void initialize(URL url, ResourceBundle rb) {

        partID = HelperFunctions.generateIDs();
        txtAddPartID.setText("Auto Gen: " + partID);
    }
}