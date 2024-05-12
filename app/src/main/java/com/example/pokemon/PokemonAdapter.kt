package com.example.pokemon

import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso

class PokemonAdapter(private val pokemonList: ArrayList<Pokemon>, private val picasso: Picasso): RecyclerView.Adapter<PokemonAdapter.PokemonHolder>() {

    var onItemClick : ((Pokemon) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PokemonHolder {
        val item = LayoutInflater.from(parent.context).inflate(R.layout.item_pokemon, parent, false)
        return PokemonHolder(item)
    }

    override fun getItemCount(): Int {
        return pokemonList.size
    }

    override fun onBindViewHolder(holder: PokemonHolder, position: Int) {
        val pokemon = pokemonList[position]
        holder.tvName.text = pokemon.name
        picasso.load(pokemon.img).into(holder.ivImage)

        holder.btnOpen.setOnClickListener {
            onItemClick?.invoke(pokemon)
        }
    }

    class PokemonHolder(item: View): RecyclerView.ViewHolder(item) {
        val tvName:TextView = item.findViewById(R.id.tvName)
        val ivImage:ImageView = item.findViewById(R.id.ivImage)
        val btnOpen:Button = item.findViewById(R.id.btnOpen)
    }
}