package filipeex.fspawn.util;

import java.util.HashMap;
import java.util.Map;

public class ConfigUpdateUtil {

    public static void performSettingsConfigurationUpdate() {

        HashMap<String, Object> temporaryKeyValueStorage = new HashMap<String, Object>();

        for (String s : Config.getSettings().getKeys(true))
            temporaryKeyValueStorage.put(s, Config.getSettings().get(s));

        Config.resetSettings();

        for (Map.Entry<String, Object> entry : temporaryKeyValueStorage.entrySet())
            if (!entry.getKey().equalsIgnoreCase("version"))
                if (entry.getValue() != null)
                    Config.getSettings().set(entry.getKey(), entry.getValue());

        Config.saveSettings();

    }

    public static void performDataConfigurationUpdate() {

        HashMap<String, Object> temporaryKeyValueStorage = new HashMap<String, Object>();

        for (String s : Config.getData().getKeys(true))
            temporaryKeyValueStorage.put(s, Config.getData().get(s));

        Config.resetData();

        for (Map.Entry<String, Object> entry : temporaryKeyValueStorage.entrySet())
            if (!entry.getKey().equalsIgnoreCase("version"))
                Config.getData().set(entry.getKey(), entry.getValue());

        Config.saveData();

    }

    public static void performMessagesConfigurationUpdate() {

        HashMap<String, Object> temporaryKeyValueStorage = new HashMap<String, Object>();

        for (String s : Config.getMessages().getKeys(true))
            temporaryKeyValueStorage.put(s, Config.getMessages().get(s));

        Config.resetMessages();

        for (Map.Entry<String, Object> entry : temporaryKeyValueStorage.entrySet())
            if (!entry.getKey().equalsIgnoreCase("version"))
                Config.getMessages().set(entry.getKey(), entry.getValue());

        Config.saveMessages();

    }

    public static boolean doSettingsNeedConfigurationUpdate(String pluginVersion) {
        boolean result = false;

        String configVersion = Config.getSettings().getString("version");
        if (!pluginVersion.equalsIgnoreCase(configVersion))
            result = true;

        return result;
    }

    public static boolean doDataNeedConfigurationUpdate(String pluginVersion) {
        boolean result = false;

        String configVersion = Config.getData().getString("version");
        if (!pluginVersion.equalsIgnoreCase(configVersion))
            result = true;

        return result;
    }

    public static boolean doMessagesNeedConfigurationUpdate(String pluginVersion) {
        boolean result = false;

        String configVersion = Config.getMessages().getString("version");
        if (!pluginVersion.equalsIgnoreCase(configVersion))
            result = true;

        return result;
    }

}
