package com.example.mod.events.impl

import com.example.mod.events.Event
import com.example.mod.events.EventManager
import com.mojang.authlib.GameProfile
import net.fabricmc.fabric.api.client.message.v1.ClientReceiveMessageEvents
import net.minecraft.client.multiplayer.chat.ChatListener
import net.minecraft.network.chat.ChatType
import net.minecraft.network.chat.Component
import net.minecraft.network.chat.PlayerChatMessage
import java.time.Instant

abstract class ChatEvent() : Event(cancellable = true)

class ChatSend(val msg: String) : ChatEvent()
class ChatReceive(val component: Component, val message: PlayerChatMessage?, val profile: GameProfile?, val bound: ChatType.Bound, val instant: Instant) : ChatEvent()