package org.leafd.chestloot.events;

import net.fabricmc.fabric.api.event.player.UseBlockCallback;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.util.ActionResult;
import net.minecraft.util.Hand;
import net.minecraft.util.hit.BlockHitResult;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.leafd.chestloot.LootTableHandler;
import org.leafd.chestloot.util.UsedMarker;

public class ChestOpenHandler implements UseBlockCallback {

    /**
     * Called when a player interacts with a block. This method checks if the
     * block is a chest and assigns a biome-specific loot table to it if it hasn't
     * already been opened.
     *
     * @param player     The player who interacted with the block.
     * @param world      The world in which the interaction occurred.
     * @param hand       The hand the player used to interact with the block.
     * @param hitResult  The result of the block hit, including block position and hit data.
     * @return           ActionResult.PASS to let the event continue, or ActionResult.SUCCESS if the chest is marked as used.
     */
    @Override
    public ActionResult interact(PlayerEntity player, World world, Hand hand, BlockHitResult hitResult) {
        // Return if the world is client-side
        if (world.isClient()) {
            return ActionResult.PASS;
        }

        BlockPos pos = hitResult.getBlockPos();
        BlockEntity blockEntity = world.getBlockEntity(pos);

        // Check if the block entity is a chest
        if (blockEntity instanceof ChestBlockEntity chestEntity) {
            UsedMarker usedMarker = (UsedMarker) chestEntity;

            // Check if the chest has not been used yet
            if (!usedMarker.pruebaCofre$isUsed()) {
                // Assign a loot table based on the biome
                LootTableHandler.assignLootTable(chestEntity, world, pos);

                // Mark the chest as used
                usedMarker.pruebaCofre$setUsed(true);
                chestEntity.markDirty();
            }
        }

        return ActionResult.PASS;
    }
}
