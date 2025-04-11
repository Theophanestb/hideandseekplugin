package com.petitpastis.commands;

import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.command.Command;
import org.bukkit.entity.Player;

import com.petitpastis.Plugin;
import com.petitpastis.enums.States;

public class CommandsHandler implements CommandExecutor {
    private Plugin plugin;    

    public CommandsHandler(Plugin plugin)
    {
        this.plugin = plugin;
    }
    @Override
    public boolean onCommand(CommandSender sender, org.bukkit.command.Command command, String label, String[] args)
    {
        if (!(sender instanceof Player))
        {
            return false;
        }
        if (command.getName().equalsIgnoreCase("start"))
        {
            if (plugin.getHiderSpawn()== null || plugin.getSeekerSpawn() == null)
            {
                sender.sendMessage("§cVeuillez définir les spawns avant de lancer la partie !");
                return true;
            }
            boolean isFull = true;
            for (Player player : plugin.getPlayers())
            {
                if (!plugin.getSeekers().contains(player) && !plugin.getHiders().contains(player))
                {
                    Bukkit.broadcastMessage(ChatColor.AQUA +player.getName() + "§c n'est dans aucune équipe !");
                    isFull = false;
                }   
            }
            if (!isFull)
            {
                return true;
            }
            if (plugin.getState() == States.WAITING)
            {
                plugin.startGame();
                return true;
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("pause"))
        {
            if (plugin.getState() == States.PLAYING)
            {
                //plugin.pauseGame(); // to implement
                return true;
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("end"))
        {
            if (plugin.getState() == States.PLAYING)
            {
                plugin.resetGame();
                return true;
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("seekerspawn"))
        {
            plugin.setSeekerSpawn(((Player)sender).getLocation());
            sender.sendMessage("§aSpawn des seekers défini !");
            return true;  
        }
        if (command.getName().equalsIgnoreCase("hiderspawn"))
        {
            plugin.setHiderSpawn(((Player)sender).getLocation());
            sender.sendMessage("§aSpawn des hiders défini !");
            return true;  
        }
        if (command.getName().equalsIgnoreCase("spawn"))
        {
            plugin.setSeekerSpawn(((Player)sender).getLocation());
            sender.sendMessage("§aSpawn par défaut défini !");
            return true;  
        }
        if (command.getName().equalsIgnoreCase("show"))
        {
            if (plugin.getPlayers().isEmpty())
            {
                sender.sendMessage("§cAucun joueur dans la partie !");
                return true;
            }
            for (Player player : plugin.getPlayers())
            {
                if (plugin.isHider(player))
                {
                    Bukkit.broadcastMessage(player.getName() + " est un hider (version is)!");
                }
                else if (plugin.isSeeker(player))
                {
                    Bukkit.broadcastMessage(player.getName() + " est un seeker (version is)!");
                }
                if (plugin.getHiders().contains(player))
                {
                    Bukkit.broadcastMessage(player.getName() + " est un hider (version list get)!");
                }
                else if (plugin.getSeekers().contains(player))
                {
                    Bukkit.broadcastMessage(player.getName() + " est un seeker (version list get)!");
                }
                else
                {
                    Bukkit.broadcastMessage(player.getName() + " n'est dans aucune équipe !");
                }
            }
            return true;
        }
        return false;
    }
}
