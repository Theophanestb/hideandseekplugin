package com.petitpastis;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;
import org.bukkit.Location;
import org.bukkit.Material;

import com.petitpastis.commands.CommandsHandler;
import com.petitpastis.enums.States;
import com.petitpastis.listeners.BlockListener;
import com.petitpastis.listeners.DeathListener;
import com.petitpastis.listeners.HungerListener;
import com.petitpastis.listeners.TeamSelectorListener;
import com.petitpastis.listeners.DamageListener;
import com.petitpastis.listeners.PlayerListener;

/*
 * hideandseek java plugin
 */
public class Plugin extends JavaPlugin
{
  private static final Logger LOGGER = Logger.getLogger("hideandseek");

  private List<Player> players = new ArrayList<>();
  private List<Player> seekers = new ArrayList<>();
  private List<Player> hiders = new ArrayList<>();

  private Scoreboard scoreboard;

  public Team hiderTeam;
  public Team seekerTeam;

  private States state;
  private GameLauncher gameLauncher;

  private Location seekerSpawn;// = new Location(Bukkit.getServer().getWorld("world"), -60, -60, -65);
  private Location hiderSpawn;// = new Location(Bukkit.getServer().getWorld("world"), -55, -60, -60);
  private Location spawn;// = new Location(Bukkit.getServer().getWorld("world"), -60, -60, -60);

  @Override
  public void onEnable()
  {
    LOGGER.info("hideandseek enabled");

    // Initialize scoreboard and teams
    this.scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();
    if (scoreboard == null) {
        LOGGER.warning("Scoreboard manager is not available. Please check the server configuration.");
        return;
    }

    // Register teams if not already registered
    hiderTeam = scoreboard.getTeam("hiders");
    if (hiderTeam == null) {
    hiderTeam = scoreboard.registerNewTeam("hiders");
    hiderTeam.setPrefix(ChatColor.GREEN.toString());
    hiderTeam.setDisplayName("Hiders");
    hiderTeam.setColor(ChatColor.GREEN);
    }

    seekerTeam = scoreboard.getTeam("seekers");
    if (seekerTeam == null) {
    seekerTeam = scoreboard.registerNewTeam("seekers");
    seekerTeam.setPrefix(ChatColor.RED.toString());
    seekerTeam.setDisplayName("Seekers");
    seekerTeam.setColor(ChatColor.RED);
    }

    noteam = scoreboard.getTeam("seekers");
    if (noteam == null) {
    seekerTeam = scoreboard.registerNewTeam("seekers");
    seekerTeam.setPrefix(ChatColor.RED.toString());
    seekerTeam.setDisplayName("Seekers");
    seekerTeam.setColor(ChatColor.RED);
    }

    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(new TeamSelectorListener(this), this);
    pm.registerEvents(new BlockListener(this), this);
    pm.registerEvents(new DamageListener(this), this);
    pm.registerEvents(new PlayerListener(this), this);
    pm.registerEvents(new HungerListener(this), this);
    pm.registerEvents(new DeathListener(this), this);

    getCommand("start").setExecutor(new CommandsHandler(this));
    getCommand("pause").setExecutor(new CommandsHandler(this));
    getCommand("end").setExecutor(new CommandsHandler(this));
    getCommand("seekerspawn").setExecutor(new CommandsHandler(this));
    getCommand("hiderspawn").setExecutor(new CommandsHandler(this));
    getCommand("spawn").setExecutor(new CommandsHandler(this));
    getCommand("show").setExecutor(new CommandsHandler(this));

    resetGame();
  }

  @Override
  public void onDisable()
  {
    LOGGER.info("hideandseek disabled");
  }

  public void setState(States state)
  {
    this.state = state;
  }

  public States getState()
  {
    return state;
  }

  public List<Player> getPlayers()
  {
    return players;
  }

  public List<Player> getSeekers()
  {
    return seekers;
  }

  public List<Player> getHiders()
  {
    return hiders;
  }

  

  public void setSeekerSpawn(Location location)
  {
    this.seekerSpawn = location;
  }

  public void setHiderSpawn(Location location)
  {
    this.hiderSpawn = location;
  }

  public Location getSeekerSpawn()
  {
    return seekerSpawn;
  }

  public Location getHiderSpawn()
  {
    return hiderSpawn;
  }


  public void setSpawn(Location location)
  {
    this.spawn = location;
  }
  public Location getSpawn()
  {
    return spawn;
  }

  public boolean isHider(Player player)
  {
    return hiders.contains(player);
  }

  public boolean isSeeker(Player player)
  {
    return seekers.contains(player);
  }


  //////////////////// ADD HIDDER AND SEEKERS //////////////////////
  public void addPlayers(Player player)
  {
    players.add(player);
    Bukkit.broadcastMessage(getName() + " : " + player.getName() + " a rejoint le jeu !");
  }

  public void removePlayers(Player player)
  {
    players.remove(player);
    if (hiders.contains(player))
    {
      hiders.remove(player);
    }
    if (seekers.contains(player))
    {
      seekers.remove(player);
    }
  }

  public void addSeeker(Player player)
  {
    if (hiders.contains(player))
    {
      hiders.remove(player);
    }
    seekerTeam.addEntry(player.getName());
    //player.setDisplayName(ChatColor.RED + player.getName());
    player.setPlayerListName(ChatColor.RED + player.getName());
    
    /*player.setCustomName(ChatColor.RED + player.getName());
    player.setCustomNameVisible(true);*/
    seekers.add(player);
  }

  public void addHider(Player player)
  {
    if (seekers.contains(player))
    {
      seekers.remove(player);
    }
    hiderTeam.addEntry(player.getName());
    player.setPlayerListName(ChatColor.GREEN + player.getName());
    //player.setDisplayName(ChatColor.GREEN + player.getName());
    
    /*player.setCustomName(ChatColor.GREEN + player.getName());
    player.setCustomNameVisible(true);*/
    hiders.add(player);
  } 

   //////////////////// END OF ADD HIDDER AND SEEKERS //////////////////////
   /// 
   /// 
   /// 
   //////////////////// GAME MANAGMENT //////////////////////

  public void startGame()
  {
    if (getState() == States.WAITING)
    {
      gameLauncher = new GameLauncher(this);
      gameLauncher.runTaskTimer(this, 0, 20);
    }
  }

  public void resetGame()
  {
    seekers.clear();
    hiders.clear();
    setState(States.WAITING);
    for (Player player : players)
    {
      player.teleport(new Location(Bukkit.getServer().getWorld("world"), -60, -60, -60));
      player.setGameMode(GameMode.SURVIVAL);
      player.setInvulnerable(true);
      player.getInventory().clear();

      ItemStack compass = new ItemStack(Material.COMPASS);
      player.getInventory().addItem(compass);

      player.setExp(0);

      if (seekerTeam.hasEntry(player.getName())) 
      {
        seekerTeam.removeEntry(player.getName());
      }
      if (hiderTeam.hasEntry(player.getName()))
      {
          hiderTeam.removeEntry(player.getName());
      }
      player.setDisplayName(ChatColor.WHITE + player.getName());
      player.setPlayerListName(ChatColor.WHITE + player.getName());
      player.setCustomName(ChatColor.WHITE + player.getName());
      player.setCustomNameVisible(false);
      
    }
    gameLauncher = null;
  }

  public void isGameOver()
  {
    if (getState() == States.PLAYING)
    {
      if (hiders.isEmpty())
      {
        Bukkit.broadcastMessage(ChatColor.RED + "Les seekers ont gagné !");
        resetGame();
      }
    }
  }
}
