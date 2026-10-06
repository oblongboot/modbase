package com.example.mod.module.impl

import com.example.mod.module.Module
import com.example.mod.module.ModuleCategory
import com.example.mod.module.annotation.BetaModule
import com.example.mod.module.annotation.ReleaseModule

@BetaModule
object ExampleBetaModule : Module(
    id = "ExampleBetaModule",
    name = "Example Beta Module",
    description = "Example module that is accessible to beta",
    category = ModuleCategory.MISC,
) {

}