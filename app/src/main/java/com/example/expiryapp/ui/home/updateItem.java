// this class allows the user to update the items object from the db
// works by opening an intent and opening a serialized object to be used and updated to the dao

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
    // object saved here
    items item;
    // setting objects for xml variables
    EditText heading;
    ImageView avatar;
    Button submit;
    Button updateImage;

    // used for image creation
    String pathToImage;
    File file;

    // empty constructor needed to initialise an intent
    public updateItem() {

    }

    // on creation of the class
    protected void onCreate(Bundle savedInstanceState) {

        // checks for any saved instances from the heirarchy
        super.onCreate(savedInstanceState);
        // sets xml layout
        setContentView(R.layout.update_item);
        // creates an intent
        Intent intent = getIntent();
        // saes serialised object that was passed by put serializable
        item = (items) intent.getExtras().getSerializable("item");

        // attaches variables to view
        avatar = findViewById(R.id.imageView3);
        updateImage = findViewById(R.id.updateimage);
        submit = findViewById(R.id.submitupdateform);
        heading = findViewById(R.id.updateheading);
        heading.setText(item.getHeading());

        // loads in an image if it exists
        Glide.with(getApplicationContext()).asBitmap().load(item.getProfiler()).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(avatar);
        // button that updates info passed
        submit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                try {
                    // sets the heading to the edit text
                    item.setHeading(heading.getText().toString());
                    // passed to update item to background async
                    updateItems(item);
                    Snackbar.make(view, "Item updated", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                    // returns to previous intent on stack
                    finish();
                }catch (Exception e){
                    Snackbar.make(view, "An Error Occured", Snackbar.LENGTH_LONG).setAction("Action", null).show();
                }


            }
        });

        // opens camera intention
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
    //eor

    private void updateItems(items item) {
        @SuppressLint("StaticFieldLeak")
        class doAsyncTask extends AsyncTask<Void, Void, Void> {

            @Nullable
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                //gets a select all from the database and saves it to a list
                DatabaseAccessLayer.getInstance(getApplicationContext()).getAppDatabase().dataBaseAction().updateAnExistingRow(item.getId(), item.getProfiler(), item.getHeading());
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
    // on return of the intent
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {

        super.onActivityResult(requestCode, resultCode, data);
        // if it was succesfull
        if (requestCode == 666 && resultCode == Activity.RESULT_OK) {
            // load the image
            Glide.with(getApplicationContext()).asBitmap().load(pathToImage).diskCacheStrategy(DiskCacheStrategy.NONE).skipMemoryCache(true).into(avatar);
        }
    }



}
