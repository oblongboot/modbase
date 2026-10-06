package com.example.mod.module.annotation

/*
* this should be used for private modules / developer only, these only register if you are in dev env and are not registered in compiled jar, u can change that tho
 */
@Target(AnnotationTarget.CLASS)
@Retention(AnnotationRetention.RUNTIME)
annotation class DevModule()
