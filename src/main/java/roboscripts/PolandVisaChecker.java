package roboscripts;

import org.openqa.selenium.By;
import org.openqa.selenium.WebDriver;
import org.openqa.selenium.firefox.FirefoxDriver;
import org.openqa.selenium.support.ui.ExpectedConditions;
import org.openqa.selenium.support.ui.Select;
import org.openqa.selenium.support.ui.WebDriverWait;

import java.util.Date;
import java.util.concurrent.TimeUnit;

/**
 * @author alre
 */
public class PolandVisaChecker implements Runnable {
    private WebDriver driver;
    private String visaCenterName;
    private String visaType;

    public PolandVisaChecker(String visaCenterName, String visaType) {
        this.visaCenterName = visaCenterName;
        this.visaType = visaType;
        setUp();
    }

    public void setUp() {
        driver = new FirefoxDriver();
        driver.manage().timeouts().implicitlyWait(5, TimeUnit.SECONDS);
        driver.get("http://www.polandvisa-ukraine.com/scheduleappointment_2.html");
    }

    @Override
    public void run() {
        try {
            goToFormChooseVisaCenter();

            while (true) {

                flow();

                switchToFrameIfPresent();
                final String lblMsg = new WebDriverWait(driver, 3).until(ExpectedConditions.presenceOfElementLocated(By.id("ctl00_plhMain_lblMsg"))).getText();

                System.out.println("[" + new Date().toString() + "] " + lblMsg);
                if (!lblMsg.startsWith("No date(s) available for appointment")) {
                    clickSubmit();
                    EmailUtils.sendEmail(visaCenterName + " visa is available!!!", "Hi, Alex. Let's go to submit your documents ASAP!!! The date: " + lblMsg);
                    while (true) ;

                } else {
                    clickCancel();
                }

                Thread.currentThread().sleep(7000);
            }
        } catch (Throwable e) {
            e.printStackTrace();
            driver.navigate().refresh();
            run();
//            sendEmail("Exception", e.getMessage());
        }


    }

    private void goToFormChooseVisaCenter() {
        switchToFrameIfPresent();
        driver.findElement(By.id("ctl00_plhMain_lnkSchApp")).click();

    }

    private void flow() {
        switchToFrameIfPresent();
        new Select(new WebDriverWait(driver, 3)
                .until(ExpectedConditions.presenceOfElementLocated(By.id("ctl00_plhMain_cboVAC"))))
                .selectByVisibleText(visaCenterName);

        switchToFrameIfPresent();

        new Select(new WebDriverWait(driver, 3)
                .until(ExpectedConditions.presenceOfElementLocated(By.id("ctl00_plhMain_cboPurpose"))))
                .selectByVisibleText("Подача документів");

        clickSubmit();

        switchToFrameIfPresent();

        new Select(new WebDriverWait(driver, 3)
                .until(ExpectedConditions.presenceOfElementLocated(By.id("ctl00_plhMain_cboVisaCategory"))))
                .selectByVisibleText(visaType);
    }

    private void clickCancel() {
        switchToFrameIfPresent();
        new WebDriverWait(driver, 3).until(ExpectedConditions.elementToBeClickable(By.id("ctl00_plhMain_btnCancel"))).click();
    }

    private void clickSubmit() {
        switchToFrameIfPresent();
        new WebDriverWait(driver, 3).until(ExpectedConditions.elementToBeClickable(By.id("ctl00_plhMain_btnSubmit"))).click();
    }

    private void switchToFrameIfPresent() {
//        System.out.println(driver.getCurrentUrl());
        if (!driver.getCurrentUrl().startsWith("https://www.vfsvisaonline.com/poland-ukraine-appointment")) {
            driver.switchTo().frame(0);
        }
    }

}
