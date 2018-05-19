package com.mike.basiclistapp

import android.content.Intent
import android.net.Uri
import android.support.v7.app.AppCompatActivity
import android.os.Bundle
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.KeyEvent
import android.widget.LinearLayout
import android.widget.TextView
import kotlinx.android.synthetic.main.activity_main.*

class KotlinMainActivity : AppCompatActivity() {

    var TAG = "MainActivity"

    // onCreate is where our Activity starts
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        Log.d(TAG, "we have been created")

        // Listen for clicks on our button so we can add the item
        button.setOnClickListener {
            Log.d(TAG, "click!")
            val text = editText.text.toString()
            addItem(text)
            editText.setText("")
        }

        // Listen for keyboard presses
        editText.setOnKeyListener { view, i, keyEvent ->
            // i represents the key number.
            // keyEvent contains information about the keypress.
            // When the user lifts up on the enter key, treat it as a button click
            if (i == KeyEvent.KEYCODE_ENTER && keyEvent.action == KeyEvent.ACTION_UP) {
                button.performClick()
            }
            false
        }
    }

    // Add an item to the itemList
    fun addItem(text: String) {
        Log.d(TAG, text)
        if (text == "") {
            return
        }

        val textView = TextView(this)
        textView.text = text
        textView.textSize = 24f

        itemList.addView(textView)

        textView.setOnClickListener {
            showItemMenu(textView)
        }
    }

    // Show the menu for an item
    fun showItemMenu(item: TextView) {

        val options = arrayOf(
            "Delete Item",  // 0
            "Highlight Item", // 1
            "Let me google that for you", // 2
            "Cancel" // 3
        )


        val builder = AlertDialog.Builder(this)
        builder.setTitle(item.text)
        builder.setItems(options, { dialogInterface, i ->
            // i is the index of the option that was clicked
            when (i) {
                0 -> deleteItem(item)
                1 -> hightlightItem(item)
                2 -> searchGoogleForText(item.text.toString())
                3 -> {} // cancel, do nothing
            }
        })
        val menu = builder.create()
        menu.show()

    }

    // Remove an item from it's parent
    fun deleteItem(item: TextView) {
        (item.parent as LinearLayout).removeView(item)
    }

    // Change the colors of an item to make it stand out
    fun hightlightItem(item: TextView) {
        item.setBackgroundColor(resources.getColor(R.color.hightlight))
        item.setTextColor(resources.getColor(R.color.white))
    }

    // Search google for some text
    fun searchGoogleForText(text: String) {
        val address = "https://google.com/search?q=" + text
        val browser = Intent(Intent.ACTION_VIEW, Uri.parse(address))
        startActivity(browser)
    }
}
