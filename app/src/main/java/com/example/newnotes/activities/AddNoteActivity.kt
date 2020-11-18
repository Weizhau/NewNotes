package com.example.newnotes.activities

import android.app.Activity
import android.content.Intent
import android.graphics.Color
import android.graphics.drawable.GradientDrawable
import android.os.Bundle
import android.text.TextUtils
import android.view.View
import android.widget.AdapterView
import android.widget.Button
import android.widget.EditText
import android.widget.Spinner
import androidx.appcompat.app.AppCompatActivity
import com.example.newnotes.ColorRow
import com.example.newnotes.R
import com.example.newnotes.adapters.ColorSpinnerAdapter
import com.example.newnotes.data.Note
import com.example.newnotes.listeners.RotationUpdateListener
import com.example.newnotes.utils.BackgroundUtil
import com.example.newnotes.utils.BorderRenderer
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json

class AddNoteActivity : AppCompatActivity(), RotationUpdateListener {
    companion object {
        const val EXTRA_REPLY = "com.example.android.wordlistsql.REPLY"
    }

    private lateinit var titleEditText: EditText
    private lateinit var textEditText: EditText

    private lateinit var titleBgDrawable: GradientDrawable
    private lateinit var textBgDrawable: GradientDrawable
    private lateinit var renderer: BorderRenderer
    private lateinit var spinner: Spinner
    private var selectedColor: String = ""
    private var note: Note? = null
    private var updateMode = false

    val colors = listOf(
        ColorRow("#99E2FF"),
        ColorRow("#FF9292"),
        ColorRow("#ABFFAF"),
        ColorRow("#FFFBA6"),
        ColorRow("#FFFFFF")
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_note)
        setSupportActionBar(findViewById(R.id.note_add_toolbar))

        titleEditText = findViewById(R.id.title_editext)
        textEditText = findViewById(R.id.text_edittext)

        if (intent.getBooleanExtra("update", false)) {
            note = Json.decodeFromString<Note>(intent.getStringExtra("note"))
            updateMode = true
            setNoteData()
        }

        titleBgDrawable = BackgroundUtil.getBackgroundDrawable(titleEditText)
        textBgDrawable = BackgroundUtil.getBackgroundDrawable(textEditText)

        renderer = BorderRenderer(this/*, this*/)
        renderer.addListener(this)

        findViewById<Button>(R.id.save_button).setOnClickListener {
            replyToMainActivity()
        }

        spinner = findViewById(R.id.color_pick_spinner)
        spinner.adapter = ColorSpinnerAdapter(this@AddNoteActivity, colors)
        addSpinnerSelectedListener()
    }

    private fun setNoteData() {
        titleEditText.setText(note?.title)
        textEditText.setText(note?.text)
    }

    private fun replyToMainActivity() {
        val replyIntent = Intent()
        if (TextUtils.isEmpty(titleEditText.text) && TextUtils.isEmpty(textEditText.text)) {
            setResult(Activity.RESULT_CANCELED, replyIntent)
        } else {
            if (updateMode) {
                val newNote = Note(
                    titleEditText.text.toString().trim(),
                    textEditText.text.toString().trim(),
                    selectedColor
                )
                newNote.id = note?.id!!

                replyIntent.putExtra(
                    EXTRA_REPLY,
                    Json.encodeToString(
                        newNote
                    )
                )
            } else {
                replyIntent.putExtra(
                    EXTRA_REPLY,
                    Json.encodeToString(
                        Note(
                            titleEditText.text.toString().trim(),
                            textEditText.text.toString().trim(),
                            selectedColor
                        )
                    )
                )
            }
            setResult(Activity.RESULT_OK, replyIntent)
        }
        finish()
    }

    private fun addSpinnerSelectedListener() {
        spinner.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(
                parent: AdapterView<*>?,
                view: View?,
                position: Int,
                id: Long
            ) {
                selectedColor = colors[position].color
                updateColor(colors[position].color)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    override fun onStart() {
        super.onStart()
        renderer.start()
    }

    override fun onPause() {
        super.onPause()
        renderer.stop()
    }

    fun updateColor(color: String) {
        titleBgDrawable.colors = intArrayOf(
            Color.parseColor(color),
            Color.parseColor("#FFFFFF")
        )
        textBgDrawable.colors = intArrayOf(
            Color.parseColor(color),
            Color.parseColor("#FFFFFF")
        )
    }

    private fun updateGradients(x: Float) {
        titleBgDrawable.setGradientCenter(x, 0.5F)
        textBgDrawable.setGradientCenter(x, 0.5F)
    }

    override fun update(x: Float) {
        val updatedX = 0.5f + x
        updateGradients(updatedX)
    }
}