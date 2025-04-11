package com.petitpastis.listeners;

import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.entity.FoodLevelChangeEvent;

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
}
