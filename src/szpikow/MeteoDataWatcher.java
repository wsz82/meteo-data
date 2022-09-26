package szpikow;

import java.io.IOException;
import java.nio.charset.Charset;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.function.Consumer;

import static java.nio.file.StandardWatchEventKinds.*;

public class MeteoDataWatcher {
    private final Path watchedDir;

    public MeteoDataWatcher(Path watchedDir) {
        this.watchedDir = watchedDir;
    }

    public void watch(Consumer<String> dataConsumer) {
        try {
            WatchService watcher = FileSystems.getDefault().newWatchService();
            watchedDir.register(watcher,
                    ENTRY_CREATE,
                    ENTRY_DELETE,
                    ENTRY_MODIFY);
            while (true) {
                WatchKey key;
                try {
                    key = watcher.take();
                } catch (InterruptedException x) {
                    return;
                }

                for (WatchEvent<?> event : key.pollEvents()) {
                    WatchEvent.Kind<?> kind = event.kind();
                    if (kind == OVERFLOW) {
                        continue;
                    }
                    WatchEvent<Path> ev = (WatchEvent<Path>) event;
                    Path fileNamePath = ev.context();
                    String fileName = fileNamePath.toString();
                    boolean isNotLookedFile = !Main.WATCHED_FILE_NAME.equals(fileName);
                    if (isNotLookedFile) {
                        continue;
                    }
                    try {
                        Path child = watchedDir.resolve(fileNamePath);
                        if (!Files.probeContentType(child).equals("text/plain")) {
                            System.err.format("New file '%s' is not a plain text file.%n", fileNamePath);
                            continue;
                        }
                        String fileFullDir = Path.of(watchedDir.toString(), fileName).toString();
                        String meteoData = readFile(fileFullDir, StandardCharsets.UTF_16LE);
                        dataConsumer.accept(meteoData);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                }
                boolean valid = key.reset();
                if (!valid) {
                    break;
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private String readFile(String path, Charset encoding) throws IOException {
        byte[] encoded = Files.readAllBytes(Paths.get(path));
        return new String(encoded, encoding);
    }
}
