package mini_project;

import java.io.File;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

import org.openqa.selenium.By;
import org.openqa.selenium.NoSuchElementException;
import org.openqa.selenium.OutputType;
import org.openqa.selenium.TakesScreenshot;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.annotations.AfterClass;
import org.testng.annotations.BeforeClass;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;


public class MiniProj_Apachi_TestNG {

    WebDriver driver;
    String filepath = System.getProperty("user.dir") + "\\testdata\\miniproject_dataa.xlsx";

    @BeforeClass
    public void setUp() {
    	
        driver = new ChromeDriver();
        driver.get("https://opensource-demo.orangehrmlive.com");
        driver.manage().window().maximize();
    }

    @Test(dataProvider = "loginData")
    public void testLogin(int rowIndex, String usr, String pass, String job_title, String job_discrp) throws InterruptedException, IOException {
    	
    	
       
    	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        driver.findElement(By.xpath("//*[@name=\"username\"]")).sendKeys(usr);
        driver.findElement(By.xpath("//*[@name=\"password\"]")).sendKeys(pass);
        driver.findElement(By.xpath("//*[@type=\"submit\"]")).click();
        
        WebDriverWait wait = new WebDriverWait(driver, Duration.ofSeconds(10));
        

        boolean isIncorrect = incorrect(driver);

        if (isIncorrect) {
            UtilsPackage.setCellData(filepath, "Sheet1", rowIndex, 2, "Login Fail");
            UtilsPackage.setRedColor(filepath, "Sheet1", rowIndex, 2);
            UtilsPackage.setCellData(filepath, "Sheet1", rowIndex, 3, "No");
            UtilsPackage.setRedColor(filepath, "Sheet1", rowIndex, 3);
            UtilsPackage.setCellData(filepath, "Sheet1", rowIndex, 5, "No Element passed");
            UtilsPackage.setCellData(filepath, "Sheet1", rowIndex, 8, "Fail");
            UtilsPackage.setRedColor(filepath, "Sheet1", rowIndex, 8);
            ScreenShot(driver, "Login_Fail.png");
        } else {
            driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(5));
            	System.out.println("Login Successful");
                UtilsPackage.setCellData(filepath, "Sheet1", rowIndex, 2, "Login Success");
                UtilsPackage.setGreenColor(filepath, "Sheet1", rowIndex, 2);
                String url = driver.getCurrentUrl();
                if(url.contains("dashboard")) {
					UtilsPackage.setCellData(filepath, "Sheet1", rowIndex, 3, "Yes");
					UtilsPackage.setGreenColor(filepath, "Sheet1", rowIndex, 3);
					System.out.println("String Dashboard is present in the url");
					driver.findElement(By.xpath("//*[@href='/web/index.php/admin/viewAdminModule']")).click();
				}else {
					UtilsPackage.setCellData(filepath, "Sheet1", rowIndex, 3, "No");
					UtilsPackage.setRedColor(filepath, "Sheet1", rowIndex, 3);
					System.out.println("String Dashboard is not in the url");
					
				}
                
                
                wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[text()='Job ']")))).click();
                driver.findElement(By.xpath("//*[text()='Job Titles']")).click();

                List<WebElement> roles = driver.findElements(By.xpath("//*[@class='oxd-table-card']"));
                List<String> j_title = new ArrayList<>();
                for (WebElement ele : roles) {
                    String tle = ele.getText();
                    j_title.add(tle);
                }

                boolean value = StringIsPresent(driver, j_title, job_title);
                if (value) {
                	ScreenShot(driver, "Duplicate_JobTitles.png");
                    UtilsPackage.setCellData(filepath, "Sheet1", rowIndex, 5, "Duplicate");
                    UtilsPackage.setCellData(filepath, "Sheet1", rowIndex, 8, "Fail");
                    UtilsPackage.setRedColor(filepath, "Sheet1", rowIndex, 8);
                    wait.until(ExpectedConditions.elementToBeClickable(driver.findElement(By.xpath("//*[@class=\"oxd-icon bi-caret-down-fill oxd-userdropdown-icon\"]")))).click();
                    driver.findElement(By.xpath("//*[text()='Logout']")).click();
                    System.out.println("Duplicate Element found and Logged Out");
                } else {
                	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
                    UtilsPackage.setCellData(filepath, "Sheet1", rowIndex, 5, "New");
                    UtilsPackage.setCellData(filepath, "Sheet1", rowIndex, 8, "Pass");
                    UtilsPackage.setGreenColor(filepath, "Sheet1", rowIndex, 8);
                    driver.findElement(By.xpath("//*[@class='oxd-button oxd-button--medium oxd-button--secondary']")).click();
                    driver.findElement(By.xpath("//*[@class=\"oxd-input-group oxd-input-field-bottom-space\"]//*[@class=\"oxd-input oxd-input--active\"]")).sendKeys(job_title);
                    driver.findElement(By.xpath("//*[@placeholder=\"Type description here\"]")).sendKeys(job_discrp);
                    driver.findElement(By.xpath("//*[@class=\"oxd-button oxd-button--medium oxd-button--secondary orangehrm-left-space\"]")).click();
                    wait.until(ExpectedConditions.visibilityOfElementLocated(By.xpath("//*[@class=\"orangehrm-container\"]")));
                    driver.findElement(By.xpath("//*[@class=\"oxd-icon bi-caret-down-fill oxd-userdropdown-icon\"]")).click();
                    driver.findElement(By.xpath("//*[text()='Logout']")).click();
                }
            
        }
    }


    @DataProvider(name = "loginData")
    public Object[][] getData() throws IOException {
        int rows = UtilsPackage.getRowCount(filepath, "Sheet1");
        Object[][] data = new Object[rows][5]; 

        for (int i = 1; i <= rows; i++) {
            data[i - 1][0] = i; 
            data[i - 1][1] = UtilsPackage.getCellData(filepath, "Sheet1", i, 0); 
            data[i - 1][2] = UtilsPackage.getCellData(filepath, "Sheet1", i, 1); 
            data[i - 1][3] = UtilsPackage.getCellData(filepath, "Sheet1", i, 4); 
            data[i - 1][4] = UtilsPackage.getCellData(filepath, "Sheet1", i, 6); 
        }

        return data;
    }


    @AfterClass
    public void tearDown() {
        if (driver != null) {
            driver.quit();
        }
    }

    public static boolean StringIsPresent(WebDriver driver, List<String> j_title, String job_title) {
    	driver.manage().timeouts().implicitlyWait(Duration.ofSeconds(10));
        for (String str : j_title) {
            if ((str.toLowerCase().contains(job_title.toLowerCase()))) {
                return true;
            }
        }
        return false;
    }

    public static boolean incorrect(WebDriver driver) {
        try {
            boolean invalid = driver.findElement(By.xpath("//*[@class='oxd-alert oxd-alert--error']")).isDisplayed();
            if (invalid) {
            	System.out.println("Login Failed");
                return true;
            }
        } catch (NoSuchElementException e) {
        	
        }
        return false;
    }
    
    public static void ScreenShot(WebDriver driver, String scimage) {
		TakesScreenshot ts = (TakesScreenshot) driver;
		File scr = ts.getScreenshotAs(OutputType.FILE);
		File target = new File(System.getProperty("user.dir")+"\\ScreenShotsFolderr\\" + scimage);
		scr.renameTo(target);

	}
}
