package com.petitpastis.listeners;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
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
        Player killer = null;
        if (victim.getKiller() != null)
            killer = event.getEntity().getKiller();

        if (plugin.isHider(victim))
        {
            if (victim.getLastDamageCause().getCause() == DamageCause.FALL)
            {
                Bukkit.broadcastMessage(ChatColor.AQUA + victim.getName() + ChatColor.GOLD +": J'ai glissé chef");
            }
            if (plugin.isHider(victim))
            {
                MoveToSeeker(victim);
                Bukkit.broadcastMessage(ChatColor.AQUA +victim.getName() +ChatColor.DARK_RED+ " devient seeker !");
            }
                
        }
        if (killer != null && plugin.isSeeker(killer)) 
        {
            killer.setExp(killer.getLevel() + 1);
        }
        if (plugin.isHider(killer) && plugin.isSeeker(victim))
        {
            Bukkit.broadcastMessage(victim.getName() + " s'est fait bousiller par " + killer.getName() + " bahaha dans l'crâne à ta mère");
        }
        
        event.setDeathMessage(null);
        event.getDrops().clear();

        // Délai de 1 tick avant de ressusciter proprement
        Bukkit.getScheduler().runTaskLater(plugin, () -> {
            victim.spigot().respawn();
            victim.teleport(plugin.getSeekerSpawn());
            victim.setHealth(20);
            victim.setGameMode(GameMode.SURVIVAL);
            plugin.isGameOver();
        }, 1L);
    }

    public void MoveToSeeker(Player player)
    {
        plugin.addSeeker(player);
    }
}
