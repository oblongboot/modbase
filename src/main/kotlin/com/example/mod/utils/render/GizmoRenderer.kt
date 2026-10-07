package com.example.mod.utils.render

import net.minecraft.core.BlockPos
import net.minecraft.gizmos.GizmoStyle
import net.minecraft.gizmos.Gizmos
import net.minecraft.util.ARGB
import net.minecraft.world.phys.AABB
import net.minecraft.world.phys.Vec3
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

    fun drawLine(
        from: Vec3,
        to: Vec3,
        color: Color,
        esp: Boolean = false,
        alphaTwo: Int = 50,
        persistTime: Int = 5000
    ) {
        if (color.alpha == 0) return

        val mainColor = ARGB.color(alphaTwo, color.red, color.green, color.blue)

        val line = Gizmos.line(from, to, mainColor, 50f)

        line.apply {
            if (esp) setAlwaysOnTop()
            persistForMillis(persistTime)
        }
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