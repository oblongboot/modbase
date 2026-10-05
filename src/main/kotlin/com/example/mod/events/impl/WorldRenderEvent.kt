package com.example.mod.events.impl

import com.example.mod.events.Event
import net.fabricmc.fabric.api.client.rendering.v1.level.LevelRenderContext
import net.minecraft.client.renderer.state.level.BlockOutlineRenderState

abstract class WorldRenderEvent() : Event(cancellable = false)

class WorldRenderBeforeBlockOutline(val context: LevelRenderContext, val state: BlockOutlineRenderState) : WorldRenderEvent()
class WorldRenderBeforeGizmos(val context: LevelRenderContext) : WorldRenderEvent()