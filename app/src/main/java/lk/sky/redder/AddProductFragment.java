package lk.sky.redder;

import android.app.Activity;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageButton;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;

import com.bumptech.glide.Glide;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.FirebaseApp;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.OnProgressListener;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.util.ArrayList;
import java.util.UUID;

import lk.sky.redder.model.Product;

public class AddProductFragment extends Fragment {

    private Uri imgUri1, imgUri2, imgUri3, imgUri4;
    private FirebaseFirestore database;
    private StorageReference storageReference, storageReference1, storageReference2, storageReference3, storageReference4;

    ArrayList<String> images = new ArrayList<>();
    ArrayList<String> downloadImagesList = new ArrayList<>();

    private ImageButton imageButton01, imageButton02, imageButton03, imageButton04;
    private Spinner brandView, categoryView, modelView;
    private TextView productTitleText, productPriceText, qtyText, productDescriptionText;
    private String categoryText, brandText, modelText;
    private ProgressBar progressBar;


    final int[] imgIndex = {0};

    public AddProductFragment() {
        // Required empty public constructor
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        return inflater.inflate(R.layout.fragment_add_product, container, false);
    }

    @Override
    public void onViewCreated(@NonNull View fragment, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(fragment, savedInstanceState);

        database = FirebaseFirestore.getInstance();

        imageButton01 = fragment.findViewById(R.id.pImg01);
        imageButton02 = fragment.findViewById(R.id.pImg02);
        imageButton03 = fragment.findViewById(R.id.pImg03);
        imageButton04 = fragment.findViewById(R.id.pImg04);

        categoryView = fragment.findViewById(R.id.categoryItems);
        brandView = fragment.findViewById(R.id.brandItems);
        modelView = fragment.findViewById(R.id.modelItems);

        productTitleText = fragment.findViewById(R.id.productTitleField);
        productPriceText = fragment.findViewById(R.id.priceField);
        qtyText = fragment.findViewById(R.id.qtyField);
        productDescriptionText = fragment.findViewById(R.id.descriptionField);

        progressBar = fragment.findViewById(R.id.loadingProgress);

        FirebaseApp.initializeApp(requireContext());
        storageReference = FirebaseStorage.getInstance().getReference();

        imageButton01.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        activityResultLauncher1.launch(Intent.createChooser(intent, "Select Image"));
                    }
                }
        );

        imageButton02.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        activityResultLauncher2.launch(Intent.createChooser(intent, "Select Image"));
                    }
                }
        );

        imageButton03.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        activityResultLauncher3.launch(Intent.createChooser(intent, "Select Image"));
                    }
                }
        );

        imageButton04.setOnClickListener(
                new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        Intent intent = new Intent();
                        intent.setType("image/*");
                        intent.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);
                        intent.setAction(Intent.ACTION_GET_CONTENT);
                        activityResultLauncher4.launch(Intent.createChooser(intent, "Select Image"));
                    }
                }
        );

        categoryView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                categoryText = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        brandView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                brandText = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        modelView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                modelText = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
        // load categories
        database.collection("/Category").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<Object> categoryArrayList = new ArrayList<>();
                            categoryArrayList.add("Select Category");
                            for (QueryDocumentSnapshot result : task.getResult()) {
                                String name = result.getId();
                                categoryArrayList.add(name);
                            }
                            ArrayAdapter<Object> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, categoryArrayList);
                            adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            categoryView.setAdapter(adapter);

                        }
                    }
                }
        );
        // load brands
        database.collection("/Brand").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<Object> brandArrayList = new ArrayList<>();
                            brandArrayList.add("Select Brand");
                            for (QueryDocumentSnapshot result : task.getResult()) {
                                String name = result.getId();
                                brandArrayList.add(name);
                            }
                            ArrayAdapter<Object> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, brandArrayList);
                            adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            brandView.setAdapter(adapter);

                        }
                    }
                }
        );
        // load models
        database.collection("/Model").get().addOnCompleteListener(
                new OnCompleteListener<QuerySnapshot>() {
                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {

                            ArrayList<Object> modelArrayList = new ArrayList<>();
                            modelArrayList.add("Select Model");
                            for (QueryDocumentSnapshot result : task.getResult()) {
                                String name = result.getId();
                                modelArrayList.add(name);
                            }
                            ArrayAdapter<Object> adapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_spinner_item, modelArrayList);
                            adapter.setDropDownViewResource(android.R.layout.select_dialog_singlechoice);
                            modelView.setAdapter(adapter);

                        }
                    }
                }
        );
        fragment.findViewById(R.id.addProductBtn).setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                addProduct();
            }
        });

        categoryView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                categoryText = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        brandView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                brandText = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });

        modelView.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> adapterView, View view, int position, long id) {
                modelText = adapterView.getItemAtPosition(position).toString();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });
    }

    ActivityResultLauncher<Intent> activityResultLauncher1 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imgUri1 = result.getData().getData();
                        Glide.with(getContext()).load(imgUri1).fitCenter().into(imageButton01);
                    }
                }
            }
    );
    ActivityResultLauncher<Intent> activityResultLauncher2 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imgUri2 = result.getData().getData();
                        Glide.with(getContext()).load(imgUri2).fitCenter().into(imageButton02);
                    }
                }
            }
    );
    ActivityResultLauncher<Intent> activityResultLauncher3 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imgUri3 = result.getData().getData();
                        Glide.with(getContext()).load(imgUri3).fitCenter().into(imageButton03);
                    }
                }
            }
    );
    ActivityResultLauncher<Intent> activityResultLauncher4 = registerForActivityResult(
            new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {
                @Override
                public void onActivityResult(ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        imgUri4 = result.getData().getData();
                        Glide.with(getContext()).load(imgUri4).fitCenter().into(imageButton04);
                    }
                }
            }
    );

    private void addProduct() {
        String title = productTitleText.getText().toString();
        String price = productPriceText.getText().toString();
        String qty = qtyText.getText().toString();
        String description = productDescriptionText.getText().toString();


        images.add(String.valueOf(imgUri1));

        if (imgUri2 != null) {
            images.add(String.valueOf(imgUri2));
        }
        if (imgUri3 != null) {
            images.add(String.valueOf(imgUri3));
        }
        if (imgUri4 != null) {
            images.add(String.valueOf(imgUri4));
        }

        new Thread(new Runnable() {
            @Override
            public void run() {

                for (int i = 0; i < images.size(); i++) {
                    Uri uri = Uri.parse(images.get(i));
                    int finalI = i;
                    storageReference.child("productImages/" + UUID.randomUUID().toString()).putFile(uri).addOnSuccessListener(
                            new OnSuccessListener<UploadTask.TaskSnapshot>() {
                                @Override
                                public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {

                                    Task<Uri> uriTask = taskSnapshot.getStorage().getDownloadUrl();
                                    while (!uriTask.isComplete()) ;
                                    String imageDwnUrl = uriTask.getResult().toString();

                                    downloadImagesList.add(imageDwnUrl);
                                    imgIndex[0] = finalI;
                                    addDetailsToDatabase(imgIndex[0], title, price, qty, description);

                                }
                            }).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), "Something went wrong.Please try again.", Toast.LENGTH_SHORT).show();
                        }
                    }).addOnProgressListener(new OnProgressListener<UploadTask.TaskSnapshot>() {
                        @Override
                        public void onProgress(@NonNull UploadTask.TaskSnapshot snapshot) {
                            double prograss = (100.0 * snapshot.getBytesTransferred() / snapshot.getTotalByteCount());
                            progressBar.setVisibility(View.VISIBLE);
                            progressBar.setProgress((int) prograss);
                        }
                    });
                }

            }
        }).start();


//        }
    }

    private void addDetailsToDatabase(int index, String title, String price, String qty, String description) {
        System.out.println("addDetailsToDatabase : start");
        System.out.println("index : " + index + " images size : " + images.size());

        if (index + 1 >= images.size()) {
            Product product = new Product();
            product.setName(title);
            product.setImageList(downloadImagesList);
            product.setPrice(Double.parseDouble(price));
            product.setQuantity(Integer.parseInt(qty));
            product.setDescription(description);
            product.setCategory(categoryText);
            product.setBrand(brandText);
            product.setModel(modelText);

            FirebaseDatabase.getInstance().getReference("Products").child(title)
                    .setValue(product)
                    .addOnCompleteListener(
                            new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
                                    if (task.isSuccessful()) {
                                        Toast.makeText(getContext(), "Product add successfully.", Toast.LENGTH_LONG).show();
                                        clearFields();
                                    }
                                }
                            }
                    ).addOnFailureListener(new OnFailureListener() {
                        @Override
                        public void onFailure(@NonNull Exception e) {
                            Toast.makeText(getContext(), e.getMessage(), Toast.LENGTH_LONG).show();
                        }
                    });
        }
    }

    private void clearFields() {
        productTitleText.setText("");
        productPriceText.setText("");
        qtyText.setText("");
        productDescriptionText.setText("");
        categoryView.setSelection(0);
        brandView.setSelection(0);
        modelView.setSelection(0);
        imageButton01.setImageResource(R.drawable.add_product_icon);
        imageButton02.setImageResource(R.drawable.add_product_icon);
        imageButton03.setImageResource(R.drawable.add_product_icon);
        imageButton04.setImageResource(R.drawable.add_product_icon);
        progressBar.setVisibility(View.INVISIBLE);
    }
}