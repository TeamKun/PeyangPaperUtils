package net.kunmc.lab.peyangpaperutils.lib.components;

import lombok.experimental.UtilityClass;
import org.bukkit.Bukkit;
import org.bukkit.inventory.ItemStack;

import java.lang.reflect.Method;

@UtilityClass
public class ItemSerializer
{
    // Lorg/bukkit/craftbukkit/<version>/inventory/CraftItemStack;
    private static final Class<?> cCraftItemStack = getCraftBukkitClass("inventory.CraftItemStack");
    // Lnet/minecraft/server/<version>/ItemStack;
    private static final Class<?> cNMSItemStack = getNMSClass("ItemStack");
    // Lnet/minecraft/server/<version>/NBTTagCompound;
    private static final Class<?> cNBTTagCompound = getNMSClass("NBTTagCompound");
    // (Lnet/minecraft/server/<version>/NBTTagCompound;)Lnet/minecraft/server/<version>/NBTTagCompound;
    private static final Method mSave = getMethod(cNMSItemStack, "save", cNBTTagCompound);
    // (Lorg/bukkit/inventory/ItemStack;)Lnet/minecraft/server/<version>/ItemStack;
    private static final Method mAsNMSCopy = getMethod(cCraftItemStack, "asNMSCopy", ItemStack.class);
    //

    public String serializeItemStack(ItemStack itemStack)
    {
        Object nmsItemStack = copyCraftItemStackToNMS(itemStack);
        Object nbtTagCompoundSaved = saveNMSItemStackNBT(nmsItemStack);
        return nbtTagCompoundSaved.toString();
    }

    private static Object saveNMSItemStackNBT(Object nmsItemStack)
    {
        try
        {
            Object nbtTagCompound = cNBTTagCompound.newInstance();
            return mSave.invoke(nmsItemStack, nbtTagCompound);
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Failed to save NMS ItemStack NBT", e);
        }
    }

    private static Method getMethod(Class<?> clazz, String methodName, Class<?>... parameterTypes)
    {
        try
        {
            Method method = clazz.getDeclaredMethod(methodName, parameterTypes);
            method.setAccessible(true);
            return method;
        }
        catch (NoSuchMethodException e)
        {
            throw new IllegalStateException("Failed to get method", e);
        }
    }

    private static Object copyCraftItemStackToNMS(ItemStack itemStack)
    {
        try
        {
            return mAsNMSCopy.invoke(null, itemStack);
        }
        catch (Exception e)
        {
            throw new IllegalStateException("Failed to copy CraftItemStack to NMS", e);
        }
    }

    private static Class<?> getNMSClass(String className)
    {
        try
        {
            return Class.forName("net.minecraft.server." + getServerNMSVersion() + "." + className);
        }
        catch (ClassNotFoundException e)
        {
            throw new IllegalStateException("Failed to get NMS class", e);
        }
    }

    private static Class<?> getCraftBukkitClass(String className)
    {
        try
        {
            if (isNewerThan_v1_20_5())
                return Class.forName("org.bukkit.craftbukkit." + className);
            else
                return Class.forName("org.bukkit.craftbukkit." + getServerNMSVersion() + "." + className);
        }
        catch (ClassNotFoundException e)
        {
            throw new IllegalStateException("Failed to get CraftBukkit class", e);
        }
    }

    private static boolean isNewerThan_v1_20_5()
    {
        try
        {
            Class.forName("org.bukkit.craftbukkit.CraftServer");
            return true;
        }
        catch (ClassNotFoundException e)
        {
            return false;
        }
    }

    private static String getServerNMSVersion()
    {
        return Bukkit.getServer().getClass().getPackage().getName().substring(23);
    }
}
