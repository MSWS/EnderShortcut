package xyz.msws.endershortcut.listeners;

import org.bukkit.Bukkit;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;

/**
 * Responsible for linking a shulker box item to the player's inventory when editing that item.
 * Automatically registers itself upon initialization and unregisters itself when the inventory is closed.
 */
public class ShulkerLinkListener implements Listener {
    private final ItemStack item;
    private final ShulkerBox shulker;
    private final HumanEntity player;

    // The original slot of the shulker box in the player's enderchest inventory.
    private final int slot;

    public ShulkerLinkListener(Plugin plugin, ItemStack item, HumanEntity player, int slot) {
        this.item = item;
        this.player = player;
        this.slot = slot;
        ItemMeta meta = item.getItemMeta();
        BlockStateMeta bsm = (BlockStateMeta) meta;
        if (!(bsm.getBlockState() instanceof ShulkerBox shulker))
            throw new IllegalArgumentException("Item is not a shulker box");
        this.shulker = shulker;

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        HumanEntity player = event.getPlayer();
        if (!player.equals(this.player)) return;
        InventoryCloseEvent.getHandlerList().unregister(this);
        shulker.getInventory().setContents(event.getInventory().getContents());

        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        BlockStateMeta bsm = (BlockStateMeta) meta;
        bsm.setBlockState(shulker);
        item.setItemMeta(bsm);

        // We've updated the item, so we need to re-insert it into the player's enderchest
        player.getEnderChest().setItem(slot, item);
        player.openInventory(player.getEnderChest());
    }
}
