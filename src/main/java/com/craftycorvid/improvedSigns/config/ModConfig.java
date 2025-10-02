package com.craftycorvid.improvedSigns.config;

import static com.craftycorvid.improvedSigns.ImprovedSignsMod.MOD_ID;

import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStreamWriter;
import java.io.Writer;
import java.nio.charset.StandardCharsets;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import net.fabricmc.loader.api.FabricLoader;

public class ModConfig {
    private static final Gson gson =
            new GsonBuilder().setPrettyPrinting().serializeNulls().disableHtmlEscaping().create();
    private static final File configFile =
            FabricLoader.getInstance().getConfigDir().resolve(MOD_ID + ".json").toFile();

    // Config values
    public boolean disableSignEditOnPlace = false;
    public boolean enableSignPassthrough = true;
    public boolean enableSignRetain = true;
    public boolean enableSignCopy = true;
    public boolean serverSideSignTextPreview = true;
    public boolean retainDyeOnSignCopy = false;

    public boolean enableFramePassthrough = true;
    public boolean enableInvisibleFrames = true;
    public InvisibleFrameEnum invisibleFrameItem = InvisibleFrameEnum.AMETHYST_SHARD;

    public enum InvisibleFrameEnum {
        AMETHYST_SHARD, GLASS_PANE
    }

    // Reading and saving
    public static ModConfig loadConfig() {
        ModConfig config = null;

        if (configFile.exists()) {
            // An existing config is present, we should use its values
            try (BufferedReader fileReader =
                    new BufferedReader(new InputStreamReader(new FileInputStream(configFile),
                            StandardCharsets.UTF_8))) {
                // Parses the config file and puts the values into config object
                config = gson.fromJson(fileReader, ModConfig.class);
            } catch (IOException e) {
                throw new RuntimeException(
                        "[improved-signs] Problem occurred when trying to load config: ", e);
            }
        }
        // gson.fromJson() can return null if file is empty
        if (config == null) {
            config = new ModConfig();
        }

        // Saves the file in order to write new fields if they were added
        config.saveConfig();
        return config;
    }

    public void saveConfig() {
        try (Writer writer =
                new OutputStreamWriter(new FileOutputStream(configFile), StandardCharsets.UTF_8)) {
            gson.toJson(this, writer);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
