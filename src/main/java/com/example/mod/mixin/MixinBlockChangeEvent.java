package com.example.mod.mixin;

import com.example.mod.ModInit;
import com.example.mod.events.EventManager;
import com.example.mod.events.impl.BlockChangeEvent;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfoReturnable;

@Mixin(Level.class)
abstract class MixinBlockChangeEvent {
    @Inject(method = "setBlock", at = @At("HEAD"), cancellable = false)
    private void modbase$onBlockChange(BlockPos BP, BlockState newBlockState, int flags, CallbackInfoReturnable<Boolean> cir) {
        assert ModInit.getMc().level != null;
        BlockState oldState = ModInit.getMc().level.getBlockState(BP);

        if (oldState.getBlock() != newBlockState.getBlock()) {
            EventManager.post(new BlockChangeEvent(BP, oldState, newBlockState, flags));
        }
    }
}
