package com.example.mod.plugin;

import com.example.mod.utils.ClassScanner;
import com.example.mod.utils.Logger;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;

import java.util.List;

@SuppressWarnings("unused")
public class MixinAutoDiscover implements IMixinConfigPlugin {
    private String mixinPackage;

    @Override
    public void onLoad(String mixinPackage) {
        Logger.debug("hey! (mixin plugin init :D)");
        this.mixinPackage = mixinPackage;
    }

    @Override
    public List<String> getMixins() {
        return ClassScanner.find(mixinPackage).stream().map(name -> name.substring(mixinPackage.length() + 1)).toList();
    }
}
