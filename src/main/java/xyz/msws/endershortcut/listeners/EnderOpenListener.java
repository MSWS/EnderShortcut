package xyz.msws.endershortcut.listeners;

import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryOpenEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.plugin.Plugin;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.msws.endershortcut.EnderView;

import java.util.HashSet;
import java.util.UUID;

public class EnderOpenListener implements Listener {
    private final Plugin plugin;
    private final HashSet<UUID> openingOriginalInventory = new HashSet<>();

    public EnderOpenListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler(ignoreCancelled = true)
    public void onOpen(InventoryOpenEvent event) {
        HumanEntity player = event.getPlayer();
        if (event.getView().getType() != InventoryType.ENDER_CHEST) return;
        if (openingOriginalInventory.contains(player.getUniqueId())) return;
        EnderView view = new EnderView(plugin, player.getEnderChest());
        openingOriginalInventory.add(player.getUniqueId());
        new BukkitRunnable() {
            @Override
            public void run() {
                player.openInventory(view.getModifiedInventory());
                openingOriginalInventory.remove(player.getUniqueId());
            }
        }.runTaskLater(plugin, 1);
    }

}
