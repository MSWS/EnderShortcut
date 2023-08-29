package xyz.msws.endershortcut;

import org.bukkit.Bukkit;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.msws.endershortcut.commands.EnderChestCommand;
import xyz.msws.endershortcut.listeners.EnderOpenListener;
import xyz.msws.endershortcut.utils.Lang;

import java.io.File;
import java.io.IOException;
import java.util.Objects;

public class EnderShortcut extends JavaPlugin {

    @Override
    public void onEnable() {
        registerListener();
        generateFiles();

        Objects.requireNonNull(getCommand("enderchest")).setExecutor(new EnderChestCommand());
    }

    private void registerListener() {
        Bukkit.getPluginManager().registerEvents(new EnderOpenListener(this), this);
    }

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
        YamlConfiguration lang = YamlConfiguration.loadConfiguration(langFile);
        Lang.load(lang);
    }
}
