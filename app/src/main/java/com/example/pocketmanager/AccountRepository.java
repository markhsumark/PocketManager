package com.example.pocketmanager;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AccountRepository {
    private LiveData<List<Account>> allAccountsLive;
    private AccountDao accountDao;
    public AccountRepository(Context context) {
        AccountDatabase accountDatabase = AccountDatabase.getDatabase(context.getApplicationContext());
        accountDao = accountDatabase.getAccountDao();
        allAccountsLive = accountDao.getAllAccountsLive();
    }

    public LiveData<List<Account>> getAllAccountsLive() {
        return allAccountsLive;
    }

    public List<Account> getAccounts(int year, int month) {
        return accountDao.getAccounts(year, month);
    }

    void insertAccounts(Account... accounts) {
        new InsertAsyncTask(accountDao).execute(accounts);
    }
    void updateAccounts(Account... accounts) {
        new UpdateAsyncTask(accountDao).execute(accounts);
    }
    void deleteAccounts(Account... accounts) {
        new DeleteAsyncTask(accountDao).execute(accounts);
    }
    void deleteAllAccounts() {
        new DeleteAllAsyncTask(accountDao).execute();
    }

    static class InsertAsyncTask extends AsyncTask<Account,Void,Void> {
        private AccountDao accountDao;

        public InsertAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            accountDao.insertAccounts(accounts);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<Account,Void,Void> {
        private AccountDao accountDao;

        public UpdateAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            accountDao.updateAccounts(accounts);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<Account,Void,Void> {
        private AccountDao accountDao;

        public DeleteAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Account... accounts) {
            accountDao.deleteAccounts(accounts);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void> {
        private AccountDao accountDao;

        public DeleteAllAsyncTask(AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            accountDao.deleteAllAccounts();
            return null;
        }
    }
}
