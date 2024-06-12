import com.android.build.api.dsl.ManagedVirtualDevice
import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidLibrary)
}

kotlin {
    jvm()
    androidTarget {
        compilations.all {
            kotlinOptions {
                jvmTarget = "11"
            }
        }
    }

    linuxX64 {
        compilations["main"].cinterops {
            val libssl by creating
        }
    }

    @OptIn(ExperimentalKotlinGradlePluginApi::class)
    compilerOptions {
        freeCompilerArgs.addAll(listOf("-linker-option", "--allow-shlib-undefined"))
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.bouncycastle.prov)
        }
        commonMain.dependencies {
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
    }
}

android {
    namespace = "io.karma.advcrypto"
    compileSdk = libs.versions.android.sdk.compile.get().toInt()

    defaultConfig {
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
        minSdk = libs.versions.android.sdk.min.get().toInt()
    }

    packaging {
        resources.excludes.add("META-INF/versions/9/OSGI-INF/*")
    }

    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }

    testOptions {
        managedDevices {
            devices {
                maybeCreate<ManagedVirtualDevice>("pixel5").apply {
                    device = "Pixel 5"
                    apiLevel = libs.versions.android.sdk.compile.get().toInt()
                    systemImageSource = "aosp"
                }
            }
        }
    }
}


dependencies {
    //Android integration tests
    testImplementation(libs.androidx.test.core)
    androidTestImplementation(libs.ui.test.junit4)
}