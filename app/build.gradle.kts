plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    id("kotlin-kapt")
    id("com.google.dagger.hilt.android")
    id ("androidx.navigation.safeargs.kotlin")
    id("com.google.gms.google-services")
}

android {
    namespace = "com.TFG_JCF.fittrack"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.TFG_JCF.fittrack"
        minSdk = 24
        targetSdk = 36
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
    kotlinOptions {
        jvmTarget = "17"
    }
    buildFeatures{
        viewBinding = true
        buildConfig = true
    }
}

kapt {
    correctErrorTypes = true
    // Ayuda a ver errores reales de KAPT cuando falla
    showProcessorStats = true
}

dependencies {
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.appcompat)
    implementation(libs.material)
    implementation(libs.androidx.activity)
    implementation(libs.androidx.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)

    val navVersion = "2.8.8"

    // NavComponent
    implementation("androidx.navigation:navigation-fragment-ktx:${navVersion}")
    implementation("androidx.navigation:navigation-ui-ktx:${navVersion}")

    //DaggerHilt
    implementation("com.google.dagger:hilt-android:2.55")
    kapt("com.google.dagger:hilt-compiler:2.55")

    //Retrofit
    implementation ("com.squareup.retrofit2:retrofit:2.11.0")
    implementation ("com.squareup.retrofit2:converter-gson:2.11.0")
    implementation ("com.squareup.okhttp3:logging-interceptor:4.12.0")

    // ViewModel + LiveData con corutinas
    implementation("androidx.lifecycle:lifecycle-viewmodel-ktx:2.8.7")
    implementation("androidx.lifecycle:lifecycle-livedata-ktx:2.8.7")

    // Coroutines Android
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.9.0")

    //Coil
    implementation("io.coil-kt:coil:2.7.0")

    // Room
    implementation("androidx.room:room-ktx:2.7.0")
    kapt("androidx.room:room-compiler:2.7.0")

    //Firebase
    implementation(platform("com.google.firebase:firebase-bom:34.9.0"))
    implementation("com.firebaseui:firebase-ui-auth:9.1.1")

    implementation("androidx.credentials:credentials:1.3.0")
    implementation("androidx.credentials:credentials-play-services-auth:1.3.0")
    implementation("com.google.android.libraries.identity.googleid:googleid:1.1.1")

    //MPAndroidchart
    implementation("com.github.PhilJay:MPAndroidChart:v3.1.0")
}
