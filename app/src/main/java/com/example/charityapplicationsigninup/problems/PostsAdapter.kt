package com.example.fblikeapp

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.net.Uri
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.core.content.ContextCompat.startActivity
import androidx.recyclerview.widget.RecyclerView
import com.example.charityapplicationsigninup.problems.Problem
import com.example.charityapplicationsigninup.R
import com.squareup.picasso.Picasso
import java.util.*

class PostsAdapter(val posts: ArrayList<Problem>, val context: Context) : RecyclerView.Adapter<PostsAdapter.ViewHolder>() {


    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.post_card, parent, false)
        return ViewHolder(view)
    }

    override fun getItemCount() = posts.size


    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {

        holder.username.text = posts[position].getmTitle()
        holder.text.text = posts[position].getmProblem() + "\n" + "Address : " + posts[position].getmAddress()
        //holder.text.text = posts[position].getmProblem() + "\n" + posts[position].getmAddress()
        holder.contact_btn.setOnClickListener {
            Log.e("button", "share button pressed")
            val intent = Intent(Intent.ACTION_DIAL).apply {

                data = Uri.parse("tel:" + posts[position].getmPhone())

            }
            startActivity(context, intent, null)
        }
        holder.share_btn.setOnClickListener {
            Log.e("button", "contact button pressed")
            val title: String = posts[position].getmTitle()
            val description: String = posts[position].getmProblem()
            val pNumber: String = posts[position].getmPhone()
            val address: String = posts[position].getmAddress()
            val strStruct = "$title\n\n$description\n\nContact: $pNumber\n\nAddress: $address\nShared by Charity App"

            val share = Intent.createChooser(Intent().apply {
                action = Intent.ACTION_SEND
                putExtra(Intent.EXTRA_TEXT, strStruct)
                setType("text/plain")
            }, null)
            startActivity(context, share, null)
        }
        Picasso.get()
                .load(posts[position].getmPhotoUrl())
                .into(holder.photo)

        if (posts[position].getmIsUrgent() == 1) {
            holder.urgphoto.visibility = View.VISIBLE
        }
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val username: TextView = itemView.findViewById(R.id.username)
        val text: TextView = itemView.findViewById(R.id.text)
        val photo: ImageView = itemView.findViewById(R.id.photo)
        val contact_btn: Button = itemView.findViewById(R.id.contact_btn)
        val share_btn: Button = itemView.findViewById(R.id.share_btn)
        val urgphoto: ImageView = itemView.findViewById(R.id.urgentphoto)

    }
}