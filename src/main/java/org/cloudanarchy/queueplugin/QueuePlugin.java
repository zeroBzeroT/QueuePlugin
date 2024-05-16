package org.cloudanarchy.queueplugin;


import com.comphenix.protocol.ProtocolLibrary;
import net.kyori.adventure.text.Component;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.*;
import org.bukkit.plugin.java.JavaPlugin;

public final class QueuePlugin extends JavaPlugin implements Listener {

    private static Location location;

    @Override
    public void onEnable() {
        location = new Location(getServer().getWorlds().get(0), 0, 140, 0);

        getServer().setDefaultGameMode(GameMode.SPECTATOR);
        getServer().setSpawnRadius(32);
        getServer().getServerTickManager().setTickRate(1);
        getServer().getPluginManager().registerEvents(this, this);
        EventCanceler eventCanceler = new EventCanceler(this);
        getServer().getPluginManager().registerEvents(eventCanceler, this);
        ProtocolLibrary.getProtocolManager().addPacketListener(eventCanceler);

        getServer().getScheduler().runTaskTimer(this, () -> {
            for (Player player : getServer().getOnlinePlayers()) {
                // vanish
                for (Player p : getServer().getOnlinePlayers()) {
                    if (player.equals(p)) continue;
                    player.hidePlayer(this, p);
                    p.hidePlayer(this, player);
                }
                // properties
                player.setGameMode(GameMode.SPECTATOR);
                player.setAllowFlight(true);
                player.setFlying(true);
                player.setInvisible(true);
                player.setInvulnerable(true);
            }
        }, 0, 0);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {
        ev.joinMessage(Component.empty());
        ev.getPlayer().teleport(location, PlayerTeleportEvent.TeleportCause.PLUGIN);
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent ev) {
        ev.setRespawnLocation(location);
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
