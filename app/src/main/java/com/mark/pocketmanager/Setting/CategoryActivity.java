package com.mark.pocketmanager.Setting;

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
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.mark.pocketmanager.Category.Category;
import com.mark.pocketmanager.Category.CategoryViewModel;
import com.mark.pocketmanager.R;
import com.google.android.material.floatingactionbutton.FloatingActionButton;

import java.util.ArrayList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    FloatingActionButton add;
    private RecyclerView categoryRecyclerView;
    private RecyclerView.LayoutManager caLayoutManager;
    private CaAdapter caAdapter;
    private List<Category> categoryData = new ArrayList<>();
    private CategoryViewModel categoryViewModel;
    private LiveData<List<Category>> categoryLiveData = null;
    private String type;
    private TextView hintTextView;

    public boolean onOptionsItemSelected(@NonNull MenuItem item) {
        if (item.getItemId() == android.R.id.home) {
            //Toast.makeText(this, "按下左上角返回鍵", Toast.LENGTH_SHORT).show();
            finish();
        }
        return super.onOptionsItemSelected(item);
    }

    @SuppressLint({"NotifyDataSetChanged", "SetTextI18n"})
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_page);
        type = getIntent().getStringExtra("type");
        hintTextView = findViewById(R.id.hintTextView);
        hintTextView.setText(type + "類別");
        Log.e("size:", Integer.toString(categoryData.size()));
        ActionBar actionBar=getSupportActionBar();
        actionBar.setDisplayHomeAsUpEnabled(true);
        categoryViewModel = new ViewModelProvider(this).get(CategoryViewModel.class);
        categoryLiveData = categoryViewModel.getCategoriesLive(type);
        categoryLiveData.observe(this, categories -> {
            categoryData = categories;
            caAdapter.notifyDataSetChanged();
        });

        add = findViewById(R.id.add);
        add.setOnClickListener(v -> {
            Intent intent = new Intent();
            intent.putExtra("type", type);
            intent.setClass(CategoryActivity.this, AddCategoryActivity.class);
            startActivity(intent);
        });

        categoryRecyclerView = findViewById(R.id.categoryRecyclerview);
        categoryRecyclerView.setHasFixedSize(true);
        caLayoutManager = new LinearLayoutManager(this);
        categoryRecyclerView.setLayoutManager(caLayoutManager);

        caAdapter = new CaAdapter();
        categoryRecyclerView.setAdapter(caAdapter);
    }



    private class CaAdapter extends RecyclerView.Adapter<CaAdapter.MyViewHolder>{

        class MyViewHolder extends RecyclerView.ViewHolder{
            public View itemView;
            public TextView category;

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
            holder.category.setText(categoryData.get(position).getCategory());
            holder.itemView.setOnClickListener(v -> {
                Integer id = categoryData.get(position).getId();
                String category = categoryData.get(position).getCategory();
                Intent intent = new Intent(CategoryActivity.this, EditCategory.class);
                intent.putExtra("type",type);
                intent.putExtra("category", category);
                intent.putExtra("id", id);
                startActivity(intent);
            });
        }

        @Override
        public int getItemCount() {
            return categoryData.size();
        }
    }
}