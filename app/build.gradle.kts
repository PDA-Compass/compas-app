import org.jetbrains.kotlin.kapt3.base.Kapt.kapt

plugins {
    id("com.android.application")
    kotlin("android")
    //id("kotlin-kapt")
}

android {
    compileSdkVersion(28)

    defaultConfig {
        applicationId = "net.afterday.compas"
        minSdkVersion(19)
        targetSdkVersion(28)
        versionCode = 1906
        versionName = "1906.0.0-alpha1"
        testInstrumentationRunner = "android.support.test.runner.AndroidJUnitRunner"
    }

    //TODO: remove
    lintOptions {
        isAbortOnError = true
        disable("UnusedResources") // https://issuetracker.google.com/issues/63150366
        disable("Instantiatable")
        disable("InvalidPackage")
        disable("VectorPath")
        disable("TrustAllX509TrustManager")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}
dependencies {
    //implementation fileTree(include: ['*.jar'], dir: 'libs')
    //androidTestImplementation('com.android.support.test.espresso:espresso-core:2.2.2', {
    //    exclude group: 'com.android.support', module: 'support-annotations'
    //})
    //debugImplementation 'com.squareup.leakcanary:leakcanary-android:1.5.4'
    //releaseImplementation 'com.squareup.leakcanary:leakcanary-android-no-op:1.5.4'


    implementation(project(":engine"))
    implementation(project(":games"))
    implementation("com.google.code.gson:gson:2.8.5")
    implementation("com.android.support.constraint:constraint-layout:1.1.3")
    implementation("com.android.support:appcompat-v7:28.0.0")
    implementation("com.android.support:percent:28.0.0")
    implementation("com.android.support:recyclerview-v7:28.0.0")
    implementation("com.journeyapps:zxing-android-embedded:3.6.0")
    implementation("com.android.support:support-v4:28.0.0")

    implementation("io.reactivex.rxjava3:rxjava:3.0.1")
    implementation("io.reactivex.rxjava3:rxkotlin:3.0.0")
    implementation("io.reactivex.rxjava3:rxandroid:3.0.0")
    implementation("net.sourceforge.streamsupport:streamsupport:1.5.3")
    implementation("com.google.dagger:dagger:2.5")

    implementation("com.squareup.retrofit2:converter-gson:2.8.1")
    implementation("com.squareup.retrofit2:retrofit:2.8.1")

    implementation("com.squareup.okhttp3:okhttp:4.6.0")

    compileOnly("javax.annotation:jsr250-api:1.0")

    //kapt("com.google.dagger:dagger-compiler:2.5")


    //testImplementation 'junit:junit:4.12'
    //implementation('com.crashlytics.sdk.android:crashlytics:2.9.4@aar') {
    //    transitive = true
    //}
}