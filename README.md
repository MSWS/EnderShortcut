# EnderShortcut

Adds a _balanced_ QoL shortcut to the Ender Chest.

## Primary Command: `/enderchest`

Aliases: `/ec`, `/echest`

### Functionality

EnderShortcut adds some quality of life features with players' enderchests.

### Opening (/ec)

Players can easily open their enderchest if they have the required tools in their inventory.
This saves players from having to place down, interact with, break, and pick up their enderchest.

1. The player must have the `endershortcut.enderchest` permission.
2. The player must have an Ender Chest in their inventory. (unless they have the `endershortcut.bypass.chest`
   permission).
3. The player must have either a **silk touch pickaxe** or an **eye of ender** in their inventory (or
   have `endershortcut.bypass.item`).
4. Access is granted to the player's Ender Chest.
5. If an eye of ender was used, it is consumed.

### Shulker Boxes

Players can easily open the shulker boxes inside their enderchest by right-clicking on the boxes.
This saves players from having to place down, interact with, break, pick up their shulker boxes, and placing them back
in their enderchest. (Requires `endershortcut.shulker` permissions).

## Permissions

| Permission                     | Description                                                                                  |
|--------------------------------|----------------------------------------------------------------------------------------------|
| `endershortcut.enderchest`     | Grants basic access to the `/enderchest` command.                                            |
| `endershortcut.bypass.chest`   | Grants access to the `enderchest` command even if the player does not have an Ender Chest.   |
| `endershortcut.bypass.anysilk` | Grants access to the `enderchest` command if any item the player's inventory has silk touch. | 
| `endershortcut.bypass.item`    | Grants access to the `enderchest` command even if the player does not have a required item.  |
| `endershortcut.shulker`        | Grants access to open shulker boxes within their enderchest.                                 |   
