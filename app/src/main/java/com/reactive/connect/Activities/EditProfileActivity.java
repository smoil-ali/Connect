package com.reactive.connect.Activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.databinding.DataBindingUtil;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.reactive.connect.Adapter.InterestAdapter;
import com.reactive.connect.Interfaces.InterestListener;
import com.reactive.connect.R;
import com.reactive.connect.Utils.Constants;
import com.reactive.connect.databinding.ActivityEditProfileBinding;
import com.reactive.connect.model.InterestClass;
import com.reactive.connect.model.ProfileClass;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class EditProfileActivity extends AppCompatActivity implements InterestListener {

    final String TAG = EditProfileActivity.class.getSimpleName();
    private final int PICK_IMAGE_REQUEST = 71;
    ActivityEditProfileBinding binding;
    ProfileClass profileClass ;
    InterestAdapter adapter;
    List<String> list = new ArrayList<>();
    List<String> interestList = new ArrayList<>();
    List<InterestClass> interestClasses = new ArrayList<>();

    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    FirebaseDatabase firebaseDatabase = FirebaseDatabase.getInstance();
    DatabaseReference databaseReference;
    private Uri filePath;
    private String downloadUrl;

    ProgressDialog progressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_edit_profile);
        if (getIntent().getExtras() != null){
            profileClass = (ProfileClass) getIntent().getExtras().getSerializable(Constants.PARAMS);
        }
        progressDialog = new ProgressDialog(this);
        progressDialog.setTitle("Profile");
        progressDialog.setMessage("wait,while your profile is being updating...");
        binding.setData(profileClass);
        if (binding.male.getText().toString().equals(profileClass.getGender())){
            binding.genderContainer.check(binding.male.getId());
        }else {
            binding.genderContainer.check(binding.female.getId());
        }

        Glide.with(this).load(profileClass.getImage()).into(binding.image);
        binding.recycler.hasFixedSize();
        adapter = new InterestAdapter(this,interestClasses);
        binding.recycler.setAdapter(adapter);
        adapter.setListener(this);
        loadList();
        loadFinalList();
    }
    void loadList(){
        list.add("Accounting & Tax Services");
        list.add("Arts, Culture & Entertainment");
        list.add("Auto Sales & Service");
        list.add("Banking & Finance");
        list.add("Business Services");
        list.add("Community Organizations");
        list.add("Dentists & Orthodontists");
        list.add("Education");
        list.add("Health & Wellness");
        list.add("Health Care");
        list.add("Home Improvement");
        list.add("Insurance");
        list.add("Internet & Web Services");
        list.add("Legal Services");
        list.add("Lodging & Travel");
        list.add("Marketing & Advertising");
        list.add("News & Media");
        list.add("Pet Services");
        list.add("Real Estate");
        list.add("Restaurants & Nightlife");
        list.add("Shopping & Retail");
        list.add("Web development");
        list.add("Sports & Recreation");
        list.add("Transportation Utilities");
        list.add("Wedding, Events & Meetings");
        list.add("It Services");
        list.add("Mobile development");
    }

    void loadFinalList(){
        for (String str:list){
            InterestClass interestClass = new InterestClass();
            interestClass.setInterest(str);
            interestClasses.add(interestClass);
        }
        Log.i(TAG,interestClasses.size()+" size");
        for (String str: profileClass.getInterest()){
            int index = list.indexOf(str);
            interestClasses.get(index).setChecked(true);
        }
        adapter.notifyDataSetChanged();

        binding.image.setOnClickListener(v -> {
            chooseImage();
        });

        binding.done.setOnClickListener( v -> {
            if (isValid()){
                progressDialog.show();
                profileClass.setInterest(interestList);
                if (downloadUrl != null){
                    profileClass.setImage(downloadUrl);
                    uploadImage();
                }else {
                    updateProfile();
                }


            }

        });
    }

    @Override
    public void onInterestChecked(String interest) {
        interestList.add(interest);
    }

    @Override
    public void onInterestUnChecked(String interest) {
        Log.i(TAG,interestList.size() + "list size");
        interestList.remove(interest);
        Log.i(TAG,interestList.size()+"list size");
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }


    boolean isValid(){
        profileClass.setDisplayError(true);
        if (!profileClass.getFullNameError().isEmpty()){
            return false;
        }
        if (!profileClass.getPhoneError().isEmpty()){
            return false;
        }
        if (!profileClass.getBioError().isEmpty()){
            return false;
        }
        if (downloadUrl == null && profileClass.getImage().isEmpty()){
            Toast.makeText(this, "Add Image Please", Toast.LENGTH_SHORT).show();
            return false;
        }
        profileClass.setDisplayError(false);
        return true;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getContentResolver(), filePath);
                binding.image.setImageBitmap(bitmap);
                uploadImage();
            }
            catch (IOException e)
            {
                e.printStackTrace();
            }
        }
    }

    void uploadImage(){
        if(filePath != null)
        {
            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            downloadUrl = uriTask.getResult().toString();
                            updateProfile();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(EditProfileActivity.this, "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    });
        }

    }

    void updateProfile(){
        databaseReference = firebaseDatabase.getReference(Constants.USERS);
        databaseReference.child(FirebaseAuth.getInstance().getCurrentUser().getUid())
                .setValue(profileClass)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
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

}