package szpikow;

import java.io.IOException;
import java.net.URI;
import java.net.http.HttpClient;
import java.net.http.HttpRequest;
import java.net.http.HttpResponse;

public class MeteoDataSender {
    private final String url;

    public MeteoDataSender(String url) {
        this.url = url;
    }


    public void send(String meteoData) {
        String password = System.getenv(Main.METEO_AUTH);
        if (password == null) {
            System.err.println("No environmental variable METEO_AUTH with password for web service");
            return;
        }
        HttpClient client = HttpClient.newHttpClient();
        HttpRequest request = HttpRequest.newBuilder()
                .uri(URI.create(url))
                .setHeader("meteoauth", password)
                .POST(HttpRequest.BodyPublishers.ofString(meteoData))
                .build();
        try {
            HttpResponse<String> response = client.send(request, HttpResponse.BodyHandlers.ofString());
            if (response.statusCode() != 200) {
                System.out.println(response);
            }
        } catch (IOException | InterruptedException e) {
            e.printStackTrace();
        }
    }
}
