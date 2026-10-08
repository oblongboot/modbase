package com.example.mod.module.impl

import com.example.mod.module.Module
import com.example.mod.module.ModuleCategory
import com.example.mod.module.annotation.DevModule

@DevModule
object ExampleDevModule : Module(
    id = "ExampleDevModule",
    name = "Example Dev Module",
    description = "Example module that is accessible to devs",
    category = ModuleCategory.MISC,
)