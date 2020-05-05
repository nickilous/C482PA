package InventorySystem.Models;

/**
 *
 * @author Nicholas Hartman
 *
 *
 */
public class OutSourced extends Part {
    private String companyName;

    public OutSourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, price, stock, min, max);
        this.companyName = companyName;
    }
    public OutSourced(){
        super();
        this.companyName = "";
    }

    public String getCompanyName() {
        return companyName;
    }
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

}
