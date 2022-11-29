package com.example.expiryapp.ui.home;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.SearchView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expiryapp.R;
import com.google.android.material.snackbar.Snackbar;


import java.util.ArrayList;


public class HomeFragment extends Fragment {
    // holds the object
    ArrayList<items> itemsArrayList;
    String[] heading;
    RecyclerView recyclerview;
    SearchView homeFragSearchView;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {


        // return that the fragment has been created
        return inflater.inflate(R.layout.fragment_home, container, false);

    }

    public void onViewCreated(@NonNull View view, Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        // Implements and inflates the search view
        homeFragSearchView = view.findViewById(R.id.searchmehomefrag);
        // Makes sure the iconified is set to false
        // as it's not in the navbar
        homeFragSearchView.setIconifiedByDefault(false);
        // creates the data
        dataCreate();
        // sets recycler view to the id
        recyclerview = view.findViewById(R.id.recycle);
        // gets the context of where the recycler view is
        recyclerview.setLayoutManager(new LinearLayoutManager(getContext()));
        // passes the array to the adapter to inflate
        itemAdapter itemAdapter = new itemAdapter(getContext(), itemsArrayList);
        // set recycler view to the adapter
        recyclerview.setAdapter(itemAdapter);





        new ItemTouchHelper(new androidx.recyclerview.widget.ItemTouchHelper.SimpleCallback(0, androidx.recyclerview.widget.ItemTouchHelper.LEFT | androidx.recyclerview.widget.ItemTouchHelper.RIGHT) {
            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                return false;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                //on recycler view item swiped then we are deleting the item of our recycler view.
                Snackbar.make(view, "item deleted: "+ itemAdapter.getId(viewHolder.getAdapterPosition()), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

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
                    // uses contain to see if if the item heading matches the inputted text
                    // forces it to lowercase
                    if (item.getHeading().toLowerCase().contains(newText.toLowerCase())) {
                        // if its a match add to the filterMe array
                        filterMe.add(item);
                    }
                }
                // if the array isn't empty set filtered list as
                // the new array list
                if (!filterMe.isEmpty()) {
                    itemAdapter.filtered(filterMe);
                }
                return false;
            }

        });

    }



    // this populates the data if no other function is called
    private void dataCreate() {
        // create an itemArray list
        itemsArrayList = new ArrayList<>();
        // get the headings
        //@TODO: 29/11/2022 set to db request
        heading = new String[]{
                "Chicken Nuggets", "50z burger", "chips", "apple", "fuck","Chicken wings", "spicebag", "m50 upgrade", "bomb", "drugs"
        };
        int[] expiration = new int[]{
                1, 12, 3, 0, 11,1, 12, 3, 0, 11
        };

        // for loop to add the headings to the item array list as an object
        for (int i = 0; i < heading.length; i++) {
            items item = new items(heading[i], expiration[i]);
            itemsArrayList.add(item);
        }





    }





    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}