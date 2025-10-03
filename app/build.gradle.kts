import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlinAndroid)
    alias(libs.plugins.compose)
    alias(libs.plugins.detekt)
    alias(libs.plugins.ksp)
    alias(libs.plugins.room)
    alias(libs.plugins.serialization)
}

android {
    namespace = "ir.miare.androidcodechallenge"
    compileSdk = libs.versions.compileSDK.get().toInt()

    buildFeatures.buildConfig = true
    defaultConfig {
        applicationId = "ir.miare.androidcodechallenge"
        minSdk = libs.versions.minSDK.get().toInt()
        targetSdk = libs.versions.targetSDK.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    buildTypes {
        release {
            isMinifyEnabled = true
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
}

kotlin {
    compilerOptions {
        jvmTarget.set(JvmTarget.JVM_17)
        freeCompilerArgs.addAll(
            listOf(
                "-opt-in=androidx.compose.material3.ExperimentalMaterial3Api",
                "-opt-in=androidx.paging.ExperimentalPagingApi",
                "-opt-in=kotlinx.coroutines.ExperimentalCoroutinesApi",
            )
        )
    }
}

room {
    schemaDirectory("$projectDir/schemas")
}

dependencies {
    implementation(libs.activityCompose)
    implementation(libs.composeUi)
    implementation(libs.composeUiToolingPreview)
    implementation(libs.composeMaterial3)
    implementation(libs.coroutinesAndroid)
    implementation(libs.composeMaterialIcons)
    implementation(libs.serialization)

    implementation(libs.koinCore)
    implementation(libs.koinAndroid)
    implementation(libs.koinAndroidxCompose)

    implementation(libs.pagingCompose)
    implementation(libs.roomRuntime)
    implementation(libs.timber)
    implementation(libs.roomPaging)
    implementation(libs.composeNavigation)
    ksp(libs.roomCompiler)

    debugImplementation(libs.composeUiTooling)

    detektPlugins(libs.detektFormatting)

    testImplementation(libs.mockk)
    testImplementation(libs.kotestAssertion)
    testImplementation(libs.junit)
    testImplementation(libs.coroutinesTest)
    testImplementation(libs.pagingTesting)
}