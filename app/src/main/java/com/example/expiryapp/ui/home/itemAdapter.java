// template adapter used by the recycler view to inflate the home navigation fragment
// allows a user to delete add and invoke a new template as an item
package com.example.expiryapp.ui.home;

import static android.app.PendingIntent.getActivity;
import static com.example.expiryapp.R.drawable.ico;

import static java.lang.Math.floor;

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

import java.text.ParseException;
import java.util.ArrayList;
import java.util.Date;


public class itemAdapter extends RecyclerView.Adapter<itemAdapter.homeFragmentViewHolder>{
    // constructor passes the context and saves it here
    Context context;
    // sets array list to what is passed in the constructor
    ArrayList<items> itemsArrayList;

    // constructor that passes the context and the array list
    public itemAdapter(Context context, ArrayList<items> itemsArrayList) {
        // sets the context
        this.context = context;
        //sets the arraylist
        this.itemsArrayList = itemsArrayList;
    }


    @NonNull
    @Override
    // create a viewHolder if there is no other viewHolder currently created
    public homeFragmentViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v = LayoutInflater.from(context).inflate(R.layout.list_items,parent,false);

        //return the view holder
        return new homeFragmentViewHolder(v);
    }

    @Override
    // reuses the viewHolders set above to bind the headings and text
    public void onBindViewHolder(@NonNull homeFragmentViewHolder holder, int position) {
        //gets the item by position from the array list and saves it to item object
        items items = this.itemsArrayList.get(position);

        holder.heading.setText(items.heading);
        //gets expiry  date from item
        Date expiry = null;

        try {
            expiry = items.getExpiryDate();
        } catch (ParseException e) {
            e.printStackTrace();
        }
        // gets the current date today
        Date current = new Date();
        // gets the difference in milliseconds
        long difference =  current.getTime() - expiry.getTime();
        // gets it in number of days

        difference = (difference / (1000 * 60 * 60 * 24));
        // rounds to nearest int
        floor(difference);

        difference = (difference < 0 ? -difference : difference);


        if(difference <= 3 )
        {
            holder.expiration.setText("Expiration: " + difference + " days");
            holder.expiration.setTextColor(Color.rgb(225,165,0));
        }
        else{

            holder.expiration.setText("Expiration: " + difference + " days");
            holder.expiration.setTextColor(Color.GREEN);
        }

        if(difference == 0 )
        {
            holder.expiration.setText("Expired");
            holder.expiration.setTextColor(Color.RED);

        }

        holder.imageId.setImageResource(ico);

        Glide.with(context.getApplicationContext()).asBitmap().load(items.getProfiler()).into(holder.imageId);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            // will show extra info and launch a new intent
            public void onClick(View view) {

                Bundle bundle = new Bundle();
                bundle.putSerializable("item", items);
                Intent i = new Intent(context, viewItem.class);
                i.putExtras(bundle);

                context.startActivity(i);


            }
        });
        // on long click will open a new edit intent
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){

            public boolean onLongClick(View view) {
                //Snackbar.make(view, "item long clicked :" + getId(holder.getAdapterPosition()), Snackbar.LENGTH_LONG)
                       // .setAction("Action", null).show();


                Bundle bundle = new Bundle();
                bundle.putSerializable("item", items);
                Intent i = new Intent(context,updateItem.class);
                i.putExtras(bundle);

                context.startActivity(i);

                return true;
            }
        });

    }
    // gets the id from the position of the adapter position
    public int getId(int position){
        items res = itemsArrayList.get(position);
        return res.getId();
    }
    public void itemRemove(int position) {
        items item = itemsArrayList.get(position);
        deleteItems(item);
        itemsArrayList.remove(position);
        notifyDataSetChanged();
    }


    public void itemInsert(items item){
        addItems(item);
        itemsArrayList.add(item);
        notifyDataSetChanged();

    }

    // reference: https://blog.devgenius.io/implementing-room-database-bc9e4deb6600
    // found an example that extends async and used it as the boiler template
    private void addItems(items item) {
        @SuppressLint("StaticFieldLeak")
        class doAsyncTask extends AsyncTask<Void, Void, Void> {

            @Nullable
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                //gets a select all from the database and saves it to a list
                DatabaseAccessLayer.getInstance(context).getAppDatabase().dataBaseAction().insertExpiryData(item);
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

    //force a refresh and to re-render the if needed
    public void refresh(ArrayList<items> itemsArrayList) {

        this.itemsArrayList = itemsArrayList;
        notifyDataSetChanged();
    }



    // temporarily sets the filteredList to the array list
    public void searchFilter(ArrayList<items> filteredList) {
        itemsArrayList = filteredList;
       // updates the adapted list accordingly
        notifyDataSetChanged();
}
    // deletes a specific item passed to it from the array list as well as the expiry table
    // based on the id of the object
    private void deleteItems(items item) {

        // creates a background thread to execute the slow running task
        @SuppressLint("StaticFieldLeak")
        class doAsyncTask extends AsyncTask<Void, Void, Void> {

            @Nullable
            @SuppressLint("WrongThread")
            @Override
            protected Void doInBackground(Void... voids) {
                //gets a select all from the database and saves it to a list
                DatabaseAccessLayer.getInstance(context).getAppDatabase().dataBaseAction().deleteExpiryFromId(item.getId());
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


    // sets the view from list_items xml
    public static class homeFragmentViewHolder extends RecyclerView.ViewHolder {

        TextView heading;
        TextView expiration;
        ImageView imageId;

        public homeFragmentViewHolder(@NonNull View itemView) {
            super(itemView);
            // sets text to the id
            heading = itemView.findViewById(R.id.Heading);
            expiration = itemView.findViewById(R.id.expiryview);
            imageId = itemView.findViewById(R.id.image_id);
        }
    }


}
