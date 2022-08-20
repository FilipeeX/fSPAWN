package filipeex.fspawn.util;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicReference;

public class PlayerUtil {

    public static boolean isOnline(String nick) {
        boolean result = false;

        for (Player p : Bukkit.getOnlinePlayers())
            if (p.getDisplayName().equalsIgnoreCase(nick))
                result = true;

        return result;
    }

    public static boolean isSelf(Player sender, Player argument) {
        boolean result = false;

        if (sender.getDisplayName().equalsIgnoreCase(argument.getDisplayName()))
            result = true;

        return result;
    }

    public static ArrayList<String> getOnlinePlayerNames() {
        ArrayList<String> result = new ArrayList<String>();

        for (Player p : Bukkit.getOnlinePlayers())
            result.add(p.getDisplayName());

        return result;
    }

}
