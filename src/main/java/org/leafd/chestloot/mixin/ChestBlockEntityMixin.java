package org.leafd.chestloot.mixin;

import net.minecraft.block.entity.ChestBlockEntity;
import net.minecraft.nbt.NbtCompound;
import org.leafd.chestloot.util.UsedMarker;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.Unique;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ChestBlockEntity.class)
public abstract class ChestBlockEntityMixin implements UsedMarker {

    @Unique
    private boolean used = false;

    @Inject(method = "writeNbt", at = @At("TAIL"))
    private void onWriteNbt(NbtCompound nbt, CallbackInfo info) {
        nbt.putBoolean("used", this.used);
    }

    @Inject(method = "readNbt", at = @At("TAIL"))
    private void onReadNbt(NbtCompound nbt, CallbackInfo info) {
        this.used = nbt.getBoolean("used");
    }

    @Override
    public boolean pruebaCofre$isUsed() {
        return this.used;
    }

    @Override
    public void pruebaCofre$setUsed(boolean used) {
        this.used = used;
    }
}
