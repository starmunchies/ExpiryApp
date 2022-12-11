// template adapter used by the recycler view to inflate the gallery navigation fragment
// allows a user to delete add and invoke a new template as an item
package com.example.expiryapp.ui.gallery;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.expiryapp.DatabaseAccessLayer;
import com.example.expiryapp.R;
import com.example.expiryapp.ui.home.items;

import java.text.ParseException;
import java.util.ArrayList;


import static android.app.PendingIntent.getActivity;

public class templateAdapter extends RecyclerView.Adapter<com.example.expiryapp.ui.gallery.templateAdapter.GalleryFragmentViewHolder> {
    // constructor passes the context and saves it here
    Context context;
    // sets array list to what is passed in the constructor
    ArrayList<templateGall> itemsArrayList;

    // constructor that passes the context and the array list
    public templateAdapter(Context context, ArrayList<templateGall> itemsArrayList) {
        // sets the context
        this.context = context;
        //sets the arraylist
        this.itemsArrayList = itemsArrayList;
    }


    @NonNull
    @Override
    // create a viewHolder if there is no other viewHolder currently created
    public templateAdapter.GalleryFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_items, parent, false);

        //return the view holder
        return new GalleryFragmentViewHolder(v);
    }

    @Override
    // reuses the viewHolders set above to bind the headings and text
    public void onBindViewHolder(@NonNull com.example.expiryapp.ui.gallery.templateAdapter.GalleryFragmentViewHolder holder, int position) {
        //gets the item by position from the array list and saves it to item object
        templateGall items = this.itemsArrayList.get(position);
        holder.heading.setText(items.heading);
        Glide.with(context).asBitmap().load(items.getProfiler()).into(holder.imageId);

        holder.expiration.setText("Days: " + items.expiryDate);
        holder.expiration.setTextColor(Color.GREEN);


        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            // will show extra info and launch a new intent
            public void onClick(View view) {
                // creates a new item
                items addItem = new items();
                // sets the heading
                addItem.setHeading(items.getHeading());
                try {
                    //passes the expiry date info
                    addItem.setExpiryDate(items.getExpiryDate());
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                //creates a new bundle
                Bundle bundle = new Bundle();
                // adds addItem object to bundle
                bundle.putSerializable("item", addItem);
                //create intent and pass context
                Intent i = new Intent(context, addItemFromTemplate.class);
                //add bundle to intent
                i.putExtras(bundle);
                // start activity based on class called
                context.startActivity(i);
            }
        });

    }

    // gets the id from the position of the adapter position
    public int getId(int position) {
        templateGall res = itemsArrayList.get(position);
        return res.getId();
    }

    // removes item from arraylist and from database
    public void itemRemove(int position) {
        templateGall item = itemsArrayList.get(position);
        deleteItems(item);
        itemsArrayList.remove(position);
        // triggers refresh
        notifyDataSetChanged();
    }


    public void itemInsert(templateGall item) {
        addTemplateItems(item);
        itemsArrayList.add(item);
        notifyDataSetChanged();

    }

    //adds template to the db using an async thread
    private void addTemplateItems(templateGall item) {
        @SuppressLint("StaticFieldLeak")
        class doAsyncTask extends AsyncTask<Void, Void, Void> {

            @Nullable
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                //gets a select all from the database and saves it to a list
                DatabaseAccessLayer.getInstance(context).getAppDatabase().dataBaseAction().insertTemplateData(item);
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
    // returns count of array list
    public int getItemCount() {
        return itemsArrayList.size();
    }


    public static class GalleryFragmentViewHolder extends RecyclerView.ViewHolder {

        TextView heading;
        TextView expiration;
        ImageView imageId;

        public GalleryFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            // sets text to the id
            heading = itemView.findViewById(R.id.Heading);
            expiration = itemView.findViewById(R.id.expiryview);
            imageId = itemView.findViewById(R.id.image_id);
        }
    }

    //deletes template item from db
    private void deleteItems(templateGall item) {

        @SuppressLint("StaticFieldLeak")
        class doAsyncTask extends AsyncTask<Void, Void, Void> {

            @Nullable
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                //gets a select all from the database and saves it to a list
                DatabaseAccessLayer.getInstance(context).getAppDatabase().dataBaseAction().deleteTemplateFromId(item.getId());
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

    // updates filtered list for search
    public void searchFilter(ArrayList<templateGall> filteredList) {
        // sets the filteredList to the array list
        itemsArrayList = filteredList;
        // updates the adapted list accordingly
        notifyDataSetChanged();

    }
}



