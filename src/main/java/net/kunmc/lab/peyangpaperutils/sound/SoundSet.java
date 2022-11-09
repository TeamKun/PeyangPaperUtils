package net.kunmc.lab.peyangpaperutils.sound;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.SoundCategory;
import org.bukkit.World;
import org.bukkit.entity.Player;
import org.jetbrains.annotations.NotNull;

/**
 * 音を列挙型として表すためのインターフェースです。
 */
public interface SoundSet
{
    /**
     * 音を名前から取得します。
     *
     * @param name 音の名前
     * @return 音
     */
    static @NotNull Sound getByName(@NotNull String name)
    {
        return Sound.valueOf(name.toUpperCase().replace('.', '_'));
    }

    /**
     * 音の種類を返します。
     *
     * @return 音の種類
     */
    @NotNull
    Sound getSound();

    /**
     * 音のソースを返します。
     *
     * @return 音のソース
     */
    @NotNull
    SoundCategory getSource();

    /**
     * 音の音量を返します。
     *
     * @return 音の音量
     */
    float getVolume();

    /**
     * 音のピッチを返します。
     *
     * @return 音のピッチ
     */
    float getPitch();

    /**
     * 音を再生します。
     *
     * @param player   再生するプレイヤー
     * @param location 再生する場所
     * @param playArea 再生する範囲
     * @param volume   音量
     * @param pitch    ピッチ
     */
    default void play(@NotNull Player player, @NotNull Location location, @NotNull PlayArea playArea, float volume, float pitch)
    {
        Sound sound = this.getSound();
        SoundCategory source = this.getSource();

        if (playArea == PlayArea.SELF)
            player.playSound(location, sound, source, volume, pitch);

        this.play(location, playArea, volume, pitch);
    }

    /**
     * 音を再生します。
     *
     * @param player   再生するプレイヤー
     * @param playArea 再生する範囲
     * @param volume   音量
     * @param pitch    ピッチ
     */
    default void play(@NotNull Player player, @NotNull PlayArea playArea, float volume, float pitch)
    {
        this.play(player, player.getLocation(), playArea, volume, pitch);
    }

    /**
     * 音を再生します。
     *
     * @param player   再生するプレイヤー
     * @param playArea 再生する範囲
     */
    default void play(@NotNull Player player, @NotNull PlayArea playArea)
    {
        this.play(player, playArea, this.getVolume(), this.getPitch());
    }

    /**
     * 音を再生します。
     *
     * @param player 再生するプレイヤー
     */
    default void play(@NotNull Player player)
    {
        this.play(player, player.getLocation(), PlayArea.SELF, this.getVolume(), this.getPitch());
    }

    /**
     * 範囲を指定して音を再生します。
     *
     * @param location 再生する場所
     * @param range    再生する範囲
     * @param volume   音量
     * @param pitch    ピッチ
     */
    default void play(Location location, int range, float volume, float pitch)
    {
        location.getWorld().getNearbyEntitiesByType(Player.class, location, range).forEach(player ->
                player.playSound(location, this.getSound(), this.getSource(), volume, pitch)
        );
    }

    /**
     * ワールド全体で音を再生します。
     *
     * @param world  再生するワールド
     * @param volume 音量
     * @param pitch  ピッチ
     */
    default void play(World world, float volume, float pitch)
    {
        world.getPlayers().forEach(player ->
                player.playSound(player.getLocation(), this.getSound(), this.getSource(), volume, pitch)
        );
    }

    /**
     * 音を再生します。
     *
     * @param location 再生する場所
     * @param playArea 再生する範囲
     * @param volume   音量
     * @param pitch    ピッチ
     */
    default void play(@NotNull Location location, @NotNull PlayArea playArea, float volume, float pitch)
    {
        switch (playArea)
        {
            case NEARBY_5:
                this.play(location, 5, volume, pitch);
                break;
            case NEARBY_10:
                this.play(location, 10, volume, pitch);
                break;
            case NEARBY_20:
                this.play(location, 20, volume, pitch);
                break;
            case SERVER_ALL:
                this.play(location.getWorld(), volume, pitch);
                break;
            case SELF:
                throw new IllegalArgumentException("PlayArea.SELF is not supported.");
        }
    }

    /**
     * 音を再生します。
     *
     * @param location 再生する場所
     * @param playArea 再生する範囲
     */
    default void play(@NotNull Location location, @NotNull PlayArea playArea)
    {
        this.play(location, playArea, this.getVolume(), this.getPitch());
    }

    /**
     * 音を再生します。
     *
     * @param location 再生する場所
     */
    default void play(@NotNull Location location)
    {
        this.play(location, PlayArea.SERVER_ALL, this.getVolume(), this.getPitch());
    }

    /**
     * 音を再生します。
     *
     * @param location 再生する場所
     * @param volume   音量
     * @param pitch    ピッチ
     */
    default void play(@NotNull Location location, float volume, float pitch)
    {
        this.play(location, PlayArea.SERVER_ALL, volume, pitch);
    }

    /**
     * 音を再生します。
     *
     * @param location 再生する場所
     * @param pitch    音量
     */
    default void play(@NotNull Location location, float pitch)
    {
        this.play(location, PlayArea.SERVER_ALL, this.getVolume(), pitch);
    }

    /**
     * 音の再生を停止します。
     *
     * @param player 停止するプレイヤー
     */
    default void stop(@NotNull Player player)
    {
        player.stopSound(this.getSound());
    }

    /**
     * 音の再生を停止します。
     */
    default void stop()
    {
        Bukkit.getOnlinePlayers().forEach(this::stop);
    }
}
