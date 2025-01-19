package com.mycompany.supermarketsimulation;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

// Represents a customer in the supermarket simulation
public class Customer {
    private int id; // Unique ID of the customer
    private long arrivalTime; // Time when the customer arrives at the supermarket
    private long startTime; // Time when the customer enters the checkout queue
    private List<Item> items; // List of items selected by the customer
    private boolean isVip; // Indicates if the customer is a VIP
    private int waitTime; // Total wait time for the customer
    private int checkoutTime; // Time required to process the customer's items

    // Constructor to initialize a customer with an ID and arrival time
    public Customer(int id, long arrivalTime) {
        this.id = id; // Set customer ID
        this.arrivalTime = arrivalTime; // Set arrival time
        this.items = new ArrayList<>(); // Initialize the list of items
    }

    // Returns the customer's ID
    public int getId() {
        return id;
    }

    // Returns the arrival time of the customer
    public long getArrivalTime() {
        return arrivalTime;
    }

    // Returns the time when the customer entered the queue
    public long getStartTime() {
        return startTime;
    }

    // Returns the list of items selected by the customer
    public List<Item> getItems() {
        return items;
    }

    // Checks if the customer is a VIP
    public boolean isVip() {
        return isVip;
    }

    // Sets the VIP status of the customer
    public void setVip(boolean isVip) {
        this.isVip = isVip;
    }

    // Returns the total wait time for the customer
    public int getWaitTime() {
        return waitTime;
    }

    // Returns the checkout time required for the customer
    public int getCheckoutTime() {
        return checkoutTime;
    }

    // Sets the start time when the customer is added to the queue
    public void setStartTime(long startTime) {
        this.startTime = startTime; // Set the start time
    }

    // Allows the customer to select items from the inventory
    public void selectItems(Item[] inventory) {
        Random rand = new Random();
        int numItems = rand.nextInt(10) + 1; // Customers can choose between 1 and 10 items

        for (int i = 0; i < numItems; i++) {
            int itemIndex = rand.nextInt(inventory.length); // Randomly select an item from the inventory
            Item item = inventory[itemIndex];

            if (item.getStockQuantity() > 0) { // Check if the item is in stock
                items.add(item); // Add the item to the customer's list
                item.decreaseStock(); // Reduce the stock quantity
            }
        }

        this.checkoutTime = numItems; // Set the checkout time based on the number of items
        this.waitTime = checkoutTime * 2; // Assume 2 seconds per item as wait time
    }
}
