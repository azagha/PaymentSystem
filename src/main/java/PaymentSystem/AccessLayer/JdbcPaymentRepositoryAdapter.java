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
            if (rowsUpdated > 0) {
                return;
            }

            String insertSql = "INSERT INTO payments (ID, AMOUNT, CURRENCY, STATUS, PAYMENTTYPE, CUSTOMER_ID, MERCHANT_ID, CREATED_AT) " +
                    "VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
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

            if (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getLong("CUSTOMER_ID"));

                Merchant merchant = new Merchant();
                merchant.setId(rs.getLong("MERCHANT_ID"));

                Payment payment = new Payment(
                        rs.getString("ID"),
                        rs.getBigDecimal("AMOUNT"),
                        rs.getString("CURRENCY"),
                        PaymentStatus.valueOf(rs.getString("STATUS").toUpperCase()),
                        PaymentType.valueOf(rs.getString("PAYMENTTYPE").toUpperCase()),
                        customer,
                        rs.getTimestamp("CREATED_AT").toLocalDateTime()
                );
                payment.setMerchant(merchant);
                return payment;
            }
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

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(rs.getLong("CUSTOMER_ID"));

                Merchant merchant = new Merchant();
                merchant.setId(rs.getLong("MERCHANT_ID"));

                Payment payment = new Payment(
                        rs.getString("ID"),
                        rs.getBigDecimal("AMOUNT"),
                        rs.getString("CURRENCY"),
                        PaymentStatus.valueOf(rs.getString("STATUS").toUpperCase()),
                        PaymentType.valueOf(rs.getString("PAYMENTTYPE").toUpperCase()),
                        customer,
                        rs.getTimestamp("CREATED_AT").toLocalDateTime()
                );
                payment.setMerchant(merchant);
                payments.add(payment);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing payments", e);
        }
        return payments;
    }

    @Override
    public List<Payment> findPaymentsByCustomerId(long customerId) {
        String sql = "SELECT * FROM payments WHERE customer_id = ?";
        List<Payment> payments = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setLong(1, customerId);
            ResultSet rs = stmt.executeQuery();

            while (rs.next()) {
                Customer customer = new Customer();
                customer.setId(customerId);

                Merchant merchant = new Merchant();
                merchant.setId(rs.getLong("MERCHANT_ID"));

                Payment payment = new Payment(
                        rs.getString("ID"),
                        rs.getBigDecimal("AMOUNT"),
                        rs.getString("CURRENCY"),
                        PaymentStatus.valueOf(rs.getString("STATUS").toUpperCase()),
                        PaymentType.valueOf(rs.getString("PAYMENTTYPE").toUpperCase()),
                        customer,
                        rs.getTimestamp("CREATED_AT").toLocalDateTime()
                );
                payment.setMerchant(merchant);
                payments.add(payment);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error listing payments by customer", e);
        }
        return payments;
    }

    @Override
    public Customer createOrGetCustomer(long customerId) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT id FROM customers WHERE id = ?")) {
            stmt.setLong(1, customerId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Customer c = new Customer();
                c.setId(customerId);
                return c;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Customer not found, insert
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO customers (id) VALUES (?)")) {
            stmt.setLong(1, customerId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Customer c = new Customer();
        c.setId(customerId);
        return c;
    }

    @Override
    public Merchant createOrGetMerchant(long merchantId) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT id FROM merchants WHERE id = ?")) {
            stmt.setLong(1, merchantId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Merchant m = new Merchant();
                m.setId(merchantId);
                return m;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        // Merchant not found, insert
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO merchants (id) VALUES (?)")) {
            stmt.setLong(1, merchantId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        Merchant m = new Merchant();
        m.setId(merchantId);
        return m;
    }
}
