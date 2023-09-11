package xyz.msws.endershortcut.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.msws.endershortcut.utils.Perm;

import java.util.HashSet;
import java.util.UUID;

/**
 * Responsible for listening for when a player opens their enderchest.
 * Conveniently, this catches when player#openInventory is called as well.
 */
public class EnderOpenListener implements Listener {
    private final Plugin plugin;

    // Used to prevent infinite recursion
    private final HashSet<UUID> openingOriginalInventory = new HashSet<>();

    public EnderOpenListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onOpen(InventoryOpenEvent event) {
        HumanEntity player = event.getPlayer();
        if (event.getView().getType() != InventoryType.ENDER_CHEST) return;
        if (!player.hasPermission(Perm.EC_SHULKER.getPermission()))
            return; // If the player doesn't have permission to open shulkers, we don't need to do anything
        if (openingOriginalInventory.contains(player.getUniqueId())) return;
        ViewEnderChestListener view = new ViewEnderChestListener(plugin, player.getEnderChest());
        openingOriginalInventory.add(player.getUniqueId());
        new BukkitRunnable() { // Delay by 1 tick cause Minecraft is minecraft
            @Override
            public void run() {
                player.openInventory(view.getModifiedInventory());
                openingOriginalInventory.remove(player.getUniqueId());
            }
        }.runTaskLater(plugin, 1);
    }

}
