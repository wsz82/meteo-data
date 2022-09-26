package szpikow;

import java.io.File;
import java.net.URL;
import java.nio.file.Path;

public class Main {
    public static String WATCHED_FILE_NAME = "latest.txt";

    public static void main(String[] args) {
        if (args.length < 2) {
            throw new IllegalArgumentException("Missing arguments. Required arguments: 1) path of directory with latest.txt file, 2) URL of the web application");
        }
        String rawDir = args[0];
        File file = new File(rawDir, WATCHED_FILE_NAME);
        if (!file.exists()) {
            throw new IllegalArgumentException("File \"latest.txt\" does not exist in location \"" + rawDir + "\"");
        }
        String url = args[1];
        try {
            new URL(url).toURI();
        } catch (Exception e) {
            throw new IllegalArgumentException("Web application URL is invalid: \"" + url + "\"");
        }

        Path path = Path.of(rawDir);
        MeteoDataWatcher watcher = new MeteoDataWatcher(path);
        MeteoDataSender sender = new MeteoDataSender(url);
        watcher.watch(sender::send);
    }
}
