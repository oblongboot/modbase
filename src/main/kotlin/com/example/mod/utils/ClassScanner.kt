package com.example.mod.utils

import java.io.File
import java.net.JarURLConnection
import java.util.jar.JarFile

object ClassScanner {
    @JvmStatic
    fun find(packageName: String): List<String> {
        val packagePath = packageName.replace('.', '/')
        val classes = mutableListOf<String>()

        val resources = Thread.currentThread().contextClassLoader.getResources(packagePath)

        while (resources.hasMoreElements()) {
            val url = resources.nextElement()

            when (url.protocol) {
                "jar" -> {
                    val connection = url.openConnection() as JarURLConnection

                    connection.jarFile.use { jar ->
                        scanJar(
                            jar = jar,
                            packagePath = packagePath,
                            classes = classes,
                        )
                    }
                }

                "file" -> {
                    scanDirectory(
                        directory = File(url.toURI()),
                        packageName = packageName,
                        classes = classes,
                    )
                }
            }
        }

        return classes
    }

    private fun scanJar(
        jar: JarFile,
        packagePath: String,
        classes: MutableList<String>,
    ) {
        val entries = jar.entries()

        while (entries.hasMoreElements()) {
            val entry = entries.nextElement()

            if (entry.isDirectory) {
                continue
            }

            val name = entry.name

            if (!name.startsWith("$packagePath/")) {
                continue
            }

            if (!name.endsWith(".class")) {
                continue
            }

            if ('$' in name) {
                continue
            }

            classes.add(
                name.removeSuffix(".class").replace('/', '.'),
            )
        }
    }

    private fun scanDirectory(
        directory: File,
        packageName: String,
        classes: MutableList<String>,
    ) {
        val files = directory.listFiles() ?: return

        for (file in files) {
            if (file.isDirectory) {
                scanDirectory(
                    directory = file,
                    packageName = "$packageName.${file.name}",
                    classes = classes,
                )

                continue
            }

            if (!file.name.endsWith(".class")) {
                continue
            }

            if ('$' in file.name) {
                continue
            }

            classes.add(
                "$packageName.${file.name.removeSuffix(".class")}"
            )
        }
    }
}
