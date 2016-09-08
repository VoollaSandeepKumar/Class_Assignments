import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.HashSet;
import java.util.Set;
import java.util.regex.Pattern;

import org.apache.http.HttpStatus;

import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.BinaryParseData;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

public class MyCrawler extends WebCrawler {
	
	public static long fetches =0;
    public static long fetches_succ =0;
    public static long fetches_aborted =0;
    public static long fetches_failed =0;
    public static long urls_extracted = 0;
    public static long maxFetch = 0;
    public static HashMap<Integer,Integer> statusCodes = new HashMap<Integer,Integer>();
    public static HashMap<String,Integer> contenthash = new HashMap<String,Integer>();
    public static Set<String> uniqueURLSet = new HashSet<String>();
    public static Set<String> schoolSet = new HashSet<String>();
    public static Set<String> uscSet = new HashSet<String>();
    public static Set<String> outUSCSet = new HashSet<String>();
    public static HashMap<String,Integer> sizesOfFiles = new HashMap<String,Integer>();
    

	private final static Pattern FILTERS = Pattern
			.compile(".*(\\.(html|doc|pdf|docx))$");

	/**
	 * This method receives two parameters. The first parameter is the page in
	 * which we have discovered this new url and the second parameter is the new
	 * url. You should implement this function to specify whether the given url
	 * should be crawled or not (based on your crawling logic). In this example,
	 * we are instructing the crawler to ignore urls that have css, js, git, ...
	 * extensions and to only accept urls that start with
	 * "http://www.viterbi.usc.edu/". In this case, we didn't need the
	 * referringPage parameter to make the decision.
	 */
	void do_nothing() {
		/*
		 * PageFetchResult result = null; PageFetcher fetcher = new
		 * PageFetcher(Controller.config); try {
		 * System.out.println(url.getURL()+"\n"); result =
		 * fetcher.fetchPage(url);
		 * 
		 * } catch (Exception e) { // TODO Auto-generated catch block
		 * e.printStackTrace(); } if(null != result) { } else {
		 * System.out.println("Error in creating page\n"); }
		 */
	}

	@Override
	public boolean shouldVisit(Page referringPage, WebURL url) {
		String href = url.getURL();
		String type = null;
		if (href.startsWith("http://priceschool.usc.edu/"))
		{
			type = "OK";
			//System.out.println(href);
			schoolSet.add(href);
		}	
		else if (href.contains("usc.edu")&&!href.startsWith("http://priceschool.usc.edu/"))
		{
			type = "USC";
			uscSet.add(href);
		}
		else
		{
			type = "outUSC";
			outUSCSet.add(href);
		}
		synchronized (Controller.urlsWriter) {
			String[] url_Values = { url.getURL(), type };
			Controller.urlsWriter.writeNext(url_Values);
		}
		urls_extracted++;
		uniqueURLSet.add(href);
		
		return href.startsWith("http://priceschool.usc.edu/");
	}

	@Override
	public void visit(Page page) {
		
		String url = page.getWebURL().getURL();		//int statusCode = page.getStatusCode();
		String[] contentType = page.getContentType().split(";");
		String content = page.getContentType();
		String temp = url;
		temp = temp.replaceFirst("//", "@@");
		temp = temp.replaceAll("/", "@@@");
		byte[] contentdata = page.getContentData();
		updateSizes(contentdata);
		Set<WebURL> links = null;
		if (FILTERS.matcher(url).matches()) {
			if (page.getParseData() instanceof HtmlParseData
					|| page.getParseData() instanceof BinaryParseData) {
				if(!contenthash.containsKey(contentType[0]))
					contenthash.put(contentType[0], 1);
		        else
		        	contenthash.put(contentType[0], contenthash.get(contentType[0])+1);

				if (page.getParseData() instanceof BinaryParseData) {
					BinaryParseData binaryParseData;
					binaryParseData = (BinaryParseData) page.getParseData();
					links = binaryParseData.getOutgoingUrls();
					writeToLocal(page, temp);

				} else {
					HtmlParseData htmlParseData = (HtmlParseData) page
							.getParseData();
					// String text = htmlParseData.getText();
					//String html = htmlParseData.getHtml();
					links = htmlParseData.getOutgoingUrls();
					writeToLocal(page, temp);
				}
			}
			synchronized (Controller.visitedWriter) {
				String[] visit_Values = { url,
						Integer.toString(contentdata.length),
						Integer.toString(links.size()), contentType[0] };
					String outgoingurls = Arrays.toString(links.toArray());
				String [] pageValues = {url,outgoingurls}; 
				Controller.visitedWriter.writeNext(visit_Values);
				Controller.pageRankWriter.writeNext(pageValues);
			}

		} else if (content.contains("text/html")
				|| content.contains("application/msword")
				|| content.contains("application/vnd.openxmlformats-officedocument.wordprocessingml.document")
				|| content.contains("application/pdf")) {
			if(!contenthash.containsKey(contentType[0]))
				contenthash.put(contentType[0], 1);
	        else
	        	contenthash.put(contentType[0], contenthash.get(contentType[0])+1);
			if (page.getParseData() instanceof HtmlParseData
					|| page.getParseData() instanceof BinaryParseData) {

				if (page.getParseData() instanceof BinaryParseData) {
					BinaryParseData binaryParseData;
					binaryParseData = (BinaryParseData) page.getParseData();
					links = binaryParseData.getOutgoingUrls();
					writeToLocal(page, temp);

				} else {
					HtmlParseData htmlParseData = (HtmlParseData) page
							.getParseData();
					// String text = htmlParseData.getText();
					//String html = htmlParseData.getHtml();
					links = htmlParseData.getOutgoingUrls();
					writeToLocal(page, temp);
				}
			}
			synchronized (Controller.visitedWriter) {
				String[] visit_Values = { url,
						Integer.toString(contentdata.length),
						Integer.toString(links.size()), contentType[0] };
				String outgoingurls = Arrays.toString(links.toArray());
				String [] pageValues = {url,outgoingurls}; 
				Controller.visitedWriter.writeNext(visit_Values);
				Controller.pageRankWriter.writeNext(pageValues);
			}

		} else {
			
		}
		
		
	}
	
	 private void updateSizes(byte[] contentdata) {
		// TODO Auto-generated method stub
		 
		 if(contentdata.length<1024) {
             if(!sizesOfFiles.containsKey("<1KB"))
                 sizesOfFiles.put("<1KB", 1);
             else
                 sizesOfFiles.put("<1KB", sizesOfFiles.get("<1KB")+1);
         }
         else if(contentdata.length>=1024 && contentdata.length<10240) {
             if(!sizesOfFiles.containsKey("1KB ~ <10KB"))
                 sizesOfFiles.put("1KB ~ <10KB", 1);
             else
                 sizesOfFiles.put("1KB ~ <10KB", sizesOfFiles.get("1KB ~ <10KB")+1);
         }
         else if(contentdata.length>=10240 && contentdata.length<102400) {
             if(!sizesOfFiles.containsKey("10KB ~ <100KB"))
                 sizesOfFiles.put("10KB ~ <100KB", 1);
             else
                 sizesOfFiles.put("10KB ~ <100KB", sizesOfFiles.get("10KB ~ <100KB")+1);
         }
         else if(contentdata.length>=102400 && contentdata.length<1048576) {
             if(!sizesOfFiles.containsKey("100KB ~ <1MB"))
                 sizesOfFiles.put("100KB ~ <1MB", 1);
             else
                 sizesOfFiles.put("100KB ~ <1MB", sizesOfFiles.get("100KB ~ <1MB")+1);
         }
         else {
             if(!sizesOfFiles.containsKey(">1MB"))
                 sizesOfFiles.put(">1MB", 1);
             else
                 sizesOfFiles.put(">1MB", sizesOfFiles.get(">1MB")+1);
         }
		
	}

	@Override
	    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
	        //do nothing
	        //-------------------------Statistic nummber 2---------------------------------------
		 fetches++;
		 synchronized (Controller.fetchedWriter) {
			 String [] values = {webUrl.getURL(),Integer.toString(statusCode)};
			 Controller.fetchedWriter.writeNext(values);
		}
		 
	        if(statusCode!=HttpStatus.SC_OK) {
	        	System.out.println(statusCode);
	            if(statusCode >= 300 && statusCode < 400){
	                fetches_aborted++;
	            }
	            else {
	                fetches_failed++;
	            }
	        }
	        if (statusCode !=200)
	        {
	        	 if(!statusCodes.containsKey(statusCode))
	 	            statusCodes.put(statusCode, 1);
	 	        else
	 	            statusCodes.put(statusCode, statusCodes.get(statusCode)+1);
	        }
	        else
	        {
	        	fetches_succ++;
	    		if(!statusCodes.containsKey(statusCode))
	    	            statusCodes.put(statusCode, 1);
	    	        else
	    	            statusCodes.put(statusCode, statusCodes.get(statusCode)+1);
	        }
	        /*if(statusCode==301) {
	            System.out.println(statusCode +" ---- "+ webUrl.getURL());
	        }*/
	    }

	private void writeToLocal(Page page, String temp) {
		try {
			FileOutputStream fos = new FileOutputStream(
					"/Users/sandeepkumarvoolla/Desktop/webcrawling/data/crawl/"
							+ temp);
			fos.write(page.getContentData());
			fos.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
	}

}
