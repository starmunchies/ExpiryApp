// Gallery fragment this is where the template recycler view will be accessed by the user
// allows user to delete a template, to add a template and to also add an item to the expiry table
package com.example.expiryapp.ui.gallery;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expiryapp.DatabaseAccessLayer;
import com.example.expiryapp.R;
import com.google.android.material.snackbar.Snackbar;


import java.text.ParseException;
import java.util.ArrayList;
import java.util.List;

public class GalleryFragment extends Fragment {

    // array list of templates
    ArrayList<templateGall> itemsArrayList = new ArrayList<>();
    // headings for data creation
    String[] heading;
    // recyclerView used for inflating list
    RecyclerView recyclerview;
    // search view to search templates
    SearchView homeFragSearchView;
    // button to add a new template
    Button button;
    // template list for conversion to array list late
    List<templateGall> list;
    com.example.expiryapp.ui.gallery.templateAdapter itemAdapter;

    // inflates the gallery nav
    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {

        // return that the fragment has been created
        return inflater.inflate(R.layout.fragment_gallery, container, false);
    }

    // once the view has been created this is called
    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // sets recycler view to the id
        recyclerview = view.findViewById(R.id.recyclegaller);
        button = view.findViewById(R.id.gallerybutton);
        // gets the context of where the recycler view is
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));

        homeFragSearchView = view.findViewById(R.id.searchmegalleryfrag);
        // Makes sure the iconified is set to false
        // as it's not in the navbar
        homeFragSearchView.setIconifiedByDefault(false);

        //dataCreate();
        getTemplateItems();
        itemAdapter = new templateAdapter(getContext(), itemsArrayList);
        // set recycler view to the adapter
        recyclerview.setAdapter(itemAdapter);

        // when the fab has been pressed call add template
        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Create new fragment and transaction
                Fragment addTemplate = new addTemplate();
                FragmentTransaction transaction = getFragmentManager().beginTransaction();

                // replaces current view with new view
                transaction.replace(R.id.recyclergaller, addTemplate);// adds the transaction to the backstack
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();


            }
        });

        // used to delete the item from array list and db when swiped left or right
        new ItemTouchHelper(new androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(0, androidx.recyclerview.widget.ItemTouchHelper.LEFT | androidx.recyclerview.widget.ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //on recycler view item swiped then we are deleting the item of our recycler view.
                Snackbar.make(view, "item deleted: " + itemAdapter.getId(viewHolder.getAdapterPosition()), Snackbar.LENGTH_LONG).setAction("Action", null).show();
                // passes the position of the adapter
                itemAdapter.itemRemove(viewHolder.getAdapterPosition());


            }
        }).attachToRecyclerView(recyclerview);

        // used to listen for changes in text and changes the the list based on what it contains
        homeFragSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Query listen that will filter the text
                ArrayList<templateGall> filterArrayList = new ArrayList<templateGall>();

                for (templateGall item : itemsArrayList) {
                    // uses contain to see  if the item heading matches the inputted text
                    // forces it to lowercase
                    if (item.getHeading().toLowerCase().contains(newText.toLowerCase())) {
                        // if its a match add to the filterMe array
                        filterArrayList.add(item);
                    }
                }
                // if the array isn't empty set filtered list as
                // the new array list
                if (!filterArrayList.isEmpty()) {
                    itemAdapter.searchFilter(filterArrayList);
                }
                return false;
            }
        });



    }
    // populates arraylist for recycler view with templates from the ROOM database
    private void getTemplateItems() {

        @SuppressLint("StaticFieldLeak")
        class doAsyncTask extends AsyncTask<Void, Void, Void> {

            @Nullable
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {

                //gets a select all from the database and saves it to a list
                list = DatabaseAccessLayer.getInstance(getActivity()).getAppDatabase().dataBaseAction().getAllTemplate();
                // iterates through the list and adds it to the itemArrayList
                itemsArrayList.clear();

                for (templateGall i : list) {itemsArrayList.add(i);}
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


    public void dataCreate(){
        @SuppressLint("StaticFieldLeak")
        class doAsyncTask extends AsyncTask<Void, Void, Void> {

            @Override
            protected Void doInBackground(Void... voids) {
                // delete the whole db
                //DatabaseAccessLayer.getInstance(getActivity()).getAppDatabase().dataBaseAction().deleteTheList();
                // create an itemArray list
                itemsArrayList = new ArrayList<>();
                // get the headings

                heading = new String[]{"Chicken Nuggets", "50z burger", "chips", "apple", "fuck", "Chicken wings", "spiceBag", "m50 upgrade", "bomb", "drugs"};
                String[] expiration = new String[]{"7","4","11","12","9","2","4","2","8","9"};

                // for loop to add the headings to the item array list as an object
                for (int i = 0; i < heading.length; i++) {
                    templateGall item = new templateGall();
                    try {
                        item.setHeading(heading[i]);
                        item.setExpiryDate(expiration[i]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //itemsArrayList.add(item);
                    // inserts an item directly into the room database
                    DatabaseAccessLayer.getInstance(getActivity()).getAppDatabase().dataBaseAction().insertTemplateData(item);
                }
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
    public void onDestroyView() {
        super.onDestroyView();

    }
}