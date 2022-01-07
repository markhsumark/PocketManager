package com.example.pocketmanager;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;

import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;

import java.util.List;

/**
 * 創建{@link }的影子類
 *
 * @author HansChen
 */
@Implements(AccountViewModel.class)
public class AccountViewModelShadow {
    /**
     * 通過@RealObject註解可以訪問原始對象，但注意，通過@RealObject註解的變量調用方法，依然會調用Shadow類的方法，而不是原始類的方法
     * 只能用來訪問原始類的field
     */
    @RealObject
    AccountViewModel realObject;

    /**
     * 需要一個無參構造方法
     */
    public AccountViewModelShadow() {
    }

    /**
     * 對應原始類的構造方法
     *
     * 對應原始類構造方法的傳入參數
     */
    public void __constructor__(@NonNull Application application) {
        realObject = new AccountViewModel(application);
    }

    /**
     * 原始對象的方法被調用的時候，Robolectric會根據方法簽名查找對應的Shadow方法並調用
     */
    @Implementation
    public LiveData<List<Account>> getAccountsLive(int year, int month) {
        System.out.println("---------getAccountLive() ----------");
        return realObject.getAccountsLive(2022, 1);
    }
    @Implementation
    public void insertAccount(Account... accounts) {
        System.out.println("---------insertAccount() ----------");
        realObject.insertAccounts(accounts);
    }
}
