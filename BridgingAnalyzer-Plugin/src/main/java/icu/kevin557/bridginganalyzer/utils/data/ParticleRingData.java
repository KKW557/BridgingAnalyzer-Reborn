package icu.kevin557.bridginganalyzer.utils.data;

import org.bukkit.Effect;

public class ParticleRingData
{
    private final Effect effect;
    private final double radius;
    private final int count;

    public ParticleRingData(Effect effect, double radius, int count)
    {
        this.effect = effect;
        this.radius = radius;
        this.count = count;
    }

    public Effect getEffect()
    {
        return effect;
    }

    public double getRadius()
    {
        return radius;
    }

    public int getCount()
    {
        return count;
    }
}
