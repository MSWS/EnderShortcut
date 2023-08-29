package xyz.msws.endershortcut.utils;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;

import java.io.File;

/**
 * An enum to keep track of messages.
 */
public enum Lang {
    PREFIX("Prefix", "&9EnderShortcut&7>"),
    MUST_BE_PLAYER("MustBePlayer", "%prefix% You must be a player to run this command."),
    MUST_HAVE_ENDERCHEST("MustHaveEnderchest", "%prefix% You must have an enderchest in your inventory to run this command."),
    MUST_HAVE_EITHER("MustHaveEither", "%prefix% You must have either an eye of ender or a silk touch pickaxe to run this command."),
    CONSUMED_EYE("ConsumedEye", "%prefix% You consumed an eye of ender to open your enderchest."),
    ITEM_CTRL_CLICK("ItemCtrlClick", "&7Right-Click to open.");

    private final String key;
    private final Object def;
    private Object value;

    Lang(String key, Object value) {
        this.key = key;
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

    public String getKey() {
        return key;
    }

    /**
     * Overrides all current values from the specified config
     *
     * @param lang
     */
    public static void load(YamlConfiguration lang) {
        for (Lang l : Lang.values())
            l.setValue(lang.get(l.getKey(), l.getValue()));
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
            config.set(l.getKey(), l.getValue());
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
