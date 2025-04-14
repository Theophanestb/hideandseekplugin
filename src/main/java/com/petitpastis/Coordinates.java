package com.petitpastis;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

public class Coordinates {
    private Plugin plugin;

    private List<Location> coordinates = new ArrayList<>();
    private List<String> coordinates_name = new ArrayList<>();

    public Coordinates(Plugin plugin) {
        this.plugin = plugin;
        coordinates.add(new Location(Bukkit.getServer().getWorld("world"), 205, 99, 494));
        coordinates_name.add("sur le bureau");
        coordinates.add(new Location(Bukkit.getServer().getWorld("world"), 378, 99, 317));
        coordinates_name.add("sur le lit");
        coordinates.add(new Location(Bukkit.getServer().getWorld("world"), 272, 99, 335));
        coordinates_name.add("sur les rails");
        coordinates.add(new Location(Bukkit.getServer().getWorld("world"), 271, 99, 240));
        coordinates_name.add("à côté de la guitare");
        coordinates.add(new Location(Bukkit.getServer().getWorld("world"),167 , 99, 321));
        coordinates_name.add("sur le skate");
        coordinates.add(new Location(Bukkit.getServer().getWorld("world"),107 , 99, 435));
        coordinates_name.add("sur l'arbre");
        coordinates.add(new Location(Bukkit.getServer().getWorld("world"),107 , 99, 491));
        coordinates_name.add("dans la poubelle");
        coordinates.add(new Location(Bukkit.getServer().getWorld("world"),253 , 99, 384));
        coordinates_name.add("sur l'avion");
        coordinates.add(new Location(Bukkit.getServer().getWorld("world"),93 , 99, 334));
        coordinates_name.add("sur le globe");
        coordinates.add(new Location(Bukkit.getServer().getWorld("world"),379 , 99, 498));
        coordinates_name.add("sur la télé");
        coordinates.add(new Location(Bukkit.getServer().getWorld("world"),257 , 99, 316));
        coordinates_name.add("sur le tapis");
    }
    
    public List<String> getCoordinatesName() 
    {
        return coordinates_name;
    }

    public List<Location> getCoordinates() 
    {
        return coordinates;
    }

    public void showCoordinates(Player player) 
    {
        for (Location loc : coordinates)
        {
            player.sendMessage("Coordinates: " + loc.getX() + ", " + loc.getY() + ", " + loc.getZ());
        }
    }
}
