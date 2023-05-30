package com.example.roomdb.ui

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.example.roomdb.utils.Constans
import com.example.roomdb.data.NoteDatabase
import com.example.roomdb.data.dao.NoteDao
import com.example.roomdb.data.entity.Note
import com.example.roomdb.databinding.ActivityEditBinding
import com.example.roomdb.presentation.MainViewModel

class EditActivity : AppCompatActivity() {

    private lateinit var viewModel: MainViewModel

    lateinit var dao : NoteDao
    lateinit var binding: ActivityEditBinding
    private var isState = false
    private var id = 0

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityEditBinding.inflate(layoutInflater)
        setContentView(binding.root)
        dao = NoteDatabase.getInstance(this).getNoteDao()

        viewModel = ViewModelProvider(
            this,
            ViewModelProvider.AndroidViewModelFactory(application)
        ).get(MainViewModel::class.java)

        getIntents()

        binding.flbtnEdit.setOnClickListener {
            binding.etTitle.isEnabled = true
            binding.etDescription.isEnabled = true
        }

        binding.fltbtnSave.setOnClickListener {
            val etTitle = binding.etTitle.text.toString()
            val etDesc = binding.etDescription.text.toString()


            if (etTitle.isNotEmpty() && etDesc.isNotEmpty()){
                if (isState){
                    lifecycleScope.launchWhenResumed {
                        viewModel.updateNote(note = Note(id,etTitle,etDesc))
                    }
                    Toast.makeText(this,"Сохранено",Toast.LENGTH_SHORT).show()
                }else{
                    lifecycleScope.launchWhenResumed {
                        viewModel.addNote(Note(id,etTitle,etDesc))
                    }
                    Toast.makeText(this,"Сохранено",Toast.LENGTH_SHORT).show()
                }
                finish()
            }else{
                Toast.makeText(this,"Заполните поля !!!",Toast.LENGTH_SHORT).show()
            }
        }

    }

    private fun getIntents() {
        binding.flbtnEdit.visibility = View.GONE

        val i = intent
        if (i!=null){

            if (i.getStringExtra(Constans.I_TITLE_KEY)!=null){

                id = i.getIntExtra(Constans.I_ID_KEY,0)
                binding.etTitle.setText(i.getStringExtra(Constans.I_TITLE_KEY))
                binding.etDescription.setText(i.getStringExtra(Constans.I_CONTENT_KEY))
                isState = true

                binding.flbtnEdit.visibility = View.VISIBLE
                binding.etTitle.isEnabled = false
                binding.etDescription.isEnabled = false
            }
        }
    }
}