package InventorySystem.Models;

import InventorySystem.Constants.HelperFunctions;
import InventorySystem.Errors.PartNotValidException;
import InventorySystem.Errors.ProductNotValidException;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;

/**
 *
 * @author Nicholas Hartman
 *
 *
 */
public class Inventory {
    private ObservableList<Product> allProducts = FXCollections.observableArrayList();
    private ObservableList<Part> allParts = FXCollections.observableArrayList();

    public void addPart(Part part) throws PartNotValidException {
        try{
            HelperFunctions.getPartValidation(part);
        } catch (PartNotValidException ex){
            throw ex;
        }
        allParts.addAll(part);
    }
    public void addProduct(Product product) throws ProductNotValidException {
        try {
            HelperFunctions.getProductValidation(product);
        } catch (ProductNotValidException ex){
            throw ex;
        }
        allProducts.add(product);
    }

    public boolean deleteProduct(Product selectedProduct) {
        for (Product p : allProducts){
            if (p.getId() == selectedProduct.getId()) {
                allProducts.remove(p);
                return true;
            }
        }
        return false;
    }

    public Part lookupPart(int partID) {
        for(Part p : allParts) {
            if(p.getId() == partID)
                return p;
        }
        return null;
    }
    public Product lookupProduct(int productID) {
        for (Product p : allProducts) {
            if (p.getId() == productID) {
                return p;
            }
        }
        return null;
    }

    public Part lookupPart(String partName){
        for(Part p : allParts) {
            if(p.getName() == partName)
                return p;
        }
        return null;
    }
    public Product lookupProduct(String productName){
        for(Product p : allProducts) {
            if(p.getName() == productName)
                return p;
        }
        return null;
    }

    public void updatePart(int partIndex, Part part) throws PartNotValidException {
        try{
            HelperFunctions.getPartValidation(part);
        } catch (PartNotValidException ex){
            throw ex;
        }
        allParts.set(partIndex, part);
    }
    public void updateProduct(int productIndex, Product product) throws ProductNotValidException{
        try{
            HelperFunctions.getProductValidation(product);
        } catch (ProductNotValidException ex){
            throw ex;
        }
        allProducts.set(productIndex, product);
    }

    public boolean deletePart(Part selectedPart) {
        for (Part p : allParts) {
            if (p.getId() == selectedPart.getId()) {
                allParts.remove(p);
                return true;
            }
        }
        return false;
    }

    public ObservableList<Part> getAllParts() {
        return allParts;
    }

    public ObservableList<Product> getAllProducts() {
        return allProducts;
    }

}