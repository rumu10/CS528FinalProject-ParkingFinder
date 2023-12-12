package com.example.firebasep

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.firebasep.DataModels.ParkingSpotsModel

class ParkingAdapter(private val parkingList: List<ParkingSpotsModel>) : RecyclerView.Adapter<ParkingAdapter.ParkingViewHolder>() {

    class ParkingViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val tvPId: TextView = view.findViewById(R.id.tvPId)
        val tvPaval: TextView = view.findViewById(R.id.tvPaval)
        val tvplong: TextView = view.findViewById(R.id.tvplong)
        val tvplat: TextView = view.findViewById(R.id.tvplat)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ParkingViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.parking_item, parent, false)
        return ParkingViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: ParkingViewHolder, position: Int) {
        val currentParking = parkingList[position]
        holder.tvPId.text = currentParking.pId
        holder.tvPaval.text = currentParking.Paval
        holder.tvplong.text = currentParking.plong.toString()
        holder.tvplat.text = currentParking.plat.toString()
        Log.e("reach to adapter ", "Addapter ")
        Log.e("ParkingAdapter", "PID: ${currentParking.pId}, Pavalaibity: ${currentParking.Paval}")
    }

    override fun getItemCount() = parkingList.size
}
