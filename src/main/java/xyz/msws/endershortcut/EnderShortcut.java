package xyz.msws.endershortcut;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.msws.endershortcut.commands.EnderChestCommand;
import xyz.msws.endershortcut.utils.Lang;

import java.io.File;
import java.io.IOException;

public class EnderShortcut extends JavaPlugin {

    @Override
    public void onEnable() {
        generateFiles();

        getCommand("enderchest").setExecutor(new EnderChestCommand());
    }

    private void generateFiles() {
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
