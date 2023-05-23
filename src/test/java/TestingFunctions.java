import main.MainClass;
import org.junit.Assert;
import org.junit.Test;
import org.openqa.selenium.By;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.time.Duration;
import java.util.List;

import static org.openqa.selenium.support.ui.ExpectedConditions.visibilityOfElementLocated;

public class TestingFunctions extends MainClass {

    List<String> relatedElements;
    String startOfElement;
    String selectedCountryPath;
    String[] correctPrices;
    String correctCurrency;
    String selectedCountryLitePrice;
    String selectedCountryClassicPrice;
    String selectedCountryPremiumPrice;
    String selectedCountryLiteCurrency;
    String selectedCountryClassicCurrency;
    String selectedCountryPremiumCurrency;
    String[] selectedCountryPricesList;
    String[] selectedCountryCurrenciesList;
    WebDriverWait wait = new WebDriverWait(driver, Duration.ofMillis(50000));
    @Test
    public void checkMainElements () throws Exception {

        startOfElement = "STC";

        relatedElements = readTags(startOfElement, mainElementsPageXMLFile);

        for (int m = 0; m < relatedElements.size(); m++) {

            driver.findElement(By.xpath(readLocator(mainElementsPageXMLFile, relatedElements.get(m))));

        }

    }

    @Test
    public void checkSelectedCountryElements() throws Exception {

        dataController();

        relatedElements = readTags(startOfElement , selectedCountryPath);

        for (int m = 0; m < relatedElements.size(); m++) {

            driver.findElement(By.xpath(readLocator(selectedCountryPath, relatedElements.get(m))));

        }

        selectedCountryLitePrice = driver.findElement(By.xpath(readLocator(selectedCountryPath , startOfElement + "LitePrice"))).getAttribute("innerHTML");
        selectedCountryClassicPrice = driver.findElement(By.xpath(readLocator(selectedCountryPath , startOfElement + "ClassicPrice"))).getAttribute("innerHTML");
        selectedCountryPremiumPrice = driver.findElement(By.xpath(readLocator(selectedCountryPath , startOfElement + "PremiumPrice"))).getAttribute("innerHTML");

        selectedCountryPricesList = new String[]{selectedCountryLitePrice, selectedCountryClassicPrice, selectedCountryPremiumPrice};

        selectedCountryLiteCurrency = driver.findElement(By.xpath(readLocator(selectedCountryPath , startOfElement + "LiteCurrency"))).getAttribute("innerHTML");
        selectedCountryClassicCurrency = driver.findElement(By.xpath(readLocator(selectedCountryPath , startOfElement + "ClassicCurrency"))).getAttribute("innerHTML");
        selectedCountryPremiumCurrency = driver.findElement(By.xpath(readLocator(selectedCountryPath , startOfElement + "PremiumCurrency"))).getAttribute("innerHTML");

        selectedCountryCurrenciesList = new String[]{selectedCountryLiteCurrency, selectedCountryClassicCurrency, selectedCountryPremiumCurrency};

        for(int i = 0 ; i < correctPrices.length ; i++){

            Assert.assertEquals(correctPrices[i] , selectedCountryPricesList[i]);
            Assert.assertEquals(correctCurrency , selectedCountryCurrenciesList[i]);

        }


    }

    public void dataController() throws Exception {

        driver.findElement(By.xpath(readLocator(mainElementsPageXMLFile , "STCCountrySelector"))).click();

        wait.until(visibilityOfElementLocated(By.xpath(readLocator(mainElementsPageXMLFile, "CSPTitle"))));

        String countryPopupElements = "CSP";

        relatedElements = readTags(countryPopupElements, mainElementsPageXMLFile);

        for (int m = 0; m < relatedElements.size(); m++) {

            driver.findElement(By.xpath(readLocator(mainElementsPageXMLFile, relatedElements.get(m))));

        }

        if(selectedCountry.equals("Bahrain")){

            driver.findElement(By.xpath(readLocator(mainElementsPageXMLFile , "CSPBahrainButton"))).click();

            Thread.sleep(1000);

            selectedCountryPath = elementsBAHPageXMLFile;

            startOfElement = "STCBAH";

            correctPrices = new String[]{"2", "3", "6"};

            correctCurrency = "BHD/month";

        } else if (selectedCountry.equals("KSA")) {

            driver.findElement(By.xpath(readLocator(mainElementsPageXMLFile , "CSPKSAButton"))).click();

            selectedCountryPath = elementsKSAPageXMLFile;

            startOfElement = "STCKSA";

            correctPrices = new String[]{"15", "25", "60"};

            correctCurrency = "SAR/month";

        } else if (selectedCountry.equals("Kuwait")) {

            driver.findElement(By.xpath(readLocator(mainElementsPageXMLFile , "CSPKuwaitButton"))).click();

            selectedCountryPath = elementsKUWPageXMLFile;

            startOfElement = "STCKAT";

            correctPrices = new String[]{"1.2", "2.5", "4.8"};

            correctCurrency = "KWD/month";

        }

    }
}
