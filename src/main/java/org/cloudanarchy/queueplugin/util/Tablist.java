package org.cloudanarchy.queueplugin.util;


import org.cloudanarchy.queueplugin.QueuePlugin;
import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_12_R1.ChatComponentText;
import net.minecraft.server.v1_12_R1.PacketPlayOutPlayerListHeaderFooter;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.craftbukkit.v1_12_R1.entity.CraftPlayer;
import org.bukkit.entity.Player;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.Iterator;
import java.util.List;

public class Tablist implements Runnable {
    QueuePlugin main;

    public Tablist(QueuePlugin main) {
        this.main = main;
    }

    public void run() {
        PacketPlayOutPlayerListHeaderFooter packet = new PacketPlayOutPlayerListHeaderFooter();

        try {
            Field a = packet.getClass().getDeclaredField("a");
            a.setAccessible(true);
            Field b = packet.getClass().getDeclaredField("b");
            b.setAccessible(true);

            if (Bukkit.getOnlinePlayers().size() == 0) {
                return;
            }

            for (Player player : Bukkit.getOnlinePlayers()) {
                StringBuilder head = new StringBuilder();
                StringBuilder footer = new StringBuilder();
                List<String> headerlist = main.getConfig().getStringList("tablist.header");
                List<String> footerlist = main.getConfig().getStringList("tablist.footer");

                for (int i = 0; i < headerlist.size(); i++) {
                    if (i == (headerlist.size() - 1)) {
                        head.append(headerlist.get(i));
                    } else {
                        head.append(headerlist.get(i)).append("\n");
                    }
                }

                for (int i = 0; i < footerlist.size(); i++) {
                    if (i == (footerlist.size() - 1)) {
                        footer.append(footerlist.get(i));
                    } else {
                        footer.append(footerlist.get(i)).append("\n");
                    }
                }

                a.set(packet, new ChatComponentText(QueuePlugin.parseText(player, head.toString())));

                b.set(packet, new ChatComponentText(QueuePlugin.parseText(player, footer.toString())));

                ((CraftPlayer) player).getHandle().playerConnection.sendPacket(packet);
            }
        } catch (IllegalAccessException | NoSuchMethodException | NoSuchFieldException | InvocationTargetException e) {
            e.printStackTrace();
        }
    }
}