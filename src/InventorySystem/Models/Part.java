package InventorySystem.Models;

/**
 *
 * @author Nicholas Hartman
 *
 *
 */
public abstract class Part {
    private int id;
    private int stock;
    private int max;
    private int min;
    private double price;
    private String name;

    public Part(int id, String name, double price, int stock, int min, int max) {
        this.id = id;
        this.name = name ;
        this.price = price;
        this.stock = stock;
        this.min = min;
        this.max = max;
    }
    public Part(){
        this.id = 0;
        this.name = "";
        this.price = 0.0;
        this.stock = 0;
        this.min = 0;
        this.max = 0;
    }


    public int getId(){ return id;}

    public void setId(int id){
        this.id = id;
    }

    public int getStock(){return stock;}

    public void setStock(int stock){this.stock = stock;}

    public int getMin(){return min;}

    public void setMin(int min){this.min = min;}

    public int getMax(){return max;}

    public void setMax(int max){this.max = max;}

    public String getName(){return name;}

    public void setName(String name){this.name = name;}

    public double getPrice(){ return price;}

    public void setPrice(double price){ this.price = price;}

}
