package com.example.pocketmanager;

import android.content.Context;
import android.os.AsyncTask;

import androidx.lifecycle.LiveData;

import java.util.List;

public class AccountRepository {
    private LiveData<List<com.example.pocketmanager.Account>> allAccountsLive;
    private com.example.pocketmanager.AccountDao accountDao;
    public AccountRepository(Context context) {
        com.example.pocketmanager.AccountDatabase accountDatabase = com.example.pocketmanager.AccountDatabase.getDatabase(context.getApplicationContext());
        accountDao = accountDatabase.getAccountDao();
        allAccountsLive = accountDao.getAllAccountsLive();
    }

    public LiveData<List<com.example.pocketmanager.Account>> getAllAccountsLive() {
        return allAccountsLive;
    }

    void insertAccounts(com.example.pocketmanager.Account... accounts) {
        new InsertAsyncTask(accountDao).execute(accounts);
    }
    void updateAccounts(com.example.pocketmanager.Account... accounts) {
        new UpdateAsyncTask(accountDao).execute(accounts);
    }
    void deleteAccounts(com.example.pocketmanager.Account... accounts) {
        new DeleteAsyncTask(accountDao).execute(accounts);
    }
    void deleteAllAccounts() {
        new DeleteAllAsyncTask(accountDao).execute();
    }

    static class InsertAsyncTask extends AsyncTask<com.example.pocketmanager.Account,Void,Void> {
        private com.example.pocketmanager.AccountDao accountDao;

        public InsertAsyncTask(com.example.pocketmanager.AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(com.example.pocketmanager.Account... accounts) {
            accountDao.insertAccounts(accounts);
            return null;
        }
    }

    static class UpdateAsyncTask extends AsyncTask<com.example.pocketmanager.Account,Void,Void> {
        private com.example.pocketmanager.AccountDao accountDao;

        public UpdateAsyncTask(com.example.pocketmanager.AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(com.example.pocketmanager.Account... accounts) {
            accountDao.updateAccounts(accounts);
            return null;
        }
    }

    static class DeleteAsyncTask extends AsyncTask<com.example.pocketmanager.Account,Void,Void> {
        private com.example.pocketmanager.AccountDao accountDao;

        public DeleteAsyncTask(com.example.pocketmanager.AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(com.example.pocketmanager.Account... accounts) {
            accountDao.deleteAccounts(accounts);
            return null;
        }
    }

    static class DeleteAllAsyncTask extends AsyncTask<Void,Void,Void> {
        private com.example.pocketmanager.AccountDao accountDao;

        public DeleteAllAsyncTask(com.example.pocketmanager.AccountDao accountDao) {
            this.accountDao = accountDao;
        }

        @Override
        protected Void doInBackground(Void... voids) {
            accountDao.deleteAllAccounts();
            return null;
        }
    }
}
