package filipeex.fspawn.util;

import filipeex.fspawn.Main;
import org.bukkit.Bukkit;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.Scanner;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Consumer;

public class UpdateChecker {

    private static final int resourceId = 104579;

    public static void getVersion(final Consumer<String> consumer) {

        try (InputStream inputStream = new URL("https://api.spigotmc.org/legacy/update.php?resource=" + resourceId).openStream(); Scanner scanner = new Scanner(inputStream)) {
            if (scanner.hasNext()) {
                consumer.accept(scanner.next());
            }
        } catch (IOException exception) {
            Main.i.getLogger().info("An error occurred while checking the plugin version. Unable to check for updates: " + exception.getMessage());
        }

    }

    public static String getActualVersion() {
        AtomicReference<String> result = new AtomicReference<>("");

        getVersion(spigotVersion -> {
            result.set(spigotVersion);
        });

        return result.get();
    }

    public static boolean checkForUpdate(String version) {
        AtomicBoolean result = new AtomicBoolean(false);

        getVersion(spigotVersion -> {
            if (version.equalsIgnoreCase(spigotVersion)) {
                result.set(false);
            } else {
                result.set(true);
            }
        });

        return result.get();
    }
}