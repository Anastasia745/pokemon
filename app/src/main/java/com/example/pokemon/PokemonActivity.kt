package com.example.pokemon

import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.squareup.picasso.Picasso

class PokemonActivity : AppCompatActivity() {
    lateinit var tvPokemonName : TextView
    lateinit var ivPokemonImage : ImageView
    lateinit var tvPokemonAbilities : TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_pokemon)

        tvPokemonName = findViewById(R.id.tvPokemonName)
        ivPokemonImage = findViewById(R.id.ivPokemonImage)
        tvPokemonAbilities= findViewById(R.id.tvPokemonAbilities)

        val pokemons = intent.extras

        if (pokemons != null){
            tvPokemonName.text = pokemons.getString("name")
            val img = pokemons.getString("img")
            val picasso = Picasso.Builder(this).build()
            picasso.load(img).into(ivPokemonImage)
            tvPokemonAbilities.text = pokemons.getString("abilities")
        }
    }
}