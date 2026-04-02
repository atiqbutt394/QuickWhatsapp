package com.atiq.quickwhatsapp.utils

import android.content.Context
import android.content.Intent
import android.net.Uri

object WhatsAppLauncher {

    private const val WA_PACKAGE = "com.whatsapp"
    private const val WA_BUSINESS_PACKAGE = "com.whatsapp.w4b"

    fun open(context: Context, url: String, useBusinessApp: Boolean): LaunchResult {
        val packageName = if (useBusinessApp) WA_BUSINESS_PACKAGE else WA_PACKAGE

        if (!isInstalled(context, packageName)) {
            return if (useBusinessApp) LaunchResult.BusinessNotInstalled
            else LaunchResult.WhatsAppNotInstalled
        }

        val intent = Intent(Intent.ACTION_VIEW, Uri.parse(url)).apply {
            setPackage(packageName)
        }
        context.startActivity(intent)
        return LaunchResult.Success
    }

    private fun isInstalled(context: Context, packageName: String): Boolean {
        return try {
            context.packageManager.getPackageInfo(packageName, 0)
            true
        } catch (e: Exception) {
            false
        }
    }
}

sealed class LaunchResult {
    object Success : LaunchResult()
    object WhatsAppNotInstalled : LaunchResult()
    object BusinessNotInstalled : LaunchResult()
}