# EnderShortcut

Adds a _balanced_ QoL shortcut to the Ender Chest.

## Primary Command: `/enderchest`

Aliases: `/ec`, `/echest`

### Functionality

EnderShortcut aims to allow players to simply use `/enderchest` instead of having to **place down** their chest,
interact with it, and then pick it back up. This is a _balanced_ shortcut because it requires the player to have an
Ender Chest in their inventory to use the command.

1. The player must have the `endershortcut.enderchest` permission.
2. The player must have an Ender Chest in their inventory. (unless they have the `endershortcut.enderchest.bypasschest`
   permission).
3. The player must have either a **silk touch pickaxe** or an **eye of ender** in their inventory.
4. Access is granted to the player's Ender Chest.
5. If an eye of ender was used, it is consumed.

## Permissions

| Permission                     | Description                                                                                  |
|--------------------------------|----------------------------------------------------------------------------------------------|
| `endershortcut.enderchest`     | Grants basic access to the `/enderchest` command.                                            |
| `endershortcut.bypass.chest`   | Grants access to the `enderchest` command even if the player does not have an Ender Chest.   |
| `endershortcut.bypass.anysilk` | Grants access to the `enderchest` command if any item the player's inventory has silk touch. | 
| `endershortcut.bypass.item`    | Grants access to the `enderchest` command even if the player does not have a required item.  |