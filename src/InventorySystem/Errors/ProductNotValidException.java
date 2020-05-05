package InventorySystem.Errors;

public class ProductNotValidException extends Exception{
    public ProductNotValidException(String message){
        super(message);
    }
}
