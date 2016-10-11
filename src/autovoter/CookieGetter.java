package autovoter;

import java.util.HashMap;
import java.util.Map;
import java.util.Scanner;
import org.openqa.selenium.By;
import org.openqa.selenium.Cookie;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.edge.EdgeDriver;
import org.openqa.selenium.firefox.FirefoxDriver;

public class CookieGetter {

    private String account;

    public WebDriver getDriver() {
        return driver;
    }
    private String pwd;
    private WebDriver driver;

    public void setAccount(String account) {
        this.account = account;
    }

    public void setPwd(String pwd) {
        this.pwd = pwd;
    }

    public CookieGetter() {
        driver = new EdgeDriver();  //for Windows 10
       //driver = new FirefoxDriver();  //for macOS

        System.getProperties().setProperty("webdriver.edge.driver", "MicrosoftWebDriver.exe"); //for Windows 10
        driver.get("http://www.mgtv.com/v/2016/jyj2016/");
    }

    public Map login() {

        Map res = new HashMap<>();
        Cookie uuid = null;
        Cookie HDCN = null;
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        driver.findElement(By.className("trigger-login")).click();

        try {
            Thread.sleep(1000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }

        driver.findElement(By.className("mini-login-name")).sendKeys(account);
        driver.findElement(By.className("hover")).click();

        driver.findElement(By.className("password")).sendKeys(pwd);
        Scanner sc = new Scanner(System.in, "GBK"); //for Windows 10
       // Scanner sc = new Scanner(System.in); //for macOS
        while (res.get("uuid") == null || res.get("HDCN") == null) {
            System.out.println("请在此输入验证码：");
            String enteredVerify = sc.nextLine();

            driver.findElement(By.className("mini-login-yzm")).sendKeys(enteredVerify);
            driver.findElement(By.className("btn-login")).click();

            try {
                Thread.sleep(2000);
            } catch (InterruptedException ex) {
                ex.printStackTrace();
            }
            res.put("account", account);
            uuid = driver.manage().getCookieNamed("uuid");
            if (uuid != null) {
                res.put(uuid.getName(), uuid.getValue());
            }
            HDCN = driver.manage().getCookieNamed("HDCN");
            if (HDCN != null) {
                res.put(HDCN.getName(), HDCN.getValue());
            }
            if (HDCN == null || uuid == null) {
                driver.findElement(By.className("mini-login-yzm")).clear();
            }
        }
        new FileOpearter().writeRecord(account, uuid.getValue(), HDCN.getValue());
        try {
            Thread.sleep(2000);
        } catch (InterruptedException ex) {
            ex.printStackTrace();
        }
        driver.navigate().to("http://i.mgtv.com/account/logout?from=http%3A%2F%2Fwww.mgtv.com%2Fv%2F2016%2Fjyj2016%2F");
        return res;
    }

}
