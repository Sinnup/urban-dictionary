package com.sinue.streetworkout.urbandictionary.view

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.sinue.streetworkout.urbandictionary.R
import com.sinue.streetworkout.urbandictionary.model.ItemSearch

class SearchResultsAdapter(val listItemSearch: List<ItemSearch>): RecyclerView.Adapter<SearchResultsAdapter.ViewHolder>() {

    private var mContext: Context? = null

    override fun getItemCount(): Int {
        return listItemSearch.size
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        this.mContext = parent.context

        return ViewHolder(
            LayoutInflater.from(mContext).inflate(
                R.layout.view_item_search,
                parent,
                false
            )
        )
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val mItem = listItemSearch.get(position)

        if (mItem.thumbs_up != null){
            holder.thumbsUp.text = mItem.thumbs_up.toString()
        }

        holder.thumbsDown.text = mItem.thumbs_down.toString()
        holder.description.text = mItem.definition
    }


    class ViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){

        val thumbsUp: TextView = itemView.findViewById(R.id.txtThumbUp)
        val thumbsDown: TextView = itemView.findViewById(R.id.txtThumbDown)
        val description: TextView = itemView.findViewById(R.id.txtDescription)

    }
}