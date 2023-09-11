package xyz.msws.endershortcut.listeners;

import org.bukkit.*;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.ClickType;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.event.inventory.InventoryCloseEvent;
import org.bukkit.event.inventory.InventoryType;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.BlockStateMeta;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.plugin.Plugin;
import xyz.msws.endershortcut.EnderTagger;

/**
 * Represents a modified view of a player's enderchest.
 * Used to lore-ize shulker boxes and allow them to be opened by right clicking.
 * Automatically registers listeners upon initialization and unregisters listeners when the inventory is closed.
 */
public class ViewEnderChestListener extends EnderTagger implements Listener {
    private final Inventory baseInv, modifiedInv;
    private final Plugin plugin;

    /**
     * Creates a new ViewEnderChestListener.
     *
     * @param plugin  The plugin to register listeners to.
     * @param baseInv The player's enderchest inventory.
     */
    public ViewEnderChestListener(Plugin plugin, Inventory baseInv) {
        super(new NamespacedKey(plugin, "endershortcut-tag-shulker"));
        this.plugin = plugin;
        this.baseInv = baseInv;
        this.modifiedInv = Bukkit.createInventory(baseInv.getHolder(), InventoryType.ENDER_CHEST, "Ender Chest");

        copyInventory(baseInv, modifiedInv, true);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        // Dropping an item directly from the enderchest
        Item item = event.getItemDrop();
        ItemStack drop = event.getItemDrop().getItemStack();
        untag(item.getItemStack());
    }

    @EventHandler
    public void onClose(InventoryCloseEvent event) {
        if (!event.getInventory().equals(this.modifiedInv)) return;
        copyInventory(modifiedInv, baseInv, false);
        copyInventory(event.getPlayer().getInventory(), event.getPlayer().getInventory(), false);
        PlayerDropItemEvent.getHandlerList().unregister(this);
        InventoryClickEvent.getHandlerList().unregister(this);
        InventoryCloseEvent.getHandlerList().unregister(this);
    }

    @EventHandler
    public void onClick(InventoryClickEvent event) {
        if (!event.getInventory().equals(this.modifiedInv)) return;
        if (event.getClick() != ClickType.RIGHT) return;
        Inventory clicked = event.getClickedInventory();
        HumanEntity player = event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (!isTagged(item)) return;
        assert item != null;
        ItemMeta meta = item.getItemMeta();
        BlockStateMeta bsm = (BlockStateMeta) meta;
        if (!(bsm.getBlockState() instanceof ShulkerBox shulker)) return;
        player.openInventory(shulker.getInventory());
        if (player instanceof Player hearer)
            hearer.playSound(hearer.getLocation(), Sound.BLOCK_SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 1, 1);
        new ShulkerLinkListener(plugin, item, player, event.getSlot());
    }

    @Override
    public boolean canTag(Material material) {
        return Tag.SHULKER_BOXES.isTagged(material);
    }

    /**
     * Utility function to copy an inventory to another. If addTag is true, it will add the PDC tag to all shulker boxes, otherwise it will remove it.
     *
     * @param from   The inventory to copy from.
     * @param to     The inventory to copy to.
     * @param addTag True to add the PDC tag, false to remove it.
     */
    private void copyInventory(Inventory from, Inventory to, boolean addTag) {
        for (int i = 0; i < from.getSize(); i++) {
            ItemStack item = from.getItem(i);
            if (item == null) {
                to.setItem(i, null);
                continue;
            }
            ItemStack clone = item.clone();
            if (addTag && canTag(clone)) tag(clone);
            else untag(clone);
            to.setItem(i, clone);
        }
    }

    /**
     * Returns the underlying base inventory that this ViewEnderChestListener is based on.
     *
     * @return The underlying base inventory.
     */
    public Inventory getBaseInventory() {
        return baseInv;
    }

    /**
     * Returns the resulting modified inventory that should be shown to the player.
     *
     * @return The resulting modified inventory.
     */
    public Inventory getModifiedInventory() {
        return modifiedInv;
    }
}
