package qa.guru;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.FileDownloadMode;
import org.apache.commons.io.FileUtils;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;

import static com.codeborne.selenide.Condition.text;
import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class SelenideFileTest {

    @BeforeAll
    static void configure() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        Configuration.browser = "chrome";
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1920x1080";
    }
    //static {Configuration.fileDownload = FileDownloadMode.PROXY;} Использовать если нет атрибута href

    @DisplayName("Demonstration test how downloaded file")
    @Test
    void selenideFileDownloadTest() throws Exception {
        open("https://github.com/valgoncharov/avtotests-test/blob/main/README.md");
        File downloadedFile = $("#raw-url").download();
        //String contents = FileUtils.readFileToString(downloadedFile, StandardCharsets.UTF_8);
        try (InputStream is = new FileInputStream(downloadedFile)) {
            byte[] fileSource = is.readAllBytes();
            String fileContent = new String(fileSource, StandardCharsets.UTF_8);
            assertThat(fileContent).contains("New file");
            is.close();
        }
    }

    @DisplayName("Demonstration how uploaded file")
    @Test
    void uploadFile() throws Exception{
        open("https://fineuploader.com/demos.html");
        //File file = new File("build/downloads/ac3c86b7-d727-470b-b129-d25dc40c863c/README.md");
        //Так делать не надо, хранить файлы в папке resources
        $("input[type='file']").uploadFromClasspath("cat01.jpg");
        $("div.qq-file-info").shouldHave(text("cat01.jpg"));
    }
}
