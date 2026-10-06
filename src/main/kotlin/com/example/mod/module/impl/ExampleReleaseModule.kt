package com.example.mod.module.impl

import com.example.mod.module.Module
import com.example.mod.module.ModuleCategory
import com.example.mod.module.annotation.ReleaseModule

@ReleaseModule
object ExampleReleaseModule : Module(
    id = "ExampleReleaseModule",
    name = "Example Release Module",
    description = "Example module that is accessible always",
    category = ModuleCategory.MISC,
) {

}