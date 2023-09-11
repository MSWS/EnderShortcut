package xyz.msws.endershortcut.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * An enum to keep track of messages.
 */
public enum Lang {
    PREFIX("&9EnderShortcut>&7"),
    MUST_BE_PLAYER("%prefix% You must be a player to run this command."),
    MUST_HAVE_ENDERCHEST("%prefix% You must have an enderchest in your inventory to run this command."),
    MUST_HAVE_EITHER("%prefix% You must have either an eye of ender or a silk touch pickaxe to run this command."),
    CONSUMED_EYE("%prefix% You consumed an eye of ender to open your enderchest."),
    ITEM_RIGHT_CLICK("&7Right-Click to open.");

    private final Object def;
    private Object value;

    Lang(Object value) {
        this.value = value;
        this.def = value;
    }

    public void setValue(Object v) {
        this.value = v;
    }

    public Object getValue() {
        return value;
    }

    public Object getDefault() {
        return def;
    }

    /**
     * Overrides all current values from the specified config
     *
     * @param lang
     */
    public static void load(YamlConfiguration lang) {
        for (Lang l : Lang.values()) {
            if (!lang.contains(l.toString())) { // If the config doesn't contain the value, add it
                lang.set(l.toString(), l.getDefault());
                continue;
            }
            l.setValue(lang.get(l.toString(), l.getValue()));
        }
    }

    /**
     * Sends the message to the {@link CommandSender}, if there are placeholders
     * they will be formatted via {@link String#format(String, Object...)}.
     *
     * @param sender
     * @param params
     */
    public void send(CommandSender sender, Object... params) {
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', String.format(value.toString().replace("%prefix%", PREFIX.getValue().toString()), params)));
    }

    /**
     * Populates the specified config with all the values from this enum
     *
     * @param config The config to populate
     */
    public static void populate(YamlConfiguration config) {
        for (Lang l : Lang.values())
            config.set(l.toString(), l.getValue());
    }

    /**
     * Populates the specified file with all the values from this enum
     * Saves the file after populating
     *
     * @param file The file to populate
     */
    public static void populate(File file) {
        YamlConfiguration config = YamlConfiguration.loadConfiguration(file);
        populate(config);
        try {
            config.save(file);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
