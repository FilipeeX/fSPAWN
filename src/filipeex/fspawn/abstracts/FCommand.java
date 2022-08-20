package filipeex.fspawn.abstracts;

import com.sun.jdi.connect.Connector;
import filipeex.fspawn.structs.TabArgument;
import filipeex.fspawn.structs.TabArgumentSet;
import filipeex.fspawn.util.TabArgumentCompleter;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

public abstract class FCommand implements TabExecutor {

    public abstract void command(CommandSender sender, Command cmd, String label, String[] args);
    public abstract TabArgumentSet tab(CommandSender sender, Command cmd, String label, String[] args);

    @Override
    public boolean onCommand(CommandSender commandSender, Command command, String s, String[] strings) {
        command(commandSender, command, s, strings);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender commandSender, Command command, String s, String[] strings) {

        TabArgumentSet argumentSet = tab(commandSender, command, s, strings);
        ArrayList<String> argumentStrings = new ArrayList<String>();
        for (TabArgument arg : argumentSet.tabArguments)
            argumentStrings.add(arg.argument);

        if (commandSender instanceof Player)
            ((Player) commandSender).getWorld().playSound(((Player) commandSender).getLocation(), Sound.UI_BUTTON_CLICK, 0.5f, new Random().nextFloat(0.75f, 1.25f));

        return TabArgumentCompleter.getCompletions(argumentStrings, strings[strings.length - 1]);
    }

}
