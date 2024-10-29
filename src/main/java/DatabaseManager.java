import org.h2.tools.Server;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.PreparedStatement;

public class DatabaseManager {

    private static Server h2Server;
    private static Server webServer;

    // Inicio de Servidor de DB H2
    public static void startH2Server() {
        try {
            h2Server = Server.createTcpServer("-tcpAllowOthers", "-tcpPort", "9093", "-ifNotExists").start();
            webServer = Server.createWebServer("-webAllowOthers", "-webPort", "8082").start();

            if (h2Server.isRunning(true) && webServer.isRunning(true)) {
                System.out.println("Iniciando API");
                //System.out.println("Consola web disponible en: http://localhost:8082/h2-console");
            } else {
                System.out.println("Error al iniciar el servidor H2 o la consola web.");
            }

        } catch (SQLException e) {
            System.out.println("Error al iniciar el servidor H2.");
            e.printStackTrace();
        }
    }

    //Finalizar Servidor de DB
    public void stopH2Server() {
        if (h2Server != null) {
            h2Server.stop();
            System.out.println("Servidor H2 detenido.");
        }
        if (webServer != null) {
            webServer.stop();
            System.out.println("Consola H2 detenida.");
        }
    }


            // Creacion de la tabla en la base de datos
    public static void createDatabaseAndTable() {
        try (Connection connection = getConnection();
             Statement statement = connection.createStatement()) {

            String createTableSQL = "CREATE TABLE IF NOT EXISTS geolocation (" +
                    "id INT AUTO_INCREMENT PRIMARY KEY, " +
                    "country_name VARCHAR(255), " +
                    "distance BIGINT, " +
                    "iteration_count INT)";

            statement.execute(createTableSQL);

        } catch (SQLException e) {
            System.out.println("Error al crear la base de datos o la tabla.");
            e.printStackTrace();
        }
    }

    //  metodo para la insercio de datos
    public static void insertData(String countryName, long roundedDistance, int iterationCount) throws SQLException {
        String insertSQL = "INSERT INTO geolocation (country_name, distance, iteration_count) VALUES (?, ?, ?)";

        try (Connection connection = getConnection();
             PreparedStatement preparedStatement = connection.prepareStatement(insertSQL)) {

            preparedStatement.setString(1, countryName);
            preparedStatement.setLong(2, roundedDistance);
            preparedStatement.setInt(3, iterationCount);

            preparedStatement.executeUpdate();
            System.out.println("Datos insertados correctamente: " + countryName + ", " + roundedDistance + ", " + iterationCount);
        } catch (SQLException e) {
            System.out.println("Error al insertar los datos.");
            throw new SQLException("Error al insertar los datos en la base de datos", e);
        }
    }

    // Metodo de connexion a base de datos a traves de puerto TCP
    public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection("jdbc:h2:tcp://localhost:9093/~/geolocationdb", "sa", "");
    }

    public static void main(String[] args) {

        startH2Server();


        if (h2Server != null && h2Server.isRunning(false)) {
            // Crear la base de datos y la tabla
            createDatabaseAndTable();

            // Insertar un ejemplo de datos
            try {
                insertData("Colombia", 5000L, 1);
            } catch (SQLException e) {
                e.printStackTrace();
            }
        } else {
            System.out.println("Servidor H2 no está corriendo. No se puede proceder con la inserción de datos.");
        }

    }
}
