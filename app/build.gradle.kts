plugins {
    alias(libs.plugins.android.application)
}

android {
    namespace = "com.example.lazabee"
    compileSdk = 36

    defaultConfig {
        applicationId = "com.example.lazabee"
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
        sourceCompatibility = JavaVersion.VERSION_11
        targetCompatibility = JavaVersion.VERSION_11
    }
    externalNativeBuild {
        cmake {
            path = file("src/main/cpp/CMakeLists.txt")
            version = "3.22.1"
        }
    }
}

dependencies {

    implementation(libs.appcompat)
    implementation(libs.material)
    implementation(libs.activity)
    implementation(libs.constraintlayout)
    testImplementation(libs.junit)
    androidTestImplementation(libs.ext.junit)
    androidTestImplementation(libs.espresso.core)
    
    // Google Services
    implementation("com.google.android.gms:play-services-auth:20.7.0")

    // UI Components
    implementation("com.google.android.material:material:1.9.0")
    implementation("androidx.recyclerview:recyclerview:1.3.0")
    implementation("androidx.viewpager2:viewpager2:1.1.0")
    implementation("androidx.cardview:cardview:1.0.0")
    
    // Image Loading
    implementation("com.github.bumptech.glide:glide:4.15.1")
    annotationProcessor("com.github.bumptech.glide:compiler:4.15.1")
    
    // Room Database (Local caching)
    implementation("androidx.room:room-runtime:2.6.1")
    annotationProcessor("androidx.room:room-compiler:2.6.1")
    
    // JSON Processing
    implementation("com.google.code.gson:gson:2.10.1")
}