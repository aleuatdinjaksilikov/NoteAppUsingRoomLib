package com.example.roomdb.ui

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdb.ui.adapter.RvAdapter
import com.example.roomdb.data.NoteDatabase
import com.example.roomdb.data.dao.NoteDao
import com.example.roomdb.databinding.ActivityMainBinding
import com.example.roomdb.presentation.MainViewModel
import com.google.android.material.snackbar.Snackbar
import kotlinx.coroutines.launch

class MainActivity : AppCompatActivity() {
    private lateinit var viewModel: MainViewModel
    lateinit var binding: ActivityMainBinding
    lateinit var dao: NoteDao
    private val adapter = RvAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dao = NoteDatabase.getInstance(this).getNoteDao()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(MainViewModel::class.java)

        initListener()
        initObservers()
        initVariables()



    }

    private fun initObservers() {
        viewModel.getAllNotesLiveData.observe(this){
            if (it.isNotEmpty()) {
                binding.tvEmpty.visibility = View.GONE
                adapter.list = it.toMutableList()
            } else {
                binding.tvEmpty.visibility = View.VISIBLE
            }
        }
    }

    private fun initListener() {
        binding.searchView.addTextChangedListener {
            lifecycleScope.launch {
                viewModel.searchNoteByTitle(it.toString())
            }
        }
        binding.fbtnAdd.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initVariables() {
        binding.rvMain.adapter = adapter

        val swapHelper = getSwapManager()
        swapHelper.attachToRecyclerView(binding.rvMain)
    }

    private fun getSwapManager(): ItemTouchHelper {
        return ItemTouchHelper(object : ItemTouchHelper
        .SimpleCallback(0, ItemTouchHelper.LEFT) {
            override fun onMove(
                recyclerView: RecyclerView,
                viewHolder: RecyclerView.ViewHolder,
                target: RecyclerView.ViewHolder
            ): Boolean {
                return false
            }

            override fun onSwiped(viewHolder: RecyclerView.ViewHolder, direction: Int) {
                val pos = viewHolder.adapterPosition
                val note : com.example.roomdb.data.entity.Note = adapter.list[pos]

                lifecycleScope.launch {
                    viewModel.deleteNote(note)
                }


                adapter.list.removeAt(pos)
                adapter.notifyItemRemoved(pos)

                Snackbar.make(
                    viewHolder.itemView,
                    "Удалено",
                    Snackbar.LENGTH_LONG
                ).apply {
                    setAction("Назад"){

                        lifecycleScope.launchWhenResumed {
                            viewModel.addNote(note)
                        }

                        adapter.list.add(pos,note)
                        adapter.notifyItemInserted(pos)

                        binding.rvMain.scrollToPosition(pos)
                    }
                    setActionTextColor(Color.YELLOW)
                }.show()
            }
        })
    }

    override fun onResume() {
        super.onResume()
        lifecycleScope.launch {
            viewModel.getAllNotes()
        }
    }
}