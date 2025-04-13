package com.petitpastis;

import java.util.ArrayList;
import java.util.List;
import java.util.logging.Logger;

import org.bukkit.Bukkit;
import org.bukkit.ChatColor;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scoreboard.Scoreboard;
import org.bukkit.scoreboard.Team;

import com.petitpastis.commands.CommandsHandler;
import com.petitpastis.enums.States;
import com.petitpastis.items.GrapplePlugin;
import com.petitpastis.listeners.BlockListener;
import com.petitpastis.listeners.DamageListener;
import com.petitpastis.listeners.DeathListener;
import com.petitpastis.listeners.HungerListener;
import com.petitpastis.listeners.PlayerListener;
import com.petitpastis.listeners.TeamSelectorListener;

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

  public boolean isGamePaused = false;

  public boolean canBuild = false;

  public boolean seekerWaiting = false;

  private boolean randomize = false;

  public Team invisibleNameTeam;

  private States state;
  private GameLauncher gameLauncher;

  private Location seekerSpawn;
  private Location hiderSpawn;
  public Location spawn;

  @Override
  public void onEnable()
  {
    LOGGER.info("hideandseek enabled");

    scoreboard = Bukkit.getScoreboardManager().getMainScoreboard();

    invisibleNameTeam = scoreboard.getTeam("invisibleNameTeam");
    if (invisibleNameTeam == null) {
        invisibleNameTeam = scoreboard.registerNewTeam("invisibleNameTeam");
    }
    invisibleNameTeam.setOption(Team.Option.NAME_TAG_VISIBILITY, Team.OptionStatus.NEVER);

    PluginManager pm = getServer().getPluginManager();
    pm.registerEvents(new TeamSelectorListener(this), this);
    pm.registerEvents(new BlockListener(this), this);
    pm.registerEvents(new DamageListener(this), this);
    pm.registerEvents(new PlayerListener(this), this);
    pm.registerEvents(new HungerListener(this), this);
    pm.registerEvents(new DeathListener(this), this);
    pm.registerEvents(new GrapplePlugin(this), this);

    getCommand("start").setExecutor(new CommandsHandler(this));
    getCommand("pause").setExecutor(new CommandsHandler(this));
    getCommand("end").setExecutor(new CommandsHandler(this));
    getCommand("seekerspawn").setExecutor(new CommandsHandler(this));
    getCommand("hiderspawn").setExecutor(new CommandsHandler(this));
    getCommand("spawn").setExecutor(new CommandsHandler(this));
    getCommand("show").setExecutor(new CommandsHandler(this));
    getCommand("random").setExecutor(new CommandsHandler(this));
    getCommand("resume").setExecutor(new CommandsHandler(this));
    getCommand("build").setExecutor(new CommandsHandler(this));

    resetGame();
  }

  @Override
  public void onDisable()
  {
    LOGGER.info("hideandseek disabled");
  }

  public void setRandomize(boolean randomize)
  {
    this.randomize = randomize;
  }

  public boolean isRandomize()
  {
    return randomize;
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
    //Bukkit.broadcastMessage(getName() + " : " + player.getName() + " a rejoint le jeu !");
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
    player.setPlayerListName(ChatColor.LIGHT_PURPLE +"[SEEKER] " + player.getName());
    if (state == States.PLAYING)
    {
      givePiglinHeadToPlayer(player);
    }
    invisibleNameTeam.addEntry(player.getName());
    seekers.add(player);
    if (state == States.PLAYING)
    {
      player.getInventory().addItem(GrapplePlugin.getGrappleItem());
    } 
  }

  public void addHider(Player player)
  {
    if (seekers.contains(player))
    {
      seekers.remove(player);
    }
    player.setPlayerListName(ChatColor.AQUA + player.getName());
    invisibleNameTeam.addEntry(player.getName());
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
      removePiglinHeadFromPlayer(player);
      if (spawn == null)
      {
        spawn = Bukkit.getServer().getWorld("world").getSpawnLocation();
        if (spawn == null)
        {
            spawn = new org.bukkit.Location(Bukkit.getServer().getWorld("world"), -60, -60, -60);
        }
      }
      player.teleport(spawn);
      player.setGameMode(GameMode.SURVIVAL);
      player.setInvulnerable(true);
      player.getInventory().clear();
      if (!isRandomize())
      {
        ItemStack compass = new ItemStack(Material.CLOCK);
        player.getInventory().setItem(4,compass);
      }
      player.setExp(0);

      player.setDisplayName(ChatColor.WHITE + player.getName());
      player.setPlayerListName(ChatColor.WHITE + player.getName());
      if (invisibleNameTeam.hasEntry(player.getName())) 
      {
        invisibleNameTeam.removeEntry(player.getName());
      }    
    }
    gameLauncher = null;
  }

  public void isGameOver()
  {
    if (getState() == States.PLAYING)
    {
      if (hiders.isEmpty())
      {
        Bukkit.broadcastMessage(ChatColor.LIGHT_PURPLE + "Victoire des Seekers !");
        resetGame();
      }
    }
  }

  public void givePiglinHeadToPlayer(Player player) 
  {
    ItemStack piglinHead = new ItemStack(Material.PIGLIN_HEAD);

    SkullMeta skullMeta = (SkullMeta) piglinHead.getItemMeta();
    skullMeta.setOwner("Piglin");

    piglinHead.setItemMeta(skullMeta);

   
    player.getInventory().setHelmet(piglinHead);
  }

  public void removePiglinHeadFromPlayer(Player player) 
  {
    player.getInventory().setHelmet(new ItemStack(Material.AIR));
  }

}
