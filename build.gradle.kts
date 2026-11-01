import io.appmetrica.analytics.gradle.agp8.appmetrica
import java.util.Properties

val secrets = Properties().apply {
    val file = file("secrets.properties")
    if (file.exists()) file.reader().use { load(it) }
}

fun secret(property: String): String =
    secrets.getProperty(property)?.takeIf { it.isNotBlank() } ?: ""

val appMetricaApiKey = secret("appmetrica.apiKey")
val appMetricaPostApiKey = secret("appmetrica.postApiKey")
val signingStoreFile = secret("signing.storeFile")
val signingKeyAlias = secret("signing.keyAlias")
val signingStorePassword = secret("signing.storePassword")
val signingKeyPassword = secret("signing.keyPassword")

plugins {
    id("com.android.application") version "9.4.1"
    id("com.google.devtools.ksp") version "2.3.12"
    id("io.appmetrica.analytics") version "2.1.0"
}

kotlin {
    jvmToolchain(21)
}

android {
    bundle {
        storeArchive {
            enable = true
        }
    }

    namespace = "kolmachikhin.alexander.epictodolist"
    compileSdk = 37

    defaultConfig {
        applicationId = "kolmachikhin.alexander.epicto_dolist"
        minSdk = 31
        targetSdk = 37
        versionCode = 100
        versionName = "2.7.0"
        base.archivesName.set("epictodolist-$versionName")
        buildConfigField("String", "APPMETRICA_API_KEY", "\"$appMetricaApiKey\"")
    }

    signingConfigs {
        getByName("debug") {
            storeFile = file(signingStoreFile)
            keyAlias = signingKeyAlias
            storePassword = signingStorePassword
            keyPassword = signingKeyPassword
        }
        create("release") {
            storeFile = file(signingStoreFile)
            keyAlias = signingKeyAlias
            storePassword = signingStorePassword
            keyPassword = signingKeyPassword
        }
    }

    buildFeatures {
        buildConfig = true
    }

    buildTypes {
        debug {
            signingConfig = signingConfigs.getByName("debug")
            appmetrica {
                enable.set(false)
                enableAnalytics.set(false)
            }
        }
        release {
            signingConfig = signingConfigs.getByName("release")
            isMinifyEnabled = true
            isShrinkResources = true
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
            appmetrica {
                // TODO: enable mapping upload after AppMetrica leaves special mode.
                enable.set(false)
                enableAnalytics.set(false)
                postApiKey.set(appMetricaPostApiKey)
            }
        }
    }
}

dependencies {
    implementation("androidx.core:core-ktx:1.19.1")
    implementation("androidx.appcompat:appcompat:1.8.0")
    implementation("androidx.room:room-runtime:2.8.5")
    ksp("androidx.room:room-compiler:2.8.5")
    implementation("com.google.code.gson:gson:2.14.0")
    implementation("com.yandex.android:mobileads:8.6.0")
    implementation("io.appmetrica.analytics:analytics:8.6.0")
    implementation("com.github.devsideal:VectorChildFinder:1.0.0")
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
    implementation("com.daimajia.swipelayout:library:1.2.0")
}

configurations.configureEach {
    exclude(group = "io.appmetrica.analytics", module = "analytics-ad-revenue")
    exclude(group = "io.appmetrica.analytics", module = "analytics-apphud")
    exclude(group = "io.appmetrica.analytics", module = "analytics-billing")
    exclude(group = "io.appmetrica.analytics", module = "analytics-id-sync")
    exclude(group = "io.appmetrica.analytics", module = "analytics-location")
    exclude(group = "io.appmetrica.analytics", module = "analytics-ndkcrashes")
    exclude(group = "io.appmetrica.analytics", module = "analytics-screenshot")
}
