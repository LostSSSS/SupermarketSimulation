package com.mycompany.supermarketsimulation;

import java.util.LinkedList;
import java.util.PriorityQueue;
import java.util.Queue;

// Represents a checkout queue, handling both VIP and regular customers
public class CheckoutQueue {

    private int queueNumber; // Unique identifier for the queue
    private Queue<Customer> regularQueue; // Queue for regular customers
    private PriorityQueue<Customer> vipQueue; // Priority queue for VIP customers
    private int customersProcessed = 0; // Total number of processed customers
    private long totalWaitTime = 0; // Cumulative wait time for all customers
    private long currentTime = 0; // Tracks the current time for the queue
    private int totalItemsProcessed = 0; // Total number of items processed

    // Constructor to initialize the checkout queue with a specific number
    public CheckoutQueue(int queueNumber) {
        this.queueNumber = queueNumber; // Set the queue number
        this.regularQueue = new LinkedList<>(); // Initialize regular queue
        this.vipQueue = new PriorityQueue<>((c1, c2) -> 
            Long.compare(c1.getArrivalTime(), c2.getArrivalTime())); // Sort VIP customers by arrival time
    }

    // Adds a customer to the appropriate queue (VIP or regular)
    public void addCustomer(Customer customer) {
        customer.setStartTime(currentTime); // Record the time when the customer is added

        if (customer.isVip()) {
            vipQueue.add(customer); // Add VIP customer to the VIP queue
            System.out.println("VIP Customer " + customer.getId() + " added to Queue " + queueNumber);
        } else {
            regularQueue.add(customer); // Add regular customer to the regular queue
            System.out.println("Regular Customer " + customer.getId() + " added to Queue " + queueNumber);
        }

        printQueue(); // Display the current state of the queue
    }

    // Processes customers from the VIP queue first, then the regular queue
    public void processCustomer(long time) {
        if (!vipQueue.isEmpty()) {
            processQueueCustomer(vipQueue, time); // Process a customer from the VIP queue
        }

        if (!regularQueue.isEmpty()) {
            processQueueCustomer(regularQueue, time); // Process a customer from the regular queue
        }
    }

    // Helper method to process a customer from a given queue
    private void processQueueCustomer(Queue<Customer> queue, long time) {
        Customer customer = queue.peek(); // Get the first customer without removing

        if (customer != null) {
            long expectedCheckoutTime = customer.getArrivalTime() + customer.getWaitTime(); // Calculate checkout time

            if (currentTime >= expectedCheckoutTime) {
                queue.poll(); // Remove the customer from the queue
                long actualWaitTime = currentTime - customer.getStartTime(); // Calculate the actual wait time
                totalWaitTime += actualWaitTime; // Add to total wait time
                customersProcessed++; // Increment the number of processed customers
                totalItemsProcessed += customer.getItems().size(); // Add customer's items to total items processed

                System.out.println("Queue " + queueNumber + ": Processed Customer " + customer.getId()
                        + ". Waited: " + actualWaitTime + "s, Items: " + customer.getItems().size());
            }
        }
    }

    // Returns the number of customers processed by this queue
    public int getCustomersProcessed() {
        return customersProcessed;
    }

    // Returns the average wait time for customers in this queue
    public long getAverageWaitTime() {
        return customersProcessed == 0 ? 0 : totalWaitTime / customersProcessed; // Avoid division by zero
    }

    // Returns the queue number
    public int getQueueNumber() {
        return queueNumber;
    }

    // Returns the total number of items processed by this queue
    public int getTotalItemsProcessed() {
        return totalItemsProcessed;
    }

    // Checks if both the VIP and regular queues are empty
    public boolean isEmpty() {
        return vipQueue.isEmpty() && regularQueue.isEmpty();
    }

    // Prints the current state of the VIP and regular queues
    public void printQueue() {
        System.out.print("Queue " + queueNumber + " current state (VIP + Regular): ");
        System.out.print("VIP Queue: ");
        for (Customer customer : vipQueue) {
            System.out.print("Customer " + customer.getId() + " ");
        }

        System.out.print("| Regular Queue: ");
        for (Customer customer : regularQueue) {
            System.out.print("Customer " + customer.getId() + " ");
        }
        System.out.println();
    }

    // Advances the processing time by a specified amount
    public void advanceProcessingTime(long timeToAdvance) {
        currentTime += timeToAdvance; // Increment the current time
    }
}
