package com.example.newnotes.activities

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import android.content.SharedPreferences
import android.os.Bundle
import android.widget.ImageButton
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import androidx.recyclerview.widget.StaggeredGridLayoutManager
import com.example.newnotes.R
import com.example.newnotes.adapters.NotesRecyclerAdapter
import com.example.newnotes.data.Note
import com.example.newnotes.data.NotesViewModel
import com.example.newnotes.dialogs.OptionsDialog
import com.example.newnotes.listeners.NoteClickListener
import com.example.newnotes.listeners.OnSwipeTouchListener
import com.example.newnotes.utils.BorderRenderer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class MainActivity : AppCompatActivity(), NoteClickListener {
    companion object {
        const val newNoteActivityRequestCode = 1
        const val updateNoteActivityRequestCode = 2
    }

    private lateinit var notesViewModel: NotesViewModel
    private lateinit var renderer: BorderRenderer
    private lateinit var sharedPref: SharedPreferences
    private lateinit var notesRecyclerView: RecyclerView
    private lateinit var notesRecyclerAdapter: NotesRecyclerAdapter
    private var chosenNote: Note? = null

    @SuppressLint("ClickableViewAccessibility")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        sharedPref = getSharedPreferences("com.example.newnotes", MODE_PRIVATE)


        notesRecyclerAdapter = NotesRecyclerAdapter(this, this)

        notesRecyclerView = findViewById(R.id.notes_recyclerview)
        notesRecyclerView.apply {
            adapter = notesRecyclerAdapter

            if (sharedPref.getInt("preview_layout", 0) == 0) {
                layoutManager = LinearLayoutManager(this@MainActivity)
            } else {
                layoutManager =
                    StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
                notesRecyclerAdapter.setLayout(NotesRecyclerAdapter.TAB_LAYOUT)
            }
        }
        notesRecyclerView.setOnTouchListener(object : OnSwipeTouchListener(this@MainActivity) {
            override fun onSwipeLeft() {
                startAddNoteActivity()
            }
        })

        notesViewModel = ViewModelProvider(this).get(NotesViewModel::class.java)
        notesViewModel.allNotes.observe(this, { notes ->
            notes?.let { notesRecyclerAdapter.setNotes(it) }
        })

        findViewById<ImageButton>(R.id.change_layout_button).setOnClickListener {
            changePreviewLayout()
        }

        renderer = BorderRenderer(this)
        renderer.addListener(notesRecyclerAdapter)
    }

    private fun changePreviewLayout() {
        if (notesRecyclerView.layoutManager is StaggeredGridLayoutManager) {
            notesRecyclerView.layoutManager = LinearLayoutManager(this)
            sharedPref.edit().putInt("preview_layout", 0).apply()
            notesRecyclerAdapter.setLayout(NotesRecyclerAdapter.LINEAR_LAYOUT)
        } else {
            notesRecyclerView.layoutManager =
                StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL)
            sharedPref.edit().putInt("preview_layout", 1).apply()
            notesRecyclerAdapter.setLayout(NotesRecyclerAdapter.TAB_LAYOUT)
        }

    }

    override fun onResume() {
        super.onResume()
        renderer.start()
    }

    override fun onPause() {
        super.onPause()
        renderer.stop()
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == newNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(AddNoteActivity.EXTRA_REPLY)?.let {
                notesViewModel.insert(Json.decodeFromString(it))
            }
        } else if (requestCode == updateNoteActivityRequestCode && resultCode == Activity.RESULT_OK) {
            data?.getStringExtra(AddNoteActivity.EXTRA_REPLY)?.let {
                val note = Json.decodeFromString<Note>(it)
                notesViewModel.delete(chosenNote!!)
                notesViewModel.insert(note)
            }
        } else {
            Toast.makeText(
                applicationContext,
                R.string.note_not_saved_text,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    override fun onNoteLongClick(note: Note) {
        val optionsDialog = OptionsDialog(this, notesViewModel, note)
        optionsDialog.show()
    }

    override fun onNoteClick(note: Note) {
        startEditNoteActivity(note)
    }

    private fun startAddNoteActivity() {
        val intent = Intent(this, AddNoteActivity::class.java)
        startActivityForResult(intent, newNoteActivityRequestCode)
    }

    private fun startEditNoteActivity(note: Note) {
        chosenNote = note
        val intent = Intent(this, AddNoteActivity::class.java)
        intent.putExtra("update", true)
        intent.putExtra("note", Json.encodeToString(note))
        startActivityForResult(intent, updateNoteActivityRequestCode)
    }
}