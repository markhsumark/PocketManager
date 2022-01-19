package com.mark.pocketmanager;

import io.appium.java_client.MobileElement;
import io.appium.java_client.TouchAction;
import io.appium.java_client.android.AndroidDriver;
import io.appium.java_client.touch.offset.PointOption;

import junit.framework.TestCase;
import org.junit.After;
import org.junit.Before;
import org.junit.BeforeClass;
import org.junit.Test;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;

public class GraphTest {

    private AndroidDriver driver;

    @Before
    public void setUp() throws MalformedURLException {
        DesiredCapabilities desiredCapabilities = new DesiredCapabilities();
        desiredCapabilities.setCapability("platformName", "Android");
        desiredCapabilities.setCapability("deviceName", "Pixel 4 API 30 2 ");
        desiredCapabilities.setCapability("automationName", "UiAutomator2");
        desiredCapabilities.setCapability("autoGrantPermission", true);
        desiredCapabilities.setCapability("app", "/Users/markhsu/AndroidStudioProjects/PocketManager/app/release/app-release.apk");

        URL remoteUrl = new URL("http://localhost:4723/wd/hub");

        driver = new AndroidDriver(remoteUrl, desiredCapabilities);
        in();
        out();
    }

    @Test
    public void monthBarDisplayTest() {
        waitUntilAndClickXPath("//android.widget.LinearLayout[@content-desc=\"圖表\"]");
        MobileElement el5 = (MobileElement) driver.findElementById("com.mark.pocketmanager:id/monthBarChart");
        if(el5.isDisplayed()){
            return;
        }else{
            driver.executeScript("client:client.setTestStatus(false,'msg : test fail')");
        }
    }
    @Test
    public void outPieCharTest() {
        waitUntilAndClickXPath("//android.widget.LinearLayout[@content-desc=\"圖表\"]");
        MobileElement el6 = (MobileElement) driver.findElementById("com.mark.pocketmanager:id/outPieChart");
        if(el6.isDisplayed()){
            return;
        }else{
            driver.executeScript("client:client.setTestStatus(false,'msg : test fail')");
        }
    }
    @Test
    public void inPieCharTest() {
        waitUntilAndClickXPath("//android.widget.LinearLayout[@content-desc=\"圖表\"]");
        MobileElement el6 = (MobileElement) driver.findElementById("com.mark.pocketmanager:id/inPieChart");
        if(el6.isDisplayed()){
            return;
        }else{
            driver.executeScript("client:client.setTestStatus(false,'msg : test fail')");
        }
    }

    @After
    public void tearDown() {
        driver.quit();
    }

    public void in(){
        waitUntilAndClick("com.mark.pocketmanager:id/adder");
        waitUntilAndClick("com.mark.pocketmanager:id/inButton");
        waitE();
        driver.findElementById("com.mark.pocketmanager:id/amountEditor").sendKeys("25");
        waitUntilAndClick("com.mark.pocketmanager:id/addButton");
    }
    public void out(){
        waitUntilAndClick("com.mark.pocketmanager:id/adder");
        driver.findElementById("com.mark.pocketmanager:id/amountEditor").sendKeys("25");
        waitE();
        waitUntilAndClick("com.mark.pocketmanager:id/addButton");
    }
    public void waitE(){
        driver.manage().timeouts().implicitlyWait(30,TimeUnit.SECONDS);
    }
    public void waitUntilAndClick(String id){
        WebDriverWait wait = new WebDriverWait(driver, 5);
        wait.until(ExpectedConditions.elementToBeClickable(By.id(id)));
        MobileElement el2 = (MobileElement) driver.findElementById(id);
        el2.click();
    }
    public void waitUntilAndClickXPath(String XPath){
        MobileElement el2 = (MobileElement) driver.findElementByXPath(XPath);
        el2.click();
    }
}

