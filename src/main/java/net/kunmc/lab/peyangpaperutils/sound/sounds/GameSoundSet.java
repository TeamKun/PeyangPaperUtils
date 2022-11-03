package net.kunmc.lab.peyangpaperutils.sound.sounds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kunmc.lab.peyangpaperutils.sound.SoundSet;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

/**
 * ゲームで使用される音を列挙型として表します。
 */
@Getter
@AllArgsConstructor
public enum GameSoundSet implements SoundSet
{
    START(Sound.ENTITY_ENDER_DRAGON_AMBIENT, SoundCategory.AMBIENT, 0.5f, 1.0f),
    END(Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.AMBIENT, 0.5f, 2.0f),

    ;
    private final Sound sound;
    private final SoundCategory source;
    private final float volume;
    private final float pitch;
}
