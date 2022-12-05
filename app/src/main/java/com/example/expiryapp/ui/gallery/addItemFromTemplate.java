package com.example.expiryapp.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.content.FileProvider;

import com.bumptech.glide.Glide;
import com.example.expiryapp.DatabaseAccessLayer;
import com.example.expiryapp.R;
import com.example.expiryapp.ui.home.items;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.DateFormat;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class addItemFromTemplate extends AppCompatActivity {
    // adding these as public o everything in the scope has access to them
    Button camerabutton;
    Button submit;
    EditText title;
    EditText days;
    ImageView avatar;
    // instantiates a new item class
    //items item = new items();
    // allos the file to be accessed by the entire scope
    File file;


    // edit view items stored her
    String image_Path;
    String Title = null;
    int noDays= 0;




    protected void onCreate(Bundle savedInstanceState) {


        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_item);
        camerabutton = findViewById(R.id.cameraIntent);
        avatar = findViewById(R.id.imageView2);
        submit = findViewById(R.id.submit);
        days = (EditText) findViewById(R.id.editTextNumber);
        title = (EditText) findViewById(R.id.name);

        Intent intent = getIntent();
        items item = (items) intent.getExtras().getSerializable("item");
        //number field needs to be set to zero otherwise it errors out
        days.setText(item.expiryDate);
        title.setText(item.heading);

        camerabutton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent cameraIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Start the activity with cameraIntent, and request pic id


                try {
                    // creates and saves the file
                    file = createFile();
                } catch (IOException ex) {

                    Snackbar.make(view, "An Error Occured", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
                // if the file hasnt been written to execute the following
                if (file != null) {
                    //sets the profile picture
                    item.setProfiler(image_Path);
                    // creates a uri for the file so it can be accessed
                    Uri fileToPhoto = FileProvider.getUriForFile(getApplicationContext(), "com.example.expiryapp", file);
                    // puts uri into the camera intent as put extra
                    cameraIntent.putExtra(MediaStore.EXTRA_OUTPUT, fileToPhoto);
                    // start activity for result is sent
                    startActivityForResult(cameraIntent, 666);
                    // avatar/imageView2 is updated accordingly if it succeeded
                    Glide.with(getApplicationContext()).asBitmap().load(image_Path).into(avatar);

                    Snackbar.make(view, "Image added", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
            }

        });
        // sets a listener for a button
        submit.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {
                // sets string to whats in title editView
                Title = title.getText().toString();
                //set number of days and parses it to an int
                noDays = Integer.parseInt(days.getText().toString());

                // if title is null or number of days hasn't been set
                // it doesn't preform the action
                if (!(noDays == -1 || Title == null)) {
                    // creates a new item passing through the header/title
                    item.setHeading(Title);
                    // sets date object
                    Date current = new Date();
                    //long dayInMilliSeconds = current.getTime();
                    // parses date and adds the multiple days from editView
                    Date sendDate = new Date(current.getTime() + (noDays * 60 * 60 * 24 * 1000));
                    //formats the date to yy-mm-dd string
                    DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
                    // saves to string
                    String date = formatDate.format(sendDate);
                    //Log.d("noDays",date);

                    try {
                        // passes the string and sets expiryDSate
                        item.setExpiryDate(date);
                        // add and inserts the item
                        addItems(item);
                        // instantiates nav controller and navigates back to nav home
                       finish();

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


            }


        });

    }


    private void addItems(items item) {
        // talks to database
        @SuppressLint("StaticFieldLeak")
                // runs an async task
        class saveTaskInBackend extends AsyncTask<Void, Void, Void> {

            @Nullable
            @SuppressLint("WrongThread")
            @Override
            // in backgrounf
            protected Void doInBackground(Void... voids) {
                //inserts object into the database
                DatabaseAccessLayer.getInstance(getApplicationContext()).getAppDatabase().dataBaseAction().insertExpiryData(item);

                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }
        saveTaskInBackend bg = new saveTaskInBackend();
        bg.execute();

    }


    private File createFile() throws IOException {
        // time stamp is created to as part of its uniqueness to log and upload
        String timeStamp = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
        // adds time stamp to the filename
        String imageFileName = "TEMP_IMG_" + timeStamp + "_";
        // gets the directory from the files_path xml file
        File storageDir = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // creates the image and saves it to a file object
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // sets the string to the absolute path of file
        image_Path = image.getAbsolutePath();
        // log where the picture is stored
        Log.d("filepath:", image_Path);
        // return the file image object
        return image;
    }




}