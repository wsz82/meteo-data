package szpikow;

import java.io.PrintStream;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public class Logger {
    private static final DateTimeFormatter dtf = DateTimeFormatter.ofPattern("yyyy/MM/dd HH:mm:ss");

    public static void out(String text) {
        log(System.out, text);
    }

    public static void err(String text) {
        log(System.err, text);
    }

    private static void log(PrintStream out, String text) {
        LocalDateTime now = LocalDateTime.now();
        out.println(dtf.format(now) + "\t" + text);
    }

}
