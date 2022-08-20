package filipeex.fspawn.commands;

import filipeex.fspawn.abstracts.FCommand;
import filipeex.fspawn.structs.Replacement;
import filipeex.fspawn.structs.ReplacementSet;
import filipeex.fspawn.structs.TabArgumentSet;
import filipeex.fspawn.util.Config;
import filipeex.fspawn.util.Message;
import filipeex.fspawn.util.PlayerUtil;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCMD extends FCommand {

    @Override
    public void command(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("fspawn.setspawn")) {
            Message.send(sender, "no-permission", new ReplacementSet(new Replacement("%permission%", "fspawn.setspawn")));
            return;
        }

        if (args.length > 1) {
            Message.send(sender, "setspawn-usage");
            return;
        }

        if (args.length == 0) {

            if (!(sender instanceof Player)) {
                Message.send(sender, "player-only");
                Message.send(sender, "setspawn-usage");
                return;
            }
            Player p = (Player) sender;
            Location newSpawnLoc = p.getLocation();

            Config.getData().set("spawn", newSpawnLoc);
            Config.saveData();

            Message.send(p, "setspawn-success-own");
            p.getWorld().playSound(newSpawnLoc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
            p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, newSpawnLoc, 1);

        } else if (args.length == 1) {

            if (!sender.hasPermission("fspawn.setspawn.other")) {
                Message.send(sender, "no-permission", new ReplacementSet(new Replacement("%permission%", "fspawn.setspawn.other")));
                return;
            }

            String targetNick = args[0];
            if (!PlayerUtil.isOnline(targetNick)) {
                Message.send(sender, "invalid-player", new ReplacementSet(new Replacement("%invalid%", targetNick)));
                return;
            }
            Player target = Bukkit.getPlayer(targetNick);

            if (sender instanceof Player)
                if (PlayerUtil.isSelf((Player) sender, target)) {
                    Message.send((Player) sender, "another-player-only");
                    return;
                }

            Location newSpawnLoc = target.getLocation();

            Config.getData().set("spawn", newSpawnLoc);
            Config.saveData();

            Message.send(sender, "setspawn-success-another", new ReplacementSet(new Replacement("%target%", targetNick)));

            Message.send(target, "setspawn-success-by-admin");
            target.getWorld().playSound(newSpawnLoc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
            target.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, newSpawnLoc, 1);

        } else {
            Message.send(sender, "setspawn-usage");
            return;
        }
    }

    @Override
    public TabArgumentSet tab(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1)
            return new TabArgumentSet(PlayerUtil.getOnlinePlayerNames());

        return new TabArgumentSet(new String[]{""});
    }

}
