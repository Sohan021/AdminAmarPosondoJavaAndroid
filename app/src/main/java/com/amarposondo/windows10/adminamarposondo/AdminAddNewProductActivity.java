package com.amarposondo.windows10.adminamarposondo;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.Continuation;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.HashMap;

public class AdminAddNewProductActivity extends AppCompatActivity {

    private String categoryName, Description, Price, ProductName, saveCurrentDate, saveCurrentTime;
    private Button AddNewProductButtion;
    private ImageView InputProductImage;
    private EditText InputProductName, InputProductDescription, InputProductPrice;
    private static final int GalleryPick = 1;
    private Uri ImageUri;
    private String productRandomKey, downloadImageUrl;
    private StorageReference ProductImagesRef;
    private DatabaseReference ProductRef;
    private ProgressDialog loadingBar;
    private FirebaseStorage storage;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_add_new_product);

        categoryName = getIntent().getExtras().get("category").toString();


        //ProductImagesRef = FirebaseStorage.getInstance().getReference().child("Product Images");
        //FirebaseStorage storage = FirebaseStorage.getInstance();  // App crash in this line
        //mStorage = storage.getReferenceFromUrl("Product Images");
        //mStorage = storage.getReferenceFromUrl("Product Image");

        storage = FirebaseStorage.getInstance();
        ProductImagesRef = storage.getReference();


        ProductRef = FirebaseDatabase.getInstance().getReference().child("Products");

        AddNewProductButtion = findViewById(R.id.add_new_product_button);
        InputProductImage = findViewById(R.id.select_product_image);
        InputProductName = findViewById(R.id.product_Name);
        InputProductDescription = findViewById(R.id.product_description);
        InputProductPrice = findViewById(R.id.prduct_price);

        loadingBar = new ProgressDialog(this);

        InputProductImage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                OpenGallery();
            }
        });

        AddNewProductButtion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                ValidateProductData();
            }
        });

    }

    private void OpenGallery() {

        Intent galleryIntent = new Intent();
        galleryIntent.setAction(Intent.ACTION_GET_CONTENT);
        galleryIntent.setType("image/*");

        startActivityForResult(galleryIntent, GalleryPick);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if(requestCode==GalleryPick && resultCode==RESULT_OK && data!=null)
        {
            ImageUri = data.getData();
            InputProductImage.setImageURI(ImageUri);
        }
    }

    private void ValidateProductData() {

        Description = InputProductDescription.getText().toString();
        Price = InputProductPrice.getText().toString();
        ProductName = InputProductName.getText().toString();


        if(ImageUri == null)
        {
            Toast.makeText(this,"Product Image is mandatory",Toast.LENGTH_SHORT).show();
        }
        else if (TextUtils.isEmpty(Description))
        {
            Toast.makeText(this,"Please Write Product Description",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(Price))
        {
            Toast.makeText(this,"Please Write Product Price",Toast.LENGTH_SHORT).show();
        }
        else if(TextUtils.isEmpty(ProductName))
        {
            Toast.makeText(this,"Please Write Product Name",Toast.LENGTH_SHORT).show();
        }
        else
        {
            StoreProductInformation();

        }
    }

    private void StoreProductInformation() {

        loadingBar.setTitle("Add New Product");
        loadingBar.setMessage("Please Wait, While we are adding the new Product");
        loadingBar.setCanceledOnTouchOutside(false);
        loadingBar.show();

        Calendar calendar = Calendar.getInstance();

        SimpleDateFormat currentDate = new SimpleDateFormat("dd MMM yyy ||");
        saveCurrentDate = currentDate.format(calendar.getTime());

        SimpleDateFormat currentTime = new SimpleDateFormat("HH:MM:SS am");
        saveCurrentTime = currentTime.format(calendar.getTime());

        productRandomKey = saveCurrentDate + saveCurrentTime;


        final StorageReference filePath = ProductImagesRef.child(ImageUri.getLastPathSegment() + productRandomKey + ".jpg");

        final UploadTask uploadTask = filePath.putFile(ImageUri);

        uploadTask.addOnFailureListener(new OnFailureListener() {
            @Override
            public void onFailure(@NonNull Exception e) {

                String message = e.toString();
                Toast.makeText(AdminAddNewProductActivity.this,"Error "+message,Toast.LENGTH_SHORT).show();
                loadingBar.dismiss();
            }
        }).addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
            @Override
            public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
                Toast.makeText(AdminAddNewProductActivity.this,"Product Image Uploaded Successfullly ",Toast.LENGTH_SHORT).show();

                Task<Uri> uriTask = uploadTask.continueWithTask(new Continuation<UploadTask.TaskSnapshot, Task<Uri>>() {
                    @Override
                    public Task<Uri> then(@NonNull Task<UploadTask.TaskSnapshot> task) throws Exception {
                        if(!task.isSuccessful())
                        {
                            throw task.getException();

                        }

                        downloadImageUrl = filePath.getDownloadUrl().toString();
                        return filePath.getDownloadUrl();

                    }
                }).addOnCompleteListener(new OnCompleteListener<Uri>() {
                    @Override
                    public void onComplete(@NonNull Task<Uri> task) {
                        if(task.isSuccessful())
                        {
                            downloadImageUrl = task.getResult().toString();

                            Toast.makeText(AdminAddNewProductActivity.this,"Get the Product Image Url Successfully",Toast.LENGTH_SHORT).show();

                            SaveProductInfoToDatabase();
                        }
                    }
                });
            }
        });



    }

    private void SaveProductInfoToDatabase() {

        HashMap<String, Object> productMap = new HashMap<>();

        productMap.put("pid",productRandomKey);
        productMap.put("date",saveCurrentDate);
        productMap.put("time",saveCurrentTime);
        productMap.put("description",Description);
        productMap.put("image",downloadImageUrl);
        productMap.put("category",categoryName);
        productMap.put("price",Price);
        productMap.put("name",ProductName);

        ProductRef.child(productRandomKey).updateChildren(productMap)
                .addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
                        if(task.isSuccessful())
                        {
                            Intent intent = new Intent(AdminAddNewProductActivity.this, AdminCategoryActivity.class);

                            loadingBar.dismiss();
                            Toast.makeText(AdminAddNewProductActivity.this,"Product is added Successfully",Toast.LENGTH_SHORT).show();

                        }
                        else
                        {
                            loadingBar.dismiss();
                            String message = task.getException().toString();
                            Toast.makeText(AdminAddNewProductActivity.this,"Error",Toast.LENGTH_SHORT).show();
                        }
                    }
                });
    }


}
