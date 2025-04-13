package com.petitpastis.listeners;

import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageEvent.DamageCause;
import org.bukkit.event.entity.PlayerDeathEvent;

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
        
        victim.spigot().respawn();
        victim.teleport(plugin.getSeekerSpawn());
        //victim.setHealth(20);
        //victim.setGameMode(org.bukkit.GameMode.SURVIVAL);
        plugin.isGameOver();
        event.setDeathMessage(null);
        event.getDrops().clear();
    }

    public void MoveToSeeker(Player player)
    {
        plugin.addSeeker(player);
    }
}
