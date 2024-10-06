package org.leafd.chestloot;

import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.registry.RegistryKey;
import net.minecraft.registry.entry.RegistryEntry;
import net.minecraft.util.Identifier;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import net.minecraft.world.biome.Biome;

import java.util.List;
import java.util.Optional;
import java.util.concurrent.ThreadLocalRandom;

public class LootTableHandler {

    /**
     * Assigns a loot table to a chest block entity based on the biome at the given position.
     *
     * @param blockEntity The chest block entity to assign the loot table to.
     * @param world       The world in which the chest is located.
     * @param pos         The position of the chest in the world.
     */
    public static void assignLootTable(ChestBlockEntity blockEntity, World world, BlockPos pos) {
        // Add null checks to prevent potential NullPointerExceptions
        if (blockEntity == null || world == null || pos == null) {
            return;
        }

        // Obtain the biome entry at the given position
        RegistryEntry<Biome> biomeEntry = world.getBiome(pos);

        // Get the registry key of the biome
        Optional<RegistryKey<Biome>> biomeKeyOptional = biomeEntry.getKey();

        biomeKeyOptional.ifPresent(biomeKey -> {
            Identifier biomeId = biomeKey.getValue();
            String biomeIdString = biomeId.toString();

            // Get the loot tables for the biome
            List<Identifier> lootTables = ConfigManager.getLootTablesForBiome(biomeIdString);

            // If no loot tables are defined for the biome, use the default list
            if (lootTables.isEmpty()) {
                lootTables = ConfigManager.getLootTablesForBiome("default");
                if (lootTables.isEmpty()) {
                    return;
                }
            }

            // Select a random loot table from the list
            ThreadLocalRandom random = ThreadLocalRandom.current();
            Identifier selectedLootTable = lootTables.get(random.nextInt(lootTables.size()));

            blockEntity.setLootTable(selectedLootTable, random.nextLong());
            blockEntity.markDirty();
        });
    }
}
