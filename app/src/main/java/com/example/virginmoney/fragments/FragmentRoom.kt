package com.example.virginmoney.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import com.example.virginmoney.base.BaseFragment
import com.example.virginmoney.databinding.FragmentProfileBinding
import com.example.virginmoney.databinding.FragmentRoomsBinding

class FragmentRoom:BaseFragment() {

    lateinit var bind: FragmentRoomsBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        bind = FragmentRoomsBinding.inflate(layoutInflater, container, false)
        return bind.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {

    }
}