package com.example.roomdb

import android.content.Intent
import android.graphics.Color
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.ContactsContract.CommonDataKinds.Note
import android.view.View
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.widget.SearchView
import androidx.core.widget.addTextChangedListener
import androidx.lifecycle.lifecycleScope
import androidx.recyclerview.widget.ItemTouchHelper
import androidx.recyclerview.widget.RecyclerView
import com.example.roomdb.adapter.RvAdapter
import com.example.roomdb.data.NoteDatabase
import com.example.roomdb.data.dao.NoteDao
import com.example.roomdb.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar

class MainActivity : AppCompatActivity() {
    lateinit var binding: ActivityMainBinding
    lateinit var dao: NoteDao
    private val adapter = RvAdapter()
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dao = NoteDatabase.getInstance(this).getNoteDao()



        binding.searchView.addTextChangedListener {
            lifecycleScope.launchWhenResumed {
                adapter.list = dao.searchNotesByTitle(it.toString()).toMutableList()
            }
        }


        binding.fbtnAdd.setOnClickListener {
            val intent = Intent(this, EditActivity::class.java)
            startActivity(intent)
        }

    }

    fun init() {
        val swapHelper = getSwapManager()
        swapHelper.attachToRecyclerView(binding.rvMain)
        binding.rvMain.adapter = adapter
        lifecycleScope.launchWhenResumed {
            adapter.list = dao.getAllNotes().toMutableList()
        }
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

                lifecycleScope.launchWhenResumed {
                    dao.deleteNote(adapter.list[pos])
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
                            dao.addNote(note = note)
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
        init()

        lifecycleScope.launchWhenResumed {
            val getAllNotes = dao.getAllNotes().toMutableList()
            if (getAllNotes.size > 0) {
                binding.tvEmpty.visibility = View.GONE
            } else {
                binding.tvEmpty.visibility = View.VISIBLE
            }
        }
    }
}