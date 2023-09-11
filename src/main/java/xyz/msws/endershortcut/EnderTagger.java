package xyz.msws.endershortcut;

import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;

public interface EnderTagger {
    /**
     * Returns true if the tagger should be able to tag the given material.
     *
     * @param material The material to check.
     * @return True if the tagger can tag the material.
     */
    boolean canTag(Material material);

    /**
     * @see #canTag(Material)
     */
    default boolean canTag(ItemStack item) {
        return canTag(item.getType());
    }

    /**
     * Returns true if the item has the PDC tag.
     *
     * @param item The item to check.
     * @return True if the item has the PDC tag.
     */
    boolean isTagged(ItemStack item);

    /**
     * Tags the item with the PDC tag.
     *
     * @param item The item to tag.
     * @return True if the item was tagged. False if the item was already tagged or the tagger cannot tag the item.
     */
    boolean tag(ItemStack item);

    /**
     * Removes the PDC tag and lore associated with the item.
     * If no tag is present, nothing happens.
     *
     * @param item The item to remove the tag from.
     * @return True if the item was untagged. False if the item was not tagged or the tagger cannot tag the item.
     */
    boolean untag(ItemStack item);
}
