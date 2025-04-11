package com.petitpastis;

import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import com.petitpastis.enums.States;

public class GameLauncher extends BukkitRunnable {

    private Plugin plugin;
    private int timer = 5;

    public GameLauncher(Plugin plugin) 
    {
        this.plugin = plugin;
    }

    @Override
    public void run() 
    {
        for(Player player : plugin.getPlayers())
        {
            player.setLevel(timer);
            if (timer == 5 || timer == 4 || timer == 3 || timer == 2 || timer == 1)
            {
                player.getWorld().playSound(player.getLocation(), Sound.BLOCK_BAMBOO_WOOD_PLACE, 0.3f, 1.0f);
            }
        }

        if (timer == 5 || timer == 4 || timer == 3 || timer == 2 || timer == 1)
        {
            Bukkit.broadcastMessage("§7§oça lance dans " + timer + "s");
        }

        if (timer == 0)
        {
            plugin.setState(States.PLAYING);
            TeleportToSpawns();
            cancel();
        }
        timer--;
    }

    private void TeleportToSpawns() 
    {
        for (Player player : plugin.getSeekers())
        {
            player.getInventory().clear();
            player.setInvulnerable(false);
            player.teleport(plugin.getSeekerSpawn());

        }
        for (Player player : plugin.getHiders())
        {
            player.getInventory().clear();
            player.setInvulnerable(false);
            player.teleport(plugin.getHiderSpawn());
        }
    }
}
