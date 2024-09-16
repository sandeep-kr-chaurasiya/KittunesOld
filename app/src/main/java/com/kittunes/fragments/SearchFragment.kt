package com.kittunes

import SharedViewModel
import ViewModelFactory
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.ServiceConnection
import android.os.Bundle
import android.os.IBinder
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import android.widget.Toast
import androidx.fragment.app.Fragment
import androidx.fragment.app.activityViewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.kittunes.Adapter.SearchAdapter
import com.kittunes.Api.ApiInterface
import com.kittunes.Api_Data.Data
import com.kittunes.Api_Data.MyData
import com.kittunes.databinding.FragmentSearchBinding
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchFragment : Fragment() {

    private val sharedViewModel: SharedViewModel by activityViewModels {
        ViewModelFactory(requireContext())
    }
    private lateinit var binding: FragmentSearchBinding
    private lateinit var adapter: SearchAdapter
    private var musicService: MusicService? = null
    private var isBound = false

    private val apiInterface by lazy {
        Retrofit.Builder()
            .baseUrl("https://deezerdevs-deezer.p.rapidapi.com/")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiInterface::class.java)
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        setupRecyclerView()
        setupSearchBar()
        bindMusicService()
    }

    private fun setupRecyclerView() {
        binding.recyclerView.layoutManager = LinearLayoutManager(requireContext())
        adapter = SearchAdapter(
            onSongClicked = { song -> onSongClicked(song) },
            onAddToQueue = { song -> sharedViewModel.addSongToQueue(song) } // Pass the add to queue callback
        )
        binding.recyclerView.adapter = adapter
    }

    private fun setupSearchBar() {
        binding.searchbar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                query?.let { searchSongs(it) }
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                if (newText.isNullOrBlank()) {
                    clearSearchResults()
                } else {
                    searchSongs(newText)
                }
                return true
            }
        })
        binding.searchbar.isFocusable = false
    }

    private fun searchSongs(query: String) {
        apiInterface.getdata(query).enqueue(object : Callback<MyData?> {
            override fun onResponse(call: Call<MyData?>, response: Response<MyData?>) {
                if (response.isSuccessful) {
                    val dataList = response.body()?.data ?: emptyList()
                    adapter.submitList(dataList)
                } else {
                    showError("Error loading search results: ${response.message()}")
                }
            }

            override fun onFailure(call: Call<MyData?>, t: Throwable) {
                Log.e(TAG, "API call failed: ${t.message}", t)
                Toast.makeText(requireContext(), "Failed to load search results", Toast.LENGTH_SHORT).show()
            }
        })
    }

    private fun showError(message: String) {
        Log.e(TAG, message)
        Toast.makeText(requireContext(), message, Toast.LENGTH_SHORT).show()
    }

    private fun clearSearchResults() {
        adapter.submitList(emptyList())
    }

    private fun onSongClicked(song: Data) {
        // Update the ViewModel
        sharedViewModel.addSongToQueue(song)
        sharedViewModel.setCurrentSong(song)
        sharedViewModel.setPlayingState(true) // Set playing state to true

        if (isBound) {
            // Prepare the song and start playback
            musicService?.prepareSong(song)
            // Add a listener to start playback once the song is prepared
            musicService?.mediaPlayer?.setOnPreparedListener {
                musicService?.startPlayback()
            }
        } else {
            // Bind to the service if not already bound
            val serviceIntent = Intent(requireContext(), MusicService::class.java)
            requireContext().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
        }

        // Update UI
        (activity as? MainActivity)?.binding?.currentsong?.visibility = View.VISIBLE
    }

    private fun bindMusicService() {
        val serviceIntent = Intent(requireContext(), MusicService::class.java)
        requireContext().bindService(serviceIntent, serviceConnection, Context.BIND_AUTO_CREATE)
    }

    private val serviceConnection = object : ServiceConnection {
        override fun onServiceConnected(name: ComponentName?, binder: IBinder?) {
            val musicBinder = binder as? MusicService.MusicBinder
            musicService = musicBinder?.getService()
            isBound = true
            Log.d(TAG, "MusicService bound")
            sharedViewModel.currentSong.value?.let { song ->
                onSongClicked(song)
            }
        }

        override fun onServiceDisconnected(name: ComponentName?) {
            isBound = false
            musicService = null
            Log.d(TAG, "MusicService unbound")
        }
    }

    override fun onDestroy() {
        super.onDestroy()
        if (isBound) {
            requireContext().unbindService(serviceConnection)
            isBound = false
        }
    }

    companion object {
        private const val TAG = "SearchFragment"
    }
}