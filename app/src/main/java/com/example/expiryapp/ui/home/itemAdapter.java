package com.example.expiryapp.ui.home;

import static com.example.expiryapp.R.drawable.ico;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.RecyclerView;

import com.example.expiryapp.R;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

public class itemAdapter extends RecyclerView.Adapter<itemAdapter.homeFragmentViewHolder> {
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

        items items = this.itemsArrayList.get(position);

        holder.heading.setText(items.heading);


        if(items.getExpiration() <= 3 )
        {
            holder.expiration.setText("Expiration: " + items.getExpiration() + " days");
            holder.expiration.setTextColor(Color.rgb(225,165,0));
        }
        else{

            holder.expiration.setText("Expiration: " + items.getExpiration() + " days");
            holder.expiration.setTextColor(Color.GREEN);
        }

        if(items.getExpiration() == 0 )
        {
            holder.expiration.setText("Expired");
            holder.expiration.setTextColor(Color.RED);

        }
        // TODO: 29/11/2022 add url from and from storage to images taken 
        holder.imageId.setImageResource(ico);

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            // willshow extra info and launch a new intent
            public void onClick(View view) {
                Snackbar.make(view, "item clicked :" + getId(holder.getAdapterPosition()), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();

            }


        });
        // on long click will open a new edit intent
        holder.itemView.setOnLongClickListener(new View.OnLongClickListener(){

            public boolean onLongClick(View view) {
                Snackbar.make(view, "item long clicked :" + getId(holder.getAdapterPosition()), Snackbar.LENGTH_LONG)
                        .setAction("Action", null).show();
                return true;
            }
        });

    }
    // gets the id from the position of the adapter position
    public String getId(int position){
        items res = itemsArrayList.get(position);
        return res.getId();
    }
    public void itemRemove(int position) {
        itemsArrayList.remove(position);
        notifyDataSetChanged();
    }
    public void filtered(ArrayList<items> filteredList) {
        // sets the filteredList to the array list
        itemsArrayList = filteredList;
       // updates the adapted list accordingly
        notifyDataSetChanged();

}

    @Override
    // returns count of array list
    public int getItemCount() {
        return itemsArrayList.size();
    }



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
