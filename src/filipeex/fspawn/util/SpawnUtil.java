package filipeex.fspawn.util;

import filipeex.fapi.util.Config;
import filipeex.fapi.util.Output;
import org.bukkit.Location;

public class SpawnUtil {

    public static boolean spawnWorking() {

        try {

            return Config.getDatabase("spawn.yml").contains("spawn") && Config.getDatabase("spawn.yml").isLocation("spawn");

        } catch (Exception ex) {
            Output.err("An error occurred while trying to load spawn from database file; " + ex.getMessage());
        }

        return false;
    }

    public static Location getSpawn() {

        try {

            if (!spawnWorking())
                return null;

            return Config.getDatabase("spawn.yml").getLocation("spawn");

        } catch (Exception ex) {
            Output.err("An error occurred while trying to load spawn from database file; " + ex.getMessage());
            return null;
        }

    }

    public static boolean setSpawn(Location spawnLoc) {

        try {

            Config.getDatabase("spawn.yml").set("spawn", spawnLoc);
            Config.saveDatabase("spawn.yml");
            return true;

        } catch (Exception ex) {
            Output.err("An error occurred while trying to set spawn in the database fule; " + ex.getMessage());
            return false;
        }

    }

}
