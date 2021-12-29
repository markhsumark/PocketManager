package com.example.pocketmanager;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AlertDialog;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.res.Configuration;
import android.content.res.Resources;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.Spinner;
import android.widget.Switch;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;
import com.kal.rackmonthpicker.RackMonthPicker;
import com.kal.rackmonthpicker.listener.DateMonthDialogListener;
import com.kal.rackmonthpicker.listener.OnCancelMonthDialogListener;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Locale;
import java.util.Objects;

public class HomeActivity extends AppCompatActivity {
    private TextView month;
    private ImageButton monthPicker;
    private FloatingActionButton previousStep, nextStep, editor, adder;
/*TODO assetLine
    private Spinner assetName;
    private TextView assetBalance;
*/
    private RecyclerView externalRecyclerView, internalRecyclerView;
    private RecyclerView.LayoutManager exLayoutManager, inLayoutManager;
    private ExAdapter exAdapter;
    //private InAdapter inAdapter;
    List<Account> data = new ArrayList<>();
    AccountViewModel accountViewModel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.home_page);

        Calendar calendar = Calendar.getInstance();
        month = findViewById(R.id.month);
        month.setText(calendar.get(Calendar.YEAR)+","+calendar.get(Calendar.MONTH)+"月");
        monthPicker = findViewById(R.id.monthPicker);
        monthPicker.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rackMonthPicker(v, month);
            }
        });
        if((getBaseContext().getResources().getConfiguration().uiMode& Configuration.UI_MODE_NIGHT_MASK) == Configuration.UI_MODE_NIGHT_YES)
            monthPicker.setBackgroundColor(Color.parseColor("#000000"));


        //  點選新增按鈕會跳轉頁面
        externalRecyclerView = findViewById(R.id.externalRecyclerView);
        externalRecyclerView.setHasFixedSize(true);
        exLayoutManager = new LinearLayoutManager(this);
        externalRecyclerView.setLayoutManager(exLayoutManager);
        exAdapter = new ExAdapter();
        externalRecyclerView.setAdapter(exAdapter);
        accountViewModel = new ViewModelProvider(this).get(AccountViewModel.class);
        accountViewModel.getAllAccountsLive().observe(this, new Observer<List<Account>>() {
            @Override
            public void onChanged(List<Account> accounts) {
                data = new ArrayList<>();
                for (int i = 0; i < accounts.size(); i++) {
                    data.add(accounts.get(i));
                }
                Log.e("size",Integer.toString(accounts.size()));
                exAdapter.notifyDataSetChanged();
                /*StringBuilder text = new StringBuilder();
                for (int i = 0; i < accounts.size(); i++) {
                    Account account = accounts.get(i);
                    text.append("id:").append(account.getId())
                            .append(",類別:").append(account.getInOut())
                            .append(",金額:").append(account.getPrice())
                            .append(",摘要:").append(account.getNotation())
                            .append('\n');
                }
                textView.setText(text.toString());*/
            }
        });
        ///*  點選新增按鈕會跳轉頁面
        previousStep = findViewById(R.id.previousStep);
        previousStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
        nextStep = findViewById(R.id.nextStep);
        nextStep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
        editor = findViewById(R.id.editor);
        editor.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //TODO
            }
        });
        adder = findViewById(R.id.adder);
        adder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(HomeActivity.this, AddOrEditActivity.class);
                intent.putExtra("mode", "add");
                startActivity(intent);
            }
        });
        /*TODO assetLine
                assetName = findViewById(R.id.assetChooser);
                assetBalance = findViewById(R.id.assetBalance);
        */



    }

    public void rackMonthPicker(View v, TextView t){

        new RackMonthPicker(this)
                .setLocale(Locale.ENGLISH)
                .setPositiveButton(new DateMonthDialogListener() {
                    @Override
                    public void onDateMonth(int month, int startDate, int endDate, int year, String monthLabel) {
                        String[] time = monthLabel.split(",");
                        if(time[0].equals("Jan")) time[0]="1";
                        else if (time[0].equals("Feb")) time[0]="2";
                        else if (time[0].equals("Mar")) time[0]="3";
                        else if (time[0].equals("Apr")) time[0]="4";
                        else if (time[0].equals("May")) time[0]="5";
                        else if (time[0].equals("Jun")) time[0]="6";
                        else if (time[0].equals("Jul")) time[0]="7";
                        else if (time[0].equals("Aug")) time[0]="8";
                        else if (time[0].equals("Sep")) time[0]="9";
                        else if (time[0].equals("Oct")) time[0]="10";
                        else if (time[0].equals("Nov")) time[0]="11";
                        else if (time[0].equals("Dec")) time[0]="12";
                        t.setText(time[1]+","+time[0]+"月");
                    }
                })
                .setNegativeButton(new OnCancelMonthDialogListener() {
                    @Override
                    public void onCancel(AlertDialog dialog) {
                        dialog.cancel();
                    }
                }).show();
    }

//  顯示RecyclerView
    private class ExAdapter extends RecyclerView.Adapter<ExAdapter.MyViewHolder>{

        class MyViewHolder extends RecyclerView.ViewHolder{
            public View itemView;
            public TextView description, money, category;

            public MyViewHolder(View v){
                super(v);
                itemView = v;
                category = itemView.findViewById(R.id.category);
                description = itemView.findViewById(R.id.description);
                money = itemView.findViewById(R.id.money);
            }
        }

        @NonNull
        @Override
        public ExAdapter.MyViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View itemView = LayoutInflater.from((parent.getContext()))
                    .inflate(R.layout.single_record, parent,false);
            return new MyViewHolder(itemView);
        }

        @Override
        public void onBindViewHolder(@NonNull ExAdapter.MyViewHolder holder, @SuppressLint("RecyclerView") int position) {
            //Resources res=getResources();
            /*holder.category.setText(res.getStringArray(R.array.category)[Integer.parseInt(data.get(position).get("category"))]);
            holder.description.setText(data.get(position).get("description"));
            holder.money.setText(data.get(position).get("money"));*/
            holder.category.setText(data.get(position).getCategoryName());
            holder.description.setText(data.get(position).getDescription());
            holder.money.setText(Integer.toString(data.get(position).getPrice()));
            holder.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    Intent intent = new Intent(HomeActivity.this, AddOrEditActivity.class);
                    intent.putExtra("mode", "edit");
                    intent.putExtra("Id", data.get(position).getId());
                    intent.putExtra("Property", data.get(position).getProperty());
                    intent.putExtra("InOut", data.get(position).getInOut());
                    intent.putExtra("Price", Integer.toString(data.get(position).getPrice()));
                    intent.putExtra("CategoryName", data.get(position).getCategoryName());
                    intent.putExtra("SubCategoryName", data.get(position).getSubCategoryName());
                    intent.putExtra("Description", data.get(position).getDescription());
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