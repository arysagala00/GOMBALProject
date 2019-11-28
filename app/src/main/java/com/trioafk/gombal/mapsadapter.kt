package com.trioafk.gombal

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import org.w3c.dom.Text

class mapsadapter(val isimaps: List<mapsdata>, context: Context):RecyclerView.Adapter<mapsadapter.mapsHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): mapsadapter.mapsHolder {
        val view: View = LayoutInflater.from(parent.context).inflate(R.layout.detailview,parent,false)
        return  mapsHolder(view)
    }

    override fun getItemCount(): Int {
        return  isimaps.size
    }

    override fun onBindViewHolder(holder: mapsadapter.mapsHolder, position: Int) {
        val mapspos = isimaps.get(position)
        holder.nama?.text=mapspos.nama
        holder.status?.text=mapspos.status
        holder.rating?.text=mapspos.rating
    }

    class mapsHolder(val view: View):RecyclerView.ViewHolder(view){
        var nama: TextView?     =null
        var status: TextView?   =null
        var rating: TextView?   =null

        init{

            nama=view.findViewById(R.id.namatxt) as TextView
            status=view.findViewById(R.id.statustxt) as TextView
            rating=view.findViewById(R.id.rating) as TextView
        }
    }
}