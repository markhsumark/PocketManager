package com.mark.pocketmanager.homepage;

import io.appium.java_client.MobileElement;
import io.appium.java_client.android.AndroidDriver;
import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.remote.DesiredCapabilities;

public class MonthPickerTest {

    private AndroidDriver driver;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("deviceName", "Pixel 5 API 30");
        desiredCapabilities.setCapability("automationName", "UiAutomator2");
        desiredCapabilities.setCapability("autoGrantPermissions", true);
        desiredCapabilities.setCapability("app", "C:\\Users\\q1234\\Desktop\\SE\\FinalProject\\PocketManager\\app\\release\\app-release.apk");

        URL remoteUrl = new URL("http://localhost:4723/wd/hub");

        driver = new AndroidDriver(remoteUrl, desiredCapabilities);
    }

    @Test
    public void sampleTest() {
        MobileElement el1 = (MobileElement) driver.findElementById("com.mark.pocketmanager:id/monthPicker");
        el1.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        MobileElement el2 = (MobileElement) driver.findElementByXPath("/hierarchy/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.FrameLayout/android.widget.LinearLayout/android.widget.LinearLayout/android.widget.LinearLayout[1]/android.widget.ScrollView/android.widget.LinearLayout/androidx.recyclerview.widget.RecyclerView/android.widget.LinearLayout[6]/android.widget.TextView");
        el2.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        MobileElement el3 = (MobileElement) driver.findElementById("com.mark.pocketmanager:id/btn_p");
        el3.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
        MobileElement el4 = (MobileElement) driver.findElementById("com.mark.pocketmanager:id/lastMonth");
        el4.click();
        driver.manage().timeouts().implicitlyWait(100, TimeUnit.SECONDS);
    }

    @After
    public void tearDown() {
        driver.quit();
    }
}
