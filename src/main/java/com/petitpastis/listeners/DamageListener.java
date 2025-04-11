package com.petitpastis.listeners;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.entity.EntityDamageByEntityEvent;

import com.petitpastis.Plugin;
import com.petitpastis.enums.States;

public class DamageListener  implements org.bukkit.event.Listener{
    
    private Plugin plugin;

    public DamageListener(Plugin plugin) {
        this.plugin = plugin;
    }

    @SuppressWarnings("deprecation")
    @EventHandler
    public void onPlayerDamage(EntityDamageByEntityEvent event) {
        if (event.getEntity() instanceof Player) 
        {
            Player victim = (Player) event.getEntity();
            Player damager = (Player) event.getDamager();
            if (plugin.getState() == States.WAITING)
            {
                event.setCancelled(true);
            } 
            else if (plugin.getState() == States.PAUSED) 
            {
                event.setCancelled(true);
            } 
            else if (plugin.getState() == States.PLAYING) 
            {
                if (plugin.isHider(victim) && damager instanceof Player && plugin.isSeeker(damager))
                {
                    MoveToSeeker(victim);
                    victim.getWorld().playSound(victim.getLocation(), Sound.ENTITY_PLAYER_LEVELUP, 0.5f, 1);
                    Bukkit.broadcastMessage(victim.getName() + " s'est fait avoir par " + damager.getName());
                    damager.setExp(damager.getLevel() + 1);
                    plugin.isGameOver();
                }
                victim.damage(0);
                victim.setHealth(victim.getMaxHealth());
            }
        }
    }

    public void MoveToSeeker(Player player)
    {
        plugin.addSeeker(player);
    }
}
