package Models;

import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.Scanner;

public class Transaction {
    private int transactionId;
    private int destinationId;
    private String destination;
    private int passengerTypeId;
    private String passengerType;
    private double passengerTypePrice;
    private int seatTypeId;
    private String seatType;
    private double seatTypePrice;

    public String getDestination() {
        return destination;
    }

    public void setDestination(String destination) {
        this.destination = destination;
    }

    public String getPassengerType() {
        return passengerType;
    }

    public void setPassengerType(String passengerType) {
        this.passengerType = passengerType;
    }

    public double getPassengerTypePrice() {
        return passengerTypePrice;
    }

    public void setPassengerTypePrice(double passengerTypePrice) {
        this.passengerTypePrice = passengerTypePrice;
    }

    public String getSeatType() {
        return seatType;
    }

    public void setSeatType(String seatType) {
        this.seatType = seatType;
    }

    public double getSeatTypePrice() {
        return seatTypePrice;
    }

    public void setSeatTypePrice(double seatTypePrice) {
        this.seatTypePrice = seatTypePrice;
    }
    private int quantity;
    private double total;
    private double amount;
    private double change;
    private Timestamp created_at;

    public int getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(int transactionId) {
        this.transactionId = transactionId;
    }

    public int getDestinationId() {
        return destinationId;
    }

    public void setDestinationId(int destinationId) {
        this.destinationId = destinationId;
    }

    public int getPassengerTypeId() {
        return passengerTypeId;
    }

    public void setPassengerTypeId(int passengerTypeId) {
        this.passengerTypeId = passengerTypeId;
    }

    public int getSeatTypeId() {
        return seatTypeId;
    }

    public void setSeatTypeId(int seatTypeId) {
        this.seatTypeId = seatTypeId;
    }

    public int getQuantity() {
        return quantity;
    }

    public void setQuantity(int quantity) {
        this.quantity = quantity;
    }

    public double getTotal() {
        return total;
    }

    public void setTotal(double total) {
        this.total = total;
    }

    public double getAmount() {
        return amount;
    }

    public void setAmount(double amount) {
        this.amount = amount;
    }

    public double getChange() {
        return change;
    }

    public void setChange(double change) {
        this.change = change;
    }

    public Timestamp getCreated_at() {
        return created_at;
    }

    public void setCreated_at(Timestamp created_at) {
        this.created_at = created_at;
    }
    
    public void displayTransaction(Transaction t) {
        System.out.println(
            "ID: " + t.getTransactionId() +
            "\nDestination: " + t.getDestination() +
            "\nPassenger Type: " + t.getPassengerType() + " - " + t.getPassengerTypePrice() +
            "\nSeat Type: " + t.getSeatType() + " - " + t.getSeatTypePrice() +
            "\nTicket/s: " + t.getQuantity() +
            "\nTotal: " + t.getTotal() +
            "\nAmount: " + t.getAmount() +
            "\nChange: " + t.getChange() +
            "\nCreated_at: " + t.getCreated_at() + "\n"
        );
    }
}
