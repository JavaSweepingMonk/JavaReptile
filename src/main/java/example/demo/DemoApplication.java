package example.demo;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.WebElement;
import org.openqa.selenium.chrome.ChromeDriver;
import org.openqa.selenium.chrome.ChromeOptions;
import org.openqa.selenium.remote.DesiredCapabilities;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import java.util.HashMap;
import java.util.List;

//@SpringBootApplication
public class DemoApplication {

	public static void main(String[] args) {
		System.setProperty("webdriver.chrome.driver","C:/Program Files (x86)/Google/Chrome/Application/chromedriver.exe");
		ChromeOptions option = new ChromeOptions();
		// 设置禁止加载项
//		Map<String, Object> prefs = new HashMap<String, Object>();
//		Map<String, Object> contentSettings = new HashMap<String, Object>();
//		contentSettings.put("images", 2);
//
//		Map<String, Object> preferences = new HashMap<String, Object>();
//		preferences.put("profile.default_content_settings", contentSettings);
//		option.setCapability("chrome.prefs", preferences);
//		// 禁止加载js
//		prefs.put("profile.default_content_settings.javascript", 2); // 2就是代表禁止加载的意思
//		// 禁止加载css
//		prefs.put("profile.default_content_settings.images", 2); // 2就是代表禁止加载的意思
//		option.setExperimentalOption("prefs", prefs);
		//后台加载
		option.addArguments("headless");
		WebDriver webDriver=new ChromeDriver(option);
		//跳转页面
		webDriver.get("https://www.lagou.com/zhaopin/Java/?labelWords=label");
		//通过xpath选中元素
		//clickOption(webDriver, "成都", "工作地点");
		String city="成都";
		WebElement cityElement = webDriver.findElement(By.xpath("//div[@class='other-hot-city']//a[contains(text(),'" + city + "')]"));
		cityElement.click();
		clickOption(webDriver, "应届毕业生", "工作经验");
		clickOption(webDriver, "不限", "学历要求");
		clickOption(webDriver, "不限", "公司规模");

		extractJobByPagination(webDriver);
		webDriver.close();
		//SpringApplication.run(DemoApplication.class, args);
	}
	//信息爬取
	private static void extractJobByPagination(WebDriver webDriver) {
		List<WebElement> jobElement = webDriver.findElements(By.className("con_list_item"));
		for (WebElement jobinfo:jobElement){
			String company_name = jobinfo.findElement(By.className("company_name")).getText();
			String moneyElement = jobinfo.findElement(By.className("position")).findElement(By.className("money")).getText();
			String siteElement = jobinfo.findElement(By.className("position")).findElement(By.className("add")).getText();
			String zhinengElement = jobinfo.findElement(By.className("position")).findElement(By.className("position_link")).findElement(By.tagName("h3")).getText();
			String fuli = jobinfo.findElement(By.className("li_b_r")).getText();
			System.out.println("公司名称: "+company_name+"  地址： "+siteElement+"     工资:  "+moneyElement+"  待遇: "+fuli+"  职位："+zhinengElement);
		}
		WebElement pager_next = webDriver.findElement(By.className("pager_next"));
		if (!pager_next.getAttribute("class").contains("pager_next_disabled")){
			pager_next.click();
			System.out.println("----------->下一页");
			try {
				Thread.sleep(1000);
			} catch (InterruptedException e) {
				e.printStackTrace();
			}
			extractJobByPagination(webDriver);
		}

	}
	//条件筛选
	private static void clickOption(WebDriver webDriver, String optionTile, String chosenTile) {
		WebElement chosenElement = webDriver.findElement(By.xpath("//li[@class='multi-chosen']/span[contains(text(),'" + chosenTile + "')]"));
		WebElement optionElement = chosenElement.findElement(By.xpath("../a[contains(text(),'" + optionTile + "')]"));
		optionElement.click();

	}

}
