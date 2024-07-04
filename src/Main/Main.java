package Main;

import Interface.TransactionActionImplementation;
import Models.Transaction;
import SQL_Connection.SQL_Connector;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.Timestamp;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class Main {
    public static void main(String[] args) {
        menu();
    }
    
    public static void menu() {
        Scanner scanner = new Scanner(System.in);
        TransactionActionImplementation tai = new TransactionActionImplementation();
        
        System.out.print(
            "Shipping Lines Ticketing\n" +
            "1. Create Transaction\n" +
            "2. View Transaction\n" +
            "3. Update Transaction\n" +
            "4. Delete Transaction\n" +
            "5.Press Any Key to Exit\n" +
            "Entry: "
        );
        int entry = scanner.nextInt();
        
        switch (entry) {
            case 1: {
                create();
                askUser();
                break;
            }
            case 2: {
                System.out.print(
                        "1. View all\n" +
                        "2. Search by date\n" +
                        "Select: "
                );
                int selected = scanner.nextInt();
                
                tai.viewTransaction(selected);
                askUser();
                break;
            }
            case 3: {
                update();
                askUser();
                break;
            }
            case 4: {
//                tai.deleteTransaction();
                askUser();
                break;
            }
            case 5: {
                bye();
                break;
            }
            default:
                invalidInput();
                menu();
        }
    }
    
    public static void create() {
        Transaction t = new Transaction();
        List<Transaction> transactions = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        
        try {
            Connection conn = SQL_Connector.getConnection();
            
            String query = "SELECT * FROM destinations ORDER BY destination_id ASC";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            System.out.println("\nDestinations");
            while(rs.next()) {
                Transaction destination = new Transaction();
                destination.setDestinationId(rs.getInt("destination_id"));
                destination.setDestination(rs.getString("destination"));
                System.out.println(destination.getDestinationId() + ". " + destination.getDestination());
                transactions.add(destination);
            }
            
            System.out.print("Select: ");
            t.setDestinationId(scanner.nextInt());
            
            for (Transaction destination : transactions) {
                if (destination.getDestinationId() == t.getDestinationId()) {
                    t.setDestination(destination.getDestination());
                    break;
                }
            }
            
            ps.close();
            rs.close();
            
            query = "SELECT * FROM passenger_types  ORDER BY passenger_type_id ASC";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            
            System.out.println("\nPassenger Types");
            while(rs.next()) {
                Transaction passengerType = new Transaction();
                passengerType.setPassengerTypeId(rs.getInt("passenger_type_id"));
                passengerType.setPassengerType(rs.getString("passenger_type"));
                passengerType.setPassengerTypePrice(rs.getDouble("passenger_type_price"));
                System.out.println(passengerType.getPassengerTypeId() + ". " + passengerType.getPassengerType() + " - " + passengerType.getPassengerTypePrice());
                transactions.add(passengerType);
            }
            
            System.out.print("Select: ");
            t.setPassengerTypeId(scanner.nextInt());
            
            for (Transaction passengerType : transactions) {
                if (passengerType.getPassengerTypeId() == t.getPassengerTypeId()) {
                    t.setPassengerType(passengerType.getPassengerType());
                    t.setPassengerTypePrice(passengerType.getPassengerTypePrice());
                    break;
                }
            }
            
            ps.close();
            rs.close();
            
            query = "SELECT * FROM seat_types  ORDER BY seat_type_id ASC";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            
            System.out.println("\nSeat Types");
            while(rs.next()) {
                Transaction seatType = new Transaction();
                seatType.setSeatTypeId(rs.getInt("seat_type_id"));
                seatType.setSeatType(rs.getString("seat_type"));
                seatType.setSeatTypePrice(rs.getDouble("seat_type_price"));
                System.out.println(seatType.getSeatTypeId() + ". " + seatType.getSeatType() + " - " + seatType.getSeatTypePrice());
                transactions.add(seatType);
            }
            
            System.out.print("Select: ");
            t.setSeatTypeId(scanner.nextInt());
            
            for (Transaction seatType : transactions) {
                if (seatType.getSeatTypeId() == t.getSeatTypeId()) {
                    t.setSeatType(seatType.getSeatType());
                    t.setSeatTypePrice(seatType.getSeatTypePrice());
                    break;
                }
            }
            
            ps.close();
            rs.close();
            
            System.out.println("\nHow many tickets? ");
            t.setQuantity(scanner.nextInt());
            
            t.setTotal((t.getSeatTypePrice() + t.getPassengerTypePrice()) * t.getQuantity());
            
            
            do {
                System.out.println("\nTotal: " + t.getTotal());
                System.out.print("Amount: ");
                t.setAmount(scanner.nextDouble());

                if(t.getAmount() < t.getTotal()) {
                    System.out.println("Insuficient amount! Please try again.");
                } else {
                    t.setChange(t.getAmount() - t.getTotal());
                }
            } while(t.getAmount() < t.getTotal());
            
            t.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));
            
            TransactionActionImplementation tai = new TransactionActionImplementation();
            tai.createTransaction(t);
        } catch (Exception e) {
            System.out.println(e);
        }
    }
    
    public static boolean update() {
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        boolean recordFound = false;
        Transaction t = new Transaction();
        List<Transaction> transactions = new ArrayList<>();
        Scanner scanner = new Scanner(System.in);
        boolean isUpdated = false;
        
        try {
            Connection conn = SQL_Connector.getConnection();
            
            System.out.println("\nEnter transaction id: ");
            t.setTransactionId(scanner.nextInt());
            
            String query = "SELECT transaction_id FROM transactions";
            ps = conn.prepareStatement(query);
            rs = ps.executeQuery();
            
            while(rs.next()) {
                if(t.getTransactionId() == rs.getInt("transaction_id")) {
                    recordFound = true;
                    
                    String query2 = "SELECT * FROM destinations ORDER BY destination_id ASC";
                    ps2 = conn.prepareStatement(query2);
                    rs2 = ps2.executeQuery();

                    System.out.println("\nDestinations");
                    while(rs2.next()) {
                        Transaction destination = new Transaction();
                        destination.setDestinationId(rs2.getInt("destination_id"));
                        destination.setDestination(rs2.getString("destination"));
                        System.out.println(destination.getDestinationId() + ". " + destination.getDestination());
                        transactions.add(destination);
                    }

                    System.out.print("Select new destination: ");
                    t.setDestinationId(scanner.nextInt());

                    for (Transaction destination : transactions) {
                        if (destination.getDestinationId() == t.getDestinationId()) {
                            t.setDestination(destination.getDestination());
                            break;
                        }
                    }

                    ps2.close();
                    rs2.close();

                    query2 = "SELECT * FROM passenger_types ORDER BY passenger_type_id ASC";
                    ps2 = conn.prepareStatement(query2);
                    rs2 = ps2.executeQuery();

                    System.out.println("\nPassenger Types");

                    while(rs2.next()) {
                        Transaction passengerType = new Transaction();
                        passengerType.setPassengerTypeId(rs2.getInt("passenger_type_id"));
                        passengerType.setPassengerType(rs2.getString("passenger_type"));
                        passengerType.setPassengerTypePrice(rs2.getDouble("passenger_type_price"));
                        System.out.println(passengerType.getPassengerTypeId() + ". " + passengerType.getPassengerType() + " - " + passengerType.getPassengerTypePrice());
                        transactions.add(passengerType);
                    }

                    System.out.print("Select new passenger type: ");
                    t.setPassengerTypeId(scanner.nextInt());

                    for (Transaction passengerType : transactions) {
                        if (passengerType.getPassengerTypeId() == t.getPassengerTypeId()) {
                            t.setPassengerType(passengerType.getPassengerType());
                            t.setPassengerTypePrice(passengerType.getPassengerTypePrice());
                            break;
                        }
                    }

                    ps2.close();
                    rs2.close();

                    query2 = "SELECT * FROM seat_types ORDER BY seat_type_id ASC";
                    ps2 = conn.prepareStatement(query2);
                    rs2 = ps2.executeQuery();

                    System.out.println("\nSeat Types");

                    while(rs2.next()) {
                        Transaction seatType = new Transaction();
                        seatType.setSeatTypeId(rs2.getInt("seat_type_id"));
                        seatType.setSeatType(rs2.getString("seat_type"));
                        seatType.setSeatTypePrice(rs2.getDouble("seat_type_price"));
                        System.out.println(seatType.getSeatTypeId() + ". " + seatType.getSeatType() + " - " + seatType.getSeatTypePrice());
                        transactions.add(seatType);
                    }

                    System.out.print("Select new seat type: ");
                    t.setSeatTypeId(scanner.nextInt());

                    for (Transaction seatType : transactions) {
                        if (seatType.getSeatTypeId() == t.getSeatTypeId()) {
                            t.setSeatType(seatType.getSeatType());
                            t.setSeatTypePrice(seatType.getSeatTypePrice());
                            break;
                        }
                    }
                    
                    ps2.close();
                    rs2.close();
                    
                    System.out.print("\nHow many tickets? ");
                    t.setQuantity(scanner.nextInt());

                    t.setTotal((t.getPassengerTypePrice() + t.getSeatTypePrice()) * t.getQuantity());

                    do {
                        System.out.println("\nTotal: " + t.getTotal());
                        System.out.print("Amount: ");
                        t.setAmount(scanner.nextDouble());

                        if(t.getAmount() < t.getTotal()) {
                            System.out.println("Insuficient amount! Please try again.");
                        } else {
                            t.setChange(t.getAmount() - t.getTotal());
                        }
                    } while(t.getAmount() < t.getTotal());
                    
                    t.setCreated_at(Timestamp.valueOf(LocalDateTime.now()));
                    
                    ps2.close();
                    break;
                }
            }
            
            if(recordFound == false) {
                System.out.println("No transaction record with an id of " + t.getTransactionId() + " found!");
                isUpdated = false;
            }
            
            ps.close();
            rs.close();
            conn.close();
            
            TransactionActionImplementation tai = new TransactionActionImplementation();
            tai.updateTransaction(t);
        } catch(Exception e) {
            System.out.println(e);
            isUpdated = false;
        }
        
        return isUpdated;
    }
    
    public static void invalidInput() {
        System.out.println("Invalid input!\n");
    }
    
    public static void bye() {
        System.out.println("\nBye! Have a good day.");
    }
    
    public static void askUser() {
        Scanner scanner = new Scanner(System.in);
        
        System.out.println("\nDo you want another transaction (Y/N)?");
        char choice = scanner.next().charAt(0);

        if(choice == 'Y' || choice == 'y') {
            System.out.println();
            menu();
        } else if (choice == 'N' || choice == 'n') {
            bye();
        } else {
            invalidInput();
            askUser();
        }
    }
    
    public static List<Transaction> destinations() {
        List<Transaction> destinations = new ArrayList<>();
        try {
            Connection conn = SQL_Connector.getConnection();

            String query = "SELECT * FROM destinations ORDER BY destination_id ASC";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();

            while(rs.next()) {
                Transaction destination = new Transaction();
                destination.setDestinationId(rs.getInt("destination_id"));
                destination.setDestination(rs.getString("destination"));
                destinations.add(destination);
            }
        } catch (Exception e) {
            
        }
        
        return destinations;
    }
    
    public static List<Transaction> passengerTypes() {
        List<Transaction> passengerTypes = new ArrayList<>();
        try {
            Connection conn = SQL_Connector.getConnection();

            String query = "SELECT * FROM passenger_types  ORDER BY passenger_type_id ASC";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Transaction passengerType = new Transaction();
                passengerType.setPassengerTypeId(rs.getInt("passenger_type_id"));
                passengerType.setPassengerType(rs.getString("passenger_type"));
                passengerType.setPassengerTypePrice(rs.getDouble("passenger_type_price"));
                passengerTypes.add(passengerType);
            }
        } catch (Exception e) {
            
        }
        
        return passengerTypes;
    }
    
    public static List<Transaction> seatTypes() {
        List<Transaction> seatTypes = new ArrayList<>();
        try {
            Connection conn = SQL_Connector.getConnection();

            String query = "SELECT * FROM seat_types  ORDER BY seat_type_id ASC";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                Transaction seatType = new Transaction();
                seatType.setSeatTypeId(rs.getInt("seat_type_id"));
                seatType.setSeatType(rs.getString("seat_type"));
                seatType.setSeatTypePrice(rs.getDouble("seat_type_price"));
                seatTypes.add(seatType);
            }
        } catch (Exception e) {
            
        }
        
        return seatTypes;
    }
    
    public static Transaction view(int id) {
        Transaction t = new Transaction();
        boolean recordFound = false;
        
        try {
            Connection conn = SQL_Connector.getConnection();

            String query = "SELECT * FROM transactions  WHERE transaction_id = ?";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, id);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                recordFound = true;
                t.setTransactionId(rs.getInt("transaction_id"));
                t.setDestinationId(rs.getInt("destination_id"));
                t.setPassengerTypeId(rs.getInt("passenger_type_id"));
                t.setSeatTypeId(rs.getInt("seat_type_id"));
                t.setQuantity(rs.getInt("quantity"));
                t.setTotal(rs.getDouble("total"));
                t.setAmount(rs.getDouble("amount"));
                t.setChange(rs.getDouble("_change"));
                t.setCreated_at(rs.getTimestamp("created_at"));
                
                String query2 = "SELECT * FROM destinations";
                PreparedStatement ps2 = conn.prepareStatement(query2);
                ResultSet rs2 = ps2.executeQuery();

                while(rs2.next()) {
                    if(t.getDestinationId() == rs2.getInt("destination_id")) {
                        t.setDestination(rs2.getString("destination"));
                        break;
                    }
                }

                ps2.close();
                rs2.close();

                query2 = "SELECT * FROM passenger_types";
                ps2 = conn.prepareStatement(query2);
                rs2 = ps2.executeQuery();

                while(rs2.next()) {
                    if(t.getPassengerTypeId() == rs2.getInt("passenger_type_id")) {
                        t.setPassengerType(rs2.getString("passenger_type"));
                        t.setPassengerTypePrice(rs2.getDouble("passenger_type_price"));
                        break;
                    }
                }

                ps2.close();
                rs2.close();

                query2 = "SELECT * FROM seat_types";
                ps2 = conn.prepareStatement(query2);
                rs2 = ps2.executeQuery();

                while(rs2.next()) {
                    if(t.getSeatTypeId() == rs2.getInt("seat_type_id")) {
                        t.setSeatType(rs2.getString("seat_type"));
                        t.setSeatTypePrice(rs2.getDouble("seat_type_price"));
                        break;
                    }
                }

                ps2.close();
                rs2.close();
            }
            
            if(recordFound == false) {
                System.out.println("No transaction record with an id of " + t.getTransactionId() + " found!");
            }
            
            ps.close();
            rs.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return t;
    }
}
