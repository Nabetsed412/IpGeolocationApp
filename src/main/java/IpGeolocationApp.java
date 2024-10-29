import com.fasterxml.jackson.databind.ObjectMapper;
import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.ZonedDateTime;
import java.util.Map;
import java.util.Scanner;
import java.sql.SQLException;


public class IpGeolocationApp {

    private static final double BUENOS_AIRES_LAT = -34.6037;
    private static final double BUENOS_AIRES_LON = -58.3816;
    private static int iterationCount = 0;




    // Metodo de validacion de la IP digitada es valida o no
    public static boolean isValidIPAddress(String ipAddress) {
        try {
            InetAddress inet = InetAddress.getByName(ipAddress);
            return inet.getHostAddress().equals(ipAddress);
        } catch (UnknownHostException e) {
            return false;
        }
    }
    // Metodo para obtener el pais con la IP
    public static void getCountryByIP(String ipAddress) {
        String accessKey = "87058f0d90e3d6256c27f06dee16fc3e"; // Clave de acceso para ipapi.com
        String url = "http://api.ipapi.com/" + ipAddress + "?access_key=" + accessKey;
        LocalDateTime currentDateTime = LocalDateTime.now();

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                System.out.println("Error al obtener datos de geolocalización. Código HTTP: " + statusCode);
                return;
            }


            String jsonResponse = EntityUtils.toString(response.getEntity());

            ObjectMapper objectMapper = new ObjectMapper();
            ApiIpResponse apiIpResponse = objectMapper.readValue(jsonResponse, ApiIpResponse.class);

            System.out.println("Fecha actual: " + currentDateTime);
            System.out.println("País: " + apiIpResponse.getCountry_name());
            System.out.println("Código ISO: " + apiIpResponse.getCountry_code());

            getCountryDetailsFromRestCountries(apiIpResponse.getCountry_code());
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Metodo para obtener la informacion adicional del pais
    public static void getCountryDetailsFromRestCountries(String countryCode) {
        String url = "https://restcountries.com/v3.1/alpha/" + countryCode;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                System.out.println("Error al obtener detalles del país. Código HTTP: " + statusCode);
                return;
            }

            String jsonResponse = EntityUtils.toString(response.getEntity());

            ObjectMapper objectMapper = new ObjectMapper();
            CountryResponse[] countryResponse = objectMapper.readValue(jsonResponse, CountryResponse[].class);

            for (CountryResponse country : countryResponse) {

                if (country.getName() != null) {
                    String countryName = country.getName().getCommon();

                    if (country.getName().getNativeName() != null) {
                        country.getName().getNativeName().forEach((key, nativeName) -> {
                            System.out.println("Nombre del país (" + key + "): " + nativeName.getCommon() );
                        });
                    }
                }

                System.out.println("Idiomas: ");
                if (country.getLanguages() != null) {
                    for (String language : country.getLanguages().values()) {
                        System.out.println("- " + language);
                    }
                }

                System.out.println("Moneda: ");
                if (country.getCurrencies() != null) {
                    for (Map.Entry<String, Currency> entry : country.getCurrencies().entrySet()) {
                        String currencyCode = entry.getKey();
                        Currency currency = entry.getValue();

                        System.out.println("- " + currency.getName() + " (" + currency.getSymbol() + ")");
                        convertCurrencyToUSD(currencyCode);
                    }
                }

                if (country.getTimezones() != null && !country.getTimezones().isEmpty()) {
                    System.out.println("Zonas horarias y horas actuales:");
                    for (String timezone : country.getTimezones()) {
                        ZonedDateTime currentTimeInZone = ZonedDateTime.now(ZoneId.of(timezone));
                        System.out.println("- Zona horaria: " + timezone + ", Hora actual: " + currentTimeInZone);
                    }
                }

                if (country.getLatlng() != null && country.getLatlng().size() == 2) {
                    String countryName = country.getName().getCommon();
                    double countryLat = country.getLatlng().get(0);  // Latitud del país
                    double countryLon = country.getLatlng().get(1);  // Longitud del país

                    double distance = calculateDistance(BUENOS_AIRES_LAT, BUENOS_AIRES_LON, countryLat, countryLon);
                    long roundedDistance = Math.round(distance);

                    System.out.println("Distancia estimada entre Buenos Aires y " + countryName +" es de " + roundedDistance + " km");

                    // Aumentar el contador de iteraciones para la insercion
                    iterationCount++;

                    try {
                        DatabaseManager.insertData(countryName, roundedDistance, iterationCount);
                    } catch (SQLException e) {
                        System.out.println("Error al insertar los datos en la base de datos.");
                        e.printStackTrace();
                    }
                }
            }

        } catch (Exception e) {
            e.printStackTrace();
        }
    }



    // Metodo para la conversion de la moneda
    public static void convertCurrencyToUSD(String currencyCode) {
        String apiKey = "4124b2b22470c5510d0218aa";
        String url = "https://v6.exchangerate-api.com/v6/" + apiKey + "/latest/" + currencyCode;

        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpGet request = new HttpGet(url);
            HttpResponse response = httpClient.execute(request);

            int statusCode = response.getStatusLine().getStatusCode();
            if (statusCode != 200) {
                System.out.println("Error al obtener la tasa de cambio. Código HTTP: " + statusCode);
                return;
            }

            String jsonResponse = EntityUtils.toString(response.getEntity());

            ObjectMapper objectMapper = new ObjectMapper();
            ExchangeRateResponse exchangeRateResponse = objectMapper.readValue(jsonResponse, ExchangeRateResponse.class);

            double rateToUSD = exchangeRateResponse.getConversionRates().get("USD");
            System.out.println("Tasa de cambio respecto al dólar: 1 " + currencyCode + " = " + rateToUSD + " USD");

        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    //Metodo que aplica la formula del Haversine para calcular la distancia entre dos puntos
    public static double calculateDistance(double lat1, double lon1, double lat2, double lon2) {
        final int R = 6371; // Radio de la Tierra en km
        double latDistance = Math.toRadians(lat2 - lat1);
        double lonDistance = Math.toRadians(lon2 - lon1);
        double a = Math.sin(latDistance / 2) * Math.sin(latDistance / 2)
                + Math.cos(Math.toRadians(lat1)) * Math.cos(Math.toRadians(lat2))
                * Math.sin(lonDistance / 2) * Math.sin(lonDistance / 2);
        double c = 2 * Math.atan2(Math.sqrt(a), Math.sqrt(1 - a));
        return R * c;
    }

    // Metodo principal
    public static void main(String[] args) {

        DatabaseManager.startH2Server();
        DatabaseManager.createDatabaseAndTable();

        Scanner scanner = new Scanner(System.in);
        String ipAddress;
        boolean continuar = true;
        int iterationCount = 0;

        while (continuar) {
            boolean validIp = false;

            // se ejecuta hasta que se ingrese una ip valida
            while (!validIp) {
                System.out.print("Por favor, introduce una dirección IP: ");
                ipAddress = scanner.nextLine();  // Leer la IP introducida

                if (isValidIPAddress(ipAddress)) {
                    validIp = true;
                    iterationCount++;
                    getCountryByIP(ipAddress);
                } else {
                    System.out.println("La dirección IP ingresada no es válida. Por favor, ingresa otra dirección IP.");
                }
            }

            System.out.print("¿Quieres buscar otra IP? (Y/N): ");
            String respuesta = scanner.nextLine().trim().toUpperCase();

            if (respuesta.equals("N")) {
                continuar = false;
                System.out.println("Saliendo de la aplicación...");
            } else if (!respuesta.equals("Y")) {
                System.out.println("Respuesta no válida, saliendo de la aplicación por defecto...");
                continuar = false;
            }
        }

        scanner.close();
        System.exit(0);
    }
}
