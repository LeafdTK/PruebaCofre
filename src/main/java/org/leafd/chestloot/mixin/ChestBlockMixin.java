package org.leafd.chestloot.mixin;

import net.minecraft.block.BlockState;
import net.minecraft.block.ChestBlock;
import net.minecraft.block.entity.BlockEntity;
import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.entity.LivingEntity;
import net.minecraft.entity.player.PlayerEntity;
import net.minecraft.item.ItemStack;
import net.minecraft.util.math.BlockPos;
import net.minecraft.world.World;
import org.leafd.chestloot.util.UsedMarker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlock.class)
public class ChestBlockMixin {

    @Inject(method = "onPlaced", at = @At("TAIL"))
    public void onPlaced(World world, BlockPos pos, BlockState state, LivingEntity placer, ItemStack itemStack, CallbackInfo info) {
        if (world.isClient()) return;

        if (placer instanceof PlayerEntity player) {
            if (!player.isCreative()) {
                BlockEntity blockEntity = world.getBlockEntity(pos);
                if (blockEntity instanceof ChestBlockEntity) {
                    UsedMarker usedMarker = (UsedMarker) blockEntity;
                    usedMarker.pruebaCofre$setUsed(true);
                    blockEntity.markDirty();
                }
            }
        }
    }
}
