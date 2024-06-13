import com.android.build.api.dsl.ManagedVirtualDevice

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
        compilations.all {
            compilerOptions.configure {
                freeCompilerArgs.addAll(listOf("-linker-option", "--allow-shlib-undefined"))
            }
            cinterops {
                val libssl by creating
            }
        }
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.bouncycastle.prov)
        }
        commonMain.dependencies {
            implementation(libs.okio)
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