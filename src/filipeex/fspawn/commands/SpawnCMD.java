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
import org.bukkit.event.player.PlayerTeleportEvent;

public class SpawnCMD extends FCommand {

    @Override
    public void command(CommandSender sender, Command cmd, String label, String[] args) {

        if (!sender.hasPermission("fspawn.spawn")) {
            Message.send(sender, "no-permission", new ReplacementSet(new Replacement("%permission%", "fspawn.spawn")));
            return;
        }

        if (args.length > 1) {
            Message.send(sender, "spawn-usage");
            return;
        }

        if (args.length == 0) {

            if (!(sender instanceof Player)) {
                Message.send(sender, "player-only");
                Message.send(sender, "spawn-usage");
                return;
            }
            Player p = (Player) sender;

            if (!Config.getData().contains("spawn") || !Config.getData().isLocation("spawn")) {
                Message.send(p, "spawn-failure-no-spawn");
                return;
            }
            Location spawnLoc = Config.getData().getLocation("spawn");
            p.teleport(spawnLoc, PlayerTeleportEvent.TeleportCause.COMMAND);

            Message.send(p, "spawn-success-own");
            p.getWorld().playSound(spawnLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

        } else if (args.length == 1) {

            if (!sender.hasPermission("fspawn.spawn.other")) {
                Message.send(sender, "no-permission", new ReplacementSet(new Replacement("%permission%", "fspawn.spawn.other")));
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

            if (!Config.getData().contains("spawn") || !Config.getData().isLocation("spawn")) {
                Message.send(sender, "spawn-failure-no-spawn");
                return;
            }
            Location spawnLoc = Config.getData().getLocation("spawn");
            target.teleport(spawnLoc, PlayerTeleportEvent.TeleportCause.COMMAND);

            Message.send(sender, "spawn-success-another", new ReplacementSet(new Replacement("%target%", targetNick)));

            Message.send(target, "spawn-success-by-admin");
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

        } else {
            Message.send(sender, "spawn-usage");
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
