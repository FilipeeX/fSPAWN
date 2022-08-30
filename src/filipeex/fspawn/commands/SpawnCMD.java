package filipeex.fspawn.commands;

import filipeex.fapi.abstracts.FCommand;
import filipeex.fapi.structs.Replacement;
import filipeex.fapi.structs.ReplacementSet;
import filipeex.fapi.structs.TabArgumentSet;
import filipeex.fapi.util.Config;
import filipeex.fapi.util.Message;
import filipeex.fapi.util.PermUtil;
import filipeex.fapi.util.PlayerUtil;
import filipeex.fspawn.util.SpawnUtil;
import org.bukkit.*;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.event.player.PlayerTeleportEvent;

import java.util.ArrayList;

public class SpawnCMD extends FCommand {

    @Override
    public void command(CommandSender sender, Command cmd, String label, String[] args) {

        if (!PermUtil.hasPermission(sender, "fspawn.spawn"))
            return;

        if (args.length > 1) {
            Message.send(sender, "spawn-usage");
            return;
        }

        if (args.length == 0) {

            teleportToSpawn(sender);

        } else if (args.length == 1) {

            if (args[0].equalsIgnoreCase("*")) {

                teleportAllOnlineToSpawn(sender);

            } else if (args[0].equalsIgnoreCase("**")) {

                teleportAllOnlineOfflineToSpawn(sender);

            } else {

                teleportAnotherToSpawn(sender, args[0]);

            }
        } else {
            Message.send(sender, "spawn-usage");
        }
    }

    // IF PLAYER >> TELEPORT TO SPAWN
    // IF CONSOLE >> SEND HOW TO USE + PLAYER ONLY MESSAGE
    // NO ARGUMENTS
    private void teleportToSpawn(CommandSender sender) {

        if (!(sender instanceof Player)) {
            Message.send(sender, "player-only");
            Message.send(sender, "spawn-usage");
            return;
        }
        Player p = (Player) sender;

        if (!SpawnUtil.spawnWorking()) {
            Message.send(p, "spawn-failure-no-spawn");
            return;
        }
        Location spawnLoc = SpawnUtil.getSpawn();
        p.teleport(spawnLoc, PlayerTeleportEvent.TeleportCause.COMMAND);

        Message.send(p, "spawn-success-own");
        p.getWorld().playSound(spawnLoc, Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

    }

    // PLAYER && CONSOLE >> TELEPORT TARGET TO SPAWN
    // 1 ARGUMENT >> TARGET NICKNAME
    private void teleportAnotherToSpawn(CommandSender sender, String targetNick) {

        if (!PermUtil.hasPermission(sender, "fspawn.spawn.other"))
            return;

        if (!PlayerUtil.isOnline(targetNick)) {
            teleportAnotherOfflineToSpawn(sender, targetNick);
            return;
        }
        Player target = Bukkit.getPlayer(targetNick);

        if (sender instanceof Player)
            if (PlayerUtil.isSelf((Player) sender, target)) {
                Message.send((Player) sender, "another-player-only");
                return;
            }

        if (!SpawnUtil.spawnWorking()) {
            Message.send(sender, "spawn-failure-no-spawn");
            return;
        }
        Location spawnLoc = SpawnUtil.getSpawn();
        target.teleport(spawnLoc, PlayerTeleportEvent.TeleportCause.COMMAND);

        Message.send(sender, "spawn-success-another-online", new ReplacementSet(new Replacement("%target%", targetNick)));

        Message.send(target, "spawn-success-by-admin");
        target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);

    }

    // PLAYER && CONSOLE >> TELEPORT OFFLINE TARGET TO SPAWN
    // 1 ARGUMENT >> OFFLINE TARGET NICKNAME
    private void teleportAnotherOfflineToSpawn(CommandSender sender, String offlineTargetNick) {

        if (!PermUtil.hasPermission(sender, "fspawn.spawn.other"))
            return;

        if (!PlayerUtil.isOffline(offlineTargetNick)) {
            Message.send(sender, "invalid-player", new ReplacementSet(new Replacement("%invalid%", offlineTargetNick)));
            return;
        }
        OfflinePlayer offlineTarget = Bukkit.getOfflinePlayer(offlineTargetNick);

        if (!SpawnUtil.spawnWorking()) {
            Message.send(sender, "spawn-failure-no-spawn");
            return;
        }
        Location spawnLoc = SpawnUtil.getSpawn();
        Config.getDatabase("lastlocs.yml").set(offlineTarget.getUniqueId().toString(), spawnLoc);

        Message.send(sender, "spawn-success-another-offline", new ReplacementSet(new Replacement("%target%", offlineTargetNick)));

    }

    // PLAYER && CONSOLE >> TELEPORT ALL ONLINE (*) TO SPAWN
    // 1 ARGUMENT >> '*'
    private void teleportAllOnlineToSpawn(CommandSender sender) {

        if (!PermUtil.hasPermission(sender, "fspawn.spawn.all.online"))
            return;

        if (!SpawnUtil.spawnWorking()) {
            Message.send(sender, "spawn-failure-no-spawn");
            return;
        }

        Location spawnLoc = SpawnUtil.getSpawn();
        for (Player p : Bukkit.getOnlinePlayers())
            p.teleport(spawnLoc, PlayerTeleportEvent.TeleportCause.COMMAND);

        Message.send(sender, "spawn-success-all-online", new ReplacementSet(new Replacement("%count%", Bukkit.getOnlinePlayers().size() + "")));

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (sender instanceof Player)
                if (!PlayerUtil.isSelf(((Player) sender), target)) {
                    Message.send(target, "spawn-success-by-admin");
                }
                else
                    Message.send(target, "spawn-success-by-admin");
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        }

    }

    // PLAYER && CONSOLE >> TELEPORT ALL OFFLINE && ONLINE (**) TO SPAWN
    // 1 ARGUMENT >> '**'
    private void teleportAllOnlineOfflineToSpawn(CommandSender sender) {

        if (!PermUtil.hasPermission(sender, "fspawn.spawn.all.offline"))
            return;

        if (!SpawnUtil.spawnWorking()) {
            Message.send(sender, "spawn-failure-no-spawn");
            return;
        }

        Location spawnLoc = SpawnUtil.getSpawn();
        int onlineCounter = 0;
        int offlineCounter = 0;
        for (Player p : Bukkit.getOnlinePlayers()) {
            p.teleport(spawnLoc, PlayerTeleportEvent.TeleportCause.COMMAND);
            onlineCounter++;
        }
        for (OfflinePlayer oP : Bukkit.getOfflinePlayers()) {
            if (!PlayerUtil.isOnline(oP.getName())) {
                Config.getDatabase("lastlocs.yml").set(oP.getUniqueId().toString(), spawnLoc);
                offlineCounter++;
            }
        }
        Config.saveDatabase("lastlocs.yml");

        Message.send(sender, "spawn-success-all-offline", new ReplacementSet(
                new Replacement[]{new Replacement("%count%", offlineCounter + onlineCounter + ""),
                        new Replacement("%online%", onlineCounter + ""),
                        new Replacement("%offline%", offlineCounter + "")}));

        for (Player target : Bukkit.getOnlinePlayers()) {
            if (sender instanceof Player)
                if (!PlayerUtil.isSelf(((Player) sender), target)) {
                    Message.send(target, "spawn-success-by-admin");
                }
                else
                    Message.send(target, "spawn-success-by-admin");
            target.getWorld().playSound(target.getLocation(), Sound.ENTITY_ENDERMAN_TELEPORT, 1, 1);
        }

    }

    @Override
    public TabArgumentSet tab(CommandSender sender, Command cmd, String label, String[] args) {

        if (args.length == 1) {
            ArrayList<String> suggestions = PlayerUtil.getOnlinePlayerNames();
            suggestions.add("*");
            suggestions.add("**");
            return new TabArgumentSet(suggestions);
        }

        return new TabArgumentSet(new String[]{""});
    }

}
