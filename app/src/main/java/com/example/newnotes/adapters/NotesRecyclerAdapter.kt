package com.example.newnotes.adapters

import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.newnotes.R
import com.example.newnotes.data.Note
import com.example.newnotes.listeners.NoteClickListener
import com.example.newnotes.listeners.RotationUpdateListener
import com.example.newnotes.utils.BackgroundUtil

class NotesRecyclerAdapter(
    private val context: Context,
    private val clickListener: NoteClickListener
) : RecyclerView.Adapter<NotesRecyclerAdapter.NotesViewHolder>(),
    RotationUpdateListener {

    companion object {
        const val LINEAR_LAYOUT = 0
        const val TAB_LAYOUT = 1
    }

    private val notes = ArrayList<Note>()
    private val holders = ArrayList<NotesViewHolder>()

    private var isTabLayout = false

    inner class NotesViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val noteTitle: TextView = view.findViewById(R.id.title_textview)
        val noteText: TextView = view.findViewById(R.id.text_textview)
        val bgDrawable = BackgroundUtil.getBackgroundDrawable(view)

        fun updateGradient(x: Float) {
            bgDrawable.setGradientCenter(x, 0.5F)
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): NotesViewHolder {
        var noteView: View? = null

        when (viewType) {
            LINEAR_LAYOUT -> {
                noteView = LayoutInflater
                    .from(context)
                    .inflate(
                        R.layout.note_recyclerview_item,
                        parent,
                        false
                    )
            }
            TAB_LAYOUT -> {
                noteView = LayoutInflater
                    .from(context)
                    .inflate(
                        R.layout.note_recyclerview_tab_item,
                        parent,
                        false
                    )
            }
        }

        val notesViewHolder = NotesViewHolder(noteView!!)
        holders.add(notesViewHolder)

        return notesViewHolder
    }

    override fun getItemCount() = notes.size

    override fun onBindViewHolder(holder: NotesViewHolder, position: Int) {
        holder.noteTitle.text = notes[position].title
        holder.noteText.text = notes[position].text
        holder.bgDrawable.colors = intArrayOf(
            Color.parseColor(notes[position].color),
            Color.parseColor("#FFFFFF")
        )

        holder.itemView.setOnLongClickListener {
            clickListener.onNoteLongClick(notes[position])
            true
        }

        holder.itemView.setOnClickListener {
            clickListener.onNoteClick(notes[position])
        }
    }

    override fun getItemViewType(position: Int): Int {
        return if (isTabLayout)
            TAB_LAYOUT
        else
            LINEAR_LAYOUT
    }

    fun setLayout(layout: Int) {
        when (layout) {
            LINEAR_LAYOUT -> {
                isTabLayout = false
            }
            TAB_LAYOUT -> {
                isTabLayout = true
            }
        }

        notifyDataSetChanged()
    }

    fun setNotes(notes: List<Note>) {
        this.notes.clear()
        this.notes.addAll(notes)
        notifyDataSetChanged()
    }

    override fun update(x: Float) {
        val updatedX = 0.5f + x
        holders.forEach {
            it.updateGradient(updatedX)
        }
    }
}