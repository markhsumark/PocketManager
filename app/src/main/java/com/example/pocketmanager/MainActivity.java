package com.example.pocketmanager;

import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.room.Room;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import java.util.Date;
import java.util.List;

public class MainActivity extends AppCompatActivity {
    TextView textView;
    Button buttonInsert, buttonUpdate, buttonClear, buttonDelete, buttonNew;
    AccountViewModel accountViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        textView = findViewById(R.id.textView);
        buttonInsert = findViewById(R.id.buttonInsert);
        buttonUpdate = findViewById(R.id.buttonUpdate);
        buttonClear = findViewById(R.id.buttonClear);
        buttonDelete = findViewById(R.id.buttonDelete);
        buttonNew = findViewById(R.id.buttonNew);
        accountViewModel.getAllAccountsLive().observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                StringBuilder text = new StringBuilder();
                for (int i = 0; i < accounts.size(); i++) {
                    Account account = accounts.get(i);
                    text.append("id:").append(account.getId())
                            .append(",類別:").append(account.getInOut())
                            .append(",金額:").append(account.getPrice())
                            .append(",摘要:").append(account.getNotation())
                            .append('\n');
                }
                textView.setText(text.toString());
            }
        });

        buttonInsert.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //Account account = new Account("現金", "收入", 81000, "生活用品", "坐墊", "你在大聲甚麼啦");
                accountViewModel.insertAccounts(new Account("現金", "收入", 81000, "生活用品", "坐墊", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "支出", 3564, "生活用品", "手機", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "支出", 10000, "生活用品", "坐墊", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "收入", 3333, "生活用品", "桌球拍", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "支出", 81000, "生活用品", "手錶", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "收入", 9999, "生活用品", "坐墊", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "收入", 7500, "生活用品", "坐墊", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "收入", 81000, "生活用品", "坐墊", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "支出", 345, "生活用品", "坐墊", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "支出", 81000, "生活用品", "坐墊", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "收入", 2320, "生活用品", "手機殼", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "支出", 81000, "生活用品", "坐墊", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "支出", 9880, "生活用品", "坐墊", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "收入", 81000, "生活用品", "坐墊", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "支出", 1223, "生活用品", "坐墊", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "收入", 81000, "生活用品", "吃飯", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "收入", 5550, "生活用品", "坐墊", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "支出", 81000, "生活用品", "電腦", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "支出", 1200, "生活用品", "坐墊", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "收入", 19000, "生活用品", "桌球膠皮", "你在大聲甚麼啦"));
                accountViewModel.insertAccounts(new Account("現金", "收入", 9000, "生活用品", "吃飯", "你在大聲甚麼啦"));
            }
        });

        buttonUpdate.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                accountViewModel.deleteAllAccounts();
            }
        });

        buttonDelete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        buttonNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(MainActivity.this, InsertActivity.class);
                startActivity(intent);
            }
        });
    }
}