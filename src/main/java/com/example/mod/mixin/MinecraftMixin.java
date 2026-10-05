package com.example.mod.mixin;

import com.example.mod.events.EventManager;
import com.example.mod.events.impl.TickEnd;
import com.example.mod.events.impl.TickStart;
import net.minecraft.client.Minecraft;
import net.minecraft.server.MinecraftServer;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Minecraft.class)
public class MinecraftMixin {
	@Inject(at = @At("HEAD"), method = "tick")
	private void modbase$onTickStart(CallbackInfo info) {
		EventManager.INSTANCE.post(new TickStart());
	}

	@Inject(at = @At("RETURN"), method = "tick")
	private void modbase$onTickEnd(CallbackInfo ci) {
		EventManager.INSTANCE.post(new TickEnd());
	}
}