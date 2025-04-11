package com.petitpastis.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerDropItemEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import com.petitpastis.Plugin;

public class HungerListener implements Listener {

    private Plugin plugin;

    public HungerListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onFoodLevelChange(FoodLevelChangeEvent event) 
    {
        event.setCancelled(true); 
    }

    @EventHandler
    public void DropItemEvent(PlayerDropItemEvent event) 
    {
        event.setCancelled(true);
    }

    @EventHandler
    public void CantMoveEvent(PlayerMoveEvent event) 
    {
        Player player = event.getPlayer();
        if (plugin.isGamePaused || (plugin.isSeeker(player) && plugin.seekerWaiting))
            event.setCancelled(true);
        
    }
}
