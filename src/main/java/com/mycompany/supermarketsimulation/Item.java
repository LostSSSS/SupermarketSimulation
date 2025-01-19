package com.mycompany.supermarketsimulation;

// Represents an item in the supermarket inventory
public class Item {
    private String name; // Name of the item
    private int stockQuantity; // Current stock quantity of the item

    // Constructor to initialize the item with a name and stock quantity
    public Item(String name, int stockQuantity) {
        this.name = name; // Set the item name
        this.stockQuantity = stockQuantity; // Set the initial stock quantity
    }

    // Getter method to retrieve the item's name
    public String getName() {
        return name;
    }

    // Getter method to retrieve the item's current stock quantity
    public int getStockQuantity() {
        return stockQuantity;
    }

    // Decreases the stock quantity by 1 if stock is available
    public void decreaseStock() {
        if (stockQuantity > 0) {
            stockQuantity--; // Reduce stock by 1
        } else {
            System.out.println("Out of stock for item: " + name); // Notify if out of stock
        }
    }

    // Restocks the item by adding the specified quantity
    public void restock(int quantity) {
        stockQuantity += quantity; // Add the specified quantity to stock
    }
}
