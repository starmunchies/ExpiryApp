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

    public viewItem(items items) {
        this.item = items;
    }

    public viewItem() {

    }

    protected void onCreate(Bundle savedInstanceState) {
        // return that the fragment has been created

        super.onCreate(savedInstanceState);
        setContentView(R.layout.view_item);
        Intent intent = getIntent();
        item = (items) intent.getExtras().getSerializable("item");

        avatar = findViewById(R.id.avatarView);
        title = (TextView)findViewById(R.id.textView2);
        days = (TextView)findViewById(R.id.expiry);
        date = (TextView)findViewById(R.id.dateView);
        title.setText(item.getHeading());
        days.setText(String.valueOf(item.getId()));
        date.setText((item.expiryDate));
        Glide.with(getApplicationContext()).asBitmap().load(item.getProfiler()).into(avatar);

        Log.d("check", "ive been created");

    }





}
