package com.example.virginmoney.adapters

import android.content.Context
import android.graphics.Color
import android.net.Uri
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import android.widget.Filterable
import androidx.core.os.bundleOf
import androidx.core.view.ViewCompat
import androidx.navigation.NavController
import androidx.navigation.fragment.FragmentNavigatorExtras
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.example.virginmoney.R
import com.example.virginmoney.fragments.FragmentPeopleDirections
import com.example.virginmoney.holders.PeopleHolder
import com.example.virginmoney.models.people.PeopleModel
import com.example.virginmoney.utils.Utils.colorMap
import com.example.virginmoney.utils.Utils.colorTransition
import com.example.virginmoney.utils.Utils.isDark
import com.google.gson.Gson

class PeopleAdapter(
    private val context: Context,
    var list: ArrayList<PeopleModel>,
    var navController: NavController
) :
    RecyclerView.Adapter<RecyclerView.ViewHolder>(), Filterable {

    var filteredList: MutableList<PeopleModel> = list

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val inflater: LayoutInflater = LayoutInflater.from(context)
        val view: View = inflater.inflate(R.layout.model_people, parent, false)
        return PeopleHolder(view)
    }

    override fun getItemCount(): Int {
        return filteredList.size
    }

    override fun onBindViewHolder(h0: RecyclerView.ViewHolder, position: Int) {
        val people = filteredList[position]
        val holder = h0 as PeopleHolder
        val transitionName = "thumbnailTransition${people.id}"

        ViewCompat.setTransitionName(holder.thumbnail, transitionName)

        val col: String =
            if (colorMap.containsKey(people.favouriteColor?.lowercase()?.replace(" ", ""))) {
                colorMap[people.favouriteColor?.lowercase()?.replace(" ", "")]!!
            } else {
                println("Not found color ${people.favouriteColor}")
                "000000"
            }
        val color: Int = Color.parseColor("#$col")

        holder.container.colorTransition(color)

        if (isDark(color)) {
            holder.jobTitle.setTextColor(Color.WHITE)
            holder.firstName.setTextColor(Color.WHITE)
            holder.lastName.setTextColor(Color.WHITE)
            holder.email.setTextColor(Color.WHITE)
        } else {
            holder.jobTitle.setTextColor(Color.BLACK)
            holder.firstName.setTextColor(Color.BLACK)
            holder.lastName.setTextColor(Color.BLACK)
            holder.email.setTextColor(Color.BLACK)
        }
        holder.jobTitle.text = people.jobtitle
        holder.firstName.text = people.firstName
        holder.lastName.text = people.lastName
        holder.email.text = people.email

        Glide.with(context)
            .load(Uri.parse(people.avatar))
            .placeholder(R.drawable.profile)
            .error(R.drawable.profile)
            .into(holder.thumbnail)

        holder.thumbnail.contentDescription = "Profile picture of ${people.firstName} ${people.lastName}"

        holder.itemView.setOnClickListener {
            val action = FragmentPeopleDirections.peopleToDetails()
            action.arguments.putString("people", Gson().toJson(people))
            action.arguments.putString("transition", transitionName)
            action.arguments.putInt("color", color)

            val extras = FragmentNavigatorExtras(
                holder.thumbnail to transitionName
            )
            navController.navigate(
                action,
                extras
            )
        }
    }

    override fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val filterResults = FilterResults()
                if (constraint.isNullOrEmpty()) {
                    filterResults.count = list.size
                    filterResults.values = list
                } else {
//                    val results: ArrayList<PeopleModel> = arrayListOf()
                    val str: String = constraint.toString()
                    val resultList = list.filter {
                        it.firstName?.contains(str) ?: false ||
                                it.lastName?.contains(str) ?: false ||
                                it.email?.contains(str) ?: false ||
                                it.jobtitle?.contains(str) ?: false
                    }
                    filterResults.count = resultList.size
                    filterResults.values = resultList
                }
                return filterResults
            }

            override fun publishResults(p0: CharSequence?, p1: FilterResults?) {
                filteredList = p1?.values as MutableList<PeopleModel>
                notifyDataSetChanged()
            }
        }
    }
}