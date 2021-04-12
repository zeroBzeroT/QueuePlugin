package org.cloudanarchy.queueplugin;


import me.clip.placeholderapi.PlaceholderAPI;
import net.minecraft.server.v1_12_R1.Packet;
import org.bukkit.*;
import org.bukkit.entity.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.EventPriority;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.event.server.TabCompleteEvent;
import org.bukkit.event.world.ChunkLoadEvent;
import org.bukkit.plugin.Plugin;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;
import org.cloudanarchy.queueplugin.util.ReflectionUtils;
import org.cloudanarchy.queueplugin.util.TabUtil;
import org.cloudanarchy.queueplugin.util.Tablist;

import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.util.HashMap;
import java.util.UUID;
import java.util.logging.Logger;

public final class QueuePlugin extends JavaPlugin implements Listener{

    public static boolean haspapi = false;


    public static long starttime;

    @Override
    public void onEnable() {
        // Plugin startup logic
        Bukkit.getScheduler().runTaskTimer(this, new Tablist(this), 0, 10L);

        if (Bukkit.getPluginManager().getPlugin("PlaceholderAPI") != null) {
            haspapi = true;
        }
        starttime = System.currentTimeMillis();
        getServer().getPluginManager().registerEvents(this, this);
    }



    @EventHandler(priority = EventPriority.HIGH)
    public void onPlayerLeave(PlayerQuitEvent playerQuitEvent) {
            playerQuitEvent.setQuitMessage("");
    }



    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent e) {

        e.getPlayer().teleport(new Location(getServer().getWorld("world_nether"), 0, 140, 0));
        e.getPlayer().setAllowFlight(true);
        e.getPlayer().setFlying(true);
        e.getPlayer().setGameMode(GameMode.SPECTATOR);
        for(Player player : Bukkit.getOnlinePlayers()){
            vanish(player);
        }
        e.setJoinMessage("");
    }
    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent e){
        e.setCancelled(true);
    }

    @EventHandler
    public void onPlayerMove(PlayerMoveEvent e){
        e.setCancelled(true);
    }


    @EventHandler
    public void onTabComplete(TabCompleteEvent e){
        e.setCancelled(true);
    }


    @EventHandler
    public void onAchievement(PlayerAchievementAwardedEvent e){ e.setCancelled(true); }



    @EventHandler
    public void onChatSend(AsyncPlayerChatEvent e){

        e.setCancelled(true);

    }

    @Override
    public void onDisable() {

        this.saveDefaultConfig();
        // Plugin shutdown logic
    }

    public static String parseText(Player player, String text) throws NoSuchFieldException, NoSuchMethodException, IllegalAccessException, InvocationTargetException {
        String newtext = text;
        Object entityPlayer = player.getClass().getMethod("getHandle").invoke(player);
        int ping = (Integer) entityPlayer.getClass().getField("ping").get(entityPlayer);
        String playerPing;

        if (ping > 200) {
            playerPing = "§6" + ping;
        }
        if (ping > 300) {
            playerPing = "§c" + ping;
        } else playerPing = "§a" + ping;

        if (haspapi) {
            newtext = PlaceholderAPI.setPlaceholders(player, text);
        }

        newtext = ChatColor.translateAlternateColorCodes('&', newtext);

        newtext = newtext
             .replaceAll("<>", Integer.toString(Bukkit.getServer().getOnlinePlayers().size()));

        return newtext;
    }


    public static double getTPS() {
        double tps = 0.0;
        try {
            Object mc = ReflectionUtils.invokeMethod((Object) Bukkit.getServer(), "getServer", false);
            Field rec = ReflectionUtils.getField(mc, "recentTps", false);
            double[] recentTps = (double[]) rec.get(mc);
            tps = recentTps[0];
        } catch (Throwable throwable) {
            // empty catch block
        }
        return tps;
    }

    private void vanish(Player player) {
        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            if (!onlinePlayer.equals(player)) {
                onlinePlayer.hidePlayer(this, player);
            }
        }
    }

    public void unvanish(Player player) {
        for (Player onlinePlayer : getServer().getOnlinePlayers()) {
            if (!onlinePlayer.equals(player)) {
                onlinePlayer.showPlayer(this, player);
            }
        }
    }


}
