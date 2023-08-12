package com.example.foodapp


import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import de.hdodenhof.circleimageview.CircleImageView

  class BreakfastAdapter(
    private var breakfastList: List<Breakfast>,
    private val orderClickListener: OrderClickListener
) : RecyclerView.Adapter<BreakfastAdapter.BreakfastViewHolder>() {

        interface OrderClickListener {
        fun onOrderClick(breakfast: Breakfast)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): BreakfastViewHolder {
        val itemView = LayoutInflater.from(parent.context).inflate(R.layout.item_breakfast, parent, false)
        return BreakfastViewHolder(itemView)
    }

    override fun onBindViewHolder(holder: BreakfastViewHolder, position: Int) {
        val currentItem = breakfastList[position]
        holder.textViewBreakfastName.text = currentItem.name
        holder.textViewBreakfastPrice.text = "Price: $${currentItem.price}"
        holder.textViewBreakfastDescription.text = currentItem.description

        Picasso.get()
            .load(currentItem.imageUrl)
            .into(holder.imageViewBreakfast)

        // Set a click listener for the "Add to Cart" button

        // Set a click listener for the "Order" button
        holder.orderButton.setOnClickListener {
            orderClickListener.onOrderClick(currentItem)
        }
    }

    override fun getItemCount() = breakfastList.size

    fun updateData(newBreakfastList: List<Breakfast>) {
        breakfastList = newBreakfastList
        notifyDataSetChanged()
    }

    inner class BreakfastViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val textViewBreakfastName: TextView = itemView.findViewById(R.id.textViewBreakfastName)
        val textViewBreakfastPrice: TextView = itemView.findViewById(R.id.textViewBreakfastPrice)
        val textViewBreakfastDescription: TextView = itemView.findViewById(R.id.textViewBreakfastDescription)
        val imageViewBreakfast: ImageView = itemView.findViewById(R.id.imageViewBreakfast)
        val orderButton: Button = itemView.findViewById(R.id.bt1)
    }

}