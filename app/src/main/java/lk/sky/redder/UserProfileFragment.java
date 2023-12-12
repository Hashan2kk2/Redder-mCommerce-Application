package lk.sky.redder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.resource.bitmap.RoundedCorners;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.UUID;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import lk.sky.redder.model.User;

public class UserProfileFragment extends Fragment {

    private FirebaseAuth firebaseAuth;
    private FirebaseUser currentUser;
    private FirebaseFirestore fireStoreDatabase;
    private StorageReference storageReference;


    private TextView userNameField, firstnameField, lastnameField, emailField, mobileField, addressField, cityField;
    private String userNameString, firstnameString, lastnameString, emailString, mobileString, addressString, cityString;
    private ImageView userImage;
    private Uri photoUrl;
  public UserProfileFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        firebaseAuth = FirebaseAuth.getInstance();
        currentUser = firebaseAuth.getCurrentUser();

        userNameField = fragment.findViewById(R.id.user_profile_name);
        userImage = fragment.findViewById(R.id.profileImageView);
        firstnameField = fragment.findViewById(R.id.firstNameField);
        lastnameField = fragment.findViewById(R.id.lastNameField);
        emailField = fragment.findViewById(R.id.user_profile_email);
        mobileField = fragment.findViewById(R.id.user_profile_mobile_number);
        addressField = fragment.findViewById(R.id.user_profile_address);
        cityField = fragment.findViewById(R.id.user_profile_city);

        fireStoreDatabase = FirebaseFirestore.getInstance();
        storageReference = FirebaseStorage.getInstance().getReference();

        loadUserDetails();

        if (currentUser != null) {
            fragment.findViewById(R.id.signInBtn).setVisibility(View.GONE);
            fragment.findViewById(R.id.signOutBtn).setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.signOutBtn).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    firebaseAuth.signOut();
                    startActivity(new Intent(getActivity(), MainActivity.class));
                }
            });

            userNameField.setText("@ " + currentUser.getEmail());
            emailField.setText(currentUser.getEmail());
            emailField.setEnabled(false);

            if (currentUser.getPhotoUrl() != null) {
                Glide.with(getContext()).load(currentUser.getPhotoUrl())
                        .transform(new RoundedCorners(40))
                        .into(userImage);
            } else {
                userImage.setImageResource(R.drawable.user);
            }

        } else {
            fragment.findViewById(R.id.signInBtn).setVisibility(View.VISIBLE);
            fragment.findViewById(R.id.signOutBtn).setVisibility(View.GONE);
            userNameField.setText("Please Sign In");
        }


        fragment.findViewById(R.id.signInBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                startActivity(new Intent(getActivity(), SignInActivity.class));
            }
        });

        fragment.findViewById(R.id.signInBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                firebaseAuth.signOut();
                Toast.makeText(getContext(), "Successfully Log Out", Toast.LENGTH_SHORT).show();
            }
        });

        fragment.findViewById(R.id.addProfileImageBtn).setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        activityResultLauncher1.launch(Intent.createChooser(intent, "Select Image"));
                    }
                }
        );

        fragment.findViewById(R.id.user_profile_update).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                updateProfile();
            }
        });


    }
    ActivityResultLauncher<Intent> activityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        photoUrl = result.getData().getData();

                        Glide.with(getContext()).load(photoUrl).fitCenter()
                                .transform(new RoundedCorners(40))
                                .into(userImage);

                    }
                }
            }
    );

    private void loadUserDetails() {


        fireStoreDatabase.collection("users").document(firebaseAuth.getUid()).get().addOnSuccessListener(
                new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(com.google.firebase.firestore.DocumentSnapshot documentSnapshot) {

                        if (documentSnapshot.exists()) {

                            User user = documentSnapshot.toObject(User.class);

                            firstnameField.setText(user.getFname());
                            lastnameField.setText(user.getLname());
                            emailField.setText(user.getEmail());
                            mobileField.setText(user.getMobile());
                            addressField.setText(user.getAddress());
                            cityField.setText(user.getCity());

                            if (currentUser.getPhotoUrl() != null) {
                                Glide.with(getContext()).load(currentUser.getPhotoUrl()).fitCenter()
                                        .transform(new RoundedCorners(40))
                                        .into(userImage);
                            }else if (user.getImageUrl() != null) {
                                Glide.with(getContext()).load(user.getImageUrl()).fitCenter()
                                        .transform(new RoundedCorners(40))
                                        .into(userImage);
                            } else {
                                userImage.setImageResource(R.drawable.user);
                            }

                        }

                    }
                }
        ).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {
                Toast.makeText(getContext(), "Something went wrong.Please try again.", Toast.LENGTH_SHORT).show();
            }
        });

    }

    private void updateProfile() {

        firstnameString = firstnameField.getText().toString();
        lastnameString = lastnameField.getText().toString();
        emailString = emailField.getText().toString();
        mobileString = mobileField.getText().toString();
        addressString = addressField.getText().toString();
        cityString = cityField.getText().toString();

        String mobileRegex = "[0][0-9]{9}";
        Pattern mobilePattern = Pattern.compile(mobileRegex);
        Matcher mobileMatcher = mobilePattern.matcher(mobileString);

        if (firstnameString.isEmpty()) {
            firstnameField.setError("Please enter your first name");
            firstnameField.requestFocus();
        } else if (lastnameString.isEmpty()) {
            lastnameField.setError("Please enter your last name");
            lastnameField.requestFocus();
        } else if (mobileString.isEmpty()) {
            mobileField.setError("Please enter your mobile number");
            mobileField.requestFocus();
        } else if (addressString.isEmpty()) {
            addressField.setError("Please enter your address");
            addressField.requestFocus();
        } else if (cityString.isEmpty()) {
            cityField.setError("Please enter your city");
            cityField.requestFocus();
        }else if(mobileString.length()!=10) {
            mobileField.setError("Please enter a valid mobile number");
            mobileField.requestFocus();
        } else if (!mobileMatcher.matches()) {
            mobileField.setError("Please enter a valid mobile number");
            mobileField.requestFocus();
        } else {

            if (currentUser.getPhotoUrl() != null && photoUrl == null) {
                uploadWithoutImage();
            } else {
                storageReference.child("userImages/" + UUID.randomUUID().toString()).putFile(photoUrl).addOnSuccessListener(
                        new OnSuccessListener<UploadTask.TaskSnapshot>() {
                            @Override
                            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                while (!uriTask.isComplete()) ;
                                String imageDwnUrl = uriTask.getResult().toString();

                                uploadWithImage(imageDwnUrl);

                            }
                        }).addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Toast.makeText(getContext(), "Something went wrong.Please try again.", Toast.LENGTH_SHORT).show();
                    }
                });

            }

        }

    }
    private void uploadWithImage(String imageDwnUrl) {

        User user = new User();
        user.setFname(firstnameString);
        user.setLname(lastnameString);
        user.setEmail(emailString);
        user.setMobile(mobileString);
        user.setAddress(addressString);
        user.setCity(cityString);
        user.setImageUrl(imageDwnUrl);
        user.setUserType("user");

        fireStoreDatabase.collection("Users").document(currentUser.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }
    private void uploadWithoutImage() {

        User user = new User();
        user.setFname(firstnameString);
        user.setLname(lastnameString);
        user.setEmail(emailString);
        user.setMobile(mobileString);
        user.setAddress(addressString);
        user.setCity(cityString);
//        user.setImageUrl(currentUser.getPhotoUrl().toString());
        user.setUserType("user");

        fireStoreDatabase.collection("Users").document(currentUser.getUid()).set(user).addOnSuccessListener(new OnSuccessListener<Void>() {
            @Override
            public void onSuccess(Void unused) {
                Toast.makeText(getContext(), "Profile Updated Successfully", Toast.LENGTH_SHORT).show();
            }
        }).addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        return inflater.inflate(R.layout.fragment_user_profile, container, false);
    }
}