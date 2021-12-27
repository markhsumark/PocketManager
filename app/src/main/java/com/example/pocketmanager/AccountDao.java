package com.example.pocketmanager;

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
        //List<Account> getAllAccounts();
    LiveData<List<Account>> getAllAccountsLive();
}