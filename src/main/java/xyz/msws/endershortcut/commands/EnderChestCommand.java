package xyz.msws.endershortcut.commands;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import xyz.msws.endershortcut.api.EnderShortcutPlugin;
import xyz.msws.endershortcut.utils.Lang;

public class EnderChestCommand implements CommandExecutor {

    private final EnderShortcutPlugin plugin;

    public EnderChestCommand(EnderShortcutPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] strings) {
        if (!(sender instanceof Player player)) {
            Lang.MUST_BE_PLAYER.send(sender);
            return true;
        }
        if (!plugin.allowEnderChest(sender)) return true;
        player.openInventory(player.getEnderChest());
        return true;
    }
}
