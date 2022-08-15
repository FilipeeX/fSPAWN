package filipeex.fspawn.util;

import org.bukkit.Bukkit;

public class Output {

    public static String builtInPrefix = "&x&2&4&6&0&f&b&lf&x&3&5&5&5&f&5&lS&x&4&5&4&9&e&e&lP&x&5&6&3&e&e&8&lA&x&6&6&3&2&e&1&lW&x&7&7&2&7&d&b&lN&8 〣&f";

    public static void log(String msg) {

        String log = Chat.color(builtInPrefix + " " + msg);
        Bukkit.getConsoleSender().sendMessage(log);

    }

    public static void warn(String msg) {

        String warn = Chat.color(builtInPrefix + "&xF&B&6&1&0&B " + msg);
        Bukkit.getConsoleSender().sendMessage(warn);

    }

    public static void err(String msg) {

        String err = Chat.color(builtInPrefix + "&x&D&A&1&2&1&2 " + msg);
        Bukkit.getConsoleSender().sendMessage(err);

    }

}
