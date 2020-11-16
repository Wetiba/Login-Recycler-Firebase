package com.example.onlineshopping;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.ContentResolver;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import org.w3c.dom.Text;

public class UploadActivity extends AppCompatActivity {
    private static final int PICK_IMAGE_REQUEST = 1;

    private Button chooseImageBtn;
    private Button uploadBtn;
    private EditText nameEditText;
    private EditText descriptionEditText;
    private ImageView choosemImageView;
    private ProgressBar uploadProgressBar;

    private Uri mImageUri;

    private StorageReference mStorageRef;
    private DatabaseReference mDatabaseRef;

    private StorageTask muploadTask;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_upload);


        chooseImageBtn=findViewById(R.id.button_choose_image);
        uploadBtn=findViewById(R.id.uploadbtn);
        nameEditText=findViewById(R.id.nameedt);
        descriptionEditText=findViewById(R.id.descriptionedt);
        choosemImageView= findViewById(R.id.chooseImageView);
        uploadProgressBar= findViewById(R.id.progress_bar);

        mStorageRef= FirebaseStorage.getInstance().getReference("Items_uploads");
        mDatabaseRef= FirebaseDatabase.getInstance().getReference("Item_uploads");


        chooseImageBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                openFileChooser();

            }
        });
        uploadBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (muploadTask !=null && muploadTask.isInProgress()){
                    Toast.makeText(UploadActivity.this,"An uploading is Still Uploading",Toast.LENGTH_SHORT).show();
                }else{
                    uploadFile();
                }
            }
        });
    }
    private void openFileChooser(){
        Intent intent  = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(intent,PICK_IMAGE_REQUEST);
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data){
        super.onActivityResult(requestCode,resultCode,data);
        if (requestCode== PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() !=null){
            mImageUri= data.getData();

            Picasso.get().load(mImageUri).into(choosemImageView);
        }
    }
    private String getFileExtension(Uri uri){
        ContentResolver cR= getContentResolver();
        MimeTypeMap mime = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(cR.getType(uri));

    }
    private void uploadFile(){
        if (mImageUri != null){
            StorageReference fileReference= mStorageRef.child(System.currentTimeMillis() + "."+ getFileExtension(mImageUri));
            uploadProgressBar.setVisibility(View.VISIBLE);
            uploadProgressBar.setIndeterminate(true);
            muploadTask=fileReference.putFile(mImageUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                @Override
                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                    Handler handler= new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            uploadProgressBar.setVisibility(View.VISIBLE);
                            uploadProgressBar.setIndeterminate(true);
                            uploadProgressBar.setProgress(0);

                        }
                    } , 500);
                    Toast.makeText(UploadActivity.this,"Item Upload Successfully",Toast.LENGTH_SHORT).show();

                    Task<Uri> result = taskSnapshot.getStorage().getDownloadUrl();
                    result.addOnSuccessListener(new OnSuccessListener<Uri>() {
                        @Override
                        public void onSuccess(Uri uri) {
                            String imageUrl = uri.toString();
                            //createNewPost(imageUrl);
                            Log.d("FIRE_URL_IMAGE", "onSuccess: "+ imageUrl);
                            Item upload = new Item ( nameEditText.getText().toString().trim(),imageUrl,
                                    "FSFSBSSHHHUJU" ,descriptionEditText.getText().toString(),8);

                            String uploadid = mDatabaseRef.push().getKey();
                            mDatabaseRef.child(uploadid).setValue(upload);
                            uploadProgressBar.setVisibility(View.INVISIBLE);
                            openImageActivity();
                        }
                    });
                   // String name, String imageURI, String key,String description, int position




                }
            })
            .addOnFailureListener(new OnFailureListener() {
                @Override
                public void onFailure(@NonNull Exception e) {
                    uploadProgressBar.setVisibility(View.INVISIBLE);
                    Toast.makeText(UploadActivity.this,e.getMessage(),Toast.LENGTH_SHORT).show();

                }
            }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double progress =(100 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount() );
                            uploadProgressBar.setProgress((int)progress);

                        }
                    });
        }else {
            Toast.makeText(UploadActivity.this,"You havent Selected any file ",Toast.LENGTH_SHORT).show();
        }


    }
    private void openImageActivity (){
        Intent intent= new Intent(this,PostingActivity.class);
        startActivity(intent);
    }
}