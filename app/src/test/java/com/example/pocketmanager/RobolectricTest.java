
package com.example.pocketmanager;

import android.widget.Button;
import android.widget.TextView;

import com.google.android.material.floatingactionbutton.FloatingActionButton;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.robolectric.Robolectric;
import org.robolectric.RobolectricTestRunner;


//這個可以讓你們大概了解一下這個test要怎麼使用
@RunWith(RobolectricTestRunner.class)
public class RobolectricTest {

    @Test
    public void clickingButton_shouldChangeResultsViewText() throws Exception {
        HomeActivity activity = Robolectric.setupActivity(HomeActivity.class);
        FloatingActionButton button =  activity.findViewById(R.id.adder);
//        TextView results = activity.findViewById(R.id.tvResult);
        //模擬點選按鈕，呼叫OnClickListener#onClick
        button.performClick();
        System.out.println("--------click adder button successed--------");
//        Assert.assertEquals("Robolectric Rocks!", results.getText().toString());
    }
}