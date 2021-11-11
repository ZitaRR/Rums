package com.rums;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.core.content.FileProvider;

import android.Manifest;
import android.app.Activity;
import android.app.Dialog;
import android.content.ContentResolver;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.webkit.MimeTypeMap;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Switch;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.UserProfileChangeRequest;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.squareup.picasso.Picasso;

import java.io.File;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class UserProfile extends BaseClassActivity implements View.OnClickListener {

    static final int CAMERA_PERM_CODE     = 101;
    static final int CAMERA_REQUEST_CODE  = 102;
    static final int GALLERY_REQUEST_CODE = 103;

    // private Toolbar actionBar;

    private EditText editUsername, editAge, editPhone, editDescription;
    //private Switch switchNotification;
    private ImageView profilePicture;
    private Button buttonSave;

    private String username, userDescription;
    private int userAge, userPhone;
    private boolean userNotification;

    //Firebase
    FirebaseAuth auth;
    FirebaseUser fUser;

    StorageReference storageReference;
    String currentPhotoPath;

    @Override
    protected void onStart() {
        super.onStart();
        if (fUser.getPhotoUrl() == null) {
            profilePicture.setImageDrawable(getDrawable(R.mipmap.ic_rums_icon_white_1_circle));
        } else {
            Picasso.get().load(fUser.getPhotoUrl()).into(profilePicture);
        }
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_user_profile);
        super.init();

        /*actionBar = (Toolbar) findViewById(R.id.main_actionbar);
        setSupportActionBar(actionBar);
        ActionBar ab = getSupportActionBar();
        if (ab != null) {
            ab.setDisplayHomeAsUpEnabled(true);
        }*/


        editUsername = (EditText) findViewById(R.id.input_username);
        editAge = (EditText) findViewById(R.id.input_user_age);
        editPhone = (EditText) findViewById(R.id.input_user_phone);
        editDescription = (EditText) findViewById(R.id.input_description);
        buttonSave = (Button) findViewById(R.id.button_save);
        profilePicture = (ImageView) findViewById(R.id.profile_picture);

        //Instantiate Firebase
        auth             = FirebaseAuth.getInstance();
        fUser            = FirebaseAuth.getInstance().getCurrentUser();
        storageReference = FirebaseStorage.getInstance().getReference();

        buttonSave.setOnClickListener(this);
        profilePicture.setOnClickListener(this);
        //switchNotification.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            /*@Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean b) {
                if (switchNotification.isChecked()) {
                    setNotifications(true);
                } else {
                    setNotifications(false);
                }
            }
        });*/

    }

    @Override
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.profile_picture:
                openDialog();
                break;
            case R.id.button_save:
                saveChanges();
                break;
        }
    }
    private void openDialog(){
        Dialog dialog = new Dialog(this);
        dialog.setContentView(R.layout.dialog_profile_picture);
        dialog.getWindow().setBackgroundDrawable(getDrawable(R.drawable.background_dialog));

        Button cameraThroughDialog  = dialog.findViewById(R.id.button_dialog_camera);
        Button galleryThroughDialog = dialog.findViewById(R.id.button_dialog_gallery);
        Button cancel               = dialog.findViewById(R.id.button_dialog_cancel);

        galleryThroughDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                changeProfilePicture();
                dialog.dismiss();
            }
        });
        cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                dialog.dismiss();
            }
        });
        cameraThroughDialog.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                askCameraPermission();
                dialog.dismiss();
            }
        });
        dialog.show();
    }
    private void changeProfilePicture() {
        Intent gallery = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        startActivityForResult(gallery, GALLERY_REQUEST_CODE);
    }
    private void askCameraPermission(){
        if(ContextCompat.checkSelfPermission(UserProfile.this,
                Manifest.permission.CAMERA)
                != PackageManager.PERMISSION_GRANTED){
            ActivityCompat.requestPermissions(UserProfile.this,
                    new String[] {Manifest.permission.CAMERA},
                    CAMERA_PERM_CODE);
        }else{
            openCameraToCapturePhoto();
        }
    }
    private void openCameraToCapturePhoto(){
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null){
            File photoFile = null;
            try {
                photoFile = createImageFile();
            } catch (IOException ex){}
            if (photoFile != null){
                Uri photoURI = FileProvider.getUriForFile(UserProfile.this,
                        "com.rums.android.fileprovider",
                        photoFile);
                takePictureIntent.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
                startActivityForResult(takePictureIntent, CAMERA_REQUEST_CODE);
            }
        }
    }
    private File createImageFile() throws IOException {
        String timeStamp     = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir      = getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image           = File.createTempFile(
                imageFileName,
                ".jpg",
                storageDir);

        currentPhotoPath = image.getAbsolutePath();
        return image;
    }
    private String getFileExt(Uri contentUri){
        ContentResolver c = getContentResolver();
        MimeTypeMap mime  = MimeTypeMap.getSingleton();
        return mime.getExtensionFromMimeType(c.getType(contentUri));
    }
    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == CAMERA_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                File f = new File(currentPhotoPath);
                Log.d("Kami", "Absolute URI from file is " + Uri.fromFile(f));

                Intent mediaScanIntent = new Intent(Intent.ACTION_MEDIA_SCANNER_SCAN_FILE);
                Uri contentUri = Uri.fromFile(f);
                mediaScanIntent.setData(contentUri);
                this.sendBroadcast(mediaScanIntent);
                uploadToFirebase(contentUri);

            }
        }
        if (requestCode == GALLERY_REQUEST_CODE) {
            if (resultCode == Activity.RESULT_OK) {
                Uri contentUri = data.getData();
                String timeStamp = new SimpleDateFormat("yyyyMMdd__HHmmss").format(new Date());
                String imageFileName = "JPEG_" + timeStamp + "." + getFileExt(contentUri);
                Log.d("Kami", "onActivityResult: Gallery Image Uri: " + imageFileName);
                uploadToFirebase(contentUri);
            }
        }
    }
    private void uploadToFirebase(Uri contentUri){
        StorageReference fileRef = storageReference.child("users/"+auth.getUid()+"/profile.jpg");
        fileRef.putFile(contentUri).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                fileRef.getDownloadUrl().addOnSuccessListener(new OnSuccessListener<Uri>() {
                    @Override
                    public void onSuccess(Uri uri) {
                        UserProfileChangeRequest updatePic = new UserProfileChangeRequest.Builder()
                                .setPhotoUri(uri)
                                .build();
                        fUser.updateProfile(updatePic)
                                .addOnCompleteListener(new OnCompleteListener<Void>() {
                                    @Override
                                    public void onComplete(@NonNull Task<Void> task) {
                                        if (task.isSuccessful()) {
                                            Toast.makeText(UserProfile.this, "Profile picture updated",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                        else {
                                            Toast.makeText(UserProfile.this, "Something went wrong",
                                                    Toast.LENGTH_SHORT).show();
                                        }
                                    }
                                });
                        Picasso.get().load(uri).into(profilePicture);
                    }
                });
                Toast.makeText(UserProfile.this, "Image uploaded",
                        Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(UserProfile.this, "Uploading failed",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }
    private void saveChanges() {

        String inputUsername = editUsername.getText().toString();
        String inputDescription = editDescription.getText().toString();
        String inputAge = editAge.getText().toString();
        String inputPhone = editPhone.getText().toString();

        boolean successfulSave = true;

        // Användarnamn. Vilkor: Mellan 5 och 13 karaktärer.
        if(inputUsername.length() < 5 || inputUsername.length() > 13) {
           Toast.makeText(UserProfile.this, "Username must be between 5 and 13 characters.", Toast.LENGTH_SHORT).show();
           successfulSave = false;
        } else {
            setUsername(inputUsername);
        }

        if (!inputDescription.isEmpty()) {
            setUserDescription(inputDescription);
        }

        if (!inputAge.isEmpty()) {
           setUserAge((Integer.parseInt(inputAge)));
        }

        if (!inputPhone.isEmpty()) {
            setUserPhone(Integer.parseInt(inputPhone));
        }

        if (successfulSave) {
            Toast.makeText(UserProfile.this, "Save Successful", Toast.LENGTH_SHORT).show();
            changeDatabase();
            Intent i = new Intent(UserProfile.this, HomeActivity.class);
            startActivity(i);
        }

    }
    private void changeDatabase() {
        // När värden ändrats, skicka till User.Class eller databas eller nått? Dunno?
    }

    // Getters & Setters

    public void setUsername(String username) {
        this.username = username;
    }

    public String getUsername() {
        return username;
    }

    public void setUserAge(int user_age) {
        this.userAge = user_age;
    }

    public int getUserAge() {
        return userAge;
    }

    public void setUserPhone(int user_phone) {
        this.userPhone = user_phone;
    }

    public int getUserPhone() {
        return userPhone;
    }

    public void setUserDescription(String user_description) {
        this.userDescription = user_description;
    }

    public String getUserDescription() {
        return userDescription;
    }



    // För att kunna använda bakåtknappen. Inte det mest effektiva sättet att göra det förmodligen men enda som jag fick att fungera.
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        switch (item.getItemId()){
            case android.R.id.home:
                startSomeActivity(HomeActivity.class);
//                Intent i = new Intent(UserProfile.this, HomeActivity.class);
//                startActivity(i);
//                finish();
                return true;
        }
        return super.onOptionsItemSelected(item);
    }

}