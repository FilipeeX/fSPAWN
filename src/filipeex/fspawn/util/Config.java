package filipeex.fspawn.util;

import filipeex.fspawn.Main;
import org.bukkit.configuration.InvalidConfigurationException;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.configuration.file.YamlConfiguration;
import org.yaml.snakeyaml.Yaml;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;

public class Config {

    // ░██████╗███████╗████████╗████████╗██╗███╗░░██╗░██████╗░░██████╗
    // ██╔════╝██╔════╝╚══██╔══╝╚══██╔══╝██║████╗░██║██╔════╝░██╔════╝
    // ╚█████╗░█████╗░░░░░██║░░░░░░██║░░░██║██╔██╗██║██║░░██╗░╚█████╗░
    // ░╚═══██╗██╔══╝░░░░░██║░░░░░░██║░░░██║██║╚████║██║░░╚██╗░╚═══██╗
    // ██████╔╝███████╗░░░██║░░░░░░██║░░░██║██║░╚███║╚██████╔╝██████╔╝
    // ╚═════╝░╚══════╝░░░╚═╝░░░░░░╚═╝░░░╚═╝╚═╝░░╚══╝░╚═════╝░╚═════╝░

    private static File settingsFile;
    private static FileConfiguration settings;

    public static FileConfiguration getSettings() {
        return settings;
    }

    public static boolean createSettings() {
        boolean success = true;

        settingsFile = new File(Main.i.getDataFolder(), "settings.yml");
        if (!settingsFile.exists()) {
            settingsFile.getParentFile().mkdirs();
            Main.i.saveResource("settings.yml", false);
        }

        settings = new YamlConfiguration();
        try {
            settings.load(settingsFile);
        } catch (Exception ex) {
            success = false;
            Output.err(ex.getMessage());
        }

        return success;
    }

    public static boolean reloadSettings() {
        boolean success = true;

        try {
            settings.load(settingsFile);
        } catch (Exception ex) {
            success = false;
            Output.err(ex.getMessage());
        }

        return success;
    }

    public static boolean saveSettings() {
        boolean success = true;

        try {
            settings.save(settingsFile);
        } catch (IOException ex) {
            success = false;
            Output.err(ex.getMessage());
        }

        return success;
    }


    // ██████╗░░█████╗░████████╗░█████╗░██████╗░░█████╗░░██████╗███████╗
    // ██╔══██╗██╔══██╗╚══██╔══╝██╔══██╗██╔══██╗██╔══██╗██╔════╝██╔════╝
    // ██║░░██║███████║░░░██║░░░███████║██████╦╝███████║╚█████╗░█████╗░░
    // ██║░░██║██╔══██║░░░██║░░░██╔══██║██╔══██╗██╔══██║░╚═══██╗██╔══╝░░
    // ██████╔╝██║░░██║░░░██║░░░██║░░██║██████╦╝██║░░██║██████╔╝███████╗
    // ╚═════╝░╚═╝░░╚═╝░░░╚═╝░░░╚═╝░░╚═╝╚═════╝░╚═╝░░╚═╝╚═════╝░╚══════╝

    private static File dataFile;
    private static FileConfiguration data;

    public static FileConfiguration getData() {
        return data;
    }

    public static boolean createData() {
        boolean success = true;

        dataFile = new File(Main.i.getDataFolder(), "data.yml");
        if (!dataFile.exists()) {
            dataFile.getParentFile().mkdirs();
            Main.i.saveResource("data.yml", false);
        }

        data = new YamlConfiguration();
        try {
            data.load(dataFile);
        } catch (Exception ex) {
            success = false;
            Output.err(ex.getMessage());
        }

        return success;
    }

    public static boolean reloadData() {
        boolean success = true;

        try {
            data.load(dataFile);
        } catch (Exception ex) {
            success = false;
            Output.err(ex.getMessage());
        }

        return success;
    }

    public static boolean saveData() {
        boolean success = true;

        try {
            data.save(dataFile);
        } catch (IOException ex) {
            success = false;
            Output.err(ex.getMessage());
        }

        return success;
    }


    // ███╗░░░███╗███████╗░██████╗░██████╗░█████╗░░██████╗░███████╗░██████╗
    // ████╗░████║██╔════╝██╔════╝██╔════╝██╔══██╗██╔════╝░██╔════╝██╔════╝
    // ██╔████╔██║█████╗░░╚█████╗░╚█████╗░███████║██║░░██╗░█████╗░░╚█████╗░
    // ██║╚██╔╝██║██╔══╝░░░╚═══██╗░╚═══██╗██╔══██║██║░░╚██╗██╔══╝░░░╚═══██╗
    // ██║░╚═╝░██║███████╗██████╔╝██████╔╝██║░░██║╚██████╔╝███████╗██████╔╝
    // ╚═╝░░░░░╚═╝╚══════╝╚═════╝░╚═════╝░╚═╝░░╚═╝░╚═════╝░╚══════╝╚═════╝░

    private static File messagesFile;
    private static FileConfiguration messages;

    public static FileConfiguration getMessages() {
        return messages;
    }

    public static boolean createMessages() {
        boolean success = true;

        messagesFile = new File(Main.i.getDataFolder(), "messages.yml");
        if (!messagesFile.exists()) {
            messagesFile.getParentFile().mkdirs();
            Main.i.saveResource("messages.yml", false);
        }

        messages = new YamlConfiguration();
        try {
            messages.load(messagesFile);
        } catch (Exception ex) {
            success = false;
            Output.err(ex.getMessage());
        }

        return success;
    }

    public static boolean reloadMessages() {
        boolean success = true;

        try {
            messages.load(messagesFile);
        } catch (Exception ex) {
            success = false;
            Output.err(ex.getMessage());
        }

        return success;
    }

    public static boolean saveMessages() {
        boolean success = true;

        try {
            messages.save(messagesFile);
        } catch (IOException ex) {
            success = false;
            Output.err(ex.getMessage());
        }

        return success;
    }

}
