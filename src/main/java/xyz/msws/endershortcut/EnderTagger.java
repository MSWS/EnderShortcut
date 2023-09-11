package xyz.msws.endershortcut;

import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.persistence.PersistentDataContainer;
import org.bukkit.persistence.PersistentDataType;
import xyz.msws.endershortcut.utils.Lang;

import java.util.ArrayList;
import java.util.List;

public abstract class EnderTagger {
    protected final NamespacedKey key;

    protected EnderTagger(NamespacedKey key) {
        this.key = key;
    }

    /**
     * Returns true if the tagger should be able to tag the given material.
     *
     * @param material The material to check.
     * @return True if the tagger can tag the material.
     */
    public abstract boolean canTag(Material material);

    /**
     * @see #canTag(Material)
     */
    public boolean canTag(ItemStack item) {
        if (item == null)
            return false;
        return canTag(item.getType());
    }

    /**
     * Returns true if the item has the PDC tag.
     *
     * @param item The item to check.
     * @return True if the item has the PDC tag.
     */
    public boolean isTagged(ItemStack item) {
        if (item == null) return false;
        if (!canTag(item)) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        return pdc.has(this.key, PersistentDataType.BOOLEAN);
    }

    /**
     * Tags the item with the PDC tag.
     *
     * @param item The item to tag.
     * @return True if the item was tagged. False if the item was already tagged or the tagger cannot tag the item.
     */
    public boolean tag(ItemStack item) {
        if (isTagged(item) || !canTag(item)) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();

        List<String> lore = meta.getLore();
        if (lore == null) lore = new ArrayList<>();
        String toAdd = Lang.ITEM_RIGHT_CLICK.getValue().toString();
        for (String line : toAdd.split("\n"))
            lore.add(ChatColor.translateAlternateColorCodes('&', line));
        meta.setLore(lore);

        pdc.set(this.key, PersistentDataType.BOOLEAN, true);

        item.setItemMeta(meta);
        return true;
    }

    /**
     * Removes the PDC tag and lore associated with the item.
     * If no tag is present, nothing happens.
     *
     * @param item The item to remove the tag from.
     * @return True if the item was untagged. False if the item was not tagged or the tagger cannot tag the item.
     */
    public boolean untag(ItemStack item) {
        if (!isTagged(item)) return false;
        ItemMeta meta = item.getItemMeta();
        if (meta == null) return false;
        List<String> lore = meta.getLore();
        if (lore == null) return false;
        PersistentDataContainer pdc = meta.getPersistentDataContainer();
        String toRemove = Lang.ITEM_RIGHT_CLICK.getValue().toString();
        for (String line : toRemove.split("\n"))
            lore.remove(ChatColor.translateAlternateColorCodes('&', line));
        meta.setLore(lore);

        pdc.remove(this.key);

        item.setItemMeta(meta);
        return true;
    }
}
