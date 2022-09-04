import edu.uci.ics.crawler4j.crawler.CrawlConfig;
import edu.uci.ics.crawler4j.crawler.CrawlController;
import edu.uci.ics.crawler4j.fetcher.PageFetcher;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtConfig;
import edu.uci.ics.crawler4j.robotstxt.RobotstxtServer;

import java.io.File;
import java.io.PrintWriter;
import java.util.ArrayList;


public class Controller {
    public static void main(String[] args) throws Exception {
        String crawlStorageFolder = "/Users/zhengzhang/Desktop/CSCI572/HW/HW2/crawlStorageFolder";
        int numberOfCrawlers = 7;
        CrawlConfig config = new CrawlConfig();
        config.setCrawlStorageFolder(crawlStorageFolder);

        // config setting
        config.setMaxPagesToFetch(20000);
        config.setMaxDepthOfCrawling(16);
        config.setIncludeBinaryContentInCrawling(true);
        config.setPolitenessDelay(200);
        //config.setUserAgentString("'Mozilla/5.0 (Windows NT 10.0; Win64; x64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/70.0.3538.77 Safari/537.36'");



        /*
         * Instantiate the controller for this crawl.
         */
        PageFetcher pageFetcher = new PageFetcher(config);
        RobotstxtConfig robotstxtConfig = new RobotstxtConfig();
        RobotstxtServer robotstxtServer = new RobotstxtServer(robotstxtConfig, pageFetcher);
        CrawlController controller = new CrawlController(config, pageFetcher, robotstxtServer);
        /*
         * For each crawl, you need to add some seed urls. These are the first
         * URLs that are fetched and then the crawler starts following links
         * which are found in these pages
         */
        controller.addSeed("https://www.nytimes.com/");
        /*
         * Start the crawl. This is a blocking operation, meaning that your code
         * will reach the line after this only when crawling is finished.
         */

        controller.start(MyCrawler.class, numberOfCrawlers);

        //File file1 = new File("/Users/zhengzhang/Desktop/CSCI572/HW/HW2/crawlStorageFolder/fetch_NYT.csv");
        //File file2 = new File("/Users/zhengzhang/Desktop/CSCI572/HW/HW2/crawlStorageFolder/visit_NYT.csv");
        //File file3 = new File("/Users/zhengzhang/Desktop/CSCI572/HW/HW2/crawlStorageFolder/urls_NYT.csv");

        StringBuilder sb1 = new StringBuilder();
        sb1.append("URL,");
        sb1.append("Status\n");

        StringBuilder sb2 = new StringBuilder();
        sb2.append("URL,");
        sb2.append("Size in Bytes,");
        sb2.append("outlinks,");
        sb2.append("content type\n");

        StringBuilder sb3 = new StringBuilder();
        sb3.append("URL,");
        sb3.append("Indicator\n");

        for(Object ob:controller.getCrawlersLocalData()) {
            ArrayList<StringBuilder> sb = (ArrayList<StringBuilder>) ob;
            sb1.append(sb.get(0));
            sb2.append(sb.get(1));
            sb3.append(sb.get(2));
        }
        PrintWriter csv1 = new PrintWriter("/Users/zhengzhang/Desktop/CSCI572/HW/HW2/crawlStorageFolder/fetch_NYT.csv");
        String fetchInfo = sb1.toString();
        csv1.write(fetchInfo);
        csv1.close();

        PrintWriter csv2 = new PrintWriter("/Users/zhengzhang/Desktop/CSCI572/HW/HW2/crawlStorageFolder/visit_NYT.csv");
        csv2.write(sb2.toString());
        csv2.close();

        PrintWriter csv3 = new PrintWriter("/Users/zhengzhang/Desktop/CSCI572/HW/HW2/crawlStorageFolder/urls_NYT.csv");
        csv3.write(sb3.toString());
        csv3.close();


    }
}