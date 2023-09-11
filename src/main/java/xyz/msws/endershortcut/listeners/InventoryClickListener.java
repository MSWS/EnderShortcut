package xyz.msws.endershortcut.listeners;

import org.bukkit.Material;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.inventory.InventoryView;
import org.bukkit.inventory.ItemStack;
import org.bukkit.scheduler.BukkitRunnable;
import xyz.msws.endershortcut.api.EnderShortcutPlugin;
import xyz.msws.endershortcut.utils.Perm;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class InventoryClickListener implements Listener {
    private final EnderShortcutPlugin plugin;
    private final Map<UUID, InventoryView> originalView = new HashMap<>();

    public InventoryClickListener(EnderShortcutPlugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (event.getClick() != ClickType.RIGHT) return;
        HumanEntity player = event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (item == null || item.getType().isAir() || item.getType() != Material.ENDER_CHEST) return;
        if (!plugin.getChestTagger().isTagged(item)) return;
        if (!player.hasPermission(Perm.EC_BACKPACK.getPermission())) return;
        if (event.getView().getType() == InventoryType.ENDER_CHEST) return; // Already viewing ender chest
        event.setCancelled(true);

        if (!plugin.allowEnderChest(player)) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                player.openInventory(player.getEnderChest());
                if (event.getView().getType() != InventoryType.CRAFTING)
                    // We can't force players to re-open their inventory
                    new BukkitRunnable() {
                        @Override
                        public void run() {
                            // Delay this to ensure the player's inventory is closed
                            originalView.put(player.getUniqueId(), event.getView());
                        }
                    }.runTaskLater(plugin, 1);
            }
        }.runTaskLater(plugin, 1);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        HumanEntity player = event.getPlayer();
        InventoryView original = originalView.get(player.getUniqueId());
        if (original == null) return;

        new BukkitRunnable() {
            @Override
            public void run() {
                if (player.getOpenInventory().getType() != InventoryType.CRAFTING)
                    // Player didn't actually close out of the inventory
                    // This can happen if they were in a shulker box from their inventory
                    return;
                if (event.getView().getType() == InventoryType.SHULKER_BOX)
                    // They were viewing a shulker box, so don't re-open the original as we go back to their ender chest
                    return;
                originalView.remove(player.getUniqueId());
                player.openInventory(original);
            }
        }.runTaskLater(plugin, 1);
    }
}
