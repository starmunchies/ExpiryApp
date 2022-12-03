package com.example.expiryapp.ui.home;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SearchView;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
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


public class HomeFragment extends Fragment {
    // holds the object
    ArrayList<items> itemsArrayList = new ArrayList<>();
    String[] heading;
    RecyclerView recyclerview;
    SearchView homeFragSearchView;
    Button button;
    List<items> list;
    itemAdapter itemAdapter;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {


        // return that the fragment has been created
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
       super.onViewCreated(view, savedInstanceState);

        // Implements and inflates the search view
        button = view.findViewById(R.id.addbutton);
        homeFragSearchView = view.findViewById(R.id.searchmehomefrag);
        // Makes sure the iconified is set to false
        // as it's not in the navbar
        homeFragSearchView.setIconifiedByDefault(false);
        // creates the data
        //dataCreate();
        getItems();
        // sets recycler view to the id
        recyclerview = view.findViewById(R.id.recycle);
        // gets the context of where the recycler view is
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        // passes the array to the adapter to inflate
        itemAdapter = new itemAdapter(getContext(), itemsArrayList);
        // set recycler view to the adapter
        recyclerview.setAdapter(itemAdapter);

        FragmentManager fm = getActivity().getSupportFragmentManager();
        fm.addOnBackStackChangedListener(
                new FragmentManager.OnBackStackChangedListener() {
                    public void onBackStackChanged() {

                       getItems();
                       itemAdapter.refresh(itemsArrayList);
                        Log.d("test","called");


                    }
                });



        button.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View view) {

                // Create new fragment and transaction
                Fragment addItem = new addItem();


                FragmentTransaction transaction = getFragmentManager().beginTransaction();

               // replaces current view with new view
                transaction.replace(R.id.recycler, addItem);// adds the transaction to the backstack
                transaction.addToBackStack(null);

                // Commit the transaction
                transaction.commit();


            }
        });

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


        homeFragSearchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {
            @Override
            public boolean onQueryTextSubmit(String query) {
                return false;
            }

            @Override
            public boolean onQueryTextChange(String newText) {
                // Query listen that will filter the text
                ArrayList<items> filterMe = new ArrayList<items>();

                for (items item : itemsArrayList) {
                    // uses contain to see  if the item heading matches the inputted text
                    // forces it to lowercase
                    if (item.getHeading().toLowerCase().contains(newText.toLowerCase())) {
                        // if its a match add to the filterMe array
                        filterMe.add(item);
                    }
                }
                // if the array isn't empty set filtered list as
                // the new array list
                if (!filterMe.isEmpty()) {
                    itemAdapter.searchFilter(filterMe);
                }
                return false;
            }

        });

    }


    private void getItems() {

        @SuppressLint("StaticFieldLeak")
        class saveTaskInBackend extends AsyncTask<Void, Void, Void> {

            @Nullable
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {

                //gets a select all from the database and saves it to a list
                list = DatabaseAccessLayer.getInstance(getActivity()).getAppDatabase().dataBaseAction().getAll();
                // iterates through the list and adds it to the itemArrayList
                itemsArrayList.clear();

                for (items i : list) {itemsArrayList.add(i);}
                return null;
            }

            @Override
            protected void onPostExecute(Void aVoid) {
                super.onPostExecute(aVoid);
            }
        }
        saveTaskInBackend st = new saveTaskInBackend();
        st.execute();
    }

    // this populates the data if no other function is called
    private void dataCreate() {
        @SuppressLint("StaticFieldLeak")
        class saveTaskInBackend extends AsyncTask<Void, Void, Void> {
           
            @Override
            protected Void doInBackground(Void... voids) {
                // delete the whole db
                //DatabaseAccessLayer.getInstance(getActivity()).getAppDatabase().dataBaseAction().deleteTheList();
                // create an itemArray list
                itemsArrayList = new ArrayList<>();
                // get the headings

                heading = new String[]{"Chicken Nuggets", "50z burger", "chips", "apple", "fuck", "Chicken wings", "spiceBag", "m50 upgrade", "bomb", "drugs"};
                String[] expiration = new String[]{"2022-12-4","2022-12-5","2022-12-6","2022-12-7","2022-12-8","2022-12-9","2022-12-10","2022-12-11","2022-12-12","2022-12-13"};

                // for loop to add the headings to the item array list as an object
                for (int i = 0; i < heading.length; i++) {
                    items item = new items(heading[i]);
                    try {
                        item.setExpiryDate(expiration[i]);
                    } catch (ParseException e) {
                        e.printStackTrace();
                    }
                    //itemsArrayList.add(item);
                    // inserts an item directly into the room database
                    DatabaseAccessLayer.getInstance(getActivity()).getAppDatabase().dataBaseAction().insertExpiryData(item);
                }
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
    @Override
    public void onResume(){

        super.onResume();

        getRefresh();

        // put your code here...

    }
    //defunct code due to hard coding of fragment essentially
    // thanks android
    public void getRefresh(){
        getItems();
        itemAdapter.refresh(itemsArrayList);
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}