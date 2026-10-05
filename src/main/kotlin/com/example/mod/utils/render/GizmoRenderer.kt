package com.example.mod.utils.render

import net.minecraft.core.BlockPos
import net.minecraft.gizmos.GizmoStyle
import net.minecraft.gizmos.Gizmos
import net.minecraft.util.ARGB
import net.minecraft.world.phys.AABB
import java.awt.Color

object GizmoRenderer {
    fun drawBlock(pos: AABB, color: Color, esp: Boolean, alphaTwo: Int = 50) {
        if (color.alpha == 0) return

        val mainColor = ARGB.color(color.alpha, color.red, color.green, color.blue)
        val fill = ARGB.color(alphaTwo, color.red, color.green, color.blue)

        val style = GizmoStyle.strokeAndFill(
            mainColor,
            5f,
            fill
        )

        Gizmos.cuboid(pos, style).apply {
            if (esp) setAlwaysOnTop()
        }
    }

    fun drawBlock(
        pos: BlockPos,
        color: Color,
        esp: Boolean,
        alphaTwo: Int = 50
    ) {
        drawBlock(pos.toAABB(), color, esp, alphaTwo)
    }

    fun BlockPos.toAABB(): AABB {
        return AABB(
            x.toDouble(),
            y.toDouble(),
            z.toDouble(),
            x + 1.0,
            y + 1.0,
            z + 1.0
        )
    }
}