package xyz.msws.endershortcut.commands;

import org.bukkit.GameMode;
import org.bukkit.Material;
import org.bukkit.Tag;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.enchantments.Enchantment;
import org.bukkit.entity.Player;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;
import xyz.msws.endershortcut.utils.Lang;

public class EnderChestCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player player)) {
            Lang.MUST_BE_PLAYER.send(sender);
            return true;
        }
        if (!allowEnderChest(sender)) return true;
        player.openInventory(player.getEnderChest());
        return true;
    }

    private boolean allowEnderChest(CommandSender sender) {
        return allowEnderChest(sender, true, true);
    }

    /**
     * Checks if the sender can open their ender chest
     * Requires they have an ender chest in their inventory
     * and a means to get it back in their inventory
     * (either an ender eye or silk touch pickaxe)
     *
     * @param sender    The sender to check
     * @param removeEye If true, will remove an ender eye from the sender's inventory if used to open
     * @param print     If true, will print feedback to the sender
     * @return True if the sender can open their ender chest
     */
    private boolean allowEnderChest(CommandSender sender, boolean removeEye, boolean print) {
        if (!(sender instanceof Player player)) {
            Lang.MUST_BE_PLAYER.send(sender);
            return false;
        }

        if (player.getGameMode() == GameMode.CREATIVE) return true;

        if (!player.getInventory().contains(Material.ENDER_CHEST) && !sender.hasPermission("endershortcut.bypass.chest")) {
            Lang.MUST_HAVE_ENDERCHEST.send(player);
            return false;
        }

        if (hasSilkTouch(player.getInventory(), sender.hasPermission("endershortcut.bypass.anysilk"))) return true;

        if (sender.hasPermission("endershortcut.bypass.item")) return true;
        if (!player.getInventory().contains(Material.ENDER_EYE)) {
            if (print) Lang.MUST_HAVE_EITHER.send(player);
            return false;
        }
        if (removeEye) {
            player.getInventory().removeItem(new ItemStack(Material.ENDER_EYE, 1));
            // If we didn't remove an eye, don't show that we consumed one
            if (print) Lang.CONSUMED_EYE.send(player);
        }

        return true;
    }

    /**
     * Checks if the specified inventory has a silk touch pickaxe
     * See {@link #hasSilkTouch(Inventory, boolean)}
     *
     * @param inv The inventory to check
     * @return True if the inventory has a silk touch pickaxe
     */
    private boolean hasSilkTouch(Inventory inv) {
        return hasSilkTouch(inv, false);
    }

    /**
     * Checks if the specified inventory has a silk touch pickaxe
     * If anyItem is true, it will check for any item with silk touch
     *
     * @param inv     The inventory to check
     * @param anyItem If true, will check for any item with silk touch
     * @return True if the inventory has a silk touch item
     */
    private boolean hasSilkTouch(Inventory inv, boolean anyItem) {
        boolean hasSilkTouch = false;
        for (ItemStack item : inv) {
            if (item == null || item.getType().isAir()) continue;
            if (!anyItem && !Tag.ITEMS_PICKAXES.isTagged(item.getType())) continue;
            if (!item.getEnchantments().containsKey(Enchantment.SILK_TOUCH)) continue;
            return true;
        }
        return false;
    }
}
