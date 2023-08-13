package icu.suc.kevin557.bridginganalyzer.configs;

import icu.suc.kevin557.bridginganalyzer.utils.data.ParticleRingData;
import icu.suc.kevin557.bridginganalyzer.utils.data.SoundData;
import org.bukkit.material.MaterialData;

public class Settings
{
    public boolean noHunger;
    public boolean clearInventory;
    public boolean replenish;
    public boolean blockDrop;

    public boolean antiCover;
    public int antiCoverDelay;

    public int breakDelay;
    public int breakMinPeriod;
    public int breakMaxPeriod;

    public MaterialData checkBlock;
    public int checkNoDamageTicks;
    public int checkMessageFadeIn;
    public int checkMessageStay;
    public int checkMessageFadeOut;
    public ParticleRingData checkParticle;
    public SoundData checkSound;

    public MaterialData endBlock;
    public int endNoDamageTicks;
    public ParticleRingData endParticle;
    public SoundData endSound;

    public MaterialData kbBlock;
    public int kbNoDamageTicks;
    public int kbDelay;

    public MaterialData spawnBlock;
    public int spawnProcessingTicks;
    public int spawnNoDamageTicks;
    public int spawnMessageFadeIn;
    public int spawnMessageStay;
    public int spawnMessageFadeOut;
    public ParticleRingData spawnParticle;
    public SoundData spawnSound;

    public MaterialData boostBlock;
    public int boostNoDamageTicks;
    public int boostDuration;
    public int boostAmplifier;

    public MaterialData liftBlock;
    public int liftNoDamageTicks;
    public SoundData liftSound;

    public double velocityX;
    public double velocityY;

    public MaterialData victoryBlock;

    public boolean pvpDefault;

    public int pvpWarnMessageFadeIn;
    public int pvpWarnMessageStay;
    public int pvpWarnMessageFadeOut;

    public int damageMessageFadeIn;
    public int damageMessageStay;
    public int damageMessageFadeOut;
}
