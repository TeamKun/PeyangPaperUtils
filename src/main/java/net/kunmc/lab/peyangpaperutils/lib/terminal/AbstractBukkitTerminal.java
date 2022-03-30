package net.kunmc.lab.peyangpaperutils.lib.terminal;

import lombok.Getter;
import net.kyori.adventure.audience.Audience;
import net.kyori.adventure.text.Component;
import org.bukkit.ChatColor;
import org.jetbrains.annotations.NotNull;

abstract class AbstractBukkitTerminal implements Terminal
{
    @Getter
    private final Audience audience;
    @Getter
    private final Input input;

    public AbstractBukkitTerminal(Audience audience)
    {
        this.audience = audience;
        this.input = new Input(this);
    }

    @Override
    public void info(@NotNull String message, Object... args)
    {
        writeLine(String.format(ChatColor.BLUE + "I: " + message, args));
    }

    @Override
    public void error(@NotNull String message, Object... args)
    {
        writeLine(String.format(ChatColor.RED + "E: " + message, args));
    }

    @Override
    public void success(@NotNull String message, Object... args)
    {
        writeLine(String.format(ChatColor.GREEN + "S: " + message, args));
    }

    @Override
    public void warn(@NotNull String message, Object... args)
    {
        writeLine(String.format(ChatColor.YELLOW + "W: " + message, args));
    }

    @Override
    public void writeLine(@NotNull String message)
    {
        write(Component.text(message));
    }

    @Override
    public void write(@NotNull Component component)
    {
        audience.sendMessage(component);
    }
}
