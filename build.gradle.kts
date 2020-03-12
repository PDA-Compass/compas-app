// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    base
    kotlin("jvm") version "1.3.61" apply false
}

allprojects {
    version = "1813.0"
    group = "net.afterday.compas"
    repositories {
        jcenter()
        google()
        mavenCentral()
    }
}

buildscript {
    repositories {
        jcenter()
        google()
        mavenCentral()
        //gradlePluginPortal()
    }
    dependencies {
        classpath("com.android.tools.build:gradle:3.5.3")
    }
}

dependencies {
    // Make the root project archives configuration depend on every subproject
    subprojects.forEach {
        archives(it)
    }
}
