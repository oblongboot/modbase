package com.example.mod.module.impl

import com.example.mod.module.Module
import com.example.mod.module.ModuleCategory
import com.example.mod.module.annotation.AlphaModule

@AlphaModule
object ExampleAlphaModule : Module(
    id = "ExampleAlphaModule",
    name = "Example Alpha Module",
    description = "Example module that is accessible to alpha",
    category = ModuleCategory.MISC,
)