package com.example.pocketmanager;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

public class AddOrEditActivity extends AppCompatActivity {
    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.add_or_edit_page);
        Intent intent = getIntent();
        boolean mode = intent.getBooleanExtra("mode", false);

        TextView title = findViewById(R.id.title);
        Button back = findViewById(R.id.back);
        Button delete = findViewById(R.id.delete);
        Button done = findViewById(R.id.done);
        if(mode) {  //edit mode
            title.setText("編輯頁面");
            EditText category = findViewById(R.id.categoryPicker);
            EditText description = findViewById(R.id.descriptionEditor);
            EditText money = findViewById(R.id.moneyEditor);

            category.setText(intent.getStringExtra("category"));
            description.setText(intent.getStringExtra("description"));
            money.setText(intent.getStringExtra("money"));
        } else {
            title.setText("新增頁面");
            delete.setVisibility(View.GONE);
        }
        back.setOnClickListener(new View.OnClickListener() {  //返回HomePage
            @Override
            public void onClick(View v) {
                finish();
            }
        });

        delete.setOnClickListener(new View.OnClickListener() {  //刪除現有資料
            @Override
            public void onClick(View v) {
                //TODO
                finish();
            }
        });

        done.setOnClickListener(new View.OnClickListener() {  //新增OR編輯完成
            @Override
            public void onClick(View v) {
                //TODO
                finish();
            }
        });
    }
}
