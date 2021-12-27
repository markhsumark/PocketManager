package com.example.pocketmanager;

import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.ViewModelProvider;

public class InsertActivity extends AppCompatActivity {

    Button buttonSave, buttonCancel;
    EditText in_out, source, amount, information;
    AccountViewModel accountViewModel;

    @Override
    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Toast.makeText(this, "按下左上角返回鍵", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setTitle("");
        setContentView(R.layout.activity_insert);
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        buttonSave = findViewById(R.id.buttonSave);
        buttonCancel = findViewById(R.id.buttonCancel);
        in_out = findViewById(R.id.in_out);
        source = findViewById(R.id.source);
        amount = findViewById(R.id.amount);
        information = findViewById(R.id.information);

        buttonSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountViewModel.insertAccounts(new Account(
                        source.getText().toString(),
                        in_out.getText().toString(),
                        Integer.parseInt(amount.getText().toString()),
                        "未知",
                        "未知",
                        information.getText().toString()));
                finish();
            }
        });

        buttonCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });


    }

}
