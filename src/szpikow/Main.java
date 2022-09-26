package szpikow;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

public class Main {
    public static final String WATCHED_FILE_NAME = "latest.txt";
    public static final String METEO_AUTH = "METEO_AUTH";

    public static void main(String[] args) {
        if (args.length < 2) {
            System.err.println("Missing arguments. Required arguments: 1) path of directory with latest.txt file, 2) URL of the web application");
            return;
        }
        String rawDir = args[0];
        File file = new File(rawDir, WATCHED_FILE_NAME);
        if (!file.exists()) {
            System.err.println("File \"latest.txt\" does not exist in location \"" + rawDir + "\"");
            return;
        }
        String url = args[1];
        try {
            new URL(url).toURI();
        } catch (Exception e) {
            System.err.println("Web application URL is invalid: \"" + url + "\"");
            return;
        }
        String password = System.getenv(METEO_AUTH);
        if (password == null) {
            System.err.println("Password for web application needs to be set in system environmental variables as METEO_AUTH");
            return;
        }

        Path path = Path.of(rawDir);
        MeteoDataWatcher watcher = new MeteoDataWatcher(path);
        MeteoDataSender sender = new MeteoDataSender(url);
        watcher.watch(sender::send);
    }
}
