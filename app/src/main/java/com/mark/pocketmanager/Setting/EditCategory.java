package com.mark.pocketmanager.Setting;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import com.mark.pocketmanager.Category.Category;
import com.mark.pocketmanager.Category.CategoryViewModel;
import com.mark.pocketmanager.R;

public class EditCategory extends AppCompatActivity {
    private Button save, delete;
    private EditText categoryEditText;
    private CategoryViewModel categoryViewModel;

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);

        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        int id = intent.getIntExtra("id", 0);
        String category = intent.getStringExtra("category");

        View actionBar = findViewById(R.id.my_actionBar);
        ImageButton backButton = actionBar.findViewById(R.id.backButton);
        TextView title = actionBar.findViewById(R.id.title);
        title.setText("編輯" + type + "類別");
        backButton.setOnClickListener(v -> {
            finish();
        });

        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryEditText = findViewById(R.id.categoryEditText);
        categoryEditText.setText(category);

        save = findViewById(R.id.save);
        save.setOnClickListener(v -> {
            if(TextUtils.isEmpty(categoryEditText.getText()))
                Toast.makeText(v.getContext(),"請輸入類別名稱",Toast.LENGTH_LONG).show();
            else {
                categoryViewModel.updateCategories(new Category(type, categoryEditText.getText().toString()));
                finish();
            }
        });

        delete = findViewById(R.id.deleteButton);
        delete.setOnClickListener(v -> {
            categoryViewModel.deleteCategories(new Category(id));
            finish();
        });
    }
}