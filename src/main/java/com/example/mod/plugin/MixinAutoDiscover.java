package com.example.mod.plugin;

import com.example.mod.utils.ClassScanner;
import com.example.mod.utils.Logger;
import org.objectweb.asm.tree.ClassNode;
import org.spongepowered.asm.mixin.extensibility.IMixinConfigPlugin;
import org.spongepowered.asm.mixin.extensibility.IMixinInfo;

import java.io.File;
import java.io.IOException;
import java.net.JarURLConnection;
import java.net.URI;
import java.net.URL;
import java.util.ArrayList;
import java.util.Enumeration;
import java.util.List;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

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
