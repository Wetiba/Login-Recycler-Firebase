package com.example.onlineshopping;

import android.content.Context;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ImageView;
import android.widget.TextView;


import androidx.recyclerview.widget.RecyclerView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.List;

public class RecyclerAdapter extends RecyclerView.Adapter<RecyclerAdapter.RecyclerViewHolder> {

    private Context mContext;
    private List<Item> items;
    private AdapterView.OnItemClickListener mListener;

    public RecyclerAdapter(Context context, List<Item> uploads) {

        this.mContext = context;
        this.items = uploads;
    }

    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup paremt, int viewtype) {
        View v = LayoutInflater.from(mContext).inflate(R.layout.row_model, paremt, false);
        return new RecyclerViewHolder(v);
    }

    @Override
    public void onBindViewHolder(RecyclerViewHolder holder, int position) {
        Item currentItem = items.get(position);
        holder.nameTextView.setText(currentItem.getName());
        holder.descriptionTextView.setText(currentItem.getDescription());
        holder.dataTextView.setText(currentItem.getKey());

        Picasso.get()
                .load(currentItem.getImageURI())
                .placeholder(R.drawable.ic_launcher_foreground)
                .fit()
                .centerCrop()
                .into(holder.itemImageView);
    }



    @Override
    public int getItemCount() {
        return items.size();
    }

    public class RecyclerViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener,
            View.OnCreateContextMenuListener, MenuItem.OnMenuItemClickListener {

        public TextView nameTextView, descriptionTextView, dataTextView;
        public ImageView itemImageView;
        public RecyclerViewHolder(View itemView) {
            super(itemView);
            nameTextView = itemView.findViewById(R.id.nameTextView);
            descriptionTextView = itemView.findViewById(R.id.descriptionTextView);
            dataTextView = itemView.findViewById(R.id.dateTextView);
            itemImageView = itemView.findViewById(R.id.item_imageview);
            itemView.setOnClickListener(this);
            itemView.setOnCreateContextMenuListener(this);
        }

        @Override
        public void onClick(View v) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    //// mListener.onItemClick(position);
                }
            }

        }

        public void OnCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {
            menu.setHeaderTitle("select Action");
            MenuItem showItem = menu.add(Menu.NONE, 1, 1, " show");
            MenuItem deleteItem = menu.add(Menu.NONE, 2, 2, "delete");

            showItem.setOnMenuItemClickListener(this);
            deleteItem.setOnMenuItemClickListener(this);
        }

        //@Override
        public boolean OnMenuItemClick(MenuItem item) {
            if (mListener != null) {
                int position = getAdapterPosition();
                if (position != RecyclerView.NO_POSITION) {
                    switch (item.getItemId()) {
                        case 1:
                            //mListener.onShowItemClick(position);
                        case 2:
                            // mListener.onDeleteItemClick(position);

                            return true;
                    }
                }
            }


            return false;
        }

        @Override
        public boolean onMenuItemClick(MenuItem item) {
            return false;
        }

        @Override
        public void onCreateContextMenu(ContextMenu menu, View v, ContextMenu.ContextMenuInfo menuInfo) {

        }
    }

    public interface OnItemClickListener {
        void onItemClick(int position);

        void onShowItemClick(int position);

        void onDeleteItemClick(int position);

    }

    public void setOnItemClickListener(OnItemClickListener listener) {
        // mListener= listener;
    }

    private String getDateToday() {
        DateFormat dateFormat = new SimpleDateFormat("yyyy/MM/dd");
        java.util.Date date = new Date();
        String today = dateFormat.format(date);
        return today;

    }


}



