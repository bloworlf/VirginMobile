package com.example.virginmoney.holders

import android.view.View
import android.widget.LinearLayout
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.virginmoney.R
import de.hdodenhof.circleimageview.CircleImageView

class PeopleHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

    val container: LinearLayout
    val jobTitle: TextView
    val thumbnail: CircleImageView
    val firstName: TextView
    val lastName: TextView
    val email: TextView

    init {
        container = itemView.findViewById(R.id.container)
        jobTitle = itemView.findViewById(R.id.jobTitle)
        thumbnail = itemView.findViewById(R.id.thumbnail)
        firstName = itemView.findViewById(R.id.firstName)
        lastName = itemView.findViewById(R.id.lastName)
        email = itemView.findViewById(R.id.email)
    }

}
