package com.example.virginmoney.ui.activities

import android.graphics.Bitmap
import android.graphics.PorterDuff
import android.graphics.drawable.BitmapDrawable
import android.graphics.drawable.Drawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.View
import androidx.appcompat.widget.Toolbar
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.MenuItemCompat
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.setupActionBarWithNavController
import androidx.navigation.ui.setupWithNavController
import com.bumptech.glide.Glide
import com.bumptech.glide.request.RequestOptions
import com.bumptech.glide.request.target.CustomTarget
import com.bumptech.glide.request.transition.Transition
import com.example.virginmoney.App
import com.example.virginmoney.R
import com.example.virginmoney.ui.base.BaseActivity
import com.example.virginmoney.databinding.ActivityHomeBinding
import com.example.virginmoney.utils.DensityUtil
import com.example.virginmoney.utils.Utils
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import dagger.hilt.android.AndroidEntryPoint
import de.hdodenhof.circleimageview.CircleImageView

@AndroidEntryPoint
class Home : BaseActivity() {

    lateinit var bind: ActivityHomeBinding

    private lateinit var controller: NavController
    private lateinit var btmView: BottomNavigationView
    private lateinit var toolbar: Toolbar
//    private lateinit var profileImage: CircleImageView

    private lateinit var user: FirebaseUser

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            window.statusBarColor = ResourcesCompat.getColor(resources, R.color.white, null)
        }
        Utils.setLightStatusBar(this)

        FirebaseAuth.getInstance().currentUser?.let {
            user = FirebaseAuth.getInstance().currentUser!!
        }

        bind = ActivityHomeBinding.inflate(layoutInflater)
        setContentView(bind.root)

        toolbar = bind.toolbar
        setSupportActionBar(toolbar)

//        profileImage = toolbar.findViewById(R.id.profile_image)

        btmView = bind.btmView
        val menu = btmView.menu
        val menuItem = menu.findItem(R.id.menu_profile)
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
//            menuItem.iconTintList = null
//            menuItem.iconTintMode = null
            MenuItemCompat.setIconTintMode(menuItem, PorterDuff.Mode.DST)

//            btmView.itemIconTintList = null
            Glide.with(App.instance)
                .asBitmap()
//                .load(Uri.parse("https://thispersondoesnotexist.com/"))
                .load(user.photoUrl)
                .placeholder(R.drawable.profile)
                .error(R.drawable.profile)
                .apply(
                    RequestOptions.circleCropTransform().placeholder(R.drawable.profile)
                )
                .into(object :
                    CustomTarget<Bitmap>(
                        DensityUtil.dip2px(this, 80F),
                        DensityUtil.dip2px(this, 80F)
                    ) {
                    override fun onResourceReady(
                        resource: Bitmap,
                        transition: Transition<in Bitmap>?
                    ) {
                        menuItem.icon = BitmapDrawable(
                            resources, resource
                        )
                    }

                    override fun onLoadFailed(errorDrawable: Drawable?) {
                        menuItem.icon = errorDrawable
                    }

                    override fun onLoadCleared(placeholder: Drawable?) {
                    }
                })
        }
        menuItem.title = user.displayName

        controller = findNavController(R.id.fragment)
        val navConfig = AppBarConfiguration(
            setOf(R.id.menu_people, R.id.menu_rooms, R.id.menu_profile)
        )

        controller.setGraph(R.navigation.navigation_home, intent.extras)

        setupActionBarWithNavController(controller, navConfig)
        btmView.setupWithNavController(controller)

        controller.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.menu_people, R.id.menu_rooms, R.id.menu_profile -> {
                    btmView.visibility = View.VISIBLE
                }

                else -> {
                    btmView.visibility = View.GONE
                }
            }
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        return controller.navigateUp() || super.onSupportNavigateUp()
    }

    override fun onBackPressed() {
        if (!onSupportNavigateUp()) {
            super.onBackPressed()
        }
    }

    fun showToolbar() {
        if (this::bind.isInitialized) {
            bind.appBar.setExpanded(true, true)
        }
    }

    fun titleColor(color: Int) {
        toolbar.setTitleTextColor(color)

        toolbar.navigationIcon?.let {
            val wrapDrawable = DrawableCompat.wrap(toolbar.navigationIcon!!)
            DrawableCompat.setTint(wrapDrawable, color)
            toolbar.navigationIcon = wrapDrawable
        }

        for (i in 0 until toolbar.menu.size()) {
            try {
                val wDrawable = DrawableCompat.wrap(toolbar.menu.getItem(i).icon!!)
                DrawableCompat.setTint(wDrawable, color)
                toolbar.menu.getItem(i).icon = wDrawable
            } catch (e: Exception) {
            }
        }
    }
}