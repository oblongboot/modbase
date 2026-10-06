package com.example.mod.module.annotation

/*
* this should be used for partially tested features but can still be very buggy, these features will only be registered in beta jars
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class BetaModule()
