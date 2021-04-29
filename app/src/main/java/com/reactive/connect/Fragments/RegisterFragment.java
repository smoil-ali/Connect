package com.reactive.connect.Fragments;

import android.app.ProgressDialog;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.radiobutton.MaterialRadioButton;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.reactive.connect.Activities.AuthenticationActivity;
import com.reactive.connect.R;
import com.reactive.connect.Utils.Constants;
import com.reactive.connect.Utils.Helper;
import com.reactive.connect.databinding.RegisterFragmentBinding;
import com.reactive.connect.model.ProfileClass;

import java.io.IOException;
import java.util.UUID;

import static android.app.Activity.RESULT_OK;

public class RegisterFragment extends Fragment {

    final String TAG = RegisterFragment.class.getSimpleName();
    private final int PICK_IMAGE_REQUEST = 71;
    RegisterFragmentBinding binding;
    MaterialRadioButton radioButton;
    AuthenticationActivity activity;
    ProfileClass model;
    FirebaseStorage firebaseStorage = FirebaseStorage.getInstance();
    StorageReference storageReference = firebaseStorage.getReference();
    private Uri filePath;
    private String downloadUrl;
    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        binding = DataBindingUtil.inflate(inflater, R.layout.register_fragment,container,false);
        activity = (AuthenticationActivity)getActivity();
        model = new ProfileClass();
        binding.setData(model);
        binding.done.setOnClickListener(v -> {
            if (isValid()){
                model.setGender(getRadioText());
                model.setImage(downloadUrl);
                Bundle bundle = new Bundle();
                bundle.putSerializable(Constants.PARAMS,model);
                activity.openInterestFragment(bundle);
            }

        });

        binding.image.setOnClickListener(v -> {
            chooseImage();
        });
        return binding.getRoot();
    }

    String getRadioText(){
        radioButton = (MaterialRadioButton)binding.getRoot().
                findViewById(binding.genderContainer.getCheckedRadioButtonId());
        return radioButton.getText().toString();
    }

    boolean isValid(){
        model.setDisplayError(true);
        if (!model.getFullNameError().isEmpty()){
            return false;
        }
        if (!model.getPasswordError().isEmpty()){
            return false;
        }
        if (!model.getEmailError().isEmpty()){
            return false;
        }
        if (!model.getPhoneError().isEmpty()){
            return false;
        }
        if (!model.getBioError().isEmpty()){
            return false;
        }
        if (downloadUrl == null && model.getImage().isEmpty()){
            Toast.makeText(activity, "Add Image Please", Toast.LENGTH_SHORT).show();
            return false;
        }
        model.setDisplayError(false);
        return true;
    }

    private void chooseImage() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Picture"), PICK_IMAGE_REQUEST);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK
                && data != null && data.getData() != null )
        {
            filePath = data.getData();
            try {
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
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
            final ProgressDialog progressDialog = new ProgressDialog(getContext());
            progressDialog.setTitle("Uploading...");
            progressDialog.show();

            StorageReference ref = storageReference.child("images/"+ UUID.randomUUID().toString());
            ref.putFile(filePath)
                    .addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                            Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                            while (!uriTask.isComplete());
                            downloadUrl = uriTask.getResult().toString();
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Uploaded", Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            progressDialog.dismiss();
                            Toast.makeText(getContext(), "Failed "+e.getMessage(), Toast.LENGTH_SHORT).show();
                        }
                    })
                    .addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(UploadTask.TaskSnapshot taskSnapshot) {
                            double progress = (100.0*taskSnapshot.getBytesTransferred()/taskSnapshot
                                    .getTotalByteCount());
                            progressDialog.setMessage("Uploaded "+(int)progress+"%");
                        }
                    });
        }

    }
}
