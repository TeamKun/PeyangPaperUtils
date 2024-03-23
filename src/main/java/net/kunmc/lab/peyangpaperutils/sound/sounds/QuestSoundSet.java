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
    /**
     * クエストが開始したときに再生するべき音です。
     */
    QUEST_START(Sound.ENTITY_ENDER_DRAGON_AMBIENT, SoundCategory.BLOCKS, 0.5f, 1.0f),
    /**
     * クエストのクリテリアが達成されたときに再生するべき音です。
     */
    CRITERIA_COMPLETE(Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.AMBIENT, 0.5f, 0.5f),
    /**
     * クエストのクリテリアに失敗したときに再生するべき音です。
     */
    CRITERIA_FAILURE(Sound.ENTITY_PLAYER_HURT_ON_FIRE, SoundCategory.AMBIENT, 0.5f, 1.0f),


    ;
    private final Sound sound;
    private final SoundCategory source;
    private final float volume;
    private final float pitch;
}
