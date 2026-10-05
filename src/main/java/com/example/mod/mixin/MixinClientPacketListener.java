package com.example.mod.mixin;

import com.example.mod.events.EventManager;
import com.example.mod.events.impl.ChatSend;
import net.minecraft.client.multiplayer.ClientPacketListener;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(ClientPacketListener.class)
public class MixinClientPacketListener {
    @Inject(method = "sendChat", at = @At("HEAD"), cancellable = true)
    public void modbase$onChatSend(String msg, CallbackInfo ci) {
        ChatSend cs = new ChatSend(msg);
        EventManager.INSTANCE.post(cs);

        if (cs.getCancelled()) {
            ci.cancel();
        }
    }
}