package com.example.virginmoney.fragments

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.Menu
import android.view.MenuInflater
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import com.example.virginmoney.R
import com.example.virginmoney.activities.Splash
import com.example.virginmoney.base.BaseFragment
import com.example.virginmoney.databinding.FragmentProfileBinding
import com.example.virginmoney.dialogs.CustomDialog
import com.facebook.login.LoginManager
import com.google.firebase.auth.FirebaseAuth

class FragmentProfile : BaseFragment() {

    lateinit var bind: FragmentProfileBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setHasOptionsMenu(true)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentProfileBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        inflater.inflate(R.menu.profile_menu, menu)
        super.onCreateOptionsMenu(menu, inflater)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item.itemId) {
            R.id.menu_logout -> {
                CustomDialog(requireContext(), true)
                    .setTitle("Log out")
                    .setMessage("Do you want to log out?")
                    .setPositive("Yes") {
                        FirebaseAuth.getInstance().signOut()
                        LoginManager.getInstance().logOut()

                        requireActivity().startActivity(
                            Intent(
                                requireContext(),
                                Splash::class.java
                            )
                        )
                        requireActivity().finish()
                    }
                    .setNegative("Cancel") {

                    }
                    .show()
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
    }
}