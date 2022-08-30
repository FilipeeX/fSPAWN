package filipeex.fspawn.commands;

import filipeex.fapi.abstracts.FCommand;
import filipeex.fapi.structs.Replacement;
import filipeex.fapi.structs.ReplacementSet;
import filipeex.fapi.structs.TabArgumentSet;
import filipeex.fapi.util.Config;
import filipeex.fapi.util.Message;
import filipeex.fapi.util.PermUtil;
import filipeex.fapi.util.PlayerUtil;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class SetSpawnCMD extends FCommand {

    @Override
    public void command(CommandSender sender, Command cmd, String label, String[] args) {

        if (!PermUtil.hasPermission(sender, "fspawn.setspawn"))
            return;

        if (args.length > 1) {
            Message.send(sender, "setspawn-usage");
            return;
        }

        if (args.length == 0) {

            setSpawnToSelfLocation(sender);

        } else if (args.length == 1) {

            setSpawnToAnotherLocation(sender, args[0]);

        } else {
            Message.send(sender, "setspawn-usage");
            return;
        }
    }

    // PLAYER >> SET SPAWN TO PLAYER'S LOCATION
    // CONSOLE >> SEND USAGE AND INVALID PLAYER MESSAGE
    // NO ARGUMENTS
    private void setSpawnToSelfLocation(CommandSender sender) {

        if (!(sender instanceof Player)) {
            Message.send(sender, "player-only");
            Message.send(sender, "setspawn-usage");
            return;
        }

        Player p = (Player) sender;
        Location newSpawnLoc = p.getLocation();

        Config.getDatabase("spawn.yml").set("spawn", newSpawnLoc);
        Config.saveDatabase("spawn.yml");

        Message.send(p, "setspawn-success-own");
        p.getWorld().playSound(newSpawnLoc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
        p.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, newSpawnLoc, 1);

    }

    // PLAYER && CONSOLE >> SET SPAWN TO TARGET'S LOCATION
    // 1 ARGUMENT >> TARGET NICKNAME
    private void setSpawnToAnotherLocation(CommandSender sender, String targetNick) {

        if (!PermUtil.hasPermission(sender, "fspawn.setspawn.other"))
            return;

        if (!PlayerUtil.isOnline(targetNick)) {
            setSpawnToAnotherOfflineLocation(sender, targetNick);
            return;
        }
        Player target = Bukkit.getPlayer(targetNick);

        if (sender instanceof Player)
            if (PlayerUtil.isSelf((Player) sender, target)) {
                Message.send((Player) sender, "another-player-only");
                return;
            }

        Location newSpawnLoc = target.getLocation();

        Config.getDatabase("spawn.yml").set("spawn", newSpawnLoc);
        Config.saveDatabase("spawn.yml");

        Message.send(sender, "setspawn-success-another-online", new ReplacementSet(new Replacement("%target%", targetNick)));

        Message.send(target, "setspawn-success-by-admin");
        target.getWorld().playSound(newSpawnLoc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
        target.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, newSpawnLoc, 1);

    }

    // PLAYER && CONSOLE >> SET SPAWN TO OFFLINE TARGET'S LOCATIOn
    // 1 ARGUMENT >> OFFLINE TARGET NICKNAME
    private void setSpawnToAnotherOfflineLocation(CommandSender sender, String offlineTargetNick) {

        if (!PlayerUtil.isOffline(offlineTargetNick)) {
            Message.send(sender, "invalid-player", new ReplacementSet(new Replacement("%invalid%", offlineTargetNick)));
            return;
        }
        OfflinePlayer target = Bukkit.getOfflinePlayer(offlineTargetNick);

        Location newSpawnLoc = Config.getDatabase("lastlocs.yml").getLocation(target.getUniqueId().toString());

        Config.getDatabase("spawn.yml").set("spawn", newSpawnLoc);
        Config.saveDatabase("spawn.yml");

        Message.send(sender, "setspawn-success-another-offline", new ReplacementSet(new Replacement("%target%", offlineTargetNick)));

        newSpawnLoc.getWorld().playSound(newSpawnLoc, Sound.UI_TOAST_CHALLENGE_COMPLETE, 1, 1);
        newSpawnLoc.getWorld().spawnParticle(Particle.EXPLOSION_HUGE, newSpawnLoc, 1);

    }

    @Override
    public TabArgumentSet tab(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1)
            return new TabArgumentSet(PlayerUtil.getOnlinePlayerNames());

        return new TabArgumentSet(new String[]{""});
    }

}
