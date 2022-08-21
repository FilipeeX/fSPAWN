package filipeex.fspawn.util;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URL;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;

import filipeex.fspawn.Main;
import org.bukkit.Bukkit;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.java.JavaPlugin;

public class UpdateDownloader {

    public static boolean download(int resourceId) {
        try {
            download(new URL("https://api.spiget.org/v2/resources/" + resourceId + "/download"),
                    new File(Main.i.getDataFolder().getParentFile(), Main.i.getDescription().getName() + ".jar"));
            return true;
        } catch (Exception e) {
            Output.err("Unable to download fSPAWN.jar file to update this plugin to the newest version! %m".
                    replace("%m", e.getMessage()));
            return false;
        }
    }

    private static void download(URL url, File target) throws IOException {
        target.getParentFile().mkdirs();
        target.createNewFile();
        ReadableByteChannel rbc = Channels.newChannel(url.openStream());
        FileOutputStream fos = new FileOutputStream(target);
        fos.getChannel().transferFrom(rbc, 0, Long.MAX_VALUE);
        fos.close();
        rbc.close();
    }

}