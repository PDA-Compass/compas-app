// Top-level build file where you can add configuration options common to all sub-projects/modules.
plugins {
    base
    kotlin("jvm") version "1.3.61" apply false
    id("io.gitlab.arturbosch.detekt") version "1.10.0"
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

subprojects {
    apply {
        plugin("io.gitlab.arturbosch.detekt")
    }

    detekt {
        ignoreFailures = true
        config = rootProject.files("config/detekt/detekt.yml")
        reports {
            html {
                enabled = true
                destination = file("build/reports/detekt.html")
            }
        }
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
        classpath("com.android.tools.build:gradle:3.6.2")
    }
}

dependencies {
    // Make the root project archives configuration depend on every subproject
    subprojects.forEach {
        archives(it)
    }
}
