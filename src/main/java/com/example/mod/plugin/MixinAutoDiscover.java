package com.example.mod.plugin;

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
    private List<String> mixins = new ArrayList<>();

    @Override
    public void onLoad(String mixinPackage) {
        Logger.debug("hey!");
        this.mixinPackage = mixinPackage;
    }

    @Override
    public List<String> getMixins() {
        String packagePath = mixinPackage.replace('.', '/');
        Logger.debug("getting mixins");
        try {
            Enumeration<URL> resources = Thread.currentThread().getContextClassLoader().getResources(packagePath);
            Logger.debug("in try");
            while (resources.hasMoreElements()) {
                URL url = resources.nextElement();
                Logger.debug("while: " + url);
                Logger.debug("protocol: " + url.getProtocol());
                if ("jar".equals(url.getProtocol())) {
                    Logger.debug("jar");
                    JarURLConnection connection = (JarURLConnection) url.openConnection();

                    scanJar(connection.getJarFile(), packagePath, mixins);
                } else if ("file".equals(url.getProtocol())) {
                    scanDirectory(new File(url.toURI()), mixins);
                }
            }
        } catch (Exception e) {
            throw new RuntimeException(
                    "Failed to discover mixins in " + mixinPackage,
                    e
            );
        }

        return mixins;
    }

    private void scanJar(JarFile jar, String packagePath, List<String> mixins) {
        Enumeration<JarEntry> entries = jar.entries();

        while (entries.hasMoreElements()) {
            JarEntry entry = entries.nextElement();

            String name = entry.getName();

            if (!name.startsWith(packagePath + "/")) {
                continue;
            }

            if (!name.endsWith(".class")) {
                continue;
            }

            if (name.contains("$")) {
                continue;
            }

            String className = name.substring(0, name.length() - ".class".length()).replace('/', '.');
            Logger.debug(className);
            mixins.add(className.substring(mixinPackage.length() + 1));
        }
    }

    private void scanDirectory(File directory, List<String> mixins) {
        File[] files = directory.listFiles();

        if (files == null) {
            return;
        }

        for (File file : files) {
            if (!file.isFile()) {
                continue;
            }

            String name = file.getName();

            if (!name.endsWith(".class")) {
                continue;
            }

            if (name.contains("$")) {
                continue;
            }

            String className = name.substring(0, name.length() - ".class".length());

            Logger.debug("found mixin: " + className);

            mixins.add(className);
        }
    }

}
