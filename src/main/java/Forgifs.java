import com.google.common.base.CharMatcher;
import com.google.common.base.Charsets;
import com.google.common.io.Files;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.htmlunit.HtmlUnitDriver;
import org.unbescape.html.HtmlEscape;
import org.w3c.dom.Document;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

import java.io.File;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.concurrent.TimeUnit;

import javax.xml.xpath.XPath;
import javax.xml.xpath.XPathConstants;
import javax.xml.xpath.XPathExpressionException;
import javax.xml.xpath.XPathFactory;

public class Forgifs {


    static WebDriver driver = null;
        static XPath xPath = XPathFactory.newInstance().newXPath();

    public static void getForgifs(String[] args) throws IOException, InterruptedException, XPathExpressionException {
        System.setProperty("webdriver.chrome.driver", new File(".").getCanonicalPath() + "/chromedriver_2.25");
//        driver = new ChromeDriver();
        driver = new HtmlUnitDriver();

        driver.manage().timeouts().implicitlyWait(0, TimeUnit.MILLISECONDS);

        List<String> galleryFirstImages = Arrays.asList(
                "http://forgifs.com/gallery/v/Animals/Baboon-amazed-by-magic.gif.html",
                "http://forgifs.com/gallery/v/Funny/Snow-sledding-fail.gif.html",
                "http://forgifs.com/gallery/v/Dogs/Fluffy-dog-air-swimming.gif.html",
                "http://forgifs.com/gallery/v/Cool/Dad-reflexes-baby-catch.gif.html",
                "http://forgifs.com/gallery/v/Cats/Sleeping-cat-eyes-dilate.gif.html",
                "http://forgifs.com/gallery/v/Sports/Volleyball-headshot-score.gif.html");

        for (String firstUrl : galleryFirstImages) {

            // get image title - remove hyphens - single tag
            //      h2 -
            //get image description - single tag
            //      p class: "giDescription"
            // get image source
            //      only img in div with id: gsImageView
            // get page url
            // get "Keywords" - separate tags  - remove "animated" and "gif"
            //      text of links in div with class: block-keyalbum-KeywordLinks


            //click "next" link (by link text)
            //       if "next" link doens't exist, go on to next galleryFirstImage

            driver.get(firstUrl);

            boolean notFinishedGalleryYet = true;
            while (notFinishedGalleryYet) {
                Document doc = Helper.getWebpageDocument_fromSource(driver.getPageSource());

                String imageTitle = getImageTitle(doc);
                String imageDescription = getImageDescription(doc);
                String imageSource = getImageSource(doc);
                String pageUrl = driver.getCurrentUrl();
                List<String> keywords = getKeywords(doc);

                String returner = "http://forgifs.com" + imageSource + "\t" + pageUrl + "\t" + imageTitle + "\t" + imageDescription + "\t" + String.join("\t", keywords) + System.getProperty("line.separator");
                System.out.print(returner);
                Files.append(returner, new File("./forgifsData.txt"), Charsets.UTF_8);

                try {
                    driver.findElement(By.linkText("next")).click();
                } catch (Exception e) {
                    notFinishedGalleryYet = false;
                }
            }


        }


    }

    private static List<String> getKeywords(Document doc) throws XPathExpressionException {

        NodeList keywordLinks = (NodeList) xPath.evaluate("//div[@class='block-keyalbum-KeywordLinks']/a", doc, XPathConstants.NODESET);

        List<String> tags = new ArrayList<>();

        for (int j = 0; j < keywordLinks.getLength(); j++) {
            Node link = keywordLinks.item(j);
            String text = link.getTextContent();
            text = HtmlEscape.unescapeHtml(text).toLowerCase().trim();
            text = CharMatcher.WHITESPACE.and(CharMatcher.isNot(' ')).removeFrom(text);
            text = text.trim();
            if (!text.equals("animated") && !text.equals("gif") && !text.equals("animated gif"))
                tags.add(text);
        }
        return tags;
    }

    private static String getImageSource(Document doc) throws XPathExpressionException {
        return (String) xPath.evaluate("//div[@id='gsImageView']//img/@src", doc, XPathConstants.STRING);
    }

    private static String getImageDescription(Document doc) throws XPathExpressionException {
        String text = (String) xPath.evaluate("//p[@class='giDescription']/text()", doc, XPathConstants.STRING);
        text = HtmlEscape.unescapeHtml(text).toLowerCase().trim();
        text = CharMatcher.WHITESPACE.and(CharMatcher.isNot(' ')).removeFrom(text);
        text = text.trim();
        return text;
    }

    private static String getImageTitle(Document doc) throws XPathExpressionException {
        String text = (String) xPath.evaluate("//h2/text()", doc, XPathConstants.STRING);
        text = text.replace("-", " ").toLowerCase().trim();
        text = HtmlEscape.unescapeHtml(text).toLowerCase().trim();
        text = CharMatcher.WHITESPACE.and(CharMatcher.isNot(' ')).removeFrom(text);
        text = text.trim();
        return text;


    }
}
