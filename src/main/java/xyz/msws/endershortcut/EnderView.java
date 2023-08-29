package xyz.msws.endershortcut;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.NamespacedKey;
import org.bukkit.Tag;
import org.bukkit.block.ShulkerBox;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Item;
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
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.Plugin;
import xyz.msws.endershortcut.listeners.ShulkerLinkListener;
import xyz.msws.endershortcut.utils.Lang;

import java.util.ArrayList;
import java.util.List;

/**
 * Represents a modified view of a player's enderchest.
 * Used to lore-ize shulker boxes and allow them to be opened by right clicking.
 */
public class EnderView implements Listener {
    private final Inventory baseInv, modifiedInv;
    private final NamespacedKey key;
    private final Plugin plugin;

    public EnderView(Plugin plugin, Inventory baseInv) {
        this.plugin = plugin;
        this.baseInv = baseInv;
        this.modifiedInv = Bukkit.createInventory(baseInv.getHolder(), InventoryType.ENDER_CHEST, "Ender Chest");
        this.key = new NamespacedKey(plugin, "endershortcut-tag");

        copyInventory(baseInv, modifiedInv, true);

        Bukkit.getPluginManager().registerEvents(this, plugin);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        Item item = event.getItemDrop();
        ItemStack drop = event.getItemDrop().getItemStack();
        removeTag(item.getItemStack());
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
        ItemMeta meta = item.getItemMeta();
        BlockStateMeta bsm = (BlockStateMeta) meta;
        if (!(bsm.getBlockState() instanceof ShulkerBox shulker)) return;
        player.openInventory(shulker.getInventory());
        new ShulkerLinkListener(plugin, item, player, event.getSlot());
    }

    private void addTag(ItemStack item) {
        if (isTagged(item) || !Tag.SHULKER_BOXES.isTagged(item.getType())) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        String toAdd = Lang.ITEM_CTRL_CLICK.getValue().toString();
        for (String line : toAdd.split("\n"))
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        meta.setLore(lore);

        pdc.set(this.key, PersistentDataType.BOOLEAN, true);

        item.setItemMeta(meta);
    }

    private void removeTag(ItemStack item) {
        if (!isTagged(item)) return;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return;
        List<String> lore = meta.getLore();
        if (lore == null) return;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String toRemove = Lang.ITEM_CTRL_CLICK.getValue().toString();
        for (String line : toRemove.split("\n"))
            lore.remove(ChatColor.translateAlternateColorCodes('&', line));
        meta.setLore(lore);

        pdc.remove(this.key);

        item.setItemMeta(meta);
    }

    private boolean isTagged(ItemStack item) {
        if (item == null) return false;
        if (!Tag.SHULKER_BOXES.isTagged(item.getType())) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.has(this.key, PersistentDataType.BOOLEAN);
    }

    private void copyInventory(Inventory from, Inventory to, boolean addTag) {
        for (int i = 0; i < from.getSize(); i++) {
            ItemStack item = from.getItem(i);
            if (item == null) {
                to.setItem(i, null);
                continue;
            }
            ItemStack clone = item.clone();
            if (addTag) addTag(clone);
            else removeTag(clone);
            to.setItem(i, clone);
        }
    }

    public Inventory getBaseInventory() {
        return baseInv;
    }

    public Inventory getModifiedInventory() {
        return modifiedInv;
    }
}
