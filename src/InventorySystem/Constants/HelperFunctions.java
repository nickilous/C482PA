package InventorySystem.Constants;

import InventorySystem.Errors.PartEmptyFieldsException;
import InventorySystem.Errors.PartNotValidException;
import InventorySystem.Errors.ProductEmptyFieldsException;
import InventorySystem.Errors.ProductNotValidException;
import InventorySystem.Models.Part;
import InventorySystem.Models.Product;

import java.util.Random;

public class HelperFunctions {
    //May or may not generate a some what unique partID
    public static int generateIDs() {
        Random rand = new Random();
        int[] ints = new int[7];
        for (int i = 0; i < ints.length; i++) {
            ints[i] = rand.nextInt(9);
        }
        StringBuilder sb = new StringBuilder(ints.length);
        for (int digit : ints) {
            sb.append(digit);
        }
        return Integer.parseInt(sb.toString());
    }

    public static void getPartValidation(Part part) throws PartNotValidException{
        String errPartValid = "";
        if (part.getStock() < 1) {
            errPartValid = errPartValid + "\nThe Inventory cannot be less than 1. ";
        } else
        if (part.getPrice() <= 0) {
            errPartValid = errPartValid + "\nThe Price must be greater than $0. ";
        } else
        if (part.getMax() < part.getMin()) {
            errPartValid = errPartValid + "\nThe Maximum stock must be greater than the Minimum stock. ";
        }
        if (part.getMin() > part.getMax()) {
            errPartValid = errPartValid + "\nThe Inventory must be less than or equal to the Maximum stock. ";
        } else
        if (part.getStock() < part.getMin()) {
            errPartValid = errPartValid + "\nThe Inventory must be greater than or equal to the Minimum stock. ";
        }
        if (!errPartValid.isEmpty()){
            throw new PartNotValidException(errPartValid);
        }
    }

    public static void getPartEmptyFields(String name, String inStock, String price, String max, String min, String partDyn) throws PartEmptyFieldsException {
        String errPartField = "";
        if (name.equals("")) {
            errPartField = errPartField + "The Part Name field cannot be empty. ";
        } else
        if (inStock.equals("")) {
            errPartField = errPartField + "\nThe Part Inventory field cannot be empty. ";
        } else
        if (price.equals("")) {
            errPartField = errPartField + "\nThe Part Price field cannot be empty. ";
        } else
        if (max.equals("")) {
            errPartField = errPartField + "\nThe Part Max field cannot be empty. ";
        } else
        if (min.equals("")) {
            errPartField = errPartField + "\nThe Part Min field cannot be empty. ";
        } else
        if (partDyn.equals("")) {
            errPartField = errPartField + "\nThe Part MachineID or Company Name field cannot be empty. ";
        }
        if (!errPartField.isEmpty()){
            throw new PartEmptyFieldsException(errPartField);
        }
    }

    public static void getProductValidation(Product product) throws ProductNotValidException {
        String errProductValid = "";
        if (product.getInStock() < 1) {
            errProductValid = errProductValid + "\nThe Inventory cannot be less than 1. ";
        } else
        if (product.getPrice() <= 0) {
            errProductValid = errProductValid + "\nThe Price must be greater than $0. ";
        } else
        if (product.getMax() < product.getMin()) {
            errProductValid = errProductValid + "\nThe Maximum stock must be greater than the Minimum stock. ";
        } else
        if (product.getInStock() > product.getMax()) {
            errProductValid = errProductValid + "\nThe Inventory must be less than or equal to the Maximum stock. ";
        } else
        if (product.getInStock() < product.getMin()) {
            errProductValid = errProductValid + "\nThe Inventory must be greater than or equal to the Minimum stock. ";
        }
        if (!errProductValid.isEmpty()){
            throw new ProductNotValidException(errProductValid);
        }
    }

    public static void getEmptyFields(String name, String inStock, String price, String max, String min) throws ProductEmptyFieldsException {
        String errProductField = "";
        if (name.equals("")) {
            errProductField = errProductField + "The Product Name field cannot be empty. ";
        } else
        if (inStock.equals("")) {
            errProductField = errProductField + "\nThe Product Inventory field cannot be empty. ";
        } else
        if (price.equals("")) {
            errProductField = errProductField + "\nThe Product Price field cannot be empty. ";
        } else
        if (max.equals("")) {
            errProductField = errProductField + "\nThe Product Max field cannot be empty. ";
        } else
        if (min.equals("")) {
            errProductField = errProductField + "\nThe Product Min field cannot be empty. ";
        }
        if (!errProductField.isEmpty()){
            throw new ProductEmptyFieldsException(errProductField);
        }
    }
}
