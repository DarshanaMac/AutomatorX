package com.adl;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Config {

	WebDriver driver = null;
	
	
	public Config(String technology,String browser) {
		//Project type-Technology
		if (technology.contains("WEB")) {
			selectBrowser(browser);
		}else{
			//mobile
		}
	}
	
	
	private void selectBrowser(String browser) {
	
		switch (browser) {
		  case "Chrome":
			  System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\src\\lib\\chromedriver.exe");
				driver = new ChromeDriver();
				driver.manage().window().maximize();
		    break;
		  case "Firefox":
		    break;
		  
		  default:
			    System.out.println("Error");
		}
		
	}
	
	
}
