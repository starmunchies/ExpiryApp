// this class shows the user the full details of the item
// it pulls an object from the bundle and displays it to the user
package com.example.expiryapp.ui.home;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.example.expiryapp.R;

public class viewItem  extends AppCompatActivity {
    items item;
    TextView title;
    TextView days;
    TextView date;
    ImageView avatar;

    // constructor that sets the item object
    public viewItem(items items) {
        this.item = items;
    }
    // empty constructor
    public viewItem() {

    }
    // on creation of the class
    protected void onCreate(Bundle savedInstanceState) {
        // return that the fragment has been created

        super.onCreate(savedInstanceState);
        // set it to the view_item layout
        setContentView(R.layout.view_item);
        // creates an intent
        Intent intent = getIntent();
        item = (items) intent.getExtras().getSerializable("item");

        // sets all the parameters for the view_item xml
        avatar = findViewById(R.id.avatarView);
        title = (TextView)findViewById(R.id.textView2);
        days = (TextView)findViewById(R.id.expiry);
        date = (TextView)findViewById(R.id.dateView);

        // sets the text based on the object passed through it
        title.setText(item.getHeading());
        days.setText(String.valueOf(item.getId()));
        date.setText((item.expiryDate));
        // loads image with the GLIDE library
        Glide.with(getApplicationContext()).asBitmap().load(item.getProfiler()).into(avatar);

        //Log.d("check", "ive been created");
    }
}
