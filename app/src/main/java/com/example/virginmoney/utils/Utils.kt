package com.example.virginmoney.utils

import android.animation.ArgbEvaluator
import android.animation.ValueAnimator
import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.content.pm.ResolveInfo
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.VibrationEffect
import android.os.Vibrator
import android.text.TextUtils
import android.view.View
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.view.inputmethod.InputMethodManager
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.browser.customtabs.CustomTabsIntent
import androidx.browser.customtabs.CustomTabsService
import androidx.core.graphics.ColorUtils
import com.example.virginmoney.R

object Utils {

    /**
     * Plays an animation where the view "vibrates".
     */
    fun View.shake() {
        val animShake: Animation = AnimationUtils.loadAnimation(this.context, R.anim.shake)
        this.startAnimation(animShake)
        this.context.vibratePhone()
    }

    /**
     * Checks whether a string matches an email pattern
     * @return true is the string matches an email pattern, false if not and if string is empty
     */
    fun String.isValidEmail(): Boolean {
        return !TextUtils.isEmpty(this) && android.util.Patterns.EMAIL_ADDRESS.matcher(this)
            .matches()
    }

    fun View.hideKeyboard() {
        val imm = this.context.getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
        imm.hideSoftInputFromWindow(windowToken, 0)
    }

    fun calculateBrightness(bitmap: Bitmap, skipPixel: Int): Int {
        var R = 0
        var G = 0
        var B = 0
        val height = bitmap.height
        val width = bitmap.width
        var n = 0
        val pixels = IntArray(width * height)
        bitmap.getPixels(pixels, 0, width, 0, 0, width, height)
        var i = 0
        while (i < pixels.size) {
            val color = pixels[i]
            R += Color.red(color)
            G += Color.green(color)
            B += Color.blue(color)
            n++
            i += skipPixel
        }
        return (R + B + G) / (n * 3)
    }

    fun isDark(color: Int): Boolean {
        if (color == 0) {
            return false
        }
        return ColorUtils.calculateLuminance(color) < 0.5
    }

    fun setLightStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = activity.window.decorView.systemUiVisibility
            flags = flags or View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR
            activity.window.decorView.systemUiVisibility = flags
        }
    }

    fun clearLightStatusBar(activity: Activity) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            var flags = activity.window.decorView.systemUiVisibility
            flags = flags and View.SYSTEM_UI_FLAG_LIGHT_STATUS_BAR.inv()
            activity.window.decorView.systemUiVisibility = flags
        }
    }

    fun View.colorTransition(endColor: Int, duration: Long = 250L) {
        var colorFrom = Color.TRANSPARENT
        if (background is ColorDrawable)
            colorFrom = (background as ColorDrawable).color

        val colorAnimation: ValueAnimator =
            ValueAnimator.ofObject(ArgbEvaluator(), colorFrom, endColor)
        colorAnimation.duration = duration

        colorAnimation.addUpdateListener {
            if (it.animatedValue is Int) {
                val color = it.animatedValue as Int
                setBackgroundColor(color)
            }
        }
        colorAnimation.start()
    }

    fun Context.vibratePhone() {
        val vibrator = this.getSystemService(Context.VIBRATOR_SERVICE) as Vibrator
        if (Build.VERSION.SDK_INT >= 26) {
            vibrator.vibrate(VibrationEffect.createOneShot(200, VibrationEffect.DEFAULT_AMPLITUDE))
        } else {
            vibrator.vibrate(200)
        }
    }

    fun openUrl(context: Context, url: String?, color: Int = Color.BLACK) {
        if (getCustomTabsPackages(context, Uri.parse(url)).size > 0) {
            val builder: CustomTabsIntent.Builder = CustomTabsIntent.Builder()
            builder.setNavigationBarColor(color)
            builder.setToolbarColor(color)
            builder.setShowTitle(true)
            val customTabsIntent: CustomTabsIntent = builder.build()
            val browserIntent = Intent(Intent.ACTION_VIEW, Uri.parse("http://"))
            val resolveInfo = context.packageManager.resolveActivity(
                browserIntent,
                PackageManager.MATCH_DEFAULT_ONLY
            )
            if ((resolveInfo != null) && resolveInfo.activityInfo.packageName.isNotEmpty()) {
                customTabsIntent.intent.setPackage(resolveInfo.activityInfo.packageName)
            }
            url?.let {
                customTabsIntent.launchUrl(context, Uri.parse(url))
            }

        } else {
            Toast.makeText(
                context,
                "Couldn't find an app to open the web page.",
                Toast.LENGTH_SHORT
            ).show()
        }
    }

    private fun getCustomTabsPackages(context: Context, url: Uri?): MutableList<ResolveInfo> {
        val pm = context.packageManager
        // Get default VIEW intent handler.
        val activityIntent = Intent(Intent.ACTION_VIEW, url)

        // Get all apps that can handle VIEW intents.
        val resolvedActivityList = pm.queryIntentActivities(activityIntent, 0)
        val packagesSupportingCustomTabs: MutableList<ResolveInfo> = mutableListOf()
        for (info in resolvedActivityList) {
            val serviceIntent = Intent()
            serviceIntent.action = CustomTabsService.ACTION_CUSTOM_TABS_CONNECTION
            serviceIntent.setPackage(info.activityInfo.packageName)
            // Check if this package also resolves the Custom Tabs service.
            if (pm.resolveService(serviceIntent, 0) != null) {
                packagesSupportingCustomTabs.add(info)
            }
        }
        return packagesSupportingCustomTabs
    }

    val colorMap: HashMap<String, String> = hashMapOf(
        "black" to "000000",
        "white" to "FFFFFF",
        "red" to "FF0000",
        "lime" to "00FF00",
        "blue" to "0000FF",
        "yellow" to "FFFF00",
        "cyan" to "00FFFF",
        "magenta" to "FF00FF",
        "gray" to "808080",
        "orange" to "FFA500",
        "silver" to "C0C0C0",
        "purple" to "800080",
        "maroon" to "800000",
        "fuchsia" to "FF00FF",
        "green" to "008000",
        "olive" to "808000",
        "navy" to "000080",
        "teal" to "008080",
        "aqua" to "00FFFF",
        "aliceblue" to "F0F8FF",
        "antiquewhite" to "FAEBD7",
        "aquamarine" to "7FFFD4",
        "azure" to "F0FFFF",
        "beige" to "F5F5DC",
        "bisque" to "FFE4C4",
        "blanchedalmond" to "FFEBCD",
        "blueviolet" to "8A2BE2",
        "brown" to "A52A2A",
        "burlywood" to "DEB887",
        "cadetblue" to "5F9EA0",
        "chartreuse" to "7FFF00",
        "chocolate" to "D2691E",
        "coral" to "FF7F50",
        "cornflowerblue" to "6495ED",
        "cornsilk" to "FFF8DC",
        "crimson" to "DC143C",
        "darkblue" to "00008B",
        "darkcyan" to "008B8B",
        "darkgoldenrod" to "B8860B",
        "darkgray" to "A9A9A9",
        "darkgreen" to "006400",
        "darkkhaki" to "BDB76B",
        "darkmagenta" to "8B008B",
        "darkolivegreen" to "556B2F",
        "darkorange" to "FF8C00",
        "darkorchid" to "9932CC",
        "darkred" to "8B0000",
        "darksalmon" to "E9967A",
        "darkseagreen" to "8FBC8F",
        "darkslateblue" to "483D8B",
        "darkslategray" to "2F4F4F",
        "darkturquoise" to "00CED1",
        "darkviolet" to "9400D3",
        "deeppink" to "FF1493",
        "deepskyblue" to "00BFFF",
        "dimgray" to "696969",
        "dodgerblue" to "1E90FF",
        "firebrick" to "B22222",
        "floralwhite" to "FFFAF0",
        "forestgreen" to "228B22",
        "gainsboro" to "DCDCDC",
        "ghostwhite" to "F8F8FF",
        "gold" to "FFD700",
        "goldenrod" to "DAA520",
        "greenyellow" to "ADFF2F",
        "honeydew" to "F0FFF0",
        "hotpink" to "FF69B4",
        "indianred" to "CD5C5C",
        "indigo" to "4B0082",
        "ivory" to "FFFFF0",
        "khaki" to "F0E68C",
        "lavender" to "E6E6FA",
        "lavenderblush" to "FFF0F5",
        "lawngreen" to "7CFC00",
        "lemonchiffon" to "FFFACD",
        "lightblue" to "ADD8E6",
        "lightcoral" to "F08080",
        "lightcyan" to "E0FFFF",
        "lightgoldenrodyellow" to "FAFAD2",
        "lightgreen" to "90EE90",
        "lightgray" to "D3D3D3",
        "lightpink" to "FFB6C1",
        "lightsalmon" to "FFA07A",
        "lightseagreen" to "20B2AA",
        "lightskyblue" to "87CEFA",
        "lightslategray" to "778899",
        "lightsteelblue" to "B0C4DE",
        "lightyellow" to "FFFFE0",
        "limegreen" to "32CD32",
        "linen" to "FAF0E6",
        "magenta" to "FF00FF",
        "mediumaquamarine" to "66CDAA",
        "mediumblue" to "0000CD",
        "mediumorchid" to "BA55D3",
        "mediumpurple" to "9370DB",
        "mediumseagreen" to "3CB371",
        "mediumslateblue" to "7B68EE",
        "mediumspringgreen" to "00FA9A",
        "mediumturquoise" to "48D1CC",
        "mediumvioletred" to "C71585",
        "midnightblue" to "191970",
        "mintcream" to "F5FFFA",
        "mistyrose" to "FFE4E1",
        "moccasin" to "FFE4B5",
        "navajowhite" to "FFDEAD",
        "oldlace" to "FDF5E6",
        "olivedrab" to "6B8E23",
        "orangered" to "FF4500",
        "orchid" to "DA70D6",
        "palegoldenrod" to "EEE8AA",
        "palegreen" to "98FB98",
        "paleturquoise" to "AFEEEE",
        "palevioletred" to "DB7093",
        "papayawhip" to "FFEFD5",
        "peachpuff" to "FFDAB9",
        "peru" to "CD853F",
        "pink" to "FFC0CB",
        "plum" to "DDA0DD",
        "powderblue" to "B0E0E6",
        "rosybrown" to "BC8F8F",
        "royalblue" to "4169E1",
        "saddlebrown" to "8B4513",
        "salmon" to "FA8072",
        "sandybrown" to "F4A460",
        "seagreen" to "2E8B57",
        "seashell" to "FFF5EE",
        "sienna" to "A0522D",
        "skyblue" to "87CEEB",
        "slateblue" to "6A5ACD",
        "slategray" to "708090",
        "snow" to "FFFAFA",
        "springgreen" to "00FF7F",
        "steelblue" to "4682B4",
        "tan" to "D2B48C",
        "thistle" to "D8BFD8",
        "tomato" to "FF6347",
        "turquoise" to "40E0D0",
        "violet" to "EE82EE",
        "wheat" to "F5DEB3",
        "whitesmoke" to "F5F5F5",
        "yellowgreen" to "9ACD32",
        "azure" to "F0FFFF",
        "beige" to "F5F5DC",
        "bisque" to "FFE4C4",
        "blanchedalmond" to "FFEBCD",
        "blueviolet" to "8A2BE2",
        "brown" to "A52A2A",
        "burlywood" to "DEB887",
        "cadetblue" to "5F9EA0",
        "chartreuse" to "7FFF00",
        "chocolate" to "D2691E",
        "coral" to "FF7F50",
        "cornflowerblue" to "6495ED",
        "cornsilk" to "FFF8DC",
        "crimson" to "DC143C",
        "darkblue" to "00008B",
        "darkcyan" to "008B8B",
        "darkgoldenrod" to "B8860B",
        "darkgray" to "A9A9A9",
        "darkgreen" to "006400",
        "darkkhaki" to "BDB76B",
        "darkmagenta" to "8B008B",
        "darkolivegreen" to "556B2F",
        "darkorange" to "FF8C00",
        "darkorchid" to "9932CC",
        "darkred" to "8B0000",
        "darksalmon" to "E9967A",
        "darkseagreen" to "8FBC8F",
        "darkslateblue" to "483D8B",
        "darkslategray" to "2F4F4F",
        "darkturquoise" to "00CED1",
        "darkviolet" to "9400D3",
        "deeppink" to "FF1493",
        "deepskyblue" to "00BFFF",
        "dimgray" to "696969",
        "dodgerblue" to "1E90FF",
        "firebrick" to "B22222",
        "floralwhite" to "FFFAF0",
        "forestgreen" to "228B22",
        "gainsboro" to "DCDCDC",
        "ghostwhite" to "F8F8FF",
        "gold" to "FFD700",
        "goldenrod" to "DAA520",
        "greenyellow" to "ADFF2F",
        "honeydew" to "F0FFF0",
        "hotpink" to "FF69B4",
        "indianred" to "CD5C5C",
        "indigo" to "4B0082",
        "ivory" to "FFFFF0",
        "khaki" to "F0E68C",
        "lavender" to "E6E6FA",
        "lavenderblush" to "FFF0F5",
        "lawngreen" to "7CFC00",
        "lemonchiffon" to "FFFACD",
        "lightblue" to "ADD8E6",
        "lightcoral" to "F08080",
        "lightcyan" to "E0FFFF",
        "lightgoldenrodyellow" to "FAFAD2",
        "lightgreen" to "90EE90",
        "lightgray" to "D3D3D3",
        "lightpink" to "FFB6C1",
        "lightsalmon" to "FFA07A",
        "lightseagreen" to "20B2AA",
        "lightskyblue" to "87CEFA",
        "lightslategray" to "778899",
        "lightsteelblue" to "B0C4DE",
        "lightyellow" to "FFFFE0",
        "limegreen" to "32CD32",
        "linen" to "FAF0E6",
        "magenta" to "FF00FF",
        "mediumaquamarine" to "66CDAA",
        "mediumblue" to "0000CD",
        "mediumorchid" to "BA55D3",
        "mediumpurple" to "9370DB",
        "mediumseagreen" to "3CB371",
        "mediumslateblue" to "7B68EE",
        "mediumspringgreen" to "00FA9A",
        "mediumturquoise" to "48D1CC",
        "mediumvioletred" to "C71585",
        "midnightblue" to "191970",
        "mintcream" to "F5FFFA",
        "mistyrose" to "FFE4E1",
        "moccasin" to "FFE4B5",
        "navajowhite" to "FFDEAD",
        "oldlace" to "FDF5E6",
        "olivedrab" to "6B8E23",
        "orangered" to "FF4500",
        "orchid" to "DA70D6",
        "palegoldenrod" to "EEE8AA",
        "palegreen" to "98FB98",
        "paleturquoise" to "AFEEEE",
        "palevioletred" to "DB7093",
        "papayawhip" to "FFEFD5",
        "peachpuff" to "FFDAB9",
        "peru" to "CD853F",
        "pink" to "FFC0CB",
        "plum" to "DDA0DD",
        "powderblue" to "B0E0E6",
        "rosybrown" to "BC8F8F",
        "royalblue" to "4169E1",
        "saddlebrown" to "8B4513",
        "salmon" to "FA8072",
        "sandybrown" to "F4A460",
        "seagreen" to "2E8B57",
        "seashell" to "FFF5EE",
        "sienna" to "A0522D",
        "skyblue" to "87CEEB",
        "slateblue" to "6A5ACD",
        "slategray" to "708090",
        "snow" to "FFFAFA",
        "springgreen" to "00FF7F",
        "steelblue" to "4682B4",
        "tan" to "D2B48C",
        "thistle" to "D8BFD8",
        "tomato" to "FF6347",
        "turquoise" to "40E0D0",
        "violet" to "EE82EE",
        "wheat" to "F5DEB3",
        "whitesmoke" to "F5F5F5",
        "yellowgreen" to "9ACD32",
        "grey" to "808080",
        "mintgreen" to "98FF98",
    )
}