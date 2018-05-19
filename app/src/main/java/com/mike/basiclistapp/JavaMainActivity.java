package com.mike.basiclistapp;

        import android.content.DialogInterface;
        import android.content.Intent;
        import android.net.Uri;
        import android.support.v4.content.ContextCompat;
        import android.support.v7.app.AlertDialog;
        import android.support.v7.app.AppCompatActivity;
        import android.os.Bundle;
        import android.util.Log;
        import android.view.KeyEvent;
        import android.view.View;
        import android.widget.Button;
        import android.widget.EditText;
        import android.widget.LinearLayout;
        import android.widget.TextView;

public class JavaMainActivity extends AppCompatActivity {

    // Declare a TAG of type String that we will output with each log entry.
    // (So we can filter the logcat by the term "MainActivity")
    String TAG = "MainActivity";

    // Declare the code objects that control each of the views we made
    // in res/layout/activity_main.xml (We will explicitly link them to each
    // layout view ID later).
    EditText editText;
    Button button;
    LinearLayout itemList;

    /**
     * OnCreate is part of the Activity Lifecycle. It is the normal entry point for
     * the developer to start defining the behavior of the page.
     * @param savedInstanceState could potentialy contain information that is passed to this
     *                           Activity when it is created
     */
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        // This is where the Activity (and the app, in this case) starts at runtime.
        // These next two lines are required to get the ball rolling...
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        // Put a message in our log (see the Logcat window)
        Log.d(TAG,"we have been created");

        // Link each of the views from res/layout/activity_main.xml to our code objects.
        // R.id refers to views that were placed in the layout designer (matching their ID)
        editText = findViewById(R.id.editText);
        button = findViewById(R.id.button);
        itemList = findViewById(R.id.itemList);

        // Now that our button is connected to the correct layout view, let's listen for
        // button clicks and write an action for when we hear one
        button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // In here, we write the click action...

                // First, let's record the click in the log
                Log.d(TAG, "Button clicked!");

                // Then let's call a function we made that reads the text from the input box
                // and creates a new item for the itemList
                submitInputText();


            }
        });

        // Let's also listen to each keyboard tap inside the editText. Then we can
        // make custom responses to different keys.
        editText.setOnKeyListener(new View.OnKeyListener() {
            @Override
            public boolean onKey(View view, int i, KeyEvent keyEvent) {

                // Record information about the keyEvent in the log
                Log.d(TAG, keyEvent.toString());

                // See which key was pressed and if it is being pressed down or released up
                int keyCode = keyEvent.getKeyCode();
                int keyAction = keyEvent.getAction();

                // When the user lifts up on the ENTER key, let's treat it like a button click
                // and add the editText text to the list.
                if (keyCode == KeyEvent.KEYCODE_ENTER && keyAction == KeyEvent.ACTION_UP) {
                    submitInputText();
                }

                /*
                you can read about the next line here:
                https://stackoverflow.com/questions/27693018/with-onkeylistener-what-is-the-difference-between-return-true-and-false?
                */
                return false;
            }
        });

    }

    /**
     * Check the current value of the editText and add it to the itemList if it
     * is acceptable.
     * @returns void
     */
    void submitInputText() {
        // Let's see what's in the input box.
        // Hopefully, the user put something in it.
        String text = editText.getText().toString();
        Log.d(TAG, text);

        // We want to add the text to the list on our page, but what if it's blank?
        // Let's make sure it actually has some content.
        // (trim() removes all leading and trailing spaces)
        if (!text.trim().equals("")) {
            // Ok, it has something besides spaces, let's add it
            addNewItem(text);
            // And lets clear the editText to get ready for new input
            editText.setText("");
        } else {
            // Otherwise, record an empty click
            Log.d(TAG, "skipping empty input");
        }
    }

    /**
     * Add a new TextView to our itemList to display a list item.
     * @param text to be displayed on the new list item
     * @returns void
     */
    void addNewItem(final String text) {
        // Make a new TextView item that we can add to the itemList.
        final TextView item = new TextView(this);
        item.setText(text);
        item.setTextSize(24);
        itemList.addView(item);

        // Each item will have some menu options that appear when it is clicked.
        item.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                showItemMenu((TextView)view);
            }
        });
    }

    /**
     * Show a menu for the given item that gives the user some options for interacting with
     * the item.
     * @param item to display a menu for
     * @returns void
     */
    void showItemMenu(final TextView item) {

        // Make an array of all the menu options
        String[] menuOptions = {
                "Delete Item", // 0
                "Highlight Item", // 1
                "Let me google that for you", // 2
                "cancel"  // 3
        };

        // Use an AlertDialog.Builder to define the behavior of the AlertDialog
        // (The AlertDialog will be our pop-up menu)
        AlertDialog.Builder builder = new AlertDialog.Builder(this);

        // Tell the builder to set the title of the menu to the text of the item
        builder.setTitle(item.getText().toString());

        // Tell the builder what menuOptions (defined above) to show and what to do
        // when each is clicked.
        builder.setItems(menuOptions, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialogInterface, int i) {
                // Do something different depending on which option number (i) was clicked.
                switch (i) {
                    case 0:
                        deleteItem(item);
                        break;
                    case 1:
                        highlightItem(item);
                        break;
                    case 2:
                        searchGoogleForText(item.getText().toString());
                        break;
                    case 3:
                        // cancel, do nothing
                        break;
                }
                /* You can read about switch statements in the Java Documentation:
                 https://docs.oracle.com/javase/tutorial/java/nutsandbolts/switch.html
                 */
            }
        });

        // Create and show the menu
        AlertDialog menu = builder.create();
        menu.show();
    }

    /**
     * Remove the given View from it's parent View, making it disappear.
     * Only safe if the view's parent is a LinearLayout
     * @param view to remove
     * @returns void
     * @throws ClassCastException if the view's parent is not a LinearLayout
     */
    void deleteItem(View view) {
        // We are forcing an explicit type cast from a ViewParent (returned from getParent()) to
        // a LinearLayout. This is an example of polymorphism in Java. The parent view qualifies
        // as both a ViewParent type and a LinearLayout type. We do this because removeView() is
        // only available from LinearLayout.
        // Note: This is only safe if the view's parent is a actually a LinearLayout.
        ((LinearLayout) view.getParent()).removeView(view);
    }

    /**
     * Make the item's appearance more prominent in the list.
     * @param view to highlight
     * @returns void
     */
    void highlightItem(TextView view) {

        // R.color refers to the res/values/colors.xml file.
        // We defined the highlight and white values there.
        view.setBackground(getDrawable(R.color.highlight));
        view.setTextColor(ContextCompat.getColor(this, R.color.white));
    }

    /**
     * Open a web browser and use it to search google for some text.
     * @param text
     * @return void
     */
    void searchGoogleForText(String text) {

        // Make the URL address to provide to the web browser.
        // Uri.parse(url) below will take care of any spaces in text.
        String url = "https://www.google.com/search?q=" + text;

        // See the address in the logcat
        Log.d(TAG, "Opening browser to " + url);

        // Make an Intent that will use the ACTION_VIEW instruction to open a browser
        // at the Uri we parse from our url.
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));

        // Open a new Android Activity as specified by the browserIntent.
        startActivity(browserIntent);
    }
}
