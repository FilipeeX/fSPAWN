package filipeex.fspawn;

import filipeex.fspawn.commands.SetSpawnCMD;
import filipeex.fspawn.commands.SpawnCMD;
import filipeex.fspawn.listeners.PlayerConncetionLIS;
import filipeex.fspawn.util.*;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Main extends JavaPlugin {

    public static Main i = null;
    public static boolean downloaded = false;

    @Override
    public void onLoad() {

        Output.log("Loading fSPAWN...");

        setUpMainInstance();

        Output.log("fSPAWN was loaded successfully!");

    }

    @Override
    public void onEnable() {

        Output.log("Enabling fSPAWN...");

        loadConfiguration();
        updateConfiguration();
        registerCommands();
        registerListeners();
        checkForEssentials();
        checkForUpdates();
        bStats();

        Output.log("fSPAWN was enabled successfully!");

    }

    private void setUpMainInstance() {

        Output.log("Setting up main instance...");

        i = this;

        Output.log("Main instance set up successfully!");

    }

    private void checkForEssentials() {

        if (!Config.getSettings().getBoolean("disable-if-essentials-is-found")) {
            Output.log("Checking for fESSENTIALS...");
            if (getServer().getPluginManager().isPluginEnabled("fESSENTIALS")) {
                Output.err("fESSENTIALS found, disabling fSPAWN because fESSENTIALS also does exactly what this plugin does!");
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
            Output.log("Checking successful, fESSENTIALS not installed, continuing to enable fSPAWN...");
        } else
            Output.warn("Skipping fESSENTIALS check, this can be kinda risky, we recommend you to have it enabled in settings.yml...");

    }

    private void loadConfiguration() {

        Output.log("Loading settings.yml...");
        if (!Config.createSettings())
            Output.warn("Configuration file settings.yml was loaded with errors, please fix them and reload it using /reload.");
        else
            Output.log("Configuration file settings.yml successfully loaded!");

        Output.log("Database files will load after the server is fully started.");
        getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
            @Override
            public void run() {

                Output.log("Loading data.yml...");
                if (!Config.createData())
                    Output.log("Database file data.yml was loaded with errors, either it's corrupted or you messed it up. It is recommended to reset it, but it can also be harmful.");
                else
                    Output.log("Database file data.yml successfully loaded!");

            }
        });

        Output.log("Loading messages.yml...");
        if (!Config.createMessages())
            Output.log("Language file messages.yml was loaded with errors, please fix them and reload it using /reload.");
        else
            Output.log("Successfully loaded language file messages.yml!");

    }

    private void updateConfiguration() {

        Output.log("Scanning configuration versions to see if there is an configration update available...");

        Output.log("Scanning configuration version of settings.yml...");
        if (ConfigUpdateUtil.doSettingsNeedConfigurationUpdate(getDescription().getVersion())) {
            Output.log("File settings.yml needs a configuration update, executing...");
            ConfigUpdateUtil.performSettingsConfigurationUpdate();
            Output.log("Successfully applied a configuration update to settings.yml!");
        } else {
            Output.log("File settings.yml doesn't need a configuration update.");
        }

        Output.log("Scanning configuration version of messages.yml...");
        if (ConfigUpdateUtil.doMessagesNeedConfigurationUpdate(getDescription().getVersion())) {
            Output.log("File messages.yml needs a configuration update, executing...");
            ConfigUpdateUtil.performMessagesConfigurationUpdate();
            Output.log("Successfully applied a configuration update to messages.yml!");
        } else {
            Output.log("File messages.yml doesn't need a configuration update.");
        }

        Output.log("Configuration update section finished, moving on..");

    }

    private void registerCommands() {

        Output.log("Registering commands...");

        getCommand("setspawn").setExecutor(new SetSpawnCMD());
        getCommand("spawn").setExecutor(new SpawnCMD());

        Output.log("Commands successfully registred!");

    }

    private void registerListeners() {

        Output.log("Registering listeners...");

        PluginManager pm = getServer().getPluginManager();
        pm.registerEvents(new PlayerConncetionLIS(), this);

        Output.log("Successfully registered listeners!");

    }

    private void checkForUpdates() {

        Output.log("Checking for updates...");

        if (UpdateChecker.checkForUpdate(getDescription().getVersion())) {

            if (!Config.getSettings().getBoolean("allow-self-update")) {

                Output.warn("There is a new version of this plugin available, please download it as soon as possible!");
                Output.warn("You are currently using %v, but the newest version of fSPAWN is %n."
                        .replace("%v", getDescription().getVersion())
                        .replace("%n", UpdateChecker.getActualVersion()));

                getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable() {
                    public void run() {

                        Output.warn("There is a new version of this plugin available, please download it as soon as possible!");
                        Output.warn("You are currently using %v, but the newest version of fSPAWN is %n."
                                .replace("%v", getDescription().getVersion())
                                .replace("%n", UpdateChecker.getActualVersion()));

                    }
                });

            } else {

                Output.log("There's a new version available, current: %v, newest: %n!".
                        replace("%v", getDescription().getVersion()).
                        replace("%n", UpdateChecker.getActualVersion()));
                Output.log("Downoading the new fSPAWN.jar...");

                if (UpdateDownloader.download(104579)) {
                    Output.log("Downloading update successfull, please restart the server for the update to take effect.");
                    downloaded = true;
                } else {
                    Output.err("Downloading update failed, please download it manually.");
                }

            }

        } else {
            Output.log("Checking for updates successful, no new updates found!");
        }

    }

    private void bStats() {

        try {

            // All you have to do is adding the following two lines in your onEnable method.
            // You can find the plugin ids of your plugins on the page https://bstats.org/what-is-my-plugin-id
            int pluginId = 16213; // <-- Replace with the id of your plugin!
            Metrics metrics = new Metrics(this, pluginId);

        } catch (NoClassDefFoundError ex) {
            Output.err("Statistics addon bStats cannot be loaded, an error occurred! Otherwise the plugin is functional.");
        }

    }


}
