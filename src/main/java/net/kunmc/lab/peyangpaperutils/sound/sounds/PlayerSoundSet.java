package net.kunmc.lab.peyangpaperutils.sound.sounds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kunmc.lab.peyangpaperutils.sound.SoundSet;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

/**
 * プレイヤー系の音を列挙型として表します。
 */
@Getter
@AllArgsConstructor
public enum PlayerSoundSet implements SoundSet
{
    /**
     * プレイヤがリスポーンしたときに再生するべき音です。
     */
    RESPAWN(Sound.BLOCK_ENCHANTMENT_TABLE_USE, SoundCategory.PLAYERS, 0.5f, 0.6f),
    /**
     * プレイヤが死亡したときに再生するべき音です。
     */
    DEATH(Sound.ENTITY_PLAYER_HURT_ON_FIRE, SoundCategory.AMBIENT, 0.5f, 1.0f),
    /**
     * プレイヤがプレイヤを殺害したときに再生するべき音です。
     */
    KILL_PLAYER(Sound.ENTITY_ARROW_HIT_PLAYER, SoundCategory.AMBIENT, 1.0f, 1.0f),
    ;
    private final Sound sound;
    private final SoundCategory source;
    private final float volume;
    private final float pitch;
}
