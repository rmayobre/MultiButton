package com.multibutton.example

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class TextAdapter(private val texts: List<String>): RecyclerView.Adapter<TextAdapter.TextViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): TextViewHolder {
        val layoutInflater = LayoutInflater.from(parent.context)
        val view = layoutInflater.inflate(R.layout.item_text, parent, false)
        return TextViewHolder(view)
    }

    override fun getItemCount(): Int {
        return texts.size
    }

    override fun onBindViewHolder(holder: TextViewHolder, position: Int) {
        holder.textView.text = texts[position]
    }

    class TextViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
        val textView: TextView = itemView.findViewById(R.id.item_text_view)
    }
}