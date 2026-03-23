package BTTH;

import java.math.BigDecimal;
import java.sql.CallableStatement;
import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Types;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

public class PrescriptionRevenueService {

    public void initializeDatabaseObjects(Connection connection) throws SQLException {
        createTables(connection);
        createProcedures(connection);
    }

    public int updateMedicineStock(Connection connection, int id, int addedQuantity) throws SQLException {
        String sql = "UPDATE medicines SET stock = stock + ? WHERE id = ?";

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setInt(1, addedQuantity);
            statement.setInt(2, id);
            return statement.executeUpdate();
        }
    }

    public List<Medicine> findMedicinesByPriceRange(Connection connection, double minPrice, double maxPrice)
            throws SQLException {
        String sql = "SELECT id, name, price, stock FROM medicines WHERE price BETWEEN ? AND ? ORDER BY price, id";
        List<Medicine> list = new ArrayList<>();

        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setDouble(1, minPrice);
            statement.setDouble(2, maxPrice);

            try (ResultSet resultSet = statement.executeQuery()) {
                while (resultSet.next()) {
                    list.add(new Medicine(
                            resultSet.getInt("id"),
                            resultSet.getString("name"),
                            resultSet.getBigDecimal("price"),
                            resultSet.getInt("stock")));
                }
            }
        }

        return list;
    }

    public BigDecimal calculatePrescriptionTotal(Connection connection, int prescriptionId) throws SQLException {
        String callSql = "{call CalculatePrescriptionTotal(?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(callSql)) {
            callableStatement.setInt(1, prescriptionId);
            callableStatement.registerOutParameter(2, Types.DECIMAL);
            callableStatement.execute();
            BigDecimal total = callableStatement.getBigDecimal(2);
            return total == null ? BigDecimal.ZERO : total;
        }
    }

    public BigDecimal getDailyRevenue(Connection connection, LocalDate date) throws SQLException {
        String callSql = "{call GetDailyRevenue(?, ?)}";

        try (CallableStatement callableStatement = connection.prepareCall(callSql)) {
            callableStatement.setDate(1, Date.valueOf(date));
            callableStatement.registerOutParameter(2, Types.DECIMAL);
            callableStatement.execute();
            BigDecimal revenue = callableStatement.getBigDecimal(2);
            return revenue == null ? BigDecimal.ZERO : revenue;
        }
    }

    private void createTables(Connection connection) throws SQLException {
        String createMedicines = """
                CREATE TABLE IF NOT EXISTS medicines (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    name VARCHAR(255) NOT NULL,
                    price DECIMAL(18,2) NOT NULL,
                    stock INT NOT NULL DEFAULT 0
                )
                """;

        String createPrescriptions = """
                CREATE TABLE IF NOT EXISTS prescriptions (
                    id INT PRIMARY KEY AUTO_INCREMENT,
                    medicine_id INT NOT NULL,
                    quantity_sold INT NOT NULL,
                    sale_date DATE NOT NULL,
                    CONSTRAINT fk_prescriptions_medicine
                        FOREIGN KEY (medicine_id) REFERENCES medicines(id)
                )
                """;

        try (Statement statement = connection.createStatement()) {
            statement.executeUpdate(createMedicines);
            statement.executeUpdate(createPrescriptions);
        }
    }

    private void createProcedures(Connection connection) throws SQLException {
        String dropTotalProcedure = "DROP PROCEDURE IF EXISTS CalculatePrescriptionTotal";
        String createTotalProcedure = """
                CREATE PROCEDURE CalculatePrescriptionTotal(IN p_id INT, OUT p_total DECIMAL(18,2))
                SELECT COALESCE(p.quantity_sold * m.price, 0)
                INTO p_total
                FROM prescriptions p
                JOIN medicines m ON p.medicine_id = m.id
                WHERE p.id = p_id
                LIMIT 1
                """;

        String dropDailyRevenueProcedure = "DROP PROCEDURE IF EXISTS GetDailyRevenue";
        String createDailyRevenueProcedure = """
                CREATE PROCEDURE GetDailyRevenue(IN p_date DATE, OUT p_revenue DECIMAL(18,2))
                SELECT COALESCE(SUM(p.quantity_sold * m.price), 0)
                INTO p_revenue
                FROM prescriptions p
                JOIN medicines m ON p.medicine_id = m.id
                WHERE p.sale_date = p_date
                """;

        try (Statement statement = connection.createStatement()) {
            statement.execute(dropTotalProcedure);
            statement.execute(createTotalProcedure);
            statement.execute(dropDailyRevenueProcedure);
            statement.execute(createDailyRevenueProcedure);
        }
    }
}
