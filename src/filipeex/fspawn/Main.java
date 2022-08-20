package filipeex.fspawn;

import filipeex.fspawn.commands.SetSpawnCMD;
import filipeex.fspawn.commands.SpawnCMD;
import filipeex.fspawn.util.Config;
import filipeex.fspawn.util.Output;
import filipeex.fspawn.util.UpdateChecker;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {

    public static Main i = null;

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
        registerCommands();
        checkForEssentials();
        checkForUpdates();

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

    private void registerCommands() {

        Output.log("Registering commands...");

        getCommand("setspawn").setExecutor(new SetSpawnCMD());
        getCommand("spawn").setExecutor(new SpawnCMD());

        Output.log("Commands successfully registred!");

    }

    private void checkForUpdates() {

        Output.log("Checking for updates...");

        if (UpdateChecker.checkForUpdate(getDescription().getVersion())) {

            Output.warn("There is a new version of this plugin available, please download it as soon as possible!");
            Output.warn("You are currently using %v, but the newest version of fSPAWN is %n."
                    .replace("%v", getDescription().getVersion())
                    .replace("%n", UpdateChecker.getActualVersion()));

            getServer().getScheduler().scheduleSyncDelayedTask(this, new Runnable(){
                public void run(){

                    Output.warn("There is a new version of this plugin available, please download it as soon as possible!");
                    Output.warn("You are currently using %v, but the newest version of fSPAWN is %n."
                            .replace("%v", getDescription().getVersion())
                            .replace("%n", UpdateChecker.getActualVersion()));

                }
            });

        } else {
            Output.log("Checking for updates successful, no new updates found!");
        }

    }


}
