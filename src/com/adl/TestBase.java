package com.adl;

import java.sql.Driver;
import java.util.List;
import java.util.concurrent.TimeUnit;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.interactions.Actions;
import org.openqa.selenium.support.ui.Select;

import net.bytebuddy.implementation.bind.annotation.Super;

public class TestBase extends Config{

	WebDriver driver = null;
	
	public TestBase(String technology,String browser){
		
		super(technology,browser);
//		System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\src\\lib\\chromedriver.exe");
//		driver = new ChromeDriver();
//		driver.manage().window().maximize();
	}
	
	public void callCommonOperations(List<String> obj) {
		int i = 0;
		String cmd = "";
		String function = "";
		for(String str : obj){
			if(i == 0){
				cmd = str;
			}else if(i == 1){
				function = str;
			}
			i++;
		}
	}
	
	
	public void open(List<String> obj){
		//System.out.println("obj>>> " + obj);
		int i = 0;
		String cmd = "";
		String objx = "";
		for(String str : obj){
			if(i == 0){
				cmd = str;
			}else if(i == 1){
				objx = str;
			}
			i++;
		}
		
		//System.out.println("obj x > " + objx);
		try{
//			System.out.println(objx.split(":")[1]+ " and "+ objx.split(":")[2]);
			driver.get(objx.split(":")[1]+":"+objx.split(":")[2]);
			
		}catch(Exception ex){
			//System.out.println("ex >>> " + ex);
		}
	
	
	}
	
	public void click(List<String> obj){
		int i = 0;
		String cmd = "";
		String objx = "";
		for(String str : obj){
			if(i == 0){
				cmd = str;
			}else if(i == 1){
				objx = str.split(":")[1];
			}
			i++;
		}
		driver.findElement(By.xpath(objx)).click();
	}
	
	public void type(List<String> obj){
		int i = 0;
		String cmd = "";
		String objx = "";
		String txt = "";
		for(String str : obj){
			//System.out.println(">>> " + str);
			if(i == 0){
				cmd = str;
			}else if(i == 1){
				objx = str.split(":")[1];
			}else if(i == 2){
				txt = str.split(":")[1];
			}
			i++;
		}
		driver.findElement(By.xpath(objx)).sendKeys(txt);
	}
	public void pause(List<String> obj){
		int i = 0;
		String cmd = "";
		String objx = "";
		for(String str : obj){
			if(i == 0){
				cmd = str;
			}else if(i == 1){
				objx = str.split(":")[1];
			}
			i++;
		}
		
		driver.manage().timeouts().implicitlyWait(Integer.parseInt(objx), TimeUnit.SECONDS);
	}
	
	
		
		public void quit(List<String> obj){
			int i = 0;
			String cmd = "";
			String objx = "";
			for(String str : obj){
				if(i == 0){
					cmd = str;
				}else if(i == 1){
					objx = str.split(":")[1];
				}
				i++;
			}
			
			driver.quit();
		}
		public void close(List<String> obj){
			int i = 0;
			String cmd = "";
			String objx = "";
			for(String str : obj){
				if(i == 0){
					cmd = str;
				}else if(i == 1){
					objx = str.split(":")[1];
				}
				i++;
			}
			
			driver.close();
		}
		public void mouseover(List<String> obj){
			int i = 0;
			String cmd = "";
			String objx = "";
			for(String str : obj){
				if(i == 0){
					cmd = str;
				}else if(i == 1){
					objx = str.split(":")[1];
				}
				i++;
			}
			
			Actions action = new Actions(driver);
			WebElement we = driver.findElement(By.xpath(objx));
			action.moveToElement(we).build().perform();
		}
		
		public void checkElementPresent(List<String> obj){
			int i = 0;
			String cmd = "";
			String objx = "";
			for(String str : obj){
				if(i == 0){
					cmd = str;
				}else if(i == 1){
					objx = str.split(":")[1];
				}
				i++;
			}
			if(driver.findElements(By.xpath(objx)).size() != 0){
				System.out.println(objx+"Element is Present");
				}else{
				System.out.println(objx+"Element is Absent");
				}
			
		}
		
		public void select(List<String> obj){
			int i = 0;
			String cmd = "";
			String objx = "";
			String txt = "";
			for(String str : obj){
				//System.out.println(">>> " + str);
				if(i == 0){
					cmd = str;
				}else if(i == 1){
					objx = str.split(":")[1];
				}else if(i == 2){
					txt = str.split(":")[1];
					
				}	i++;
				
			
			}
				
				WebElement mySelectedElement = driver.findElement(By.xpath(objx));
				Select dropdown= new Select(mySelectedElement);
				dropdown.selectByVisibleText(txt);
			}
		public void selectframe(List<String> obj){
			int i = 0;
			String cmd = "";
			String objx = "";
			for(String str : obj){
				if(i == 0){
					cmd = str;
				}else if(i == 1){
					objx = str.split(":")[1];
				}
				i++;
			}
			
			driver.switchTo().frame(objx);
		}
			
			
		}
		
	
		
		
	
	

