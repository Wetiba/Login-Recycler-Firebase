package com.example.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;

import java.util.ArrayList;
import java.util.List;

public class ItemActivity extends AppCompatActivity implements RecyclerAdapter.OnItemClickListener {
    private RecyclerView mRecyclerView;
    private RecyclerAdapter mRecyclerAdapter;
    private ProgressBar mProgressBar;
    private FirebaseStorage mStorage;
    private DatabaseReference mDatabaseRef;
    private ValueEventListener mDBlistener;
    private List <Item> mItems;
    private String TAG="FIRE_DB";

    private void openDetailActivity(String[] data){
        Intent intent= new Intent(this,DetailsActivity.class);
        intent.putExtra("NAME_KEY",data[0]);
        intent.putExtra("DESCRIPTION_KEY",data[1]);
        intent.putExtra("IMAGE_KEY",data[2]);
        startActivity(intent);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_item);

        mRecyclerView=findViewById(R.id.rc_view);
        mRecyclerView.setHasFixedSize(true);
        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));
        mProgressBar=findViewById(R.id.pg_progress);
        mProgressBar.setVisibility(View.VISIBLE);

        mItems= new ArrayList<>();
        //String name, String imageURI, String key,String description, int position
//        Item test_item=new Item("John Mark",
//                "https://firebasestorage.googleapis.com/v0/b/online-shopping-7e34b.appspot.com/o/Items_uploads%2F1605527770297.jpg?alt=media&token=45200fd4-7219-4254-ab9a-69f15d226059",
//                "Skey_xyz","Sample", 8);
//        mItems.add(test_item);
        mRecyclerAdapter= new RecyclerAdapter(getApplicationContext(),mItems);
        mRecyclerAdapter.setOnItemClickListener(ItemActivity.this);
        mRecyclerView.setAdapter(mRecyclerAdapter);

        mStorage =FirebaseStorage.getInstance();
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Item_uploads");
        mDBlistener= mDatabaseRef.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot snapshot) {
                mItems.clear();
                Log.d("FIRE_DB", "onDataChange: "+snapshot.toString());
                for (DataSnapshot itemSnapshot : snapshot.getChildren()){
                    Log.d(TAG, "onDataChange: Showing snapshot data");
                    Item upload= itemSnapshot.getValue(Item.class);
                    upload.setKey(itemSnapshot.getKey());
                    Log.d(TAG, "onDataChange: "+upload.getImageURI());
                    mItems.add(upload);
                }
                mRecyclerAdapter.notifyDataSetChanged();
                mProgressBar.setVisibility(View.GONE);
            }

            @Override
            public void onCancelled(@NonNull DatabaseError error) {
                Toast.makeText(ItemActivity.this,"error",Toast.LENGTH_SHORT).show();
                mProgressBar.setVisibility(View.INVISIBLE);

            }
        });





    }
    private void omItemClick(int position){
        Item clickedItems = mItems.get(position);
        String[] itemsData= {clickedItems.getName(),clickedItems.getDescription(),clickedItems.getImageURI()};
        openDetailActivity(itemsData);
    }

    @Override
    public void onItemClick(int position) {

    }

    @Override
    public void onShowItemClick(int position){
        Item clickedItems = mItems.get(position);
        String[] itemsData= {clickedItems.getName(),clickedItems.getDescription(),clickedItems.getImageURI()};
        openDetailActivity(itemsData);

    }
    @Override
    public void onDeleteItemClick(int position){

        Item selectedItem= mItems.get(position);
        final String selectedKey= selectedItem.getKey();


        StorageReference imageRef= mStorage.getReferenceFromUrl(selectedItem.getImageURI());
        imageRef.delete().addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void aVoid) {
                mDatabaseRef.child(selectedKey).removeValue();
                Toast.makeText(ItemActivity.this,"Item deleted",Toast.LENGTH_SHORT).show();
            }
        });
    }
    public void onDestroy(){
        super.onDestroy();
        mDatabaseRef.removeEventListener(mDBlistener);


    }
}