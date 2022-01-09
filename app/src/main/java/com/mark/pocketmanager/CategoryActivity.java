package com.mark.pocketmanager;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.pocketmanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    FloatingActionButton add;
    private RecyclerView categoryRecycleview;
    private RecyclerView.LayoutManager caLayoutManager;
    private CaAdapter caAdapter;
    private List<String> categorys = new ArrayList<>(Arrays.asList("食物","運動","娛樂","交通","家居","健康","教育"));

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
        setContentView(R.layout.category_page);

        Log.e("size:", Integer.toString(categorys.size()));

        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);

        add=findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CategoryActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });

        categoryRecycleview = findViewById(R.id.categoryRecycleview);
        categoryRecycleview.setHasFixedSize(true);
        caLayoutManager = new LinearLayoutManager(this);
        categoryRecycleview.setLayoutManager(caLayoutManager);

        caAdapter = new CaAdapter();
        categoryRecycleview.setAdapter(caAdapter);
        Log.e("size2:", Integer.toString(categorys.size()));
    }



    private class CaAdapter extends RecyclerView.Adapter<CaAdapter.MyViewHolder>{

        class MyViewHolder extends RecyclerView.ViewHolder{
            public View itemView;
            public TextView description, money, category;

            public MyViewHolder(View v){
                super(v);
                itemView = v;
                category = itemView.findViewById(R.id.category);
            }
        }

        @NonNull
        @Override
        public CaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from((parent.getContext()))
                    .inflate(R.layout.category_list, parent,false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull CaAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            holder.category.setText(categorys.get(position));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    String category = categorys.get(position);
                    Intent intent = new Intent(CategoryActivity.this, EditCategory.class);
                    intent.putExtra("category", category);
                    startActivity(intent);
                }
            });
        }

        @Override
        public int getItemCount() {
            return categorys.size();
        }
    }
}