package com.petitpastis;

import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Coordinates {
    private Plugin plugin;

    private Map<Location, String> coordinates_map = new HashMap<>();

    public Coordinates(Plugin plugin) 
    {
        this.plugin = plugin;
        coordinates_map.put(new Location(Bukkit.getServer().getWorld("world"), 205, -13, 494), "sur le bureau");
        coordinates_map.put(new Location(Bukkit.getServer().getWorld("world"), 378, -31, 317), "sur le lit");
        coordinates_map.put(new Location(Bukkit.getServer().getWorld("world"), 272, -16, 335), "sur les rails");
        coordinates_map.put(new Location(Bukkit.getServer().getWorld("world"), 270, 10, 236), "sur piggy bank");
        coordinates_map.put(new Location(Bukkit.getServer().getWorld("world"), 167, -55, 321), "sur le skate");
        coordinates_map.put(new Location(Bukkit.getServer().getWorld("world"), 107, 20, 435), "sur l'arbre");
        coordinates_map.put(new Location(Bukkit.getServer().getWorld("world"), 103, -42, 498), "dans la poubelle");
        coordinates_map.put(new Location(Bukkit.getServer().getWorld("world"), 253, 88, 384), "sur l'avion");
        coordinates_map.put(new Location(Bukkit.getServer().getWorld("world"), 93, 61, 334), "sur le globe");
        coordinates_map.put(new Location(Bukkit.getServer().getWorld("world"), 379, -23, 498), "sur la télé");
        coordinates_map.put(new Location(Bukkit.getServer().getWorld("world"), 257, -59, 316), "sur le tapis");
    }
    
    public Location getLocationAtIndex(int index) 
    {
        return new ArrayList<>(coordinates_map.keySet()).get(index);
    }
    
    public String getNameAtIndex(int index) 
    {
        return new ArrayList<>(coordinates_map.values()).get(index);
    }

    public List<Map.Entry<Location, String>> getShuffledEntries() 
    {
        List<Map.Entry<Location, String>> entries = new ArrayList<>(coordinates_map.entrySet());
        Collections.shuffle(entries);
        return entries;
    }

    public int size() 
    {
        return coordinates_map.size();
    }

    public void showCoordinates(Player player) 
    {
        for (Map.Entry<Location, String> entry : coordinates_map.entrySet()) {
            Location location = entry.getKey();
            String name = entry.getValue();
            player.sendMessage("Coordonnées de " + name + ": " + location.getBlockX() + ", " + location.getBlockY() + ", " + location.getBlockZ());
        }
    }
}
