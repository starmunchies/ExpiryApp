package com.example.expiryapp.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.net.Uri;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.navigation.NavController;
import androidx.navigation.Navigation;

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

public class addTemplate extends Fragment {
    // adding these as public o everything in the scope has access to them
    Button camerabutton;
    Button submit;
    EditText title;
    EditText days;
    ImageView avatar;
    // instantiates a new item class
    templateGall item = new templateGall();
    // allos the file to be accessed by the entire scope
    File file;


    // edit view items stored her
    String imageFilePath;
    String Title = null;
    String noDays= "0";




    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // return that the fragment has been created
        return inflater.inflate(R.layout.add_template, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        // attaches the ids to each part
        camerabutton = view.findViewById(R.id.cameraIntentv1);
        avatar = view.findViewById(R.id.imageView2v1);
        submit = view.findViewById(R.id.submitv1);
        days = (EditText) view.findViewById(R.id.editTextNumberv1);
        title = (EditText) view.findViewById(R.id.namev1);
        //number field needs to be set to zero otherwise it errors out
        days.setText("0");



        camerabutton.setOnClickListener(new View.OnClickListener() {
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
                    item.setProfiler(imageFilePath);
                    // creates a uri for the file so it can be accessed
                    Uri fileToPhoto = FileProvider.getUriForFile(getContext(), "com.example.expiryapp", file);
                    // puts uri into the camera intent as put extra
                    camera_intent.putExtra(MediaStore.EXTRA_OUTPUT, fileToPhoto);
                    // start activity for result is sent
                    startActivityForResult(camera_intent, 123);
                    // avatar/imageView2 is updated accordingly if it succeeded
                    Glide.with(getContext()).asBitmap().load(imageFilePath).into(avatar);

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
                noDays = days.getText().toString();

                // if title is null or number of days hasn't been set
                // it doesn't preform the action
                if(!(noDays == "" || Title == "")){
                    // creates a new item passing through the header/title
                    item.setHeading(Title);
                    // sets date object

                    //Log.d("noDays",date);

                    try {
                        // passes the string and sets expiryDSate
                        item.setExpiryDate(noDays);
                        // add and inserts the item
                        addItems(item);
                        // instantiates nav controller and navigates back to nav home
                        NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                        // pops the top of the stack i.e addItem
                        navController.popBackStack();
                        // navigates to a fresh nav_home
                        navController.navigate(R.id.nav_gallery);

                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                }


            }


        });

    }

    private void addItems(templateGall item) {
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
                DatabaseAccessLayer.getInstance(getActivity()).getAppDatabase().dataBaseAction().insertTemplateData(item);

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
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        // creates the image and saves it to a file object
        File image = File.createTempFile(imageFileName, ".jpg", storageDir);

        // sets the string to the absolute path of file
        imageFilePath = image.getAbsolutePath();
        // log where the picture is stored
        Log.d("filepath:",imageFilePath);
        // return the file image object
        return image;
    }




}