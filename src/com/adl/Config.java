package com.adl;

import java.awt.event.ActionEvent;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import java.util.Properties;

import org.openqa.selenium.WebDriver;
import org.openqa.selenium.chrome.ChromeDriver;

public class Config {

	WebDriver driver = null;
	Properties prop = null;
	
	//Select framework
	public Config() {		
		//read properties
		readProperties();
		//Project type-Technology
		if (prop.getProperty("project.tech").contains("WEB")) {
			selectBrowser(prop.getProperty("project.browser"));
		}else{
			//mobile
		}
	}
	
	//Select The browser for selinium execution
	private void selectBrowser(String browser) {
	
		switch (browser.toUpperCase()) {
		  case "CHROME":
			  System.setProperty("webdriver.chrome.driver", System.getProperty("user.dir")+"\\src\\lib\\chromedriver.exe");
				driver = new ChromeDriver();
				driver.manage().window().maximize();
		    break;
		  case "FIREFOX":
		    break;
		  
		  default:
			    System.out.println("Error");
		}
		
	}
	
	//Load configuration file
	private void readProperties() {
		try (InputStream input = new FileInputStream("D:\\Automator\\ExampleProject\\config.properties")) {

            prop = new Properties();
            // load a properties file
            prop.load(input);

            // get the property value and print it out
            System.out.println(prop.getProperty("project.tech"));
            System.out.println(prop.getProperty("project.browser"));
            System.out.println(prop.getProperty("project.path"));

        } catch (IOException ex) {
            ex.printStackTrace();
        }
	}
	
	//Read function file
	protected List<String> readFunction(String lib,String function) {
		
		List<String> lines=null;
		try {
			lines = Files.readAllLines(new File(prop.getProperty("project.path")+"\\CommonFunctions\\"+lib+"\\"+function+".txt").toPath());
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return lines;
	}
	
	//Create command string array
	protected List<List<String>> createCommandList(String lib,String function) {
		List<List<String>> finalStringList = new ArrayList<>();
		List<String> stList = null;
		int tempInt = 0;
		
		List<String> lines =readFunction(lib,function);

			for (String line : lines) {
				if (tempInt < 0)
					tempInt = 0;
				if (line.equalsIgnoreCase("call")) {
					// System.out.println(">>> from click");

					stList = new ArrayList<>();
					stList.add(line);
					tempInt = 1;
				} else if (line.equalsIgnoreCase("click")) {
					// System.out.println(">>> from click");

					stList = new ArrayList<>();
					stList.add(line);
					tempInt = 1;
				} else if (line.equalsIgnoreCase("type")) {
					// System.out.println(">>> from type");

					stList = new ArrayList<>();
					stList.add(line);
					tempInt = 2;
				} else if (line.equalsIgnoreCase("open")) {
					// System.out.println(">>> from type");

					stList = new ArrayList<>();
					stList.add(line);
					tempInt = 1;
				} else if (line.equalsIgnoreCase("pause")) {
					// System.out.println(">>> from type");

					stList = new ArrayList<>();
					stList.add(line);
					tempInt = 1;
				} else if (line.equalsIgnoreCase("quit")) {

					stList = new ArrayList<>();
					stList.add(line);
					tempInt = 0;
					// System.out.println(">>> from type");
				} else if (line.equalsIgnoreCase("close")) {
					stList = new ArrayList<>();
					stList.add(line);
					tempInt = 0;

				} else if (line.equalsIgnoreCase("mouseover")) {
					stList = new ArrayList<>();
					stList.add(line);
					tempInt = 1;
				} else if (line.equalsIgnoreCase("checkElementPresent")) {
					stList = new ArrayList<>();
					stList.add(line);
					tempInt = 1;
				} else if (line.equalsIgnoreCase("select")) {
					// System.out.println(">>> from type");

					stList = new ArrayList<>();
					stList.add(line);
					tempInt = 2;
				} else if (line.equalsIgnoreCase("selectframe")) {
					// System.out.println(">>> from type");

					stList = new ArrayList<>();
					stList.add(line);
					tempInt = 2;
				}

				else {
					// System.out.println(">>> else part");
					try {
						stList.add(line);
					} catch (NullPointerException nex) {

					} catch (Exception ex) {
						ex.printStackTrace();
					}
					tempInt--;
				}
				if (tempInt == 0) {
					finalStringList.add(stList);
				}
				// System.out.println("temp int >>> " + tempInt);
			}
			return finalStringList;
	}
	
}
