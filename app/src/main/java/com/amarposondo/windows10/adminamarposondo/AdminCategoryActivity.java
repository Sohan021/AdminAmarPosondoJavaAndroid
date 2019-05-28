package com.amarposondo.windows10.adminamarposondo;

import android.content.Intent;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;

public class AdminCategoryActivity extends AppCompatActivity {

    private ImageView appple;


    private Button logoutBtn, CheckOrdersBtn, allproducts;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_admin_categary);
        logoutBtn = findViewById(R.id.admin_logout_btn);
        CheckOrdersBtn = findViewById(R.id.check_orders_btn);
        allproducts = findViewById(R.id.seeollproducts);

        allproducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AllProductsActivity.class);
                startActivity(intent);
            }
        });


        logoutBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
            }
        });

        CheckOrdersBtn.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminNewOrdersActivity.class);
                startActivity(intent);
            }
        });

        appple = findViewById(R.id.apple);




        appple.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view)
            {
                Intent intent = new Intent(AdminCategoryActivity.this, AdminAddNewProductActivity.class);
                intent.putExtra("category", "apple");
                startActivity(intent);
            }
        });




    }
}
