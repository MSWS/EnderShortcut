package xyz.msws.endershortcut.listeners;

import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.entity.HumanEntity;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.EntityPickupItemEvent;
import org.bukkit.event.inventory.InventoryCreativeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.Plugin;
import xyz.msws.endershortcut.EnderTagger;

public class ObtainEnderChestListener extends EnderTagger implements Listener {

    public ObtainEnderChestListener(Plugin plugin) {
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
        tag(item);
    }

    @EventHandler
    public void onJoin(PlayerJoinEvent event) {
        Player player = event.getPlayer();
        for (ItemStack item : player.getInventory().getContents())
            tag(item);
    }

    @EventHandler
    public void onCreative(InventoryCreativeEvent event) {
        HumanEntity player = event.getWhoClicked();
        ItemStack item = event.getCurrentItem();
        if (item != null && !item.getType().isAir())
            tag(item);
        item = event.getCursor();
        if (!item.getType().isAir())
            tag(item);
    }

    @EventHandler
    public void onDrop(PlayerDropItemEvent event) {
        ItemStack item = event.getItemDrop().getItemStack();
        untag(item);
    }
}
