import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.Random;

public class InsertBooks {

    // Oracle bağlantı bilgilerini kendi sistemine göre değiştir
    private static final String DB_URL = "jdbc:oracle:thin:@ExternalIP:xe";
    private static final String DB_USER = "SYS as SYSDBA";
    private static final String DB_PASSWORD = "ORACLE";

    private static final String INSERT_SQL = "INSERT INTO BOOK (ID, NAME, ISBN) VALUES (?, ?, ?)";

    public static void main(String[] args) {
        try {
            // Oracle JDBC driver'ını yükle
            Class.forName("oracle.jdbc.driver.OracleDriver");
        } catch (ClassNotFoundException e) {
            System.out.println("Oracle JDBC Driver bulunamadı!");
            e.printStackTrace();
            return;
        }

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD);
             PreparedStatement pstmt = conn.prepareStatement(INSERT_SQL)) {

            conn.setAutoCommit(false);  // Toplu işlem için commit kontrolü

            Random random = new Random();

            for (int i = 1; i <= 100; i++) {
                pstmt.setInt(1, i);  // ID (manuel artan)
                pstmt.setString(2, randomString(random, 10)); // NAME
                pstmt.setString(3, randomISBN(random));       // ISBN
                pstmt.addBatch();
            }

            pstmt.executeBatch();
            conn.commit();

            System.out.println("100 kayıt başarıyla eklendi.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    // Rastgele isim üretir (harflerden)
    private static String randomString(Random random, int length) {
        String chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz";
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < length; i++) {
            sb.append(chars.charAt(random.nextInt(chars.length())));
        }
        return sb.toString();
    }

    // Rastgele 13 haneli ISBN üretir (sadece rakam)
    private static String randomISBN(Random random) {
        StringBuilder sb = new StringBuilder();
        for(int i = 0; i < 13; i++) {
            sb.append(random.nextInt(10));
        }
        return sb.toString();
    }
}
