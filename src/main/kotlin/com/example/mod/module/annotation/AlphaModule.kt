package com.example.mod.module.annotation

/*
* this should be used for practically untested features that are very buggy, these features will only be registered in alpha jars
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class AlphaModule()
