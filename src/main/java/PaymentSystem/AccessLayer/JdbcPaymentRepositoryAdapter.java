package PaymentSystem.AccessLayer;

import PaymentSystem.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcPaymentRepositoryAdapter implements PaymentRepositoryPort {
    private final Connection connection;

    public JdbcPaymentRepositoryAdapter(Connection connection) {
        this.connection = connection;
    }

    // Save payment
    @Override
    public void save(Payment payment) {
        String sql = "INSERT INTO payments (id, amount, currency, status, type, customer_id, merchant_id, created_at) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, payment.getId());
            stmt.setBigDecimal(2, payment.getAmount());
            stmt.setString(3, payment.getCurrency());
            stmt.setString(4, payment.getStatus().name());
            stmt.setString(5, payment.getPaymentType().name());
            stmt.setLong(6, payment.getCustomer().getId());
            stmt.setLong(7, payment.getMerchant().getId());
            stmt.setTimestamp(8, Timestamp.valueOf(payment.getCreatedAt()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving payment", e);
        }
    }

    // Find Payment by id
    @Override
    public Payment findById(String id) {
        String sql = "SELECT * FROM payments WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();

            if (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getLong("customer_id"));

                Merchant merchant = new Merchant();
                merchant.setId(rs.getLong("merchant_id"));

                Payment payment = new Payment(
                        rs.getString("id"),
                        rs.getBigDecimal("amount"),
                        rs.getString("currency"),
                        PaymentStatus.valueOf(rs.getString("status").toUpperCase()),
                        PaymentType.valueOf(rs.getString("type").toUpperCase()),
                        customer,
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                payment.setMerchant(merchant);
                return payment;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payment", e);
        }
    }

    // List all payments with pagination
    @Override
    public List<Payment> findAll(int limit, int offset) {
        String sql = "SELECT * FROM payments ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Payment> payments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getLong("customer_id"));

                Merchant merchant = new Merchant();
                merchant.setId(rs.getLong("merchant_id"));

                Payment payment = new Payment(
                        rs.getString("id"),
                        rs.getBigDecimal("amount"),
                        rs.getString("currency"),
                        PaymentStatus.valueOf(rs.getString("status").toUpperCase()),
                        PaymentType.valueOf(rs.getString("type").toUpperCase()),
                        customer,
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                payment.setMerchant(merchant);
                payments.add(payment);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing payments", e);
        }
        return payments;
    }

    // Find payments by customer id
    @Override
    public List<Payment> findCustomerById(long customerId) {
        String sql = "SELECT * FROM payments WHERE customer_id = ?";
        List<Payment> payments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(customerId);

                Merchant merchant = new Merchant();
                merchant.setId(rs.getLong("merchant_id"));

                Payment payment = new Payment(
                        rs.getString("id"),
                        rs.getBigDecimal("amount"),
                        rs.getString("currency"),
                        PaymentStatus.valueOf(rs.getString("status").toUpperCase()),
                        PaymentType.valueOf(rs.getString("type").toUpperCase()),
                        customer,
                        rs.getTimestamp("created_at").toLocalDateTime()
                );
                payment.setMerchant(merchant);
                payments.add(payment);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing payments by customer", e);
        }
        return payments;
    }
}
