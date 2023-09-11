package xyz.msws.endershortcut;

import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.command.CommandSender;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.event.Listener;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.java.JavaPlugin;
import xyz.msws.endershortcut.api.EnderShortcutPlugin;
import xyz.msws.endershortcut.commands.EnderChestCommand;
import xyz.msws.endershortcut.listeners.EnderOpenListener;
import xyz.msws.endershortcut.listeners.InventoryClickListener;
import xyz.msws.endershortcut.listeners.ObtainEnderChestListener;
import xyz.msws.endershortcut.utils.Lang;

import java.io.File;
import java.io.IOException;
import java.util.List;
import java.util.Objects;

public class EnderShortcut extends JavaPlugin implements EnderShortcutPlugin {
    private EnderTagger chestTagger;

    @Override
    public void onEnable() {
        generateFiles();

        registerListener();
        registerCommands();
    }

    private void registerListener() {
        this.chestTagger = new ObtainEnderChestListener(this);
        List<Listener> listeners = List.of(
                new EnderOpenListener(this),
                (Listener) this.chestTagger,
                new InventoryClickListener(this)
        );
        listeners.forEach(listener -> Bukkit.getPluginManager().registerEvents(listener, this));
    }

    private void registerCommands() {
        Objects.requireNonNull(getCommand("enderchest")).setExecutor(new EnderChestCommand(this));
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

    /**
     * Checks if the sender can open their ender chest
     * Requires they have an ender chest in their inventory
     * and a means to get it back in their inventory
     * (either an ender eye or silk touch pickaxe)
     *
     * @param sender    The sender to check
     * @param removeEye If true, will remove an ender eye from the sender's inventory if used to open
     * @param print     If true, will print feedback to the sender
     * @return True if the sender can open their ender chest
     */
    public boolean allowEnderChest(CommandSender sender, boolean removeEye, boolean print) {
        if (!(sender instanceof Player player)) {
            Lang.MUST_BE_PLAYER.send(sender);
            return false;
        }

        if (player.getGameMode() == GameMode.CREATIVE) return true;

        if (!player.getInventory().contains(Material.ENDER_CHEST) && !sender.hasPermission("endershortcut.bypass.chest")) {
            Lang.MUST_HAVE_ENDERCHEST.send(player);
            return false;
        }

        if (hasSilkTouch(player.getInventory(), sender.hasPermission("endershortcut.bypass.anysilk"))) return true;

        if (sender.hasPermission("endershortcut.bypass.item")) return true;
        if (!player.getInventory().contains(Material.ENDER_EYE)) {
            if (print) Lang.MUST_HAVE_EITHER.send(player);
            return false;
        }
        if (removeEye) {
            player.getInventory().removeItem(new ItemStack(Material.ENDER_EYE, 1));
            // If we didn't remove an eye, don't show that we consumed one
            if (print) Lang.CONSUMED_EYE.send(player);
        }

        return true;
    }

    @Override
    public EnderTagger getChestTagger() {
        return chestTagger;
    }

    /**
     * Checks if the specified inventory has a silk touch pickaxe
     * See {@link #hasSilkTouch(Inventory, boolean)}
     *
     * @param inv The inventory to check
     * @return True if the inventory has a silk touch pickaxe
     */
    public boolean hasSilkTouch(Inventory inv) {
        return hasSilkTouch(inv, false);
    }

    /**
     * Checks if the specified inventory has a silk touch pickaxe
     * If anyItem is true, it will check for any item with silk touch
     *
     * @param inv     The inventory to check
     * @param anyItem If true, will check for any item with silk touch
     * @return True if the inventory has a silk touch item
     */
    public boolean hasSilkTouch(Inventory inv, boolean anyItem) {
        for (ItemStack item : inv) {
            if (item == null || item.getType().isAir()) continue;
            if (!anyItem && !Tag.ITEMS_PICKAXES.isTagged(item.getType())) continue;
            if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) continue;
            return true;
        }
        return false;
    }
}
