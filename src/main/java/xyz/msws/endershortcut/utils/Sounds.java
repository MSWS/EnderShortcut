package xyz.msws.endershortcut.utils;

import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.configuration.ConfigurationSection;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.entity.Player;

public enum Sounds {
    USE_ENDER_EYE(Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 1, 2),
    OPEN_SHULKER_BOX(Sound.BLOCK_SHULKER_BOX_OPEN, SoundCategory.BLOCKS, 1, 1),
    CLOSE_SHULKER_BOX(Sound.BLOCK_SHULKER_BOX_CLOSE, SoundCategory.BLOCKS, 1, 1);

    private Sound bukkitSound;
    private SoundCategory category;
    private float volume, pitch;

    Sounds(Sound sound, SoundCategory category, float volume, float pitch) {
        String sound1 = sound.toString();
        this.bukkitSound = sound;
        this.category = category;
        this.volume = volume;
        this.pitch = pitch;
    }

    Sounds(String sound, SoundCategory category, float volume, float pitch) {
        this(Sound.valueOf(sound), category, volume, pitch);
    }

    Sounds(String sound, float volume, float pitch) {
        this(sound, SoundCategory.MASTER, volume, pitch);
    }

    Sounds(Sound sound, float volume, float pitch) {
        this(sound, SoundCategory.MASTER, volume, pitch);
    }

    Sounds(String sound, SoundCategory category) {
        this(sound, category, 1, 1);
    }

    Sounds(Sound sound, SoundCategory category) {
        this(sound, category, 1, 1);
    }

    Sounds(String sound) {
        this(sound, SoundCategory.MASTER);
    }

    Sounds(Sound sound) {
        this(sound, SoundCategory.MASTER);
    }

    public void playSound(Player player) {
        if (bukkitSound == null) return;
        player.playSound(player.getLocation(), bukkitSound, category, volume, pitch);
    }

    public void setVolume(float volume) {
        this.volume = volume;
    }

    public void setPitch(float pitch) {
        this.pitch = pitch;
    }

    public void setCategory(SoundCategory category) {
        this.category = category;
    }

    public void setSound(Sound sound) {
        this.bukkitSound = sound;
    }

    public void setSound(String sound) {
        this.bukkitSound = Sound.valueOf(sound);
    }

    /**
     * Overrides all current values from the specified config
     *
     * @param lang
     */
    public static void load(YamlConfiguration sounds) {
        ConfigurationSection soundsSection = sounds.getConfigurationSection("Sounds");
        if (soundsSection == null) soundsSection = sounds.createSection("Sounds");
        for (Sounds sound : Sounds.values()) {
            ConfigurationSection section = soundsSection.getConfigurationSection(sound.toString());
            if (section == null) section = soundsSection.createSection(sound.toString());

            sound.setSound(section.getString("Sound", sound.bukkitSound.toString()));
            sound.setCategory(SoundCategory.valueOf(section.getString("Category", sound.category.toString())));
            sound.setVolume((float) section.getDouble("Volume", sound.volume));
            sound.setPitch((float) section.getDouble("Pitch", sound.pitch));
        }
    }

    /**
     * Populates the specified config with all the values from this enum
     *
     * @param config The config to populate
     */
    public static void populate(ConfigurationSection config) {
        for (Sounds sound : Sounds.values()) {
            ConfigurationSection section = config.createSection(sound.toString());
            populate(section, sound);
        }
    }

    public static void populate(ConfigurationSection section, Sounds sound) {
        section.set("Sound", sound.bukkitSound.toString());
        section.set("Category", sound.category.toString());
        section.set("Volume", sound.volume);
        section.set("Pitch", sound.pitch);
    }
}
