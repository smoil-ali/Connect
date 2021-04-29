package com.reactive.connect.Activities;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.databinding.DataBindingUtil;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.StorageTask;
import com.google.firebase.storage.UploadTask;
import com.reactive.connect.R;
import com.reactive.connect.Utils.Constants;
import com.reactive.connect.Utils.Helper;
import com.reactive.connect.databinding.PostActivityBinding;
import com.reactive.connect.model.PostClass;
import com.reactive.connect.model.ProfileClass;
import com.theartofdev.edmodo.cropper.CropImage;

import java.util.UUID;

public class PostActivity extends AppCompatActivity {

    final String TAG = PostActivity.class.getSimpleName();
    PostActivityBinding binding;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    private Uri filePath;
    private String downloadUrl;
    PostClass postClass;
    ProgressDialog progressDialog;
    ProfileClass profileClass;
    boolean isEdit;
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this, R.layout.post_activity);
        postClass = new PostClass();
        profileClass = Helper.fromStringToProfile(Helper.getUserInfo(this));
        if(getIntent().getExtras() != null){
            postClass = (PostClass)getIntent().getExtras().getSerializable(Constants.PARAMS);
            Glide.with(this).load(postClass.getPostImage()).into(binding.imageAdded);
            isEdit = true;
        }
        binding.setData(postClass);
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Post");
        progressDialog.setMessage("wait,while your post is being uploading...");
        binding.imageAdded.setOnClickListener(v -> {
            chooseImage();
        });

        binding.close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(PostActivity.this, HomeActivity.class));
                finish();
            }
        });

        binding.post.setOnClickListener(v -> {
            if (isValid()){
                progressDialog.show();
                if (filePath == null && isEdit){
                    updatePost();
                }else {
                    uploadImage();
                }

            }
        });


    }

    boolean isValid(){
        if (binding.description.getText().toString().trim().isEmpty()){
            Toast.makeText(this, "Description is empty", Toast.LENGTH_SHORT).show();
            return false;
        }
        if (filePath == null && postClass.getPostImage().isEmpty()){
            Toast.makeText(this, "Add Image", Toast.LENGTH_SHORT).show();
            return false;
        }
        return true;
    }

    void chooseImage(){
        CropImage.activity()
                .setAspectRatio(2,1)
                .start(PostActivity.this);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE && resultCode == RESULT_OK) {

            CropImage.ActivityResult result = CropImage.getActivityResult(data);
            filePath = result.getUri();
            binding.imageAdded.setImageURI(filePath);
        } else {
            startActivity(new Intent(PostActivity.this, HomeActivity.class));
            finish();
        }
    }

    void uploadImage(){
        if(filePath != null)
        {
            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(taskSnapshot -> {
                        Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                        while (!uriTask.isComplete());
                        downloadUrl = uriTask.getResult().toString();
                        if (isEdit)
                            updatePost();
                        else
                            addPost();
                    })
                    .addOnFailureListener(e -> {
                        progressDialog.dismiss();
                        Toast.makeText(PostActivity.this, "Failed "+e.getMessage(),
                                Toast.LENGTH_SHORT).show();
                    });

        }

    }

    void updatePost(){
        postClass.setDescription(binding.description.getText().toString());
        if (downloadUrl != null)
            postClass.setPostImage(downloadUrl);
        postClass.setPublisher(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference = firebaseDatabase.getReference(Constants.POST);
        databaseReference.child(postClass.getPostId())
                .setValue(postClass)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        onBackPressed();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        progressDialog.dismiss();
                        Log.i(TAG,e.getMessage());
                    }
                });
    }

    void addPost(){
        postClass.setDescription(binding.description.getText().toString());
        postClass.setPostImage(downloadUrl);
        postClass.setPublisher(FirebaseAuth.getInstance().getCurrentUser().getUid());
        databaseReference = firebaseDatabase.getReference(Constants.POST);
        databaseReference.push()
                .setValue(postClass)
                .addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {
                        progressDialog.dismiss();
                        startActivity(new Intent(PostActivity.this,HomeActivity.class));
                        finish();
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.i(TAG,e.getMessage());
                        progressDialog.dismiss();
                    }
                });
    }
}
