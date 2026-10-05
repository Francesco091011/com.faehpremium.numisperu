plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.faehpremium.numisperu"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.faehpremium.numisperu"
        minSdk = 16
        targetSdk = 35
        versionCode = 2
        versionName = "0.1.2"

        multiDexEnabled = true
        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            optimization {
                enable = false
            }
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
}

dependencies {
    implementation(libs.androidx.appcompat)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.recyclerview)
    implementation(libs.androidx.gridlayout)
    implementation(libs.androidx.cardview)
    implementation(libs.androidx.multidex)
    implementation(libs.material)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(libs.androidx.junit)
}