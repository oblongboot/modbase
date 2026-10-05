package com.example.mod.events.impl

import com.example.mod.events.Event
import net.minecraft.core.BlockPos
import net.minecraft.world.level.block.state.BlockState

class BlockChangeEvent(val BP: BlockPos, val oldState: BlockState, val newBlockState: BlockState, val flags: Int): Event(cancellable = false)