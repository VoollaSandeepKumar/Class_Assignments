import java.io.FileWriter;
import au.com.bytecode.opencsv.CSVWriter;
import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

public class Controller {
	
	public static CSVWriter urlsWriter; 
	public static CSVWriter fetchedWriter; 
	public static CSVWriter visitedWriter;
	public static CSVWriter pageRankWriter;
	public static CrawlConfig config;

public static void main(String[] args) throws Exception {
	
	urlsWriter = new CSVWriter(new FileWriter("/Users/sandeepkumarvoolla/Desktop/webcrawling/output/urls.csv",true),',');
	String [] urls_Header = {"CRAWLED_URlS","TYPE_OF_URL"};	
	urlsWriter.writeNext(urls_Header);
	fetchedWriter = new CSVWriter(new FileWriter("/Users/sandeepkumarvoolla/Desktop/webcrawling/output/fetch.csv",true),',');
	String [] fetch_Header ={"FETCHED_URLS","HTTPS_STATUS_CODE"};
	fetchedWriter.writeNext(fetch_Header);
	visitedWriter = new CSVWriter(new FileWriter("/Users/sandeepkumarvoolla/Desktop/webcrawling/output/visit.csv",true),',');
	String [] visit_Header ={"VISITED_URLS","SIZE","NO_OF_OUTLINKS","TYPE_OF_CONTENT"};
	visitedWriter.writeNext(visit_Header);
	pageRankWriter = new CSVWriter(new FileWriter("/Users/sandeepkumarvoolla/Desktop/webcrawling/output/pageRank.csv",true),',');
	String [] page_Header ={"VISITED_URLS","NO_OUTGOING_LINKS"};
	pageRankWriter.writeNext(page_Header);
	int maxPagesToFetch = 5000;
		 int numberOfCrawlers = 10;
		  	 config = new CrawlConfig();
			 config.setCrawlStorageFolder( "/Users/sandeepkumarvoolla/Desktop/webcrawling/data/crawl");
			 config.setMaxDepthOfCrawling(5);
			// config.setPolitenessDelay(1000);
			 config.setUserAgentString("voollask@usc.edu crawler");
			 config.setMaxPagesToFetch(maxPagesToFetch);
			// config.setResumableCrawling(false);
			 config.setMaxDownloadSize(100000000);
			 config.setIncludeBinaryContentInCrawling(true);
			 	PageFetcher pageFetcher = new PageFetcher(config);
				RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
				RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
				CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
				controller.addSeed("http://priceschool.usc.edu/");
				//MyCrawler.configure("http://priceschool.usc.edu/", "/Users/sandeepkumarvoolla/Desktop/webcrawling/data/crawl");
				controller.start(MyCrawler.class, numberOfCrawlers);
			System.out.println("Total Number of fetches attempted : "+MyCrawler.fetches);
	        System.out.println("Total Number of fetches that are Successful : "+MyCrawler.fetches_succ);
	        System.out.println("Total Number of fetches that are Aborted : "+MyCrawler.fetches_aborted);
	        System.out.println("Total Number of fetches that are Failed : "+MyCrawler.fetches_failed);
	        System.out.println("Total Number of URLS Extracted : "+ MyCrawler.urls_extracted);
	        System.out.println("Total Number of Unique URLS :"+MyCrawler.uniqueURLSet.size());
	        System.out.println("Total Number of Unique URLS within school :" + MyCrawler.schoolSet.size());
	        System.out.println("Total Number of Unique URLS outside school :"+MyCrawler.uscSet.size());
	        System.out.println("Total Number of Unique URLS outside USC :"+MyCrawler.outUSCSet.size());
	        //System.out.println("Max Fetches : "+fetches_succ);
	        for(Integer i:MyCrawler.statusCodes.keySet()) {
	            Integer val = MyCrawler.statusCodes.get(i);
	            System.out.println(i+" "+val);
	        }
	        for(String i:MyCrawler.sizesOfFiles.keySet()) {
	            Integer val = MyCrawler.sizesOfFiles.get(i);
	            System.out.println(i+" "+val);
	        }

	        for(String i:MyCrawler.contenthash.keySet()) {
	            Integer val = MyCrawler.contenthash.get(i);
	            System.out.println(i+" "+val);
	        }
	urlsWriter.close();
	fetchedWriter.close();
	visitedWriter.close();
	pageRankWriter.close();
 }
}