package test;

import java.util.List;
import java.util.Random;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.JavascriptExecutor;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;
import org.testng.Assert;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.Test;

public class NewDepositTest {
	
	WebDriver driver;	
	
@BeforeMethod	
public void launchBrowser () {
		
		System.setProperty("webdriver.chrome.driver", "./driver/chromedriver.exe");
		driver = new ChromeDriver(); //this instantiate the Chrome Driver. It launches the Chrome Driver.
		driver.manage().window().maximize();
		driver.manage().deleteAllCookies();
		driver.manage().timeouts().implicitlyWait(10, TimeUnit.SECONDS);		
		driver.get("http://techfios.com/test/billing/?ng=login/");
	}

@Test
public void userShouldBeAbleToAddDeposit () throws InterruptedException {
	driver.findElement(By.xpath("//*[@placeholder='Email Address']")).sendKeys("techfiosdemo@gmail.com");
	driver.findElement(By.xpath("//*[contains(@placeholder, 'assword') and contains(@class,'form-')]")).sendKeys("abc123");
	driver.findElement(By.xpath("//*[contains(text(), 'Sign in') and @name='login']")).click();
	String expectedTitle = "Dashboard- TechFios Test Application - Billing";
	// For Validation
	Assert.assertEquals(driver.getTitle(), expectedTitle, "Dashbord Page Did Not Display!!");
	
	By TRANSACTIONS_MENU_LOCATOR = By.xpath("//ul[@id='side-menu']/descendant::span[text()='Transactions']");
	driver.findElement(TRANSACTIONS_MENU_LOCATOR).click();
	
	By NEW_DEPOSIT_PAGE_LOCATOR = By.linkText("New Deposit");
	waitForElement(NEW_DEPOSIT_PAGE_LOCATOR, driver, 20);
	driver.findElement(NEW_DEPOSIT_PAGE_LOCATOR).click();
	
	//Select an account from a drop down
	Select select = new Select (driver.findElement(By.cssSelector("select#account")));
	//This dropdown list here on this website keeps getting updated every minute, so please make sure to confirm if
	//the text we are trying to read is available in the list at this point in time.
	select.selectByVisibleText("loan");
	Thread.sleep(3000);
	//Below code with print on the console Automation test + some kind of random number which is going to be less than 999.
	String expectedDiscription = "AutomationTest" + new Random().nextInt(999);
	System.out.println(expectedDiscription);
	//Below code will print Automation Test and three digit random number on the description tab.
	driver.findElement(By.id("description")).sendKeys(expectedDiscription);
	//Below code will type 100,000 as the amount to deposit in the amount tab
	driver.findElement(By.id("amount")).sendKeys("100,000");
	//Below code will click submit button and we can see on the left hand side of the screen as the updated transaction list.
	driver.findElement(By.id("submit")).click();
	//explicit wait 
	new WebDriverWait(driver, 40).until(ExpectedConditions.invisibilityOfElementLocated(By.xpath("//*[contains@class, 'blockUI']")));
	//cheap way to get to that WebElement //td/a
	//Below is the Xpath to get the same element but it is the much better way t do:
	////*[contains(text(), 'Recent Deposits')]/../following-sibling::*/descendant::a
	
	waitForElement(By.linkText(expectedDiscription), driver, 60);
	
	//Below code is storing the list of all transactions and storing it in descriptionElements.
	List<WebElement> descriptionElements = driver.findElements(By.xpath("//table/descendant::a"));
	
	//For scrolling the window up and down.
	JavascriptExecutor js = (JavascriptExecutor)driver;
	js.executeScript("scroll(0,600)");
	//to scroll up: js.executeScript("scroll(600,0)"); or (0, -600)
	
	Assert.assertTrue(isDescriptionMatch(expectedDiscription,descriptionElements),"Deposit Unsuccessful!!");
	
	//Below code will print the first description from the transaction list and will print it on console.
	System.out.println(descriptionElements.get(0).getText());
	Thread.sleep(6000);
}

private boolean isDescriptionMatch(String expectedDiscription, List<WebElement> descriptionElements) {
	for (int i=0; i < descriptionElements.size(); i++) {
		if (expectedDiscription.equalsIgnoreCase(descriptionElements.get(i).getText())) {
			return true;
		}
	}
	return false;
}

private void waitForElement(By locator, WebDriver driver1, int time) {
 new WebDriverWait (driver1, time).until(ExpectedConditions.visibilityOfAllElementsLocatedBy(locator));
	
}

@AfterMethod
public void closeEverything () {
	driver.close();
	driver.quit();
}

}
