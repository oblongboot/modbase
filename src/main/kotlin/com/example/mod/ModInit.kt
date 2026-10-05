package com.example.mod

import com.example.mod.commands.CommandRegistry
import com.example.mod.commands.impl.MainCommand
import com.example.mod.events.EventManager
import com.example.mod.events.FabricProvidedEvents
import com.example.mod.events.impl.BlockChangeEvent
import com.example.mod.events.impl.packet.impl.SelfVelocityChangeEvent
import com.example.mod.events.impl.packet.impl.SlotChangeEvent
import com.example.mod.events.impl.packet.impl.TeleportEvent
import com.example.mod.utils.ChatUtils
import com.example.mod.utils.Logger
import net.fabricmc.api.ModInitializer
import net.minecraft.resources.Identifier

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

		Logger.debug("mod init! :D")
	}

	fun id(path: String): Identifier = Identifier.fromNamespaceAndPath(MOD_ID, path)
}
