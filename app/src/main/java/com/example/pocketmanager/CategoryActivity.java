package com.example.pocketmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.content.res.Resources;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ScrollView;
import android.widget.TextView;

import java.util.HashMap;
import java.util.LinkedList;

public class CategoryActivity extends AppCompatActivity {
    Button back,add,income,expenditure;
    ScrollView linear;
    private RecyclerView categoryRecycleview;
    private RecyclerView.LayoutManager caLayoutManager;
    private CaAdapter caAdapter;
    private LinkedList<HashMap<String,String>> data;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.category_page);
        back=findViewById(R.id.back);
        back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            };
        });
        add=findViewById(R.id.add);
        add.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent();
                intent.setClass(CategoryActivity.this, AddCategoryActivity.class);
                startActivity(intent);
            }
        });
        income=findViewById(R.id.income);
        income.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });
        expenditure=findViewById(R.id.expenditure);
        expenditure.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            }
        });

        categoryRecycleview = findViewById(R.id.categoryRecycleview);
        categoryRecycleview.setHasFixedSize(true);
        caLayoutManager = new LinearLayoutManager(this);
        categoryRecycleview.setLayoutManager(caLayoutManager);

        makeData();
        /*
        inAdapter = new InAdapter();
        internalRecyclerView.setAdapter(inAdapter);
*/
        caAdapter = new CaAdapter();
        categoryRecycleview.setAdapter(caAdapter);

    }


    private void makeData(){
        data = new LinkedList<>();
        for(int i=1; i<20; i++){
            HashMap<String, String> row = new HashMap<>();
            //row.put("type", String.valueOf(i/10));
            //row.put("asset", String.valueOf(i/8));
            row.put("category", String.valueOf(i));
            row.put("description", "category"+i);
            row.put("money", "");
            data.add(row);
        }
    }

    private class CaAdapter extends RecyclerView.Adapter<CaAdapter.MyViewHolder>{

        class MyViewHolder extends RecyclerView.ViewHolder{
            public View itemView;
            public TextView description, money, category;

            public MyViewHolder(View v){
                super(v);
                itemView = v;
                category = itemView.findViewById(R.id.category);
                description = itemView.findViewById(R.id.asset);
                money = itemView.findViewById(R.id.amount);
            }
        }

        @NonNull
        @Override
        public CaAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from((parent.getContext()))
                    .inflate(R.layout.single_record, parent,false);

            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull CaAdapter.MyViewHolder holder, int position) {
            Resources res=getResources();
            //holder.category.setText(res.getStringArray(R.array.category)[Integer.parseInt(data.get(position).get("category"))]);
            holder.category.setText(data.get(position).get("category"));
            holder.description.setText(data.get(position).get("description"));
            holder.money.setText(data.get(position).get("money"));

            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    //String type = data.get(position).get("type");
                    //String assets = data.get(position).get("asset");
                    String category = data.get(position).get("category");
                    //String description = data.get(position).get("description");
                    //String money = data.get(position).get("money");
                    Intent intent = new Intent(CategoryActivity.this, ChildCategory.class);
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
            return data.size();
        }
    }
}