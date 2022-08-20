package filipeex.fspawn.util;

import filipeex.fspawn.structs.Replacement;
import filipeex.fspawn.structs.ReplacementSet;
import org.bukkit.Bukkit;
import org.bukkit.Server;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Message {

    public static void send(Player p, String msg) {

        String rawMessageFromFile = Config.getMessages().getString(msg);
        String rawMessageFromFileReplaced = rawMessageFromFile
                .replace("%player%", p.getDisplayName())
                .replace("%prefix%", Config.getMessages().getString("prefix"));
        String finalMessage = Chat.color(rawMessageFromFileReplaced);

        p.sendMessage(finalMessage);

    }

    public static void send(Player p, String msg, ReplacementSet rSet) {

        String rawMessageFromFile = Config.getMessages().getString(msg);
        String rawMessageFromFileReplaced = rawMessageFromFile
                .replace("%player%", p.getDisplayName())
                .replace("%prefix%", Config.getMessages().getString("prefix"));
        String rawMesageEverythingReplaced = replaceEverything(rawMessageFromFileReplaced, rSet);
        String finalMessage = Chat.color(rawMesageEverythingReplaced);

        p.sendMessage(finalMessage);

    }

    public static void send(CommandSender sender, String msg) {

        String rawMessageFromFile = Config.getMessages().getString(msg);
        String rawMessageFromFileReplaced = rawMessageFromFile
                .replace("%prefix%", Config.getMessages().getString("prefix"));
        String finalMessage = Chat.color(rawMessageFromFileReplaced);

        sender.sendMessage(finalMessage);

    }

    public static void send(CommandSender sender, String msg, ReplacementSet rSet) {

        String rawMessageFromFile = Config.getMessages().getString(msg);
        String rawMessageFromFileReplaced = rawMessageFromFile
                .replace("%prefix%", Config.getMessages().getString("prefix"));
        String rawMessageEverythingReplaced = replaceEverything(rawMessageFromFileReplaced, rSet);
        String finalMessage = Chat.color(rawMessageEverythingReplaced);

        sender.sendMessage(finalMessage);

    }

    private static String replaceEverything(String msg, ReplacementSet rSet) {
        String result = msg;

        for (Replacement r : rSet.replacements)
            result = result.replace(r.thingToReplace, r.replaceWith);

        return result;
    }

}
