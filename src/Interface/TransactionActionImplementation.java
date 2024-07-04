package Interface;

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

public class TransactionActionImplementation implements TransactionAction {
    public boolean createTransaction(Transaction t) {
        try {
            Connection conn = SQL_Connector.getConnection();
            
            String query = "INSERT INTO transactions VALUES (null, ?, ?, ?, ?, ?, ?, ?, ?)";
            PreparedStatement ps = conn.prepareStatement(query);
            ps.setInt(1, t.getDestinationId());
            ps.setInt(2, t.getPassengerTypeId());
            ps.setInt(3, t.getSeatTypeId());
            ps.setInt(4, t.getQuantity());
            ps.setDouble(5, t.getTotal());
            ps.setDouble(6, t.getAmount());
            ps.setDouble(7, (t.getChange()));
            ps.setTimestamp(8, t.getCreated_at());

            if(ps.executeUpdate() == 1) {
                String query2 = "SELECT LAST_INSERT_ID()";
                PreparedStatement ps2 = conn.prepareStatement(query2);
                ResultSet rs2 = ps2.executeQuery();
                
                while(rs2.next()) {
                    t.setTransactionId(rs2.getInt(1));
                    break;
                }
                
                ps2.close();
                rs2.close();
                
                System.out.println("\nTransaction Successful!");
                t.displayTransaction(t);
                ps.close();
                conn.close();
                return true;
            } else {
                return false;
            }
        } catch (Exception e) {
            System.out.println(e);
            return false;
        }
    }
    
    public List<Transaction> viewTransaction(int selected) {
        Connection conn = null;
        PreparedStatement ps = null;
        ResultSet rs = null;
        PreparedStatement ps2 = null;
        ResultSet rs2 = null;
        List<Transaction> transactions = new ArrayList<>();
        String query = "";
        String query2 = "";
        boolean recordFound = false;

        try {
            conn = SQL_Connector.getConnection();
            
            switch (selected) {
                case 1: {
                    query = "SELECT * FROM transactions";
                    ps = conn.prepareStatement(query);
                    rs = ps.executeQuery();

                    System.out.println("\nTransactions");
                    
                    while(rs.next()) {
                        Transaction t = new Transaction();
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

                        query2 = "SELECT * FROM destinations";
                        ps2 = conn.prepareStatement(query2);
                        rs2 = ps2.executeQuery();

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

                        transactions.add(t);
                        t.displayTransaction(t);
                    }
                    
                    if(recordFound == false) {
                        System.out.println("No transaction/s");
                    }
                    
                    ps.close();
                    rs.close();
                    break;
                }
//                case 2: {
//                    System.out.println("\nEnter date (YYYY-MM-DD): ");
//                    String date = scanner.nextLine();
//
//                    query = "SELECT * FROM transactions WHERE DATE(created_at) = ? ORDER BY transaction_id ASC";
//                    ps = conn.prepareStatement(query);
//                    ps.setString(1, date);
//                    rs = ps.executeQuery();
//                    
//                    while(rs.next()) {
//                        recordFound = true;
//                        t.setTransactionId(rs.getInt("transaction_id"));
//                        t.setDestinationId(rs.getInt("destination_id"));
//                        t.setPassengerTypeId(rs.getInt("passenger_type_id"));
//                        t.setSeatTypeId(rs.getInt("seat_type_id"));
//                        t.setQuantity(rs.getInt("quantity"));
//                        t.setTotal(rs.getDouble("total"));
//                        t.setAmount(rs.getDouble("amount"));
//                        t.setChange(rs.getDouble("_change"));
//                        t.setCreated_at(rs.getTimestamp("created_at"));
//
//                        query2 = "SELECT * FROM destinations";
//                        ps2 = conn.prepareStatement(query2);
//                        rs2 = ps2.executeQuery();
//
//                        while(rs2.next()) {
//                            if(t.getDestinationId() == rs2.getInt("destination_id")) {
//                                d.setDestination(rs2.getString("destination"));
//                                break;
//                            }
//                        }
//
//                        ps2.close();
//                        rs2.close();
//
//                        query2 = "SELECT * FROM passenger_types";
//                        ps2 = conn.prepareStatement(query2);
//                        rs2 = ps2.executeQuery();
//
//                        while(rs2.next()) {
//                            if(t.getPassengerTypeId() == rs2.getInt("passenger_type_id")) {
//                                pt.setPassengerType(rs2.getString("passenger_type"));
//                                pt.setPassengerTypePrice(rs2.getDouble("passenger_type_price"));
//                                break;
//                            }
//                        }
//
//                        ps2.close();
//                        rs2.close();
//
//                        query2 = "SELECT * FROM seat_types";
//                        ps2 = conn.prepareStatement(query2);
//                        rs2 = ps2.executeQuery();
//
//                        while(rs2.next()) {
//                            if(t.getSeatTypeId() == rs2.getInt("seat_type_id")) {
//                                st.setSeatType(rs2.getString("seat_type"));
//                                st.setSeatTypePrice(rs2.getDouble("seat_type_price"));
//                                break;
//                            }
//                        }
//
//                        ps2.close();
//                        rs2.close();
//
//                        t.displayTransaction(t, d, pt, st);
//                    }
//                    
//                    if(recordFound == false) {
//                        System.out.println("No transaction record with a date of " + date + " found!");
//                    }
//                    
//                    ps.close();
//                    rs.close();
//                    break;
//                }
            }
            
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
        }
        
        return transactions;
    }
    
    public boolean updateTransaction(Transaction t) {
        PreparedStatement ps = null;
        boolean isUpdated = false;
        System.out.println("test");
        t.displayTransaction(t);
        try {
            Connection conn = SQL_Connector.getConnection();
            
            String query = "UPDATE transactions SET destination_id=?, passenger_type_id=?, seat_type_id=?, quantity=?, total =?, amount=?, _change=?, created_at=? WHERE transaction_id=?";
            ps = conn.prepareStatement(query);
            ps.setInt(1, t.getDestinationId());
            ps.setInt(2, t.getPassengerTypeId());
            ps.setInt(3, t.getSeatTypeId());
            ps.setInt(4, t.getQuantity());
            ps.setDouble(5, t.getTotal());
            ps.setDouble(6, t.getAmount());
            ps.setDouble(7, (t.getChange()));
            ps.setTimestamp(8, t.getCreated_at());
            ps.setInt(9, (t.getTransactionId()));

            if(ps.executeUpdate() == 1) {
                System.out.println("\nUpdated Successfully");
                t.displayTransaction(t);
                isUpdated =  true;
            } else {
                isUpdated =  false;
            }

            ps.close();
            conn.close();
        } catch (Exception e) {
            System.out.println(e);
            isUpdated =  false;
        }
        
        return isUpdated;
    }
    
    public boolean deleteTransaction(int id) {
        Transaction t = new Transaction();
        boolean recordFound = false;
        Scanner scanner = new Scanner(System.in);
        boolean isDeleted = false;
        
        System.out.println(id);
        System.out.print("Enter transaction id: ");
        t.setTransactionId(id);
        
        try {
            Connection conn = SQL_Connector.getConnection();
            
            String query = "SELECT transaction_id FROM transactions";
            PreparedStatement ps = conn.prepareStatement(query);
            ResultSet rs = ps.executeQuery();
            
            while(rs.next()) {
                if (t.getTransactionId() == rs.getInt("transaction_id")) {
                    recordFound = true;
                    query = "DELETE FROM transactions WHERE transaction_id = ?";
                    ps = conn.prepareStatement(query);
                    ps.setInt(1, t.getTransactionId());

                    if(ps.executeUpdate() == 1) {
                        System.out.println("\nDeleted Successfully");
                        isDeleted = true;
                    }
                }
            }
            
            if(recordFound == false) {
                System.out.println("\nNo transaction record with an id of " + t.getTransactionId() + " found!");
                isDeleted = false;
            }
            
            ps.close();
            rs.close();
            conn.close();
        } catch(Exception e) {
            System.out.println(e);
            isDeleted = false;
        }
        
        return recordFound && isDeleted;
    }
}
