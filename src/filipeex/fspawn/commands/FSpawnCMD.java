package filipeex.fspawn.commands;

import filipeex.fapi.abstracts.FCommand;
import filipeex.fapi.structs.Replacement;
import filipeex.fapi.structs.ReplacementSet;
import filipeex.fapi.structs.TabArgumentSet;
import filipeex.fapi.util.Config;
import filipeex.fapi.util.Message;
import filipeex.fapi.util.Output;
import filipeex.fapi.util.PermUtil;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FSpawnCMD extends FCommand {

    @Override
    public void command(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("reload")) {

                if (!Config.getConfig("settings.yml").getBoolean("allow-fspawn-reload")) {
                    Message.send(sender, "module-disabled");
                    return;
                }

                if (!PermUtil.hasPermission(sender, "fspawn.admin.reload"))
                    return;

                Output.log("Reloading configuration...");
                Output.log("Reloading settings.yml...");
                Config.reloadConfig("settings.yml");
                Output.log("File settings.yml successfully reloaded!");
                Output.log("Reloading spawn.yml...");
                Config.reloadConfig("spawn.yml");
                Output.log("File spawn.yml successfully reloaded!");
                Output.log("Reloading messages.yml...");
                Config.reloadConfig("messages.yml");
                Output.log("File messages.yml successfully reloaded!");
                Output.log("Finisged reloading configuration!");

                Message.send(sender, "fspawn-reload-success");

            } else {
                Message.send(sender, "fspawn-usage");
            }

        } else {
            Message.send(sender, "fspawn-usage");
        }

    }

    @Override
    public TabArgumentSet tab(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1)
            return new TabArgumentSet(new String[]{"reload"});

        return new TabArgumentSet(new String[]{""});
    }

}
