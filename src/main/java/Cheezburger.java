import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class Cheezburger {


    static WebDriver driver = null;
    static XPath xPath = XPathFactory.newInstance().newXPath();


    //TODO - put this in its own class.  Next: scrape reddit gifs!


    public static void main(String[] args) throws IOException, InterruptedException, XPathExpressionException {
        System.setProperty("webdriver.chrome.driver", new File(".").getCanonicalPath() + "/chromedriver_2.25");
//        driver = new ChromeDriver();
        driver = new HtmlUnitDriver();

        driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);

        //get gif pane:  //a[@class='js-img-link']

        //per pane:

        // image title - make into a tag
        //      //a[@class='js-img-link']/@title

        // tags:
        //     //a[@class='js-img-link']//img/@alt

        // img source:
        //     //a[@class='js-img-link']//img/@src

        // content source:
        //      //a[@class='js-img-link']/@href

        //click next page
        //      "a" with class: "glyphicon glyphicon-chevron-right"

        driver.get("http://memebase.cheezburger.com/senorgif");

        boolean notFinishedGalleryYet = true;

        while (notFinishedGalleryYet) {
            Document doc = Helper.getWebpageDocument_fromSource(driver.getPageSource());

            NodeList aElements = (NodeList) xPath.evaluate("//a[@class='js-img-link']", doc, XPathConstants.NODESET);


            for (int j = 0; j < aElements.getLength(); j++) {
                Node aElement = aElements.item(j);

                String title = ((String) xPath.evaluate("./@title", aElement, XPathConstants.STRING));
                String contentSource = (String) xPath.evaluate("./@href", aElement, XPathConstants.STRING);
                String tags = ((String) xPath.evaluate(".//img/@alt", aElement, XPathConstants.STRING));
                List<String> tagsList = Arrays.asList(tags.split(","));
                String source = (String) xPath.evaluate(".//img/@src", aElement, XPathConstants.STRING);

//
//                System.out.println(title);
//                System.out.println(tagsList);
//                System.out.println(contentSource);
//                System.out.println(source);
//                System.out.println();


                String returner = source + "\t" + contentSource + "\t" + title + "\t" + String.join("\t", tagsList) + System.getProperty("line.separator");
                System.out.print(returner);
                Files.append(returner, new File("./cheezburgerData.txt"), Charsets.UTF_8);
            }


            try {
                driver.findElement(By.xpath("//a[@class='glyphicon glyphicon-chevron-right']")).click();
            } catch (Exception e) {
                notFinishedGalleryYet = false;
            }
        }
    }


}
