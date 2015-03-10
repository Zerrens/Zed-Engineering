package com.zerren.zedeng.handler;

import com.zerren.zedeng.reference.Reference;
import cpw.mods.fml.client.event.ConfigChangedEvent;
import cpw.mods.fml.common.eventhandler.SubscribeEvent;
import net.minecraftforge.common.config.Configuration;

import java.io.File;

/**
 * Created by Zerren on 2/19/2015.
 */
public class ConfigHandler {

    public static Configuration config;

    public static boolean devDebug = false;

    public static float vaultResistance = 100F;
    public static boolean bedrockVault = false;
    public static int vaultPages = 10;
    public static float steamFactor = 1.0F;
    public static String steamName = "zedSteam";

    public static int potionIDAlpha = 66;
    public static int potionIDBeta = 67;
    public static int potionIDGamma = 68;
    public static int potionIDNeutron = 69;
    public static int potionIDSickness = 70;


    public static void init(File configFile){

        if (config == null) {
            config = new Configuration(configFile);
            loadConfig();
        }
    }

    private static void loadConfig() {
        potionIDAlpha = config.getInt("potionIDAlpha", "potionIDs", 66, 33, 127, "Alpha radiation potion effect ID");
        potionIDBeta = config.getInt("potionIDBeta", "potionIDs", 67, 33, 127, "Beta radiation potion effect ID");
        potionIDGamma = config.getInt("potionIDGamma", "potionIDs", 68, 33, 127, "Gamma radiation potion effect ID");
        potionIDNeutron = config.getInt("potionIDNeutron", "potionIDs", 69, 33, 127, "Neutron radiation potion effect ID");
        potionIDSickness = config.getInt("potionIDSickness", "potionIDs", 70, 33, 127, "Radiation sickness potion effect ID");

        devDebug = config.getBoolean("devDebug", "general", false, "System print for debugging--leave false unless you have a log spam fetish");

        vaultResistance = config.getFloat("vaultResistance", "tweaks", 100F, 10F, 600F, "Blast resistance of a player owned and locked vault");
        bedrockVault = config.getBoolean("bedrockVault", "tweaks", false, "If true, player owned and locked vaults are immune to explosions--overwrites vaultResistance");
        vaultPages = config.getInt("vaultPages", "tweaks", 10, 1, 20, "How many pages of 54 slots the vault will have. Each page is a double chest worth of space");

        steamName = config.getString("steamName", "tweaks", "zedSteam", "Dictionary name of this mod's steam. set to 'steam' for use with other mods (not balanced)");
        steamFactor = config.getFloat("steamFactor", "tweaks", 1F, 0.1F, 5F, "Multiplier on steam produced");

        if (config.hasChanged()) {
            config.save();
        }
    }

    @SubscribeEvent
    public void onConfigurationChangedEvent(ConfigChangedEvent.OnConfigChangedEvent event) {
        if (event.modID.equalsIgnoreCase(Reference.MOD_ID)) {
            loadConfig();
        }
    }
}
