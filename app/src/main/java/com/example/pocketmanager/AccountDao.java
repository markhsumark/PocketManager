package com.example.pocketmanager;

import android.content.Intent;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Delete;
import androidx.room.Insert;
import androidx.room.Query;
import androidx.room.Update;

import java.util.List;

@Dao
public interface AccountDao {
    @Insert
    void insertAccounts(Account... accounts);

    @Update
    void updateAccounts(Account... accounts);

    @Delete
    void deleteAccounts(Account... accounts);

    @Query("DELETE FROM Account")
    void deleteAllAccounts();

    @Query("SELECT * FROM Account ORDER BY Id DESC")
    LiveData<List<Account>> getAllAccountsLive();

    @Query("SELECT * FROM Account WHERE Year = :year AND Month = :month ORDER BY Time DESC")
    LiveData<List<Account>> getAccountsLive(int year, int month);

    @Query("SELECT SUM(Amount) FROM Account WHERE Year = :year AND Month = :month AND Day = :day AND Type = :type")
    long getDayAmount(int year, int month, int day, String type);

    @Query("SELECT SUM(Amount) FROM Account WHERE Year = :year AND Month = :month AND Type = :type")
    long getMonthAmount(int year, int month, String type);

    @Query("SELECT Category, SUM(Amount) as Amount FROM Account WHERE Year = :year AND Month = :month AND Type = :type GROUP BY Category")
    LiveData<List<CategoryAmount>> getCategoryAmountsLive(int year, int month, String type);
}
