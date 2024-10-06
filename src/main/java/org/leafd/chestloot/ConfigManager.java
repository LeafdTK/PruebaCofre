package org.leafd.chestloot;

import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;
import net.minecraft.util.Identifier;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.lang.reflect.Type;
import java.nio.charset.StandardCharsets;
import java.nio.file.*;
import java.util.*;
import java.util.logging.Level;
import java.util.logging.Logger;

public class ConfigManager {

    private static final Path CONFIG_DIR = Paths.get("config", "chestloot");
    private static final Path CONFIG_FILE = CONFIG_DIR.resolve("chestloot.json");
    private static final Logger LOGGER = Logger.getLogger(ConfigManager.class.getName());

    private static Map<String, List<String>> biomeLootTableMap = new HashMap<>();

    /**
     * Loads the configuration from the JSON file.
     */
    public static void loadConfig() {
        try {

            if (Files.notExists(CONFIG_DIR)) {
                Files.createDirectories(CONFIG_DIR);
            }

            // If the config file does not exist, create the default configuration
            if (Files.notExists(CONFIG_FILE)) {
                createDefaultConfig();
                return;
            }

            try (BufferedReader reader = Files.newBufferedReader(CONFIG_FILE, StandardCharsets.UTF_8)) {
                Type type = new TypeToken<Map<String, List<String>>>() {}.getType();
                biomeLootTableMap = new Gson().fromJson(reader, type);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to load configuration", e);
        }
    }

    /**
     * Creates the default configuration and saves it to the config file.
     */
    private static void createDefaultConfig() {
        biomeLootTableMap = new HashMap<>();
        // Default configuration
        biomeLootTableMap.put("default", Collections.emptyList());
        biomeLootTableMap.put("minecraft:plains",
                Collections.singletonList("minecraft:chests/village/village_plains_house"));
        biomeLootTableMap.put("minecraft:desert",
                Collections.singletonList("minecraft:chests/desert_pyramid"));
        biomeLootTableMap.put("minecraft:forest",
                Arrays.asList(
                        "minecraft:chests/abandoned_mineshaft",
                        "minecraft:chests/stronghold_corridor"
                ));


        saveConfig();
        LOGGER.info("[ChestLoot] Configuration file created at " + CONFIG_FILE.toAbsolutePath());
    }

    /**
     * Saves the current configuration to the config file.
     */
    public static void saveConfig() {
        try {
            // Ensure the configuration directory exists
            if (Files.notExists(CONFIG_DIR)) {
                Files.createDirectories(CONFIG_DIR);
            }

            try (BufferedWriter writer = Files.newBufferedWriter(CONFIG_FILE, StandardCharsets.UTF_8)) {
                new Gson().toJson(biomeLootTableMap, writer);
            }
        } catch (IOException e) {
            LOGGER.log(Level.SEVERE, "Failed to save configuration", e);
        }
    }

    /**
     * Retrieves the loot tables for a given biome ID.
     *
     * @param biomeId The ID of the biome.
     * @return A list of Identifiers representing the loot tables.
     */
    public static List<Identifier> getLootTablesForBiome(String biomeId) {
        List<String> lootTables = biomeLootTableMap.getOrDefault(biomeId, biomeLootTableMap.get("default"));
        if (lootTables == null) {
            return Collections.emptyList();
        }

        List<Identifier> identifiers = new ArrayList<>();
        for (String lootTable : lootTables) {
            identifiers.add(new Identifier(lootTable));
        }
        return identifiers;
    }

    /**
     * Reloads the configuration from the config file.
     */
    public static void reloadConfig() {
        loadConfig();
    }
}
