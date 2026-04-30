package com.example.khushimotors

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.card.MaterialCardView

class ServicesAdapter(
    private val services: List<ServicesFragment.Service>,
    private val onItemClick: (ServicesFragment.Service) -> Unit
) : RecyclerView.Adapter<ServicesAdapter.ViewHolder>() {

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val card: MaterialCardView = view.findViewById(R.id.card_service_item)
        val tvEmoji: TextView = view.findViewById(R.id.tv_service_emoji)
        val tvTitle: TextView = view.findViewById(R.id.tv_service_title)
        val tvDescription: TextView = view.findViewById(R.id.tv_service_description)
        val tvTag: TextView = view.findViewById(R.id.tv_service_tag)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_service_card, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val service = services[position]
        holder.tvEmoji.text = service.emoji
        holder.tvTitle.text = service.title
        holder.tvDescription.text = service.description
        if (service.tag.isNotEmpty()) {
            holder.tvTag.visibility = View.VISIBLE
            holder.tvTag.text = service.tag
        } else {
            holder.tvTag.visibility = View.GONE
        }
        holder.card.setOnClickListener { onItemClick(service) }
    }

    override fun getItemCount() = services.size
}
