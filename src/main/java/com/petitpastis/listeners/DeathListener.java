package com.petitpastis.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.PlayerDeathEvent;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;

import com.petitpastis.Plugin;

public class DeathListener implements org.bukkit.event.Listener {
    private Plugin plugin;

    public DeathListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @EventHandler
    public void onPlayerDeath(PlayerDeathEvent event) {
        Player victim = event.getEntity();
        Player killer = event.getEntity().getKiller();

        victim.spigot().respawn();
        victim.teleport(plugin.getSeekerSpawn());

        if (plugin.isHider(victim))
        {
            if (victim.getLastDamageCause().getCause() == DamageCause.FALL)
            {
                Bukkit.broadcastMessage(victim.getName() + ": J'ai glissé chef");
            }
            MoveToSeeker(victim);
        }
        if (killer != null && plugin.isSeeker(killer)) 
        {
            killer.setExp(killer.getLevel() + 1);
        }
        if (plugin.isHider(killer) && plugin.isSeeker(victim)) 
        {
            Bukkit.broadcastMessage(victim.getName() + " s'est fait bousiller par " + killer.getName() + " bahaha dans l'crâne à ta mère");
        }
        plugin.isGameOver();
    
    }

    public void MoveToSeeker(Player player)
    {
        plugin.addSeeker(player);
    }
}
