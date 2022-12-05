package com.example.expiryapp.ui.home;

import android.annotation.SuppressLint;
import android.app.Activity;
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
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.example.expiryapp.DatabaseAccessLayer;
import com.example.expiryapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Locale;

public class updateItem extends AppCompatActivity {
    items item;
    EditText heading;
    ImageView avatar;
    Button submit;
    Button updateImage;
    String pathToImage;
    File file;

    public updateItem() {

    }

    protected void onCreate(Bundle savedInstanceState) {
        // return that the fragment has been created

        super.onCreate(savedInstanceState);
        setContentView(R.layout.update_item);
        Intent intent = getIntent();
        item = (items) intent.getExtras().getSerializable("item");

        avatar = findViewById(R.id.imageView3);
        updateImage = findViewById(R.id.updateimage);
        submit = findViewById(R.id.submitupdateform);
        heading = findViewById(R.id.updateheading);
        heading.setText(item.getHeading());

        Glide.with(getApplicationContext()).asBitmap().load(item.getProfiler()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(avatar);
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    item.setHeading(heading.getText().toString());
                    updateItems(item);
                    Snackbar.make(view, "Item updated", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    finish();
                }catch (Exception e){
                    Snackbar.make(view, "An Error Occured", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }


            }
        });

        updateImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                Intent camera_intent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
                // Start the activity with camera_intent, and request pic id


                try {
                    // creates and saves the file
                    file = createFile();
                } catch (IOException ex) {

                    Snackbar.make(view, "An Error Occured", Snackbar.LENGTH_LONG).setAction("Action", null).show();

                }
                // if the file hasnt been written to execute the following
                if (file != null) {
                    //sets the profile picture
                    // creates a uri for the file so it can be accessed
                    Uri fileToPhoto = FileProvider.getUriForFile(getApplicationContext(), "com.example.expiryapp", file);
                    // puts uri into the camera intent as put extra
                    camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, fileToPhoto);
                    // start activity for result is sent
                    startActivityForResult(camera_intent, 666);

                }
            }

        });
    }

    // reference: https://medium.com/android-news/androids-new-image-capture-from-a-camera-using-file-provider-dd178519a954
    // creating an  image file and saving it to a directory
    private File createFile() throws IOException {
        // time stamp is created to as part of its uniqueness to log and upload
        String timeStamp = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());
        // adds time stamp to the filename
        String imageFileName = "TEMP_IMG_" + timeStamp + "_";
        // gets the directory from the files_path xml file
        File directory = getApplicationContext().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // creates the image and saves it to a file object
        File imageFile = File.createTempFile(imageFileName, ".jpg", directory);

        // sets the string to the absolute path of file
        pathToImage = imageFile.getAbsolutePath();
        // log where the picture is stored
        Log.d("filepath:", pathToImage);
        item.setProfiler(imageFile.getAbsolutePath());
        // return the file image object
        return imageFile;
    }

    private void updateItems(items item) {
        @SuppressLint("StaticFieldLeak")
        class doAsyncTask extends AsyncTask<Void, Void, Void> {

            @Nullable
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                //gets a select all from the database and saves it to a list
                DatabaseAccessLayer.getInstance(getApplicationContext()).getAppDatabase().dataBaseAction().updateAnExistingRow(item.getId(), item.getProfiler(), item.getHeading());
                // iterates through the list and adds it to the itemArrayList
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }
        doAsyncTask bg = new doAsyncTask();
        bg.execute();

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 666 && resultCode == Activity.RESULT_OK) {

            Glide.with(getApplicationContext()).asBitmap().load(pathToImage).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(avatar);
        }
    }



}
