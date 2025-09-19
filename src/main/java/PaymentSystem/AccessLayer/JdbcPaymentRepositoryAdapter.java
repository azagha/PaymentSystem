package PaymentSystem.AccessLayer;

import PaymentSystem.*;
import PaymentSystem.Entities.Customer;
import PaymentSystem.Entities.Merchant;
import PaymentSystem.Entities.Payment;
import PaymentSystem.Entities.Refund;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class JdbcPaymentRepositoryAdapter implements PaymentRepositoryPort {

    private final Connection connection;

    public JdbcPaymentRepositoryAdapter(Connection connection) {
        this.connection = connection;
    }

    // Save or update a Payment
    @Override
    public void save(Payment payment) {
        createOrGetCustomer(payment.getCustomer().getId());
        createOrGetMerchant(payment.getMerchant().getId());

        String updateSql = "UPDATE payments SET amount=?, currency=?, status=?, paymentType=?, customer_id=?, merchant_id=?, created_at=? WHERE id=?";
        try (PreparedStatement updateStmt = connection.prepareStatement(updateSql)) {
            updateStmt.setBigDecimal(1, payment.getAmount());
            updateStmt.setString(2, payment.getCurrency());
            updateStmt.setString(3, payment.getStatus().name());
            updateStmt.setString(4, payment.getPaymentType().name());
            updateStmt.setLong(5, payment.getCustomer().getId());
            updateStmt.setLong(6, payment.getMerchant().getId());
            updateStmt.setTimestamp(7, Timestamp.valueOf(payment.getCreatedAt()));
            updateStmt.setString(8, payment.getId());

            int rowsUpdated = updateStmt.executeUpdate();
            if (rowsUpdated > 0) return;

            // Insert if not updated
            String insertSql = "INSERT INTO payments (id, amount, currency, status, paymentType, customer_id, merchant_id, created_at) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
            try (PreparedStatement insertStmt = connection.prepareStatement(insertSql)) {
                insertStmt.setString(1, payment.getId());
                insertStmt.setBigDecimal(2, payment.getAmount());
                insertStmt.setString(3, payment.getCurrency());
                insertStmt.setString(4, payment.getStatus().name());
                insertStmt.setString(5, payment.getPaymentType().name());
                insertStmt.setLong(6, payment.getCustomer().getId());
                insertStmt.setLong(7, payment.getMerchant().getId());
                insertStmt.setTimestamp(8, Timestamp.valueOf(payment.getCreatedAt()));
                insertStmt.executeUpdate();
            }

        } catch (SQLException e) {
            throw new RuntimeException("Error saving payment", e);
        }
    }

    @Override
    public Payment findById(String id) {
        String sql = "SELECT * FROM payments WHERE id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) return mapPayment(rs);
            return null;
        } catch (SQLException e) {
            throw new RuntimeException("Error finding payment", e);
        }
    }

    @Override
    public List<Payment> findAll(int limit, int offset) {
        String sql = "SELECT * FROM payments ORDER BY created_at DESC LIMIT ? OFFSET ?";
        List<Payment> payments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, limit);
            stmt.setInt(2, offset);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) payments.add(mapPayment(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error listing payments", e);
        }
        return payments;
    }

    @Override
    public List<Payment> findPaymentsByCustomerId(long customerId) {
        String sql = "SELECT * FROM payments WHERE customer_id = ? ORDER BY created_at DESC";
        List<Payment> payments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, customerId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) payments.add(mapPayment(rs));
        } catch (SQLException e) {
            throw new RuntimeException("Error listing payments by customer", e);
        }
        return payments;
    }

    @Override
    public Customer createOrGetCustomer(long customerId) {
        try {
            String select = "SELECT id FROM customers WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(select)) {
                stmt.setLong(1, customerId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return new Customer(customerId);
            }

            String insert = "INSERT INTO customers (id) VALUES (?)";
            try (PreparedStatement stmt = connection.prepareStatement(insert)) {
                stmt.setLong(1, customerId);
                stmt.executeUpdate();
            }

            return new Customer(customerId);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or getting customer", e);
        }
    }

    @Override
    public Merchant createOrGetMerchant(long merchantId) {
        try {
            String select = "SELECT id FROM merchants WHERE id = ?";
            try (PreparedStatement stmt = connection.prepareStatement(select)) {
                stmt.setLong(1, merchantId);
                ResultSet rs = stmt.executeQuery();
                if (rs.next()) return new Merchant(merchantId);
            }

            String insert = "INSERT INTO merchants (id) VALUES (?)";
            try (PreparedStatement stmt = connection.prepareStatement(insert)) {
                stmt.setLong(1, merchantId);
                stmt.executeUpdate();
            }

            return new Merchant(merchantId);
        } catch (SQLException e) {
            throw new RuntimeException("Error creating or getting merchant", e);
        }
    }

    @Override
    public void saveRefund(Refund refund) {
        String sql = "INSERT INTO refunds (id, payment_id, amount, created_at) VALUES (?, ?, ?, ?)";
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, refund.getId());
            stmt.setString(2, refund.getPayment().getId());
            stmt.setBigDecimal(3, refund.getAmount());
            stmt.setTimestamp(4, Timestamp.valueOf(refund.getCreatedAt()));
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException("Error saving refund", e);
        }
    }

    @Override
    public List<Refund> findRefundsByPaymentId(String paymentId) {
        String sql = "SELECT * FROM refunds WHERE payment_id = ? ORDER BY created_at DESC";
        List<Refund> refunds = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setString(1, paymentId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Refund refund = new Refund(
                        new Payment(rs.getString("payment_id")), // minimal Payment object with ID
                        rs.getBigDecimal("amount")
                );
                refund.setId(rs.getString("id"));
                refund.setCreatedAt(rs.getTimestamp("created_at").toLocalDateTime());
                refunds.add(refund);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing refunds", e);
        }
        return refunds;
    }

    // Helper to map ResultSet to Payment object
    private Payment mapPayment(ResultSet rs) throws SQLException {
        Customer customer = new Customer(rs.getLong("customer_id"));
        Merchant merchant = new Merchant(rs.getLong("merchant_id"));
        Payment payment = new Payment(
                rs.getString("id"),
                rs.getBigDecimal("amount"),
                rs.getString("currency"),
                PaymentStatus.valueOf(rs.getString("status").toUpperCase()),
                PaymentType.valueOf(rs.getString("paymentType").toUpperCase()),
                customer,
                merchant,
                rs.getTimestamp("created_at").toLocalDateTime()
        );
        return payment;
    }
}
