package org.cloudanarchy.queueplugin;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.entity.EntityType;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPhysicsEvent;
import org.bukkit.event.entity.EntitySpawnEvent;
import org.bukkit.event.player.PlayerAttemptPickupItemEvent;
import org.bukkit.event.player.PlayerCommandPreprocessEvent;
import org.bukkit.event.raid.RaidTriggerEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashSet;
import java.util.Set;
import java.util.stream.Collectors;

public class EventCanceler extends PacketAdapter implements Listener {

    public EventCanceler(JavaPlugin plugin) {
        super(plugin, getAllPacketTypes());
    }

    private static Iterable<? extends PacketType> getAllPacketTypes() {
        final Set<PacketType> types = new HashSet<>();
        types.addAll(PacketType.Play.Client.getInstance().values().stream().filter(PacketType::isSupported).collect(Collectors.toSet()));
        types.addAll(PacketType.Play.Server.getInstance().values().stream().filter(PacketType::isSupported).collect(Collectors.toSet()));
        return types;
    }

    @Override
    public void onPacketReceiving(PacketEvent ev) {
        if (ev.getPacketType() == PacketType.Play.Client.KEEP_ALIVE) return;
        ev.setCancelled(true);
    }

    @Override
    public void onPacketSending(PacketEvent ev) {
        if (ev.getPacketType() == PacketType.Play.Server.UPDATE_TIME) return;
        if (ev.getPacketType() == PacketType.Play.Server.KEEP_ALIVE) return;
        if (ev.getPacketType() == PacketType.Play.Server.POSITION) return; // this is needed or client will not join
        if (ev.getPacketType() == PacketType.Play.Server.LOGIN) return;
        if (ev.getPacketType() == PacketType.Play.Server.CHAT) return;
        //if (ev.getPacketType() == PacketType.Play.Server.PLAYER_INFO) return; // if we dont send this, player has default skin lol
        ev.setCancelled(true);
    }

    @EventHandler
    public void onEntitySpawn(EntitySpawnEvent ev) {
        if (ev.getEntityType() == EntityType.PLAYER) return;
        ev.setCancelled(true);
    }

    @EventHandler
    public void onBlockPhysics(BlockPhysicsEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onCommand(PlayerCommandPreprocessEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onPlayerAttemptPickupItem(PlayerAttemptPickupItemEvent ev) {
        ev.setCancelled(true);
    }

    @EventHandler
    public void onRaidTrigger(RaidTriggerEvent ev) {
        ev.setCancelled(true);
    }

}
