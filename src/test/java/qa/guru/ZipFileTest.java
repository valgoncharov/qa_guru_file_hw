package qa.guru;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import qa.guru.model.Films;
import static org.assertj.core.api.Assertions.assertThat;
import java.io.*;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

public class ZipFileTest {

    ClassLoader cl = FileParseTest.class.getClassLoader();//Позволяет ходит в папку resources

    @DisplayName("Test get pdf file from zip")
    @Test
    void pdfFileGetFromZipTest() throws Exception{
        ZipFile zf = new ZipFile(new File("src/test/resources/auto-test-guru.zip"));
        ZipInputStream is = new ZipInputStream((cl.getResourceAsStream("auto-test-guru.zip")));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null){
            if ((entry.getName().contains("ЧЛ ТРЕБОВАНИЙ.pdf"))){
                try(InputStream inputStream = zf.getInputStream(entry)) {
                    PDF pdf = new PDF(inputStream);
                    assertThat(pdf.text).contains("Синхронизация с системой 1С");
                }
            }
        }
    }

    @DisplayName("Test get xls file from zip")
    @Test
    void xlsFileGetFromZipTest() throws Exception{
        ZipFile zf = new ZipFile(new File("src/test/resources/auto-test-guru.zip"));
        ZipInputStream is = new ZipInputStream((cl.getResourceAsStream("auto-test-guru.zip")));
        ZipEntry entry;
        while ((entry = is.getNextEntry() != null)){
            if (entry.getName().contains("Bug report Stepik.xlsx")){
                try (InputStream inputStream = zf.getInputStream(entry)){
                    XLS xls = new XLS(inputStream);
                    assertThat(xls.excel
                            .getSheetAt(0)
                            .getRow(0)
                            .getCell(1)
                            .getStringCellValue()).isEqualTo("Summary");
                }
            }
        }
    }

    @DisplayName("Test get csv file from zip")
    @Test
    void csvFileGetFromZipTest() throws Exception{
        ZipFile zf = new ZipFile(new File("src/test/resources/auto-test-guru.zip"));
        ZipInputStream is = new ZipInputStream((cl.getResourceAsStream("auto-test-guru.zip")));
        ZipEntry entry;
        while ((entry = is.getNextEntry()) != null){
            if (entry.getName().contains("qa_test.csv")){
                try (InputStream inputStream = zf.getInputStream(entry)){
                    CSVReader reader = new CSVReader((new InputStreamReader(inputStream));
                    List<String[]> content = reader.readAll();
                    String[] row = content.get(1);
                    assertThat(row[0]).isEqualTo("Valentine");
                    assertThat(row[1]).isEqualTo("Tester");
                }
            }
        }
    }

    @DisplayName("Test json file")
    @Test
    void jsonFileTest() throws Exception{
        File file = new File("src/test/resources/Films.json");
        ObjectMapper objectMapper = new ObjectMapper();
        Films films = objectMapper.readValue(file, Films.class);
        assertThat(films.title).isEqualTo("The Avengers");
        assertThat(films.actors).isEqualTo("Robert Downey Jr.");
        assertThat(films.language).isEqualTo("Russian");
        assertThat(films.respons).isEqualTo(true);
        assertThat(films.year).isEqualTo(2012);
        assertThat(films.awards.world).isEqualTo(5);
        assertThat(films.rental.get(2)).isEqualTo("China");
        assertThat(films.awards.country).isEqualTo("USA");
    }
}
