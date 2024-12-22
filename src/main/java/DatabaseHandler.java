import java.sql.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class DatabaseHandler {
    private static final String DB_URL = "jdbc:sqlite:grants.db";

    // SQL запросы для создания таблиц
    private static final String CREATE_TABLE_GRANTS = """
        CREATE TABLE IF NOT EXISTS Grants (
            id INTEGER PRIMARY KEY AUTOINCREMENT,
            companyName TEXT NOT NULL,
            streetName TEXT NOT NULL,
            grantAmount REAL NOT NULL,
            fiscalYear INTEGER NOT NULL,
            businessType TEXT NOT NULL,
            workplacesQuantity INTEGER NOT NULL
        );
    """;

    // Метод для подключения к БД
    public Connection connect() {
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(DB_URL);
            System.out.println("Connection to SQLite has been established.");
        } catch (SQLException e) {
            System.out.println(e.getMessage());
        }
        return conn;
    }

    // Метод для создания таблицы
    public void createTables() {
        try (Connection conn = connect();
             PreparedStatement stmt = conn.prepareStatement(CREATE_TABLE_GRANTS)) {
            stmt.execute();
            System.out.println("Tables have been created.");
        } catch (SQLException e) {
            System.out.println("Error creating tables: " + e.getMessage());
        }
    }
    public void fetchAndPrintGrants() {
        String selectAllGrantsSQL = "SELECT * FROM Grants";

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(selectAllGrantsSQL)) {

            System.out.println("Данные из таблицы Grants:");
            while (rs.next()) {
                System.out.println("ID: " + rs.getInt("id") +
                        ", Компания: " + rs.getString("companyName") +
                        ", Улица: " + rs.getString("streetName") +
                        ", Грант: $" + rs.getDouble("grantAmount") +
                        ", Год: " + rs.getInt("fiscalYear") +
                        ", Тип бизнеса: " + rs.getString("businessType") +
                        ", Рабочих мест: " + rs.getInt("workplacesQuantity"));
            }

        } catch (SQLException e) {
            System.out.println("Error fetching data: " + e.getMessage());
        }
    }

    public void deleteLastRow() {
        String deleteLastRowSQL = "DELETE FROM Grants WHERE id = (SELECT MAX(id) FROM Grants)";

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(deleteLastRowSQL)) {

            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Последняя строка успешно удалена.");
            } else {
                System.out.println("Не удалось удалить строку. Возможно, таблица пуста.");
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при удалении строки: " + e.getMessage());
        }
    }

    public Map<Integer, Double> getAverageWorkplacesPerYear() {
        String query = "SELECT fiscalYear, AVG(workplacesQuantity) AS average_workplaces " +
                "FROM Grants " +
                "GROUP BY fiscalYear " +
                "ORDER BY fiscalYear";

        Map<Integer, Double> averages = new HashMap<>();

        try (Connection conn = connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(query)) {

            while (rs.next()) {
                int year = rs.getInt("fiscalYear");
                double averageWorkplaces = rs.getDouble("average_workplaces");
                averages.put(year, averageWorkplaces);
            }

        } catch (SQLException e) {
            System.out.println("Ошибка при расчете среднего количества рабочих мест: " + e.getMessage());
        }

        return averages;
    }

    // Метод для вставки данных в таблицу
    public void insertGrants(ArrayList<Grant> grants) {
        String insertGrantSQL = """
            INSERT INTO Grants (companyName, streetName, grantAmount, fiscalYear, businessType, workplacesQuantity)
            VALUES (?, ?, ?, ?, ?, ?);
        """;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(insertGrantSQL)) {

            for (Grant grant : grants) {
                pstmt.setString(1, grant.companyName);
                pstmt.setString(2, grant.streetName);
                pstmt.setDouble(3, grant.grantAmount);
                pstmt.setInt(4, grant.year);
                pstmt.setString(5, grant.businessType);
                pstmt.setInt(6, grant.workplacesQuantity);

                pstmt.addBatch(); // Добавляем запрос в пакет
            }
            pstmt.executeBatch(); // Выполняем все запросы из пакета
            System.out.println("Data has been inserted into the database.");
        } catch (SQLException e) {
            System.out.println("Error inserting data: " + e.getMessage());
        }
    }

    public double getAverageGrantForBusinessType(String businessType) {
        String query = "SELECT AVG(grantAmount) AS average_grant FROM Grants WHERE businessType = ?";
        double averageGrant = 0;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setString(1, businessType);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    averageGrant = rs.getDouble("average_grant");
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при вычислении среднего гранта: " + e.getMessage());
        }

        return averageGrant;
    }
    public String getBusinessTypeWithMaxWorkplacesUnderGrant(double maxGrantAmount) {
        String query = """
        SELECT businessType, SUM(workplacesQuantity) AS total_workplaces
        FROM Grants
        WHERE grantAmount <= ?
        GROUP BY businessType
        ORDER BY total_workplaces DESC
        LIMIT 1;
    """;
        String businessType = null;

        try (Connection conn = connect();
             PreparedStatement pstmt = conn.prepareStatement(query)) {
            pstmt.setDouble(1, maxGrantAmount);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    businessType = rs.getString("businessType");
                    int totalWorkplaces = rs.getInt("total_workplaces");
                    System.out.println("Тип бизнеса с наибольшим количеством рабочих мест (грант ≤ $" + maxGrantAmount + "): " +
                            businessType + " (" + totalWorkplaces + " рабочих мест)");
                }
            }
        } catch (SQLException e) {
            System.out.println("Ошибка при поиске типа бизнеса: " + e.getMessage());
        }

        return businessType;
    }
}
