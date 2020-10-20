package me.joshmckinney.inventory.Model;

public class InHouse extends Part {
    // Declaration
    private int machineId;
    public InHouse(int id, String name, double price, int stock, int min, int max, int machineId) {
        super(id, name, stock, price, min, max);
        setMachineId(machineId);
    }

    // Setter
    public void setMachineId(int machineId) {
        this.machineId = machineId;
    }

    // Getter
    public int getMachineId() {
        return machineId;
    }
}
