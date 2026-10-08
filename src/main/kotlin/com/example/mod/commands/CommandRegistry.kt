package com.example.mod.commands

import com.example.mod.commands.annotation.CommandHandler
import com.example.mod.commands.annotation.SubCommand
import com.mojang.brigadier.CommandDispatcher
import com.mojang.brigadier.builder.LiteralArgumentBuilder
import net.fabricmc.fabric.api.client.command.v2.ClientCommandRegistrationCallback
import net.fabricmc.fabric.api.client.command.v2.FabricClientCommandSource
import kotlin.reflect.KFunction
import kotlin.reflect.full.declaredFunctions
import kotlin.reflect.full.findAnnotation
import kotlin.reflect.jvm.isAccessible

object CommandRegistry {
    private val commands = mutableMapOf<String, Command>()

    fun init() {
        ClientCommandRegistrationCallback.EVENT.register { dispatcher, _ ->
            registerCommands(dispatcher)
        }
    }

    fun register(vararg commands: Command) {
        commands.forEach(::register)
    }

    fun get(name: String): Command? =
        commands[name.lowercase()]

    fun getAll() = commands

    private fun register(command: Command) {
        command.names.forEach { name ->
            val lowerName = name.lowercase()

            if (commands.containsKey(lowerName)) {
                error("Command '$lowerName' is already registered")
            }

            commands[lowerName] = command
        }
    }

    private fun registerCommands(dispatcher: CommandDispatcher<FabricClientCommandSource>) {
        commands.values
            .distinct()
            .forEach { command ->
                command.names.forEach { name ->
                    registerCommand(dispatcher, name, command)
                }
            }
    }

    private fun registerCommand(dispatcher: CommandDispatcher<FabricClientCommandSource>, name: String, command: Command) {
        val root = LiteralArgumentBuilder.literal<FabricClientCommandSource>(name)

        command::class.declaredFunctions
            .mapNotNull(::getCommandFunction)
            .forEach { handler ->
                registerHandler(root, command, handler)
            }

        dispatcher.register(root)
    }

    private fun getCommandFunction(function: KFunction<*>): KFunction<*>? {
        val commandHandler = function.findAnnotation<CommandHandler>()
        val subCommand = function.findAnnotation<SubCommand>()

        if (commandHandler == null && subCommand == null) {
            return null
        }

        function.isAccessible = true
        return function
    }

    private fun registerHandler(root: LiteralArgumentBuilder<FabricClientCommandSource>, command: Command, function: KFunction<*>) {
        when {
            function.findAnnotation<CommandHandler>() != null -> {
                root.executes {
                    function.call(command)
                    1
                }
            }

            function.findAnnotation<SubCommand>() != null -> {
                val subCommand = LiteralArgumentBuilder.literal<FabricClientCommandSource>(function.name)

                subCommand.executes {
                    function.call(command)
                    1
                }

                root.then(subCommand)
            }
        }
    }
}
