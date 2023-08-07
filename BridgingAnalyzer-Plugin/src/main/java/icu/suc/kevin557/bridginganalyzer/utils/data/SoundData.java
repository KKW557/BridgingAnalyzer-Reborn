package icu.suc.kevin557.bridginganalyzer.utils.data;

import org.bukkit.Sound;

import java.util.Objects;

public class SoundData
{
    private final Sound sound;
    private final float volume;
    private final float pitch;

    public SoundData(Sound sound, float volume, float pitch)
    {
        this.sound = sound;
        this.volume = volume;
        this.pitch = pitch;
    }

    public Sound getSound()
    {
        return sound;
    }

    public float getVolume()
    {
        return volume;
    }

    public float getPitch()
    {
        return pitch;
    }

    @Override
    public boolean equals(Object o)
    {
        if (this == o)
        {
            return true;
        }
        if (o == null || getClass() != o.getClass())
        {
            return false;
        }
        SoundData soundData = (SoundData) o;
        return Float.compare(soundData.volume, volume) == 0 && Float.compare(soundData.pitch, pitch) == 0 && sound == soundData.sound;
    }

    @Override
    public int hashCode()
    {
        return Objects.hash(sound, volume, pitch);
    }

    @Override
    public String toString()
    {
        return "SoundData{" +
                "sound=" + sound +
                ", volume=" + volume +
                ", pitch=" + pitch +
                '}';
    }
}
