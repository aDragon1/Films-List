package com.adragon.filmslist.helpres

import android.annotation.SuppressLint
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.adragon.filmslist.R
import com.adragon.filmslist.movie.info.Movie

class MovieAdapter(private val context: Context) :
    RecyclerView.Adapter<MovieAdapter.MyViewHolder>() {

    private var movies: List<Movie> = emptyList()
    private var checkedId = booleanArrayOf(false)

    private var itemClickListener: OnItemClickListener = OnItemClickListener {  }

    fun interface OnItemClickListener {
        fun onItemClick(movie: Movie)
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

        init {
            elementCardView.setOnClickListener {
                val movie = getItem(bindingAdapterPosition)
                if (movie != null)
                    itemClickListener.onItemClick(movie)
            }
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MyViewHolder {
        val itemView = LayoutInflater.from(parent.context)
            .inflate(R.layout.movie_item, parent, false)

        return MyViewHolder(itemView)
    }


    override fun getItemCount(): Int = movies.size

    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: MyViewHolder, position: Int) {
        movies[position].apply {
            val cond = if (rating == -1) "" else " - $rating"
            holder.filmNameTextView.text = "$title$cond"
        }
    }

    fun getItem(id: Int) = if (id in movies.indices) movies[id] else null
    fun getFilmNames() = movies.map { it.title to it.id }
    fun fillData(m: List<Movie>) {
        movies = m
        checkedId = BooleanArray(movies.size) { false }
    }
}