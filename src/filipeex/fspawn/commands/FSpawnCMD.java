package filipeex.fspawn.commands;

import filipeex.fspawn.abstracts.FCommand;
import filipeex.fspawn.structs.Replacement;
import filipeex.fspawn.structs.ReplacementSet;
import filipeex.fspawn.structs.TabArgument;
import filipeex.fspawn.structs.TabArgumentSet;
import filipeex.fspawn.util.Config;
import filipeex.fspawn.util.Message;
import filipeex.fspawn.util.Output;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

public class FSpawnCMD extends FCommand {

    @Override
    public void command(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {

            if (args[0].equalsIgnoreCase("reload")) {

                if (!Config.getSettings().getBoolean("allow-fspawn-reload")) {
                    Message.send(sender, "module-disabled");
                    return;
                }

                if (!sender.hasPermission("fspawn.admin.reload")) {
                    Message.send(sender, "no-permission", new ReplacementSet(new Replacement("%permission%", "fspawn.admin.reload")));
                    return;
                }

                Output.log("Reloading configuration...");
                Output.log("Reloading settings.yml...");
                Config.reloadSettings();
                Output.log("File settings.yml successfully reloaded!");
                Output.log("Reloading data.yml...");
                Config.reloadData();
                Output.log("File data.yml successfully reloaded!");
                Output.log("Reloading messages.yml...");
                Config.reloadMessages();
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
