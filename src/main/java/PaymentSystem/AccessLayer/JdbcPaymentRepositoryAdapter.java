package PaymentSystem.AccessLayer;

import PaymentSystem.Payment;
import PaymentSystem.PaymentStatus;
import PaymentSystem.PaymentType;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcPaymentRepositoryAdapter implements PaymentRepositoryPort{
    private final Connection connection;
    public JdbcPaymentRepositoryAdapter(Connection connection) {
        this.connection = connection;
    }

    // Save payment
    @Override
    public void save(Payment payment){
        try{
            PreparedStatement stmt = connection.prepareStatement("INSERT INTO payments (id, amount,currency,status,paymentType) VALUES (?, ?, ?, ?,?)");
            stmt.setString(1, payment.getId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setString(3, payment.getCurrency());
            stmt.setString(4, payment.getStatus().name());
            stmt.setString(5, payment.getPaymentType().name());
            stmt.executeUpdate();
        }catch (SQLException e){
            throw new RuntimeException("Error saving payment", e);
        }
    }

    //Find Payment by id
    @Override
    public Payment findById(String id) {
        try{
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM payments WHERE id = ?");
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            Payment payment = null;
            if(rs.next()){
                payment = new Payment(
                        rs.getString("id"),
                        rs.getBigDecimal("amount"),
                        rs.getString("currency"),
                        PaymentStatus.valueOf(rs.getString("status")),
                        PaymentType.valueOf(rs.getString("paymentType"))
                        );
            }
            rs.close();
            stmt.close();
            return payment;
        }catch(SQLException e){
            throw new RuntimeException("Error finding payment", e);
        }
    }

    //Get All Payment from the Database
    @Override
    public List<Payment> findAll() {
        try{
            PreparedStatement stmt = connection.prepareStatement("SELECT * FROM payments");
            ResultSet rs = stmt.executeQuery();

            List<Payment> payments = new ArrayList<>();
            while(rs.next()){
                payments.add(new Payment(
                        rs.getString("id"),
                        rs.getBigDecimal("amount"),
                        rs.getString("currency"),
                        PaymentStatus.valueOf(rs.getString("status")),
                        PaymentType.valueOf(rs.getString("paymentType"))
                ));
            }
            rs.close();
            stmt.close();
            return payments;
        }catch (SQLException e){
            throw new RuntimeException("Error finding all payments", e);
        }
    }
}
