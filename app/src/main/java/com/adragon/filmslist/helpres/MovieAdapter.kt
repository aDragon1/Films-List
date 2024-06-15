package com.adragon.filmslist.helpres

import android.annotation.SuppressLint
import android.content.Context
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.CheckBox
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.core.view.isVisible
import androidx.recyclerview.widget.RecyclerView
import com.adragon.filmslist.R
import com.adragon.filmslist.movie.info.Movie

class MovieAdapter(private val context: Context) :
    RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {

    private var movies: List<Movie> = emptyList()
    private var checkedId = booleanArrayOf(false)

    private var itemClickListener: OnItemClickListener = OnItemClickListener { }

    fun interface OnItemClickListener {
        fun onItemClick(position: Int)
    }

    fun setOnItemClickListener(listener: OnItemClickListener) {
        itemClickListener = listener
    }


    inner class MyViewHolder(
        itemView: View
    ) :
        RecyclerView.ViewHolder(itemView) {
        private val elementCardView: CardView = itemView.findViewById(R.id.elementCardView)
        val filmImageImageView: ImageView =
            itemView.findViewById<ImageView?>(R.id.filmImageImageView).apply {
                scaleType = ImageView.ScaleType.CENTER_INSIDE
            }
        val filmNameTextView: TextView = itemView.findViewById(R.id.filmNameTextView)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)

        return MyViewHolder(itemView)
    }


    override fun getItemCount(): Int = movies.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {

        Log.d("mytag", "checked = ${checkedId.toList()}\n any - ${checkedId.any { it }}")
        movies[position].apply {
//            holder.filmImageImageView.setImageBitmap(decodeBitmap())
            holder.filmNameTextView.text = "$title - $rank"
        }
    }


    fun getItem(id: Int) = if (id in movies.indices) movies[id] else null
    fun fillData(m: List<Movie>) {
        movies = m
        checkedId = BooleanArray(movies.size) { false }
    }
}