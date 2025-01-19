package com.mycompany.supermarketsimulation;

import java.util.Random;
import java.util.Scanner;

public class SupermarketSimulation {

    private static final int NUM_COUNTERS = 3; // Number of checkout counters
    private CheckoutQueue[] checkoutQueues;    // Array for the checkout queues
    private Item[] inventory;                  // Array for inventory items
    private Random rand;                       // Random generator for simulation randomness
    private int numCustomersToProcess;         // Total number of customers to process
    private int customersProcessed = 0;        // Counter for processed customers

    // Constructor to initialize the simulation with a given number of customers
    public SupermarketSimulation(int numCustomersToProcess) {
        this.checkoutQueues = new CheckoutQueue[NUM_COUNTERS]; // Initialize checkout queues
        this.inventory = new Item[10]; // Initialize inventory with 10 items
        this.rand = new Random(); // Create a random generator instance
        this.numCustomersToProcess = numCustomersToProcess; // Set the customer processing target
        initialize(); // Setup the initial state of the simulation
    }

    // Initializes checkout queues and inventory items
    private void initialize() {
        for (int i = 0; i < NUM_COUNTERS; i++) {
            checkoutQueues[i] = new CheckoutQueue(i + 1); // Create queues with unique numbers
        }

        for (int i = 0; i < 10; i++) {
            inventory[i] = new Item("Item " + (i + 1), 20); // Add items with initial stock of 20
        }
    }

    // Assigns customers to appropriate queues based on VIP status and items
    private void assignToQueue(Customer customer) {
        boolean isVip = rand.nextDouble() < 0.2; // 20% chance of being a VIP customer
        customer.setVip(isVip); // Mark customer as VIP if applicable
        CheckoutQueue selectedQueue;

        if (isVip) {
            // VIP customers are evenly distributed across queues
            selectedQueue = checkoutQueues[customersProcessed % NUM_COUNTERS];
            System.out.println("VIP Customer " + customer.getId() + " arrived with " +
                    customer.getItems().size() + " items. Assigned to Queue " + selectedQueue.getQueueNumber() + ".");
        } else if (customer.getItems().size() <= 3) {
            // Customers with 3 or fewer items use the express checkout
            selectedQueue = checkoutQueues[0];
            System.out.println("Customer " + customer.getId() + " arrived with " +
                    customer.getItems().size() + " items. Assigned to Express Checkout.");
        } else {
            // Regular customers are distributed across queues
            selectedQueue = checkoutQueues[customersProcessed % NUM_COUNTERS];
            System.out.println("Customer " + customer.getId() + " arrived with " +
                    customer.getItems().size() + " items. Assigned to Queue " + selectedQueue.getQueueNumber() + ".");
        }

        selectedQueue.addCustomer(customer); // Add customer to the selected queue
    }

    // Runs the supermarket simulation
    public void runSimulation() {
        long time = 0; // Simulation time starts at 0

        while (true) {
            System.out.println("\n[SIMULATION] Time Unit: " + time);

            // Generate new customers if the target is not yet reached
            if (customersProcessed < numCustomersToProcess) {
                int batchSize = rand.nextInt(5) + 1; // Randomly create 1 to 5 customers at a time
                for (int i = 0; i < batchSize; i++) {
                    if (customersProcessed < numCustomersToProcess) {
                        Customer customer = new Customer(customersProcessed + 1, time); // Create a new customer
                        customer.selectItems(inventory); // Assign items to the customer
                        customersProcessed++; // Increment processed customer count
                        assignToQueue(customer); // Assign the customer to a queue
                    }
                }
            }

            // Process customers in each queue
            boolean queuesAreEmpty = true;
            for (int i = 0; i < NUM_COUNTERS; i++) {
                CheckoutQueue queue = checkoutQueues[i];
                queue.processCustomer(time); // Process the next customer in the queue
                if (!queue.isEmpty()) {
                    queuesAreEmpty = false; // Check if any queue still has customers
                }
            }

            // Restock inventory every 5 time units
            if (time % 5 == 0) {
                restockInventory(); // Add stock to inventory items
            }

            // Advance the processing time for all queues
            for (int i = 0; i < NUM_COUNTERS; i++) {
                checkoutQueues[i].advanceProcessingTime(1);
            }

            // End the simulation if all queues are empty and all customers are processed
            if (queuesAreEmpty && customersProcessed >= numCustomersToProcess) {
                break;
            }

            // Add a delay for better visualization of the simulation
            try {
                Thread.sleep(500); // Wait for 500 milliseconds
            } catch (InterruptedException e) {
                Thread.currentThread().interrupt(); // Restore interrupted status
            }

            time++; // Increment the simulation time
        }

        printSummary(); // Print the final summary after the simulation
    }

    // Restocks inventory items by adding 50 units to each item
    private void restockInventory() {
        for (Item item : inventory) {
            item.restock(50); // Restock 50 units per item
            System.out.println("[INVENTORY] " + item.getName() + " restocked by 50 units. New stock: " + item.getStockQuantity());
        }
    }

    // Prints the summary of the simulation results
    private void printSummary() {
        System.out.println("\n[SUMMARY] Simulation Complete");
        int totalItemsProcessed = 0; // Total items processed across all queues
        int totalCustomersProcessed = 0; // Total customers processed across all queues

        for (int i = 0; i < NUM_COUNTERS; i++) {
            CheckoutQueue queue = checkoutQueues[i];
            System.out.println("Queue " + queue.getQueueNumber() + ":");
            System.out.println("  Customers processed: " + queue.getCustomersProcessed());
            System.out.println("  Average wait time: " + queue.getAverageWaitTime() + "s.");
            totalItemsProcessed += queue.getTotalItemsProcessed(); // Add items processed by this queue
            totalCustomersProcessed += queue.getCustomersProcessed(); // Add customers processed by this queue
        }

        System.out.println("Total customers processed: " + totalCustomersProcessed);
        System.out.println("Total items processed: " + totalItemsProcessed);
    }

    // Main method to start the supermarket simulation
    public static void main(String[] args) {
        Scanner scanner = new Scanner(System.in);
        System.out.print("Enter the number of customers to process: ");
        int numCustomersToProcess = scanner.nextInt(); // Get the target number of customers

        SupermarketSimulation simulation = new SupermarketSimulation(numCustomersToProcess); // Create simulation instance
        simulation.runSimulation(); // Run the simulation
    }
}
