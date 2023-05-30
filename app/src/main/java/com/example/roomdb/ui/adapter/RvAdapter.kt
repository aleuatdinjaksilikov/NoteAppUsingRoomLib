package com.example.roomdb.ui.adapter

import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdb.utils.Constans
import com.example.roomdb.ui.EditActivity
import com.example.roomdb.data.entity.Note
import com.example.roomdb.databinding.RcItemBinding

class RvAdapter():RecyclerView.Adapter<RvAdapter.MyViewHolder>() {

    var list = mutableListOf<Note>()
    set(value) {
        field = value
        notifyDataSetChanged()
    }

    inner class MyViewHolder(private val binding: RcItemBinding):RecyclerView.ViewHolder(binding.root){
        fun setData(note: Note){
            binding.rcTvItem.text = note.title
            binding.root.setOnClickListener {
                val intent = Intent(binding.root.context, EditActivity::class.java)
                intent.putExtra(Constans.I_ID_KEY,note.id)
                intent.putExtra(Constans.I_TITLE_KEY,note.title)
                intent.putExtra(Constans.I_CONTENT_KEY,note.content)
                binding.root.context.startActivity(intent)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        return MyViewHolder(RcItemBinding.inflate(LayoutInflater.from(parent.context),parent,false))
    }

    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        holder.setData(list[position])
    }

    override fun getItemCount(): Int {
        return list.size
    }


}