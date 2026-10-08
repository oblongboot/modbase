package com.example.mod.mixin;

import com.example.mod.events.EventManager;
import com.example.mod.events.impl.PacketReceive;
import com.example.mod.events.impl.PacketSend;
import io.netty.channel.ChannelFutureListener;
import net.minecraft.network.Connection;
import net.minecraft.network.PacketListener;
import net.minecraft.network.protocol.Packet;
import org.spongepowered.asm.mixin.Mixin;
import org.spongepowered.asm.mixin.injection.At;
import org.spongepowered.asm.mixin.injection.Inject;
import org.spongepowered.asm.mixin.injection.callback.CallbackInfo;

@Mixin(Connection.class)
public class MixinPacketEvent {
    @Inject(method = "genericsFtw", at = @At("HEAD"), cancellable = true)
    private static void modbase$onPacketIncoming(Packet<?> packet, PacketListener packetlistener, CallbackInfo ci) {
        PacketReceive packetTwo = new PacketReceive(packet);

        EventManager.post(packetTwo);

        if (packetTwo.getCancelled()) {
            ci.cancel();
        }
    }

    @Inject(method = "sendPacket", at = @At("HEAD"), cancellable = true)
    public void modbase$onPacketSend(Packet<?> packet, ChannelFutureListener listener, boolean flush, CallbackInfo ci) {
        PacketSend packetTwo = new PacketSend(packet);

        EventManager.post(packetTwo);

        if (packetTwo.getCancelled()) {
            ci.cancel();
        }
    }
}