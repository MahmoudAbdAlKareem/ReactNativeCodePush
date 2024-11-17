package com.rncodepuhs

import android.app.Application
import android.util.Log
import com.facebook.react.PackageList
import com.facebook.react.ReactApplication
import com.facebook.react.ReactHost
import com.facebook.react.ReactNativeHost
import com.facebook.react.ReactPackage
import com.facebook.react.defaults.DefaultNewArchitectureEntryPoint.load
import com.facebook.react.defaults.DefaultReactHost.getDefaultReactHost
import com.facebook.react.defaults.DefaultReactNativeHost
import com.facebook.react.soloader.OpenSourceMergedSoMapping
import com.facebook.soloader.SoLoader
import java.io.File
import java.net.URL

class MainApplication : Application(), ReactApplication {

    override val reactNativeHost: ReactNativeHost =
        object : DefaultReactNativeHost(this) {

            override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

            override fun getJSMainModuleName(): String = "index"

            override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED

            override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED

            // Always load a specific bundle from assets
            override fun getBundleAssetName(): String = "index.android.bundle"

            // Specify the path for the JS bundle if loading from assets or external storage
            override fun getJSBundleFile(): String? {
                val localBundlePath = "${applicationContext.filesDir}/index.android.bundle"

                // Check if the local bundle exists
                if (File(localBundlePath).exists()) {
                    return localBundlePath
                }

                // Fallback to the assets bundle if the local one is not available
                return super.getJSBundleFile()
            }

            override fun getPackages(): List<ReactPackage> =
                PackageList(this).packages.apply {
                    // Add any custom packages here if necessary
                }
        }

    override val reactHost: ReactHost
        get() = getDefaultReactHost(applicationContext, reactNativeHost)

    override fun onCreate() {
        super.onCreate()
        downloadLatestBundle()
        SoLoader.init(this, OpenSourceMergedSoMapping)
        Log.d("MainApplication", "Application initialized with bundle: index.android.bundle")

        if (BuildConfig.IS_NEW_ARCHITECTURE_ENABLED) {
            // If you opted-in for the New Architecture, we load the native entry point for this app.
            load()

        }
    }

    private fun downloadLatestBundle() {
        val bundleUrl = "https://github.com/MahmoudAbdAlKareem/ReactNativeCodePush/dist/index.android.bundle"
        val localBundlePath = "${applicationContext.filesDir}/index.android.bundle"

        try {
            val url = URL(bundleUrl)
            url.openStream().use { input ->
                File(localBundlePath).outputStream().use { output ->
                    input.copyTo(output)
                }
            }
        } catch (e: Exception) {
            Log.e("MainApplication", "Failed to download bundle", e)
        }
    }
}