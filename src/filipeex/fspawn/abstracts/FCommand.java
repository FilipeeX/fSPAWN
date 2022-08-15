package filipeex.fspawn.abstracts;

import com.sun.jdi.connect.Connector;
import filipeex.fspawn.structs.TabArgument;
import filipeex.fspawn.structs.TabArgumentSet;
import filipeex.fspawn.util.TabArgumentCompleter;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabExecutor;

import java.util.ArrayList;
import java.util.List;

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
        return TabArgumentCompleter.getCompletions(argumentStrings, strings[strings.length - 1]);
    }

}
