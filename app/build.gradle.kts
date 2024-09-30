import java.util.Properties

plugins {
    id("com.android.application")
    id("org.jetbrains.kotlin.android")
    id("com.google.gms.google-services")
    id("kotlin-android")
    id("kotlin-kapt")
    id("kotlin-parcelize")
}

val projectProperties = Properties().apply {
    file("../settings.properties").inputStream().use { fis ->
        load(fis)
    }
}

android {
    namespace = "com.example.news"
    compileSdk = 34

    defaultConfig {
        applicationId = "com.example.news"
        minSdk = 26
        targetSdk = 34
        versionCode = 1
        versionName = "1.0.2"

        testInstrumentationRunner = "com.example.news.ui.utils.TestRunner"
        vectorDrawables {
            useSupportLibrary = true
        }
    }

    buildTypes {
        debug {
            buildConfigField("String", "apiKey", projectProperties["API_KEY"] as String)
        }
        release {
            buildConfigField("String", "apiKey", projectProperties["API_KEY"] as String)

            isMinifyEnabled = false
            proguardFiles(
                getDefaultProguardFile("proguard-android-optimize.txt"),
                "proguard-rules.pro"
            )
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_1_8
        targetCompatibility = JavaVersion.VERSION_1_8
    }
    kotlinOptions {
        jvmTarget = "1.8"
    }
    buildFeatures {
        viewBinding = true
        buildConfig = true
    }
    composeOptions {
        kotlinCompilerExtensionVersion = "1.4.3"
    }
    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    testOptions {
        unitTests {
            isIncludeAndroidResources = true
        }
    }
}

dependencies {
//    Main
    implementation("androidx.core:core-ktx:1.13.1")
    implementation("androidx.lifecycle:lifecycle-runtime-ktx:2.8.6")
    implementation("androidx.activity:activity-compose:1.9.2")
    implementation("com.google.android.material:material:1.12.0")
    implementation("androidx.databinding:viewbinding:8.6.1")
    implementation("org.jetbrains.kotlin:kotlin-stdlib:2.0.0")

//    UI Tools
    implementation("androidx.swiperefreshlayout:swiperefreshlayout:1.1.0")
    implementation("io.github.ahmad-hamwi:tabsync:1.0.1")
    implementation("com.alexvasilkov:gesture-views:2.8.2")
    implementation("com.github.bumptech.glide:glide:4.16.0")

//    Dagger
    implementation("com.google.dagger:dagger:2.51.1")
    implementation("androidx.test:runner:1.6.2")
    implementation("com.android.identity:identity-credential-android:20231002")
    kapt("com.google.dagger:dagger-compiler:2.51.1")
    kapt("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.6.2")

//    Retrofit
    implementation("com.squareup.retrofit2:retrofit:2.9.0")
    implementation("com.squareup.retrofit2:converter-gson:2.9.0")
    implementation("com.squareup.okhttp3:logging-interceptor:4.11.0")

//    Firebase
    implementation(platform("com.google.firebase:firebase-bom:33.3.0"))
    implementation("com.google.firebase:firebase-analytics-ktx")
    implementation("com.google.firebase:firebase-auth")
    implementation("com.google.firebase:firebase-firestore-ktx")
    implementation("com.google.firebase:firebase-storage")

//    Navigation
    implementation("androidx.navigation:navigation-fragment-ktx:2.8.1")
    implementation("androidx.navigation:navigation-ui-ktx:2.8.1")

//    Date Time
    implementation("net.danlew:android.joda:2.12.7")
    implementation("org.jetbrains.kotlinx:kotlinx-datetime:0.6.0-RC.2")

//    Test
    testImplementation("androidx.test:core:1.6.1")
    testImplementation("androidx.arch.core:core-testing:2.2.0")
    debugImplementation("androidx.fragment:fragment-testing:1.8.3")
    kaptTest("com.google.dagger:dagger-compiler:2.51.1")
    kaptTest("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.6.2")
    kaptAndroidTest("com.google.dagger:dagger-compiler:2.51.1")
    kaptAndroidTest("org.jetbrains.kotlinx:kotlinx-metadata-jvm:0.6.2")

//      JUnit4
    testImplementation("junit:junit:4.13.2")
    testImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("androidx.test.ext:junit:1.2.1")
    androidTestImplementation("junit:junit:4.13.2")

//      Mockito
    testImplementation("org.mockito:mockito-core:5.12.0")
    testImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    androidTestImplementation("org.mockito:mockito-core:5.12.0")
    androidTestImplementation("org.mockito.kotlin:mockito-kotlin:4.1.0")
    androidTestImplementation("org.mockito:mockito-android:5.12.0")

//      Robolectric
    testImplementation("org.robolectric:robolectric:4.12.2")

//      Espresso
    androidTestImplementation("androidx.test.espresso:espresso-core:3.6.1")
    debugImplementation("androidx.fragment:fragment-testing:1.8.3")
    implementation("androidx.test.uiautomator:uiautomator:2.3.0")
    androidTestImplementation("com.android.support.test.uiautomator:uiautomator-v18:2.1.3")
}