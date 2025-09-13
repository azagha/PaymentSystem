import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.Statement;

public class H2Test {
    public static void main(String[] args) {
        String url = "jdbc:h2:mem:testdb"; // In-memory DB
        String user = "sa";
        String password = "";

        try (
                // 1. Connect to H2
                Connection conn = DriverManager.getConnection(url, user, password);
                // 2. Create a statement to run SQL
                Statement stmt = conn.createStatement()
        ) {
            // Create table
            stmt.execute("CREATE TABLE users (id INT PRIMARY KEY, name VARCHAR(255))");

            // Insert data
            stmt.execute("INSERT INTO users VALUES (1, 'Alice'), (2, 'Bob')");

            // Query
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                System.out.println(rs.getInt("id") + " - " + rs.getString("name"));
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
