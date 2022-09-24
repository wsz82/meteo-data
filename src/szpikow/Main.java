package szpikow;

import java.nio.file.Path;

public class Main {

    public static void main(String[] args) {
        String rawDir = args[0];
        String url = args[1];

        Path path = Path.of(rawDir);
        MeteoDataWatcher watcher = new MeteoDataWatcher(path);
        MeteoDataSender sender = new MeteoDataSender(url);
        watcher.watch(meteoData -> {
            sender.send(meteoData);
        });
    }
}
