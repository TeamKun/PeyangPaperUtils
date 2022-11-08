package net.kunmc.lab.peyangpaperutils.sound.sounds;

import lombok.AllArgsConstructor;
import lombok.Getter;
import net.kunmc.lab.peyangpaperutils.sound.SoundSet;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;

/**
 * GUIで使用される音を列挙型として表します。
 */
@Getter
@AllArgsConstructor
public enum GUISoundSet implements SoundSet
{
    /**
     * GUIを開いたときに再生するべき音です。
     */
    OPEN(Sound.BLOCK_CHEST_OPEN, SoundCategory.BLOCKS, 0.5f, 1.0f),
    /**
     * GUIの有効なボタンをクリックしたときに再生するべき音です。
     */
    CLICK(Sound.BLOCK_STONE_BUTTON_CLICK_ON, SoundCategory.BLOCKS, 0.5f, 1.0f),
    /**
     * GUIの無効なボタンをクリックしたときに再生するべき音です。
     */
    CLICK_INVALID(Sound.ENTITY_ENDERMAN_TELEPORT, SoundCategory.BLOCKS, 0.5f, 0.5f),
    /**
     * GUIで何かを購入したときに再生するべき音です。
     */
    PURCHASE(Sound.ENTITY_PLAYER_LEVELUP, SoundCategory.AMBIENT, 0.5f, 2.0f),
    ;
    private final Sound sound;
    private final SoundCategory source;
    private final float volume;
    private final float pitch;
}
