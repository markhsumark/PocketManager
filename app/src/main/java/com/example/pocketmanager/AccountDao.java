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

    @Query("DELETE FROM ACCOUNT")
    void deleteAllAccounts();

    @Query("SELECT * FROM ACCOUNT ORDER BY ID DESC")
    LiveData<List<Account>> getAllAccountsLive();

    @Query("SELECT * FROM ACCOUNT WHERE YEAR = :year AND MONTH = :month ORDER BY TIME DESC")
    LiveData<List<Account>> getAccountsLive(int year, int month);

    @Query("SELECT SUM(Amount) FROM ACCOUNT WHERE YEAR = :year AND MONTH = :month AND DAY = :day AND TYPE = :type")
    long getDayAmount(int year, int month, int day, String type);

    @Query("SELECT SUM(Amount) FROM ACCOUNT WHERE YEAR = :year AND MONTH = :month AND TYPE = :type")
    long getMonthAmount(int year, int month, String type);
}
