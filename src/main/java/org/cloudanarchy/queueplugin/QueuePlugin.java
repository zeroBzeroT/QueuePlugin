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
import org.bukkit.scheduler.BukkitRunnable;
import org.cloudanarchy.queueplugin.packetwrapper.PacketGameState;
import org.jetbrains.annotations.NotNull;

public final class QueuePlugin extends JavaPlugin implements Listener {

    private static final PacketGameState creditScreenPacket = PacketGameState.creditScreenPacket();

    private void process(@NotNull Player player) {
        player.teleport(new Location(getServer().getWorlds().get(0), 0, 140, 0));
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
        // win blockgame while some other players are in queue
        if (getServer().getOnlinePlayers().size() < 4) return;
        try {
            creditScreenPacket.sendPacket(player);
        } catch (RuntimeException ex) {
            getLogger().warning("Unable to send fake packet: " + ex.getMessage());
        }
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(this, this);
        EventCanceler eventCanceler = new EventCanceler(this);
        getServer().getPluginManager().registerEvents(eventCanceler, this);
        ProtocolLibrary.getProtocolManager().addPacketListener(eventCanceler);
    }

    @EventHandler
    public void onPlayerJoin(PlayerJoinEvent ev) {
        ev.joinMessage(Component.empty());
        process(ev.getPlayer());
    }

    @EventHandler
    public void onPlayerRespawn(PlayerRespawnEvent ev) {
        ev.setRespawnLocation(new Location(getServer().getWorlds().get(0), 0, 140, 0));
        new BukkitRunnable() {
            @Override
            public void run() {
                process(ev.getPlayer());
            }
        }.runTaskLater(this, 1);
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
