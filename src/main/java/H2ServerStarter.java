import org.h2.tools.Server;
import java.sql.SQLException;
// Clase para realizar pruebas de conexion al servidor de base de datos H2, solamente para pruebas.
public class H2ServerStarter {

    public static void main(String[] args) {
        try {

            Server h2Server = Server.createTcpServer("-tcpAllowOthers", "-tcpPort", "9093", "-ifNotExists").start();
            Server webServer = Server.createWebServer("-webPort", "8082", "-webAllowOthers").start();

            if (h2Server.isRunning(true) && webServer.isRunning(true)) {
                System.out.println("Servidor H2 iniciado correctamente en el puerto 9093.");
                System.out.println("Consola web H2 disponible en: http://localhost:8082");
                System.out.println("URL JDBC: jdbc:h2:tcp://localhost:9093/~/geolocationdb");
            } else {
                System.out.println("Error al iniciar el servidor H2 o la consola web.");
            }


            System.out.println("Presiona Ctrl+C para detener el servidor.");
            while (true) {

            }

        } catch (SQLException e) {
            System.out.println("Error al iniciar el servidor H2 o la consola web.");
            e.printStackTrace();
        }
    }
}
