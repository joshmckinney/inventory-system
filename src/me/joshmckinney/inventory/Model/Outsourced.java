package me.joshmckinney.inventory.Model;

public class Outsourced extends Part {
    // Declaration
    private String companyName;
    public Outsourced(int id, String name, double price, int stock, int min, int max, String companyName) {
        super(id, name, stock, price, min, max);
        setCompanyName(companyName);
    }

    // Setter
    public void setCompanyName(String companyName) {
        this.companyName = companyName;
    }

    // Getter
    public String getCompanyName() {
        return companyName;
    }
}
