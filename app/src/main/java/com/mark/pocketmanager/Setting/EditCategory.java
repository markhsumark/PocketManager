package com.mark.pocketmanager.Setting;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.mark.pocketmanager.Category.Category;
import com.mark.pocketmanager.Category.CategoryViewModel;
import com.mark.pocketmanager.R;

public class EditCategory extends AppCompatActivity {
    private Button save, delete;
    private EditText categoryEditText;
    private TextView hintTextView;
    private CategoryViewModel categoryViewModel;
    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Toast.makeText(this, "按下左上角返回鍵", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint("SetTextI18n")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edit_category);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        Intent intent = getIntent();
        String type = intent.getStringExtra("type");
        int id = intent.getIntExtra("id", 0);
        String category = intent.getStringExtra("category");
        hintTextView = findViewById(R.id.hintTextView);
        hintTextView.setText("編輯" + type + "類別");
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