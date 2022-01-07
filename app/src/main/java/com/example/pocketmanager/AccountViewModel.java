package com.example.pocketmanager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;

import java.util.ArrayList;
import java.util.List;

public class AccountViewModel extends AndroidViewModel {
    private final AccountRepository accountRepository;

    public AccountViewModel(@NonNull Application application) {
        super(application);
        accountRepository = new AccountRepository(application);
    }

    public LiveData<List<Account>> getAllAccountsLive() {
        return accountRepository.getAllAccountsLive();
    }

    public LiveData<List<Account>> getAccountsLive(int year, int month) {
        return accountRepository.getAccountsLive(year, month);
    }

    public LiveData<List<CategoryAmount>> getCategoryAmountsLive(int year, int month, String type) {
        return accountRepository.getCategoryAmountsLive(year, month, type);
    }

    public long getDayAmount(int year, int month, int day, String type) {
        return accountRepository.getDayAmount(year, month, day, type);
    }

    public long getMonthAmount(int year, int month, String type) {
        return accountRepository.getMonthAmount(year, month, type);
    }

    void insertAccounts(Account... accounts) {
        accountRepository.insertAccounts(accounts);
    }

    void updateAccounts(Account... accounts) {
        accountRepository.updateAccounts(accounts);
    }

    void deleteAccounts(Account... accounts) {
        accountRepository.deleteAccounts(accounts);
    }

    void deleteAllAccounts() {
        accountRepository.deleteAllAccounts();
    }

}
