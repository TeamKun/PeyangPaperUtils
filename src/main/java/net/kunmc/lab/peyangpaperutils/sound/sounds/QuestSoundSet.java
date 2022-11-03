package net.kunmc.lab.peyangpaperutils.sound.sounds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kunmc.lab.peyangpaperutils.sound.SoundSet;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

/**
 * クエスト系の音を列挙型として表します。
 */
@Getter
@AllArgsConstructor
public enum QuestSoundSet implements SoundSet
{
    QUEST_START(Sound.BLOCK_BELL_USE, SoundCategory.BLOCKS, 0.5f, 1.0f),
    CRITERIA_COMPLETE(Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.AMBIENT, 0.5f, 0.5f),
    CRITERIA_FAILURE(Sound.ENTITY_PLAYER_HURT_ON_FIRE, SoundCategory.AMBIENT, 0.5f, 1.0f),
    ;
    private final Sound sound;
    private final SoundCategory source;
    private final float volume;
    private final float pitch;
}
