package xyz.msws.endershortcut.api;

import org.bukkit.command.CommandSender;
import org.bukkit.plugin.Plugin;
import xyz.msws.endershortcut.EnderTagger;

public interface EnderShortcutPlugin extends Plugin {

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
    boolean allowEnderChest(CommandSender sender, boolean removeEye, boolean print);

    default boolean allowEnderChest(CommandSender sender) {
        return allowEnderChest(sender, true, true);
    }

    EnderTagger getChestTagger();
}
