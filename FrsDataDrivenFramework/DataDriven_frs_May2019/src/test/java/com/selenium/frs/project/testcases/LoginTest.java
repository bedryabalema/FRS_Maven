package com.selenium.frs.project.testcases;

import java.util.Hashtable;

import org.testng.SkipException;
import org.testng.annotations.AfterMethod;
import org.testng.annotations.BeforeMethod;
import org.testng.annotations.DataProvider;
import org.testng.annotations.Test;
import org.testng.asserts.SoftAssert;

import com.relevantcodes.extentreports.LogStatus;
import com.selenium.frs.project.base.BaseTest;
import com.selenium.frs.project.util.DataUtil;
import com.selenium.frs.project.util.Xls_Reader;

public class LoginTest extends BaseTest {
	String testCaseName="LoginTest";
	SoftAssert softAssert;
	Xls_Reader xls;
	
	@Test(dataProvider="getData")
	public void doLoginTest(Hashtable<String,String> data){
		
		test = rep.startTest("LoginTest");
		test.log(LogStatus.INFO, data.toString());
		if(!DataUtil.isRunnable(testCaseName, xls) ||  data.get("Runmode").equals("N")){
			test.log(LogStatus.SKIP, "Skipping the test as runmode is N");
			throw new SkipException("Skipping the test as runmode is N");
		}
		
		openBrowser(data.get("Browser"));
		navigate("appurl");
		boolean actualResult=doLogin(data.get("Username"), data.get("Password"));
		
		boolean expectedResult=false;
		if(data.get("ExpectedResult").equals("Y"))
		expectedResult=true;
		else
		expectedResult=false;

		if(expectedResult!=actualResult)
		reportFailure("Login Test Failed");
		reportPass("Login Test Passed");
	}
	
	
	@BeforeMethod
	public void init(){
		softAssert = new SoftAssert();
	}
	
	
	@AfterMethod
	public void quit(){
		try{
			softAssert.assertAll();
		}catch(Error e){
			test.log(LogStatus.FAIL, e.getMessage());
		}
		if(rep!=null){
			rep.endTest(test);
			rep.flush();
		}
		
		if(driver!=null)
			driver.quit();
	}
	
	@DataProvider(parallel=true)
	public Object[][] getData(){		
		super.init();	
		xls = new Xls_Reader(prop.getProperty("xlspath"));
		return DataUtil.getTestData(xls, testCaseName);
		
	}

}
