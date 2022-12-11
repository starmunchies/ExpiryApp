    // allows for an item to be added to the database
    // uses a variety pf features suh as camera intents
    // image creation, date conversion and database updates.

    package com.example.expiryapp.ui.home;

    import static android.content.Intent.getIntent;

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
    import com.google.android.material.snackbar.Snackbar;
    // all of these are imported in order to read or parse the date object
    import java.io.File;
    import java.io.IOException;
    import java.text.DateFormat;
    import java.text.ParseException;
    import java.text.SimpleDateFormat;
    import java.util.Date;
    import java.util.Locale;

    public class addItem extends Fragment {
        // adding these as public so everything in the scope has access to them
        Button camerabutton;
        Button submit;
        EditText title;
        EditText days;
        ImageView avatar;
        // instantiates a new item class
        items item = new items();
        // allows the file to be accessed by the entire scope
        File file;


        // edit view items stored her
        String imageFilePath;
        String Title = null;
        int noDays= 0;




        public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


            // return that the fragment has been created
            return inflater.inflate(R.layout.add_item, container, false);

        }

        // once the view has been inflated call this

        public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
            // attaches the ids to each part
            camerabutton = view.findViewById(R.id.cameraIntent);
            avatar = view.findViewById(R.id.imageView2);
            submit = view.findViewById(R.id.submit);
            days = (EditText) view.findViewById(R.id.editTextNumber);
            title = (EditText) view.findViewById(R.id.name);
            //number field needs to be set to zero otherwise it errors out
            days.setText("0");


            // when camera button is clicked an intent will be instantiated
            camerabutton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    // implicit intent
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
                        startActivityForResult(camera_intent, 666);
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
                    noDays = Integer.parseInt(days.getText().toString());
                   // if title is null or number of days hasn't been set
                    // it doesn't preform the action
                    if(!(noDays == -1 || Title == null)){
                        // creates a new item passing through the header/title
                        item.setHeading(Title);
                        // sets date object
                        Date current = new Date();
                        //long dayInMilliSeconds = current.getTime();
                        // parses date and adds the multiple days from editView
                        Date sendDate = new Date(current.getTime() + (noDays*60*60*24*1000));
                        //formats the date to yy-mm-dd string
                        DateFormat formatDate = new SimpleDateFormat("yyyy-MM-dd");
                        // saves to string
                        String date = formatDate.format(sendDate);
                        //Log.d("noDays",date);

                        try {
                            // passes the string and sets expiryDate
                            item.setExpiryDate(date);
                            // add and inserts the item
                            addItems(item);
                            // instantiates nav controller and navigates back to nav home
                            NavController navController = Navigation.findNavController(getActivity(), R.id.nav_host_fragment_content_main);
                            // pops the top of the stack i.e addItem
                            navController.popBackStack();
                            // navigates to a fresh nav_home
                            navController.navigate(R.id.nav_home);

                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                    }


                }


            });

        }

        private void addItems(items item) {
            // talks to database

            // runs an async task
            @SuppressLint("StaticFieldLeak")
            class saveTaskInBackend extends AsyncTask<Void, Void, Void> {

                @Nullable
                @SuppressLint("WrongThread")
                @Override
                // in background
                protected Void doInBackground(Void... voids) {
                    //inserts object into the database
                    DatabaseAccessLayer.getInstance(getActivity()).getAppDatabase().dataBaseAction().insertExpiryData(item);

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

        // reference: https://medium.com/android-news/androids-new-image-capture-from-a-camera-using-file-provider-dd178519a954
        // creating an  image file and saving it to a directory
        private File createFile() throws IOException {
            // create a timestamp to add to img
            String timeStamp = new SimpleDateFormat("HHmmss", Locale.getDefault()).format(new Date());

            // create a unique filename that cant be replicated
            String imageFileName = "TEMP_IMG_" + timeStamp + "_";
           // gets the directory from the directory_pictures located in the xml files
            File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
            // creates an object of the image
            File image = File.createTempFile(imageFileName, ".jpg", storageDir);

            // sets the string to the absolute path of file
            imageFilePath = image.getAbsolutePath();
            // log where the picture is stored
            Log.d("filepath:",imageFilePath);
            // return the file image object
            return image;
        }
        //end reference
    }