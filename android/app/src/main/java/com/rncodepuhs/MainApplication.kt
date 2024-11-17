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
import java.net.HttpURLConnection
import java.net.URL

class MainApplication : Application(), ReactApplication {

    override val reactNativeHost: ReactNativeHost =
        object : DefaultReactNativeHost(this) {

            override fun getUseDeveloperSupport(): Boolean = BuildConfig.DEBUG

            override fun getJSMainModuleName(): String = "index"

            override val isNewArchEnabled: Boolean = BuildConfig.IS_NEW_ARCHITECTURE_ENABLED

            override val isHermesEnabled: Boolean = BuildConfig.IS_HERMES_ENABLED

            override fun getJSBundleFile(): String? {
                val localBundlePath = "${applicationContext.filesDir}/index.android.bundle"

                // Check if the downloaded bundle exists
                if (File(localBundlePath).exists()) {
                    Log.d("MainApplication", "Using downloaded JS bundle: $localBundlePath")
                    return localBundlePath
                }

                // Fallback to the default assets bundle
                Log.d("MainApplication", "Using fallback JS bundle from assets")
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
        SoLoader.init(this, OpenSourceMergedSoMapping)
        downloadLatestBundle()
    }

    private fun downloadLatestBundle() {
        Log.d("MainApplication", "downloadLatestBundle")

        val bundleUrl = "https://github.com/MahmoudAbdAlKareem/ReactNativeCodePush/releases/download/static-release/index.android.bundle"
        val localBundlePath = "${applicationContext.filesDir}/index.android.bundle"

        Thread {
            try {
                Log.d("MainApplication", "Starting download from: $bundleUrl")
                val url = URL(bundleUrl)
                url.openStream().use { input ->
                    File(localBundlePath).outputStream().use { output ->
                        input.copyTo(output)
                    }
                }
                Log.d("MainApplication", "Downloaded bundle to: $localBundlePath")
            } catch (e: Exception) {
                Log.e("MainApplication", "Failed to download bundle: ${e.message}", e)
            }
        }.start()
    }


}
