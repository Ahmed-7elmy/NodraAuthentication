plugins {
    alias(libs.plugins.android.application)
    alias(libs.plugins.kotlin.android)
    alias(libs.plugins.kotlin.compose)
    id("com.google.gms.google-services")
    id("com.google.devtools.ksp")
    id("kotlin-parcelize")
}

android {
    namespace = "com.example.nodrah_project"
    compileSdk = 35

    defaultConfig {
        applicationId = "com.example.nodrah_project"
        minSdk = 24
        targetSdk = 35
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    kotlinOptions {
        jvmTarget = "11"
    }
    buildFeatures {
        compose = true
    }
}

dependencies {

    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.androidx.activity.compose)
    implementation(platform(libs.androidx.compose.bom))
    implementation(libs.androidx.ui)
    implementation(libs.androidx.ui.graphics)
    implementation(libs.androidx.ui.tooling.preview)
    implementation(libs.androidx.material3)
    implementation(libs.firebase.common.ktx)
    implementation(libs.firebase.auth.ktx)
    implementation(libs.androidx.animation.core.lint)
    testImplementation(libs.junit)
    androidTestImplementation(libs.androidx.junit)
    androidTestImplementation(libs.androidx.espresso.core)
    androidTestImplementation(platform(libs.androidx.compose.bom))
    androidTestImplementation(libs.androidx.ui.test.junit4)
    debugImplementation(libs.androidx.ui.tooling)
    debugImplementation(libs.androidx.ui.test.manifest)
    implementation ("androidx.navigation:navigation-compose:2.7.5")
    implementation(platform("com.google.firebase:firebase-bom:33.13.0"))
    implementation ("androidx.lifecycle:lifecycle-viewmodel-compose:2.6.1")
    implementation ("androidx.compose.material:material-icons-extended:1.5.4")
    implementation ("androidx.lifecycle:lifecycle-runtime-compose:2.6.2")
    implementation ("com.google.android.gms:play-services-auth:20.7.0")

    //retrofit
    implementation("com.squareup.retrofit2:retrofit:2.11.0")
    //Gson converter
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    //Glide
    implementation("com.github.bumptech.glide:glide:4.16.0")
    //swipe to refresh
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    //likeButton
    implementation ("com.google.android.material:material:1.6.0")
    ksp("androidx.room:room-compiler:2.6.1")
    implementation("androidx.room:room-runtime:2.7.1")

    implementation("androidx.navigation:navigation-compose:2.8.9")
    implementation ("androidx.compose.material:material-icons-extended:1.7.8")// Acc icon

    // Retrofit for networking
    implementation("com.squareup.retrofit2:converter-moshi:2.9.0")
    // Kotlin Coroutines for asynchronous operations
    implementation("org.jetbrains.kotlinx:kotlinx-coroutines-android:1.7.3")
    // Use the latest stable version
    implementation("androidx.lifecycle:lifecycle-viewmodel-compose:2.8.7")

    // Coil for image loading
    implementation("io.coil-kt:coil-compose:2.5.0")
    // Moshi for JSON parsing
    implementation("com.squareup.moshi:moshi-kotlin:1.15.0")
    implementation("androidx.media3:media3-ui:1.3.1") // If you've migrated fully to media3
    implementation("androidx.media3:media3-exoplayer:1.3.1")
    implementation ("com.facebook.android:facebook-login:15.1.0")
    implementation("androidx.compose.material:material-icons-extended")


}