package com.arslan.taskmaster.model

import Item
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.arslan.taskmaster.R
import com.arslan.taskmaster.interfaces.GenericCallbackAdapter

class ItemListAdapter (
    var list: List<Item>,
    var context: Context, var childClickObj: GenericCallbackAdapter
) : RecyclerView.Adapter<ItemListAdapter.StatusViewHolder>() {


    override fun onCreateViewHolder(
        parent: ViewGroup,
        viewType: Int
    ): ItemListAdapter.StatusViewHolder {
        val v = LayoutInflater.from(context).inflate(R.layout.item_list_layout, parent, false)
        return ItemListAdapter.StatusViewHolder(v)
    }

    override fun getItemCount(): Int = list.size

    override fun getItemId(position: Int): Long {
        return position.toLong()
    }

    override fun getItemViewType(position: Int): Int {
        return position
    }


    override fun onBindViewHolder(holder: StatusViewHolder, position: Int) {
        holder.itemName.text = list[position].productName
        holder.itemDelete.setOnClickListener {
            childClickObj.getClickedObject(
                list[position].id,
                position,
                "DELETE_ITEM"
            )
        }
        holder.itemView.setOnClickListener {
            childClickObj.getClickedObject(
                list[position],
                position,
                "VIEW_ITEM"
            )
        }
        holder.itemEdit.setOnClickListener {
            childClickObj.getClickedObject(
                list[position],
                position,
                "Edit_ITEM"
            )
        }
    }

    class StatusViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {

        val itemName: TextView = itemView.findViewById(R.id.item_name)
        val itemDelete: ImageView = itemView.findViewById(R.id.item_delete)
        val itemView: ImageView = itemView.findViewById(R.id.item_view)
        val itemEdit: ImageView = itemView.findViewById(R.id.item_edit)
    }
}