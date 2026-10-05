package com.example.mod

import com.example.mod.commands.CommandRegistry
import com.example.mod.commands.impl.MainCommand
import com.example.mod.events.EventManager
import com.example.mod.events.FabricProvidedEvents
import com.example.mod.events.impl.BlockChangeEvent
import com.example.mod.events.impl.ChatSend
import com.example.mod.events.impl.WorldRenderBeforeGizmos
import com.example.mod.events.impl.packet.EntityVelocityChangeEvent
import com.example.mod.events.impl.packet.SelfVelocityChangeEvent
import com.example.mod.events.impl.packet.TeleportEvent
import com.example.mod.utils.ChatUtils
import com.example.mod.utils.Logger
import com.example.mod.utils.render.GizmoRenderer
import net.fabricmc.api.ModInitializer
import net.minecraft.client.Minecraft
import net.minecraft.core.BlockPos
import net.minecraft.resources.Identifier
import org.slf4j.LoggerFactory
import java.awt.Color

object ModInit : ModInitializer {
	const val MOD_ID: String = "modbase"
	const val MOD_NAME: String = "modbase"
	const val MOD_VERSION: String = "1.0.0"

	override fun onInitialize() {
		CommandRegistry.register(
			MainCommand
		)

		FabricProvidedEvents.register()

		CommandRegistry.init()

		EventManager.register(BlockChangeEvent::class, {
			ChatUtils.modMessage("block changed !!! bp:${it.BP}, oldState:${it.oldState}, newState:${it.newBlockState}")
		})

		Logger.debug("mod init! :D")
	}

	fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)
}
