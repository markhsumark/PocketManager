package com.example.pocketmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.ActionBar;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;

public class CategoryActivity extends AppCompatActivity {
    Button back,add,income,expenditure;
    ScrollView linear;
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

        /*
        inAdapter = new InAdapter();
        internalRecyclerView.setAdapter(inAdapter);
*/
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
//                description = itemView.findViewById(R.id.asset);
//                money = itemView.findViewById(R.id.amount);
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
            Resources res=getResources();
            //holder.category.setText(res.getStringArray(R.array.category)[Integer.parseInt(data.get(position).get("category"))]);
            holder.category.setText(categorys.get(position));
            //holder.description.setText(data.get(position).get("description"));
            //holder.money.setText(data.get(position).get("money"));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //String type = data.get(position).get("type");
                    //String assets = data.get(position).get("asset");
                    String category = categorys.get(position);
                    //String description = data.get(position).get("description");
                    //String money = data.get(position).get("money");
                    Intent intent = new Intent(CategoryActivity.this, EditCategory.class);
                    //intent.putExtra("type", type);
                    //intent.putExtra("asset", assets);
                    intent.putExtra("category", category);
                    //intent.putExtra("description", description);
                    //intent.putExtra("money", money);
                    //intent.putExtra("mode", true); //edit
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