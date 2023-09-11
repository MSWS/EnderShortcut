package xyz.msws.endershortcut.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import xyz.msws.endershortcut.EnderTagger;

public class PickupEnderChestListener extends EnderTagger implements Listener {

    public PickupEnderChestListener(Plugin plugin) {
        super(new NamespacedKey(plugin, "endershortcut-tag-enderchest"));
    }

    @Override
    public boolean canTag(Material material) {
        return material == Material.ENDER_CHEST;
    }

    @EventHandler
    public void onPickup(EntityPickupItemEvent event) {
        if (!(event.getEntity() instanceof HumanEntity player))
            return;
        player.sendMessage("Picked up");
        ItemStack item = event.getItem().getItemStack();
        if (!canTag(item))
            return;
        tag(item);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        if (!isTagged(item))
            return;
        untag(item);
    }
}
