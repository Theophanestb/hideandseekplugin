package com.petitpastis.commands;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.Material;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

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
            int nbPlayers = plugin.getPlayers().size();
            if (nbPlayers < 2)
            {
                sender.sendMessage("§cPas assez de joueurs jouer ! (pas d'amis?)");
                return true;
            }
            
            if (plugin.getState() != States.WAITING)
            {
                sender.sendMessage("§cLa partie a déjà commencé !");
                return true;
            }

            if (plugin.getSeekers().isEmpty() && plugin.isRandomize() == false)
            {
                Bukkit.broadcastMessage("§cAucun seeker défini !");
                return true;
            }
            if (plugin.isRandomize() == true)
            {
                for (Player player : plugin.getPlayers())
                {
                    player.getInventory().clear();
                }
            }
            
            //lancement random
            if (plugin.isRandomize())
            {
                int random = (int) (Math.random() * nbPlayers);
                Player player = plugin.getPlayers().get(random);
                plugin.addSeeker(player);
                for (int i = 0; i < plugin.getPlayers().size(); i++) {
                    if (i != random) 
                    {
                        plugin.addHider(plugin.getPlayers().get(i));
                    }
                }
                
                plugin.startGame();
                return true;
            }
            //lancement seeker prédéfini
            boolean isFull = true;
            for (Player player : plugin.getPlayers())
            {
                if (!plugin.getSeekers().contains(player) && !plugin.getHiders().contains(player))
                {
                    Bukkit.broadcastMessage(ChatColor.GOLD +player.getName() + "§c n'est dans aucune équipe !");
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
                Bukkit.broadcastMessage("§aPouce");
                for (Player player : plugin.getPlayers())
                {
                    player.setInvulnerable(true);
                }
                plugin.isGamePaused = true;
                return true;
            }
            return true;
        }
        if (command.getName().equalsIgnoreCase("resume"))
        {
            if (plugin.isGamePaused == true)
            {
                Bukkit.broadcastMessage("§aLa partie reprend !");
            }
            for (Player player : plugin.getPlayers())
            {
                player.setInvulnerable(false);
            }
            plugin.isGamePaused = false;
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
            plugin.spawn = ((Player)sender).getLocation();
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
        if (command.getName().equalsIgnoreCase("random"))
        {
            if (plugin.getState() == States.PLAYING)
            {
                sender.sendMessage("§cImpossible de randomiser les équipes pendant la partie !");
                return true;
            }
            if (!plugin.isRandomize())
            {
                plugin.setRandomize(true);
                Bukkit.broadcastMessage("§aRandomisation des équipes activée !");
            }
            else
            {
                plugin.setRandomize(false);
                Bukkit.broadcastMessage("§cRandomisation des équipes désactivée !");
                for (Player player : plugin.getPlayers())
                {
                    ItemStack compass = new ItemStack(Material.CLOCK);
                    player.getInventory().setItem(4,compass);
                }
                return true;
            }
            for (Player player : plugin.getPlayers())
            {
                player.getInventory().clear();
                player.setDisplayName(ChatColor.WHITE + player.getName());
                player.setPlayerListName(ChatColor.WHITE + player.getName());  
                if (plugin.invisibleNameTeam.hasEntry(player.getName())) 
                {
                    plugin.invisibleNameTeam.removeEntry(player.getName());
                }
                if (plugin.getHiders().contains(player))
                {
                    plugin.getHiders().remove(player);
                }
                if (plugin.getSeekers().contains(player))
                {
                    plugin.getSeekers().remove(player);
                }
            }
            return true;
        }
        return false;
    }
}
