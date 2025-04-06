package com.craftycorvid.improvedSigns.config;

import com.craftycorvid.improvedSigns.ImprovedSignsMod;

import eu.midnightdust.lib.config.MidnightConfig;

public class ModConfig extends MidnightConfig {

    @Comment(centered = true)
    public static Comment sectionSigns;
    @Entry
    public static boolean disableSignEditOnPlace = false;
    @Entry
    public static boolean enableSignPassthrough = true;
    @Entry
    public static boolean enableSignRetain = true;
    @Entry
    public static boolean enableSignCopy = true;
    @Entry
    public static boolean serverSideSignTextPreview = true;
    @Entry
    public static boolean retainDyeOnSignCopy = false;

    @Comment(centered = true)
    public static Comment sectionItemFrames;
    @Entry
    public static boolean enableFramePassthrough = true;
    @Entry
    public static boolean enableInvisibleFrames = true;
    @Entry
    public static InvisibleFrameEnum invisibleFrameItem = InvisibleFrameEnum.AMETHYST_SHARD;

    public enum InvisibleFrameEnum {
        AMETHYST_SHARD, GLASS_PANE
    }

    public static void init() {
        MidnightConfig.init(ImprovedSignsMod.MOD_ID, ModConfig.class);
    }
}
