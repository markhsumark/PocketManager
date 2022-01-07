
package com.example.pocketmanager;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertNull;

import android.app.Application;
import android.content.Intent;
import android.widget.Button;

import androidx.annotation.NonNull;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.ViewModelProvider;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;
import org.robolectric.Shadows;
import org.robolectric.android.controller.ActivityController;
import org.robolectric.annotation.Config;
import org.robolectric.annotation.Implementation;
import org.robolectric.annotation.Implements;
import org.robolectric.annotation.RealObject;
import org.robolectric.shadows.ShadowActivity;

import java.util.List;


//這個可以讓你們大概了解一下這個test要怎麼使用
@RunWith(RobolectricTestRunner.class)
@Config(shadows = AccountViewModelShadow.class)
public class RobolectricTest {

    @Test
    public void HomePageTest() throws Exception {
        HomeActivity activity = Robolectric.setupActivity(HomeActivity.class);
        FloatingActionButton adder =  activity.findViewById(R.id.adder);
        FloatingActionButton editor =  activity.findViewById(R.id.editor);
//        TextView results = activity.findViewById(R.id.tvResult);
        //模擬點選按鈕，呼叫OnClickListener#onClick
        adder.performClick();
        System.out.println("--------click adder button successed--------");
        editor.performClick();
        System.out.println("--------click editor button successed--------");
//        Assert.assertEquals("Robolectric Rocks!", results.getText().toString());
    }
    @Test
    public void testJump() throws Exception {
        // 默認會調用Activity的生命週期: onCreate->onStart->onResume
        HomeActivity activity = Robolectric.setupActivity(HomeActivity.class);
        // 觸發按鈕點擊
        activity.findViewById(R.id.adder).performClick();
        // 獲取對應的Shadow類
        ShadowActivity shadowActivity = Shadows.shadowOf(activity);
        // 藉助Shadow類獲取啟動下一Activity的Intent
        Intent nextIntent = shadowActivity.getNextStartedActivity();
        // 校驗Intent的正確性
        assertEquals(nextIntent.getComponent().getClassName(), AddOrEditActivity.class.getName());
        System.out.println("--------testJump successed--------");
    }
    @Test
    public void testShadow() throws Exception{
//        AccountViewModelShadow shadowAccount = (AccountViewModelShadow) ShadowExtractor.extract();
    }


}