package filipeex.fspawn.util;

import org.bukkit.ChatColor;

public class Chat {

    public static String color(String msg) {

        String result = "";

        result = ChatColor.translateAlternateColorCodes('&', msg);

        return result;
    }

}
