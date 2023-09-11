package xyz.msws.endershortcut;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.msws.endershortcut.commands.EnderChestCommand;
import xyz.msws.endershortcut.listeners.EnderOpenListener;
import xyz.msws.endershortcut.listeners.PickupEnderChestListener;
import xyz.msws.endershortcut.utils.Lang;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class EnderShortcut extends JavaPlugin {

    @Override
    public void onEnable() {
        generateFiles();

        registerListener();
        registerCommands();
    }

    private void registerListener() {
        Bukkit.getPluginManager().registerEvents(new EnderOpenListener(this), this);
        Bukkit.getPluginManager().registerEvents(new PickupEnderChestListener(this), this);
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("enderchest")).setExecutor(new EnderChestCommand());
    }

    /**
     * Generates the lang.yml parent folder and lang.yml file itself
     */
    private void generateFiles() {
        File parent = this.getDataFolder();
        if (!parent.exists())
            parent.mkdirs();
        File langFile = new File(this.getDataFolder(), "lang.yml");
        try {
            if (langFile.createNewFile())
                Lang.populate(langFile);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        YamlConfiguration lang = YamlConfiguration.loadConfiguration(langFile); // Parse the file into a YamlConfiguration
        Lang.load(lang); // Load the Configuration's values
    }
}
