package filipeex.fspawn.listeners;

import filipeex.fapi.structs.Replacement;
import filipeex.fapi.structs.ReplacementSet;
import filipeex.fapi.util.Config;
import filipeex.fapi.util.Message;
import filipeex.fapi.util.Output;
import filipeex.fapi.util.UpdateChecker;
import filipeex.fspawn.Main;
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

        tpToLastLoc(e.getPlayer());
        clearLastLoc(e.getPlayer());
        firstJoin(e.getPlayer());
        teleportToSpawn(e.getPlayer());
        sendUpdateMessage(e.getPlayer());

    }

    private void tpToLastLoc(Player p) {

        if (Config.getDatabase("lastlocs.yml").contains(p.getUniqueId().toString()) &&
            Config.getDatabase("lastlocs.yml").isLocation(p.getUniqueId().toString())) {

            new BukkitRunnable() {
                @Override
                public void run() {
                    p.teleport(Config.getDatabase("lastlocs.yml").getLocation(p.getUniqueId().toString()));
                }
            }.runTaskLater(Main.i, 4);

        }

    }

    private void clearLastLoc(Player p) {

        Config.getDatabase("lastlocs.yml").set(p.getUniqueId().toString(), null);
        Config.saveDatabase("lastlocs.yml");

    }

    private void firstJoin(Player p) {

        if (p.hasPlayedBefore())
            return;

        if (Config.getConfig("settings.yml").getBoolean("teleport-after-first-join")) {

            int delay = Config.getConfig("settings.yml").getInt("teleport-delay");
            new BukkitRunnable() {
                @Override
                public void run() {

                    if (!Config.getDatabase("spawn.yml").contains("spawn") || !Config.getDatabase("spawn.yml").isLocation("spawn")) {
                        Output.warn("Failed to teleport %p to spawn after first join (it's configured so in settings.yml)" +
                                ", because the server spawn is not set. Please set spawn by using /setspawn.".
                                replace("%p", p.getDisplayName()));
                    } else {
                        Location spawnLoc = Config.getDatabase("spawn.yml").getLocation("spawn");
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

        if (Config.getConfig("settings.yml").getBoolean("teleport-after-joining")) {

            int delay = Config.getConfig("settings.yml").getInt("teleport-delay");
            new BukkitRunnable() {
                @Override
                public void run() {

                    if (!Config.getDatabase("spawn.yml").contains("spawn") || !Config.getDatabase("spawn.yml").isLocation("spawn")) {
                        Output.warn("Failed to teleport %p to spawn after joining (it's configured so in settings.yml)" +
                                ", because the server spawn is not set. Please set spawn by using /setspawn.".
                                        replace("%p", p.getDisplayName()));
                    } else {
                        Location spawnLoc = Config.getDatabase("spawn.yml").getLocation("spawn");
                        p.teleport(spawnLoc, PlayerTeleportEvent.TeleportCause.PLUGIN);
                        Output.log("Successfully teleported %p to spawn, after they connected (it's configured so in settings.yml).".
                                replace("%p", p.getDisplayName()));
                    }

                }
            }.runTaskLater(Main.i, delay);

        }

    }

    private void sendUpdateMessage(Player p) {

        if (!Config.getConfig("settings.yml").getBoolean("update-notification"))
            return;

        if (!UpdateChecker.checkForUpdate(Main.i.getDescription().getVersion()))
            return;

        if (!p.hasPermission("fspawn.update"))
            return;

        if (Main.downloaded)
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

        setLastLoc(e.getPlayer());

    }

    private void setLastLoc(Player p) {

        Config.getDatabase("lastlocs.yml").set(p.getUniqueId().toString(), p.getLocation());
        Config.saveDatabase("lastlocs.yml");

    }

}
