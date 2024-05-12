package com.example.pokemon

import android.content.Intent
import android.os.Bundle
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.squareup.picasso.Picasso
import org.json.JSONArray
import org.json.JSONObject
import java.io.BufferedReader
import java.io.InputStreamReader
import java.net.HttpURLConnection
import java.net.URL
import kotlin.concurrent.thread

class MainActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var pokemonList: ArrayList<Pokemon>

    lateinit var nameList: ArrayList<String>
    lateinit var imgList: ArrayList<String>
    lateinit var abilities: ArrayList<String>
    lateinit var pokemonAdapter: PokemonAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        thread {
            nameList = ArrayList()
            imgList = ArrayList()
            abilities = ArrayList()

            val arrPokemons = getJson("https://pokeapi.co/api/v2/pokemon/?limit=20", "results")

            for (i in 0 until arrPokemons.length()) {

                nameList.add(arrPokemons.getJSONObject(i).getString("name"))
                val urlPokemon = arrPokemons.getJSONObject(i).getString("url")
                val id = (urlPokemon.split("/")).dropLast(1).last()
                imgList.add("https://raw.githubusercontent.com/PokeAPI/sprites/master/sprites/pokemon/$id.png")
                var abilityStr = ""
                val arrAbilities = getJson(urlPokemon, "abilities")

                for (j in 0 until arrAbilities.length()) {
                    val ability = arrAbilities.getJSONObject(j).getJSONObject("ability")
                    val urlAbility = ability.getString("url")

                    val arrEffects = getJson(urlAbility, "effect_entries")
                    val effect = arrEffects.getJSONObject(0).getString("effect")
                    abilityStr += (effect + "\n")
                }

                abilities.add(abilityStr)
            }

            runOnUiThread {
                recyclerView = findViewById(R.id.rv)
                recyclerView.layoutManager = LinearLayoutManager(this)
                recyclerView.hasFixedSize()
                pokemonList = arrayListOf<Pokemon>()
                val picasso = Picasso.Builder(this).build()
                getData(picasso)
            }
        }
    }

    private fun getJson(urlString: String, arrResult: String): JSONArray {
        var arr = JSONArray()
        val url = URL(urlString)
        val connection = url.openConnection() as HttpURLConnection
        connection.requestMethod = "GET"
        connection.setRequestProperty("Content-length", "0")
        connection.useCaches = false
        connection.connect()
        if (connection.responseCode == HttpURLConnection.HTTP_OK) {
            val inputStream = connection.inputStream
            val bufferedReader = BufferedReader(InputStreamReader(inputStream))
            val json = bufferedReader.use(BufferedReader::readText)
            arr = JSONObject(json).getJSONArray(arrResult)
            bufferedReader.close()
        }
        return arr
    }

    private fun getData(picasso: Picasso) {
        for (i in 0 until 19) {
            val pokemon = Pokemon(nameList[i], imgList[i], abilities[i])
            pokemonList.add(pokemon)
        }
        pokemonAdapter = PokemonAdapter(pokemonList, picasso)
        recyclerView.adapter = pokemonAdapter
        pokemonAdapter.onItemClick = {

            val intent = Intent(this, PokemonActivity::class.java)
            intent.putExtra("name", it.name)
            intent.putExtra("img", it.img)
            intent.putExtra("abilities", it.abilities)
            startActivity(intent)
        }
    }
}