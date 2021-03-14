package com.example.WebScrapper;

import com.gargoylesoftware.htmlunit.WebClient;
import com.gargoylesoftware.htmlunit.html.HtmlAnchor;
import com.gargoylesoftware.htmlunit.html.HtmlElement;
import com.gargoylesoftware.htmlunit.html.HtmlPage;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import java.net.URLEncoder;
import java.util.List;

import java.io.File;  // Import the File class
import java.io.IOException;  // Import the IOException class to handle errors
import java.io.FileWriter;


@SpringBootApplication
@EnableAutoConfiguration(exclude = {org.springframework.boot.autoconfigure.gson.GsonAutoConfiguration.class})
public class WebScrapperApplication implements CommandLineRunner {

	public static void main(String[] args) {
		SpringApplication.run(WebScrapperApplication.class, args);
	}

	@Override
	public void run(String... args) throws Exception {
		System.out.println("======= Starting WebScrapper Application ==========");
		WebClient client = new WebClient();
		client.getOptions().setCssEnabled(false);
		client.getOptions().setJavaScriptEnabled(false);
		try {
			String searchUrl = "https://www.marketsandmarkets.com/wireless-communication-market-research-114.html";

			HtmlPage page = client.getPage(searchUrl);

			//Getting the tiles
			List<HtmlElement> TitleItems = page.getByXPath("//a[@href[contains(.,\"Market-Reports\")]]/text()");
			//System.out.println("item : " + TitleItems.toString());

			if(TitleItems.isEmpty()){
				System.out.println("No Title items found !");
			}

			//Getting the published dates
			List<HtmlElement> DateItems = page.getByXPath("//td[@class='displaynone']/text()[contains(.,'20')]");
			//System.out.println("Date and Price of Items : " + DateItems.toString());
			if(DateItems.isEmpty()){
				System.out.println("No Date items found !");
			}

            //Getting the links
			List<HtmlElement> linkItems = page.getByXPath("//a[@href[contains(.,'Market-Reports')]]");
			//System.out.println("Link Items : " + linkItems.toString());
			if(linkItems.isEmpty()){
				System.out.println("No Link items found !");
			}

			//Getting report description
			List<HtmlElement> descItems = page.getByXPath("//p/text()");
			//System.out.println("Description Items : " + descItems.toString());
			if(descItems.isEmpty()){
				System.out.println("No Description items found !");
			}else {

				for (int i = 0; i < 5; i++) {
					descItems.remove(0);
				}
			}

			FileWriter myWriter = new FileWriter("report.csv");

			for(int i=0;i<TitleItems.size();i++)
			{
				String hrefVal="";
				String report = TitleItems.get(i) + "|" + DateItems.get(i) + "|" + linkItems.get(i).getAttribute("href") + "|" + descItems.get(i);
				myWriter.write(report);
				myWriter.write("\n");
				System.out.println(report);
				System.out.println("--------------------------------");

			}
			myWriter.close();


		}catch(Exception e){
			e.printStackTrace();
		}
		}

}
