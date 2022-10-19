package qa.guru;

import com.codeborne.pdftest.PDF;
import com.codeborne.selenide.Configuration;
import com.codeborne.xlstest.XLS;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static com.codeborne.selenide.Selenide.$;
import static com.codeborne.selenide.Selenide.open;
import static org.assertj.core.api.Assertions.assertThat;

public class FileParseTest {

    ClassLoader cl = FileParseTest.class.getClassLoader();//Позволяет ходит в папку resources

    @BeforeAll
    static void configure() {
        System.setProperty("webdriver.chrome.driver", "C:\\chromedriver\\chromedriver.exe");
        Configuration.browser = "chrome";
        Configuration.holdBrowserOpen = true;
        Configuration.browserSize = "1920x1080";
    }

    @DisplayName("Demonstration how use pdf")
    @Test
    void pdfTest() throws Exception {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File downlodedFile = $("a[href*='junit-user-guide-5.9.1.pdf']").download();
        PDF pdf = new PDF(downlodedFile);
        assertThat(pdf.author).contains("Sam Brannen");
    }

    @DisplayName("Demonstration how use pdf")
    @Test
    void pdfTestMy() throws Exception {
        open("https://junit.org/junit5/docs/current/user-guide/");
        File downlodedFile = $("a[href*='junit-user-guide-5.9.1.pdf']").download();
        PDF pdf = new PDF(downlodedFile);
        assertThat(pdf.author).contains("Sam Brannen");
    }

    @DisplayName("Demonstration how use xls")
    @Test
    void xlsTest() throws Exception {
        InputStream is = cl.getResourceAsStream("Bug report Stepik.xlsx");
        XLS xsl = new XLS(is);
        assertThat(xsl.excel
                .getSheetAt(0)
                .getRow(0)
                .getCell(1)
                .getStringCellValue()).isEqualTo("Summary");
    }

    @DisplayName("Demonstration how use csv")
    @Test
    void csvTest() throws Exception {
        InputStream is = cl.getResourceAsStream("TestData/qa_test.csv");
        CSVReader reader = new CSVReader(new InputStreamReader(is));
        List<String[]> content = reader.readAll();
        String[] row = content.get(1);
        assertThat(row[0]).isEqualTo("Valentine");
        assertThat(row[1]).isEqualTo("Tester");
    }

    @DisplayName("Demonstration how use zip file")
    @Test
    void zipTest() throws IOException {
        ZipFile zf = new ZipFile(new File("auto-test-guru.zip"));
        ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("auto-test-guru.zip"));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null) {
            if (entry.getName().contains("ЧЛ ТРЕБОВАНИЙ.pdf")) {
                try (InputStream inputStream = zf.getInputStream(entry)) {
                    PDF pdf = new PDF(inputStream);
                    assertThat(pdf.text).contains("Синхронизация с системой 1С");
                }

            }

        }
    }

        @DisplayName("Try read file")
        @Test
        void readZipFileTest() throws IOException{
            try {
                FileInputStream fis = new FileInputStream("src/test/resources/auto-test-guru.zip");
                ZipInputStream zis = new ZipInputStream(fis);
                ZipEntry ze;
                while((ze = zis.getNextEntry())!=null){
                    System.out.println(ze.getName());
                    zis.closeEntry();
                }
                zis.close();

            }catch (FileNotFoundException e){
                e.printStackTrace();
            }catch (IOException e){
                e.printStackTrace();
            }
        }
    }



