package xyz.msws.endershortcut.utils;

public enum Perm {
    EC_COMMAND("endershortcut.enderchest"),
    BYPASS_CHEST("endershortcut.bypass.chest"),
    BYPASS_ANYSILK("endershortcut.bypass.anysilk"),
    BYPASS_ITEM("endershortcut.bypass.item"),
    EC_SHULKER("endershortcut.shulker"),
    EC_BACKPACK("endershortcut.backpack");
    private String perm;

    Perm(String perm) {
        this.perm = perm;
    }

    public String getPermission() {
        return perm;
    }
}
