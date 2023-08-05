package icu.kevin557.bridginganalyzer.utils;

import icu.kevin557.bridginganalyzer.utils.data.ParticleRingData;
import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class ParticleUtils
{
    public static void playRing(Location center, ParticleRingData data)
    {
        if (data == null)
        {
            return;
        }

        for (Location location : calcLocations(center, data.getRadius(), data.getCount()))
        {
            center.getWorld().spigot().playEffect(location, data.getEffect(), 0, 0, 0, 0, 0, 0, 1,64);
        }
    }

    private static List<Location> calcLocations(Location center, double radius, double count)
    {
        List<Location> locations = new ArrayList<>();

        double angle = 360.0D / count;

        for (int i = 0; i < count; i++)
        {
            double radians = Math.toRadians(angle * i);
            double vecX = radius * Math.sin(radians);
            double vecZ = radius * Math.cos(radians);

            locations.add(center.clone().add(vecX, 0, vecZ));
        }

        return locations;
    }
}
