package org.cloudanarchy.queueplugin;


import com.comphenix.protocol.ProtocolLibrary;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerAdvancementDoneEvent;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.event.player.PlayerRespawnEvent;
import org.bukkit.plugin.java.JavaPlugin;

public final class QueuePlugin extends JavaPlugin implements Listener {

    public static final int X = 420;
    public static final int Z = 69;
    public static final int Y = 1337;

    @Override
    public void onEnable() {
        getServer().setDefaultGameMode(GameMode.SPECTATOR);
        getServer().setSpawnRadius(32);
        getServer().getServerTickManager().setTickRate(1);
        getServer().getPluginManager().registerEvents(this, this);
        EventCanceler eventCanceler = new EventCanceler(this);
        getServer().getPluginManager().registerEvents(eventCanceler, this);
        ProtocolLibrary.getProtocolManager().addPacketListener(eventCanceler);

        Location location = new Location(getServer().getWorlds().get(0), X, Y, Z);
        getServer().getScheduler().runTaskTimer(this, () -> {
            for (Player player : getServer().getOnlinePlayers()) {
                player.teleport(location);
                player.setAllowFlight(true);
                player.setFlying(true);
                player.setInvisible(true);
                player.setInvulnerable(true);
                player.setGameMode(GameMode.SPECTATOR);
                for (Player p : getServer().getOnlinePlayers()) {
                    if (player.equals(p)) continue;
                    player.hidePlayer(this, p);
                    p.hidePlayer(this, player);
                }
            }
        }, 0, 0);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {
        ev.joinMessage(Component.empty());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent ev) {
        ev.setRespawnLocation(new Location(getServer().getWorlds().get(0), X, Y, Z));
    }

    @EventHandler
    public void onPlayerLeave(PlayerQuitEvent ev) {
        ev.quitMessage(Component.empty());
    }

    @EventHandler
    public void onPlayerAdvancementDone(PlayerAdvancementDoneEvent ev) {
        ev.message(Component.empty());
    }

}
