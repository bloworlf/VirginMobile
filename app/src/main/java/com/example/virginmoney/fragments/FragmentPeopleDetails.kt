package com.example.virginmoney.fragments

import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.view.doOnPreDraw
import com.bumptech.glide.Glide
import com.example.virginmoney.R
import com.example.virginmoney.activities.Home
import com.example.virginmoney.base.BaseFragment
import com.example.virginmoney.databinding.FragmentPeopleDetailsBinding
import com.example.virginmoney.models.people.PeopleModel
import com.example.virginmoney.utils.Utils.clearLightStatusBar
import com.example.virginmoney.utils.Utils.isDark
import com.example.virginmoney.utils.Utils.setLightStatusBar
import com.google.gson.Gson
import de.hdodenhof.circleimageview.CircleImageView
import java.text.SimpleDateFormat
import java.util.Locale

class FragmentPeopleDetails : BaseFragment() {

    lateinit var bind: FragmentPeopleDetailsBinding

    lateinit var people: PeopleModel

    lateinit var thumbnail: CircleImageView
    private lateinit var transitionName: String
    var color: Int = 0

    lateinit var jobTitle: TextView
    lateinit var firstName: TextView
    lateinit var lastName: TextView
    lateinit var email: TextView
    lateinit var favColor: TextView
    lateinit var createdAt: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        people = Gson().fromJson(arguments?.getString("people"), PeopleModel::class.java)
        transitionName = arguments?.getString("transition")!!
        color = arguments?.getInt("color")!!
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentPeopleDetailsBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        (activity as Home).showToolbar()
        (activity as Home).supportActionBar?.let {
            (activity as Home).supportActionBar!!.title = "${people.firstName} ${people.lastName}"
        }

        (activity as AppCompatActivity).supportActionBar!!.setBackgroundDrawable(
            ColorDrawable(color)
        )
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.statusBarColor = color
        }

        val wrapDrawable = DrawableCompat.wrap(
            ResourcesCompat.getDrawable(
                resources,
                R.drawable.back,
                null
            )!!
        )
        if (isDark(color)) {
            clearLightStatusBar(requireActivity())
            (activity as Home).titleColor(Color.WHITE)


            DrawableCompat.setTint(wrapDrawable, Color.WHITE)
            (requireActivity() as Home).supportActionBar?.setHomeAsUpIndicator(
                wrapDrawable
            )
        } else {
            setLightStatusBar(requireActivity())
            (activity as Home).titleColor(Color.BLACK)

            DrawableCompat.setTint(wrapDrawable, Color.BLACK)
            (requireActivity() as Home).supportActionBar?.setHomeAsUpIndicator(
                wrapDrawable
            )
        }

        thumbnail = bind.thumbnail
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            thumbnail.transitionName = transitionName
        }

        Glide.with(requireContext())
            .load(Uri.parse(people.avatar))
            .error(R.drawable.profile)
            .placeholder(R.drawable.profile)
//            .into(object : CustomViewTarget<CircleImageView, Bitmap>(thumbnail) {
//                override fun onLoadFailed(errorDrawable: Drawable?) {}
//                override fun onResourceCleared(placeholder: Drawable?) {
//                    thumbnail.setImageDrawable(placeholder)
//                }
//
//                override fun onResourceReady(resource: Bitmap, transition: Transition<in Bitmap>?) {
//                    transition?.let {
//                        val didSucceedTransition =
//                            transition.transition(resource, BitmapImageViewTarget(thumbnail))
//                        if (!didSucceedTransition) thumbnail.setImageBitmap(resource)
//                    }
//                }
//            }.view)
            .into(thumbnail)

        postponeEnterTransition()
        thumbnail.doOnPreDraw {
            startPostponedEnterTransition()
        }

        jobTitle = bind.jobPosition
        firstName = bind.firstName
        lastName = bind.lastName
        email = bind.email
        favColor = bind.favColor
        createdAt = bind.createdAt

        jobTitle.text = people.jobtitle
        firstName.text = people.firstName
        lastName.text = people.lastName
        email.text = people.email
        favColor.text = people.favouriteColor
        createdAt.text = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSS'Z'", Locale.getDefault()).parse(people.createdAt).toString()
    }

    override fun onDestroy() {
        (activity as Home).titleColor(
            ResourcesCompat.getColor(
                requireActivity().resources,
                android.R.color.tab_indicator_text,
                null
            )
        )
        (activity as AppCompatActivity).supportActionBar!!.setBackgroundDrawable(
            ColorDrawable(
                ResourcesCompat.getColor(
                    requireActivity().resources,
                    R.color.white,
                    null
                )
            )
        )

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            requireActivity().window.statusBarColor =
                ResourcesCompat.getColor(resources, R.color.white, null)
        }
        setLightStatusBar(requireActivity())

        super.onDestroy()
    }
}