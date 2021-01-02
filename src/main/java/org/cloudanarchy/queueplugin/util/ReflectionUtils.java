package org.cloudanarchy.queueplugin.util;

import java.lang.reflect.Field;
import java.lang.reflect.Method;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

public class ReflectionUtils {
    ReflectionUtils() {
    }

    public static Object getHandle(Object obj) throws Exception {
        Method method = obj.getClass().getDeclaredMethod("getHandle", new Class[0]);
        if (!method.isAccessible()) {
            method.setAccessible(true);
        }
        return method.invoke(obj, new Object[0]);
    }

    public static Object invokeMethod(Object obj, String name) throws Exception {
        return ReflectionUtils.invokeMethod(obj, name, true);
    }

    public static Object invokeMethod(Object obj, String name, boolean declared) throws Exception {
        Method met = declared ? obj.getClass().getDeclaredMethod(name, new Class[0]) : obj.getClass().getMethod(name, new Class[0]);
        return met.invoke(obj, new Object[0]);
    }

    public static Object getNMSPlayer(Player p) throws Throwable {
        return ReflectionUtils.getHandle((Object)p);
    }

    public static Class<?> getNMSClass(String name) throws ClassNotFoundException {
        return Class.forName("net.minecraft.server." + ReflectionUtils.getPackageVersion() + "." + name);
    }

    public static Class<?> getCraftClass(String className) throws ClassNotFoundException {
        return Class.forName("org.bukkit.craftbukkit." + ReflectionUtils.getPackageVersion() + "." + className);
    }

    public static Field getField(Object clazz, String name) throws Exception {
        return ReflectionUtils.getField(clazz, name, true);
    }

    public static Field getField(Object clazz, String name, boolean declared) throws Exception {
        return ReflectionUtils.getField(clazz.getClass(), name, declared);
    }

    public static Field getField(Class<?> clazz, String name) throws Exception {
        return ReflectionUtils.getField(clazz, name, true);
    }

    public static Field getField(Class<?> clazz, String name, boolean declared) throws Exception {
        Field field;
        Field field2 = field = declared ? clazz.getDeclaredField(name) : clazz.getField(name);
        if (!field.isAccessible()) {
            field.setAccessible(true);
        }
        return field;
    }

    public static void modifyFinalField(Field field, Object target, Object newValue) throws Exception {
        field.setAccessible(true);
        ReflectionUtils.getField(Field.class, "modifiers").setInt(field, field.getModifiers() & 0xFFFFFFEF);
        field.set(target, newValue);
    }

    public static Object getFieldObject(Object object, Field field) throws Exception {
        return field.get(object);
    }

    public static void setField(Object object, String fieldName, Object fieldValue) throws Exception {
        ReflectionUtils.getField(object, fieldName).set(object, fieldValue);
    }

    public static void sendPacket(Player player, Object packet) {
        try {
            Object playerHandle = ReflectionUtils.getNMSPlayer(player);
            Object playerConnection = ReflectionUtils.getFieldObject(playerHandle, ReflectionUtils.getField(playerHandle, "playerConnection"));
            playerConnection.getClass().getDeclaredMethod("sendPacket", ReflectionUtils.getNMSClass("Packet")).invoke(playerConnection, packet);
        }
        catch (Throwable t) {
            t.printStackTrace();
        }
    }

    public static String getPackageVersion() {
        return Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
    }
}
