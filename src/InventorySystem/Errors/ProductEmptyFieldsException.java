package InventorySystem.Errors;

public class ProductEmptyFieldsException extends Exception{
    public ProductEmptyFieldsException(String message){
        super(message);
    }
}
