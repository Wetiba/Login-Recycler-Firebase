package com.example.onlineshopping;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.squareup.picasso.Picasso;

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.Random;

public class DetailsActivity extends AppCompatActivity {
    TextView nameDetailTextView,descriptionDetailTextView,dateDetailTextView,categoryDetailTextView;
    ImageView itemDetailImageView;

    private void  initializeWidget(){
        nameDetailTextView=findViewById(R.id.nameDetailTv);
        descriptionDetailTextView=findViewById(R.id.descriptionDetailTv);
        dateDetailTextView=findViewById(R.id.dateDetailTv);
        categoryDetailTextView=findViewById(R.id.categoryDetailTv);
        itemDetailImageView=findViewById(R.id.itemdetailimage);

    }
    private String getDateToday(){
        DateFormat dateFormat= new SimpleDateFormat("yyyy/MM/dd");
        Date date= new Date();
        String today= dateFormat.format(date);
        return today;
    }
    private String getRandomCategory(){
        String [] categories = {"LAND","CARS","ELECTRONICS"};
       Random random=new Random();
        int index = random.nextInt(categories.length-1);
        return categories[index];
   }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details);

        initializeWidget();

        Intent i= this.getIntent();
        String name=i.getExtras().getString("NAME_KEY");
        String description =i.getExtras().getString("DESCRIPTION_KEY");
        String imageURL=i.getExtras().getString("IMAGE_KEY");



        nameDetailTextView.setText(name);
        descriptionDetailTextView.setText(description);
        dateDetailTextView.setText("DATE: "+getDateToday());
       // categoryDetailTextView.setText("CATEGORY: " +getRandomCategory());
        Picasso.get()
                .load(imageURL)
                .placeholder(R.drawable.ic_launcher_foreground)
                .fit()
                .centerCrop()
                .into(itemDetailImageView);


    }
}
