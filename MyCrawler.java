import edu.uci.ics.crawler4j.crawler.Page;
import edu.uci.ics.crawler4j.crawler.WebCrawler;
import edu.uci.ics.crawler4j.parser.HtmlParseData;
import edu.uci.ics.crawler4j.url.WebURL;

import java.util.*;
import java.util.regex.Pattern;

public class MyCrawler extends WebCrawler {
    private StringBuilder sbArr1 = new StringBuilder();
    private StringBuilder sbArr2 = new StringBuilder();
    private StringBuilder sbArr3 = new StringBuilder();

    private List<StringBuilder> sbList = new ArrayList<>();

    private final static Pattern FILTERS = Pattern.compile(".*(\\.(css|js|json"
          + "|mp2|mp3|zip|gz))$");

    private HashSet<String> seen = new HashSet<>();


    @Override
    protected WebURL handleUrlBeforeProcess(WebURL curURL) {
        return curURL;
    }

    // fetch info collecting
    @Override
    protected void handlePageStatusCode(WebURL webUrl, int statusCode, String statusDescription) {
        String url = webUrl.getURL().replaceAll(",", "_");
        sbArr1.append(url);
        sbArr1.append(",");
        sbArr1.append(statusCode);
        sbArr1.append("\n");
        seen.add(url);
    }

/**
 * This method receives two parameters. The first parameter is the page
 * in which we have discovered this new url and the second parameter is
 * the new url. You should implement this function to specify whether
 * the given url should be crawled or not (based on your crawling logic).
 * In this example, we are instructing the crawler to ignore urls that
 * have css, js, git, ... extensions and to only accept urls that start
 * with "http://www.viterbi.usc.edu/". In this case, we didn't need the
 * referringPage parameter to make the decision.
 */
    @Override
    public boolean shouldVisit(Page referringPage, WebURL url) {
        String href = url.getURL().toLowerCase().replaceAll(",", "_");
        boolean goodsites = href.startsWith("https://www.nytimes.com/")
                            || href.startsWith("https://nytimes.com/")
                            || href.startsWith("http://www.nytimes.com/")
                            || href.startsWith("http://nytimes.com/");

        sbArr3.append(url.getURL());
        sbArr3.append(",");
        if(goodsites) {
            sbArr3.append("OK");
        } else {
            sbArr3.append("N_OK");
        }
        sbArr3.append("\n");

        boolean hasNotSeen = !seen.contains(href);
        return !FILTERS.matcher(href).matches()
              && goodsites && hasNotSeen;
    }
    /**
     * This function is called when a page is fetched and ready
     * to be processed by your program.
     */
    @Override
    public void visit(Page page) {
        String url = page.getWebURL().getURL().toLowerCase().replaceAll(",", "_");
        int size = page.getContentData().length;

        String contentType = page.getContentType().split(";")[0];
        boolean goodToVisit = contentType.contains("html") | contentType.contains("image") | contentType.contains("pdf") | contentType.contains("doc");
        if(!goodToVisit) {
            return;
        }

        sbArr2.append(url);
        sbArr2.append(",");
        sbArr2.append(size);
        sbArr2.append(",");
        int noOutLinks = 0;

        if (page.getParseData() instanceof HtmlParseData) {
            HtmlParseData htmlParseData = (HtmlParseData) page.getParseData();
            Set<WebURL> links = htmlParseData.getOutgoingUrls();
            noOutLinks += links.size();
        }
        sbArr2.append(noOutLinks);
        sbArr2.append(",");
        sbArr2.append(contentType);
        sbArr2.append("\n");
    }

    /**
     * This function is called by controller to get the local data of this crawler when job is
     * finished
     */
    @Override
    public Object getMyLocalData() {
        sbList.add(sbArr1);
        sbList.add(sbArr2);
        sbList.add(sbArr3);
        return sbList;
    }


}