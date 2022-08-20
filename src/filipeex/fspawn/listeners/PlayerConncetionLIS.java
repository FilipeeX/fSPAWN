package filipeex.fspawn.listeners;

import filipeex.fspawn.Main;
import filipeex.fspawn.structs.Replacement;
import filipeex.fspawn.structs.ReplacementSet;
import filipeex.fspawn.util.*;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.scheduler.BukkitRunnable;

public class PlayerConncetionLIS implements Listener {

    @EventHandler(priority = EventPriority.LOWEST)
    public void onJoin(PlayerJoinEvent e) {

        firstJoin(e.getPlayer());
        teleportToSpawn(e.getPlayer());
        sendUpdateMessage(e.getPlayer());

    }

    private void firstJoin(Player p) {

        if (p.hasPlayedBefore())
            return;

        if (Config.getSettings().getBoolean("teleport-after-first-join")) {

            int delay = Config.getSettings().getInt("teleport-delay");
            new BukkitRunnable() {
                @Override
                public void run() {

                    if (!Config.getData().contains("spawn") || !Config.getData().isLocation("spawn")) {
                        Output.warn("Failed to teleport %p to spawn after first join (it's configured so in settings.yml)" +
                                ", because the server spawn is not set. Please set spawn by using /setspawn.".
                                replace("%p", p.getDisplayName()));
                    } else {
                        Location spawnLoc = Config.getData().getLocation("spawn");
                        p.teleport(spawnLoc, PlayerTeleportEvent.TeleportCause.PLUGIN);
                        Output.log("Successfully teleported %p to spawn, after they're first server connection.".
                                replace("%p", p.getDisplayName()));

                        p.setBedSpawnLocation(spawnLoc);
                        Output.log("Successfully set spawnpoint for %p on spawn, after they're fisrt server connection (it's configured so in settings.yml).".
                                replace("%p", p.getDisplayName()));
                    }

                }
            }.runTaskLater(Main.i, delay);

        }

    }

    private void teleportToSpawn(Player p) {

        if (!p.hasPlayedBefore())
            return;

        if (Config.getSettings().getBoolean("teleport-after-joining")) {

            int delay = Config.getSettings().getInt("teleport-delay");
            new BukkitRunnable() {
                @Override
                public void run() {

                    if (!Config.getData().contains("spawn") || !Config.getData().isLocation("spawn")) {
                        Output.warn("Failed to teleport %p to spawn after joining (it's configured so in settings.yml)" +
                                ", because the server spawn is not set. Please set spawn by using /setspawn.".
                                        replace("%p", p.getDisplayName()));
                    } else {
                        Location spawnLoc = Config.getData().getLocation("spawn");
                        p.teleport(spawnLoc, PlayerTeleportEvent.TeleportCause.PLUGIN);
                        Output.log("Successfully teleported %p to spawn, after they connected (it's configured so in settings.yml).".
                                replace("%p", p.getDisplayName()));
                    }

                }
            }.runTaskLater(Main.i, delay);

        }

    }

    private void sendUpdateMessage(Player p) {

        if (!Config.getSettings().getBoolean("update-notification"))
            return;

        if (!UpdateChecker.checkForUpdate(Main.i.getDescription().getVersion()))
            return;

        if (!p.hasPermission("fspawn.update"))
            return;

        new BukkitRunnable() {
            @Override
            public void run() {
                Message.send(p, "update0");
                Message.send(p, "update1");
                Message.send(p, "update2", new ReplacementSet(new Replacement[]{
                        new Replacement("%v", Main.i.getDescription().getVersion()),
                        new Replacement("%n", UpdateChecker.getActualVersion())}));
            }
        }.runTaskLater(Main.i, 5 * 20);

    }

    @EventHandler(priority = EventPriority.LOWEST)
    public void onLeave(PlayerQuitEvent e) {

        // NOTHING

    }

}
