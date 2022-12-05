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

    import com.example.expiryapp.DatabaseAccessLayer;
    import com.example.expiryapp.R;
    import com.example.expiryapp.ui.home.items;

    import java.text.ParseException;
    import java.util.ArrayList;


    import static android.app.PendingIntent.getActivity;

    public class templateAdapter  extends RecyclerView.Adapter<com.example.expiryapp.ui.gallery.templateAdapter.GalleryFragmentViewHolder>{
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
                View v = LayoutInflater.from(context).inflate(R.layout.list_items,parent,false);

                //return the view holder
                return new GalleryFragmentViewHolder(v);
            }

            @Override
            // reuses the viewHolders set above to bind the headings and text
            public void onBindViewHolder(@NonNull com.example.expiryapp.ui.gallery.templateAdapter.GalleryFragmentViewHolder holder, int position) {
                //gets the item by position from the array list and saves it to item object
                templateGall items = this.itemsArrayList.get(position);
                holder.heading.setText(items.heading);
               // holder.imageId.setImageResource();

                holder.expiration.setText("Days: " + items.expiryDate);
                holder.expiration.setTextColor(Color.GREEN);


                holder.itemView.setOnClickListener(new View.OnClickListener() {
                    @Override
                    // will show extra info and launch a new intent
                    public void onClick(View view) {
                        items additem = new items();
                        additem.setHeading(items.getHeading());
                        try {
                            additem.setExpiryDate(items.getExpiryDate());
                        } catch (ParseException e) {
                            e.printStackTrace();
                        }
                        Bundle bundle = new Bundle();
                        bundle.putSerializable("item", additem);
                        Intent i = new Intent(context, addItemFromTemplate.class);
                        i.putExtras(bundle);
                        context.startActivity(i);

                    }


                });

            }
            // gets the id from the position of the adapter position
            public int getId(int position){
                templateGall res = itemsArrayList.get(position);
                return res.getId();
            }

        public void itemRemove(int position) {
            templateGall item = itemsArrayList.get(position);
            deleteItems(item);
            itemsArrayList.remove(position);
            notifyDataSetChanged();
        }



        public void itemInsert(templateGall item){
                addTemplateItems(item);
                itemsArrayList.add(item);
                notifyDataSetChanged();

            }

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

        public void searchFilter(ArrayList<templateGall> filteredList) {
            // sets the filteredList to the array list
            itemsArrayList = filteredList;
            // updates the adapted list accordingly
            notifyDataSetChanged();

        }
    }



