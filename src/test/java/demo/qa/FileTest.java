package demo.qa;

import com.codeborne.pdftest.PDF;
import com.codeborne.xlstest.XLS;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.opencsv.CSVReader;
import model.Student;
import org.junit.jupiter.api.Test;

import java.io.File;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.nio.charset.StandardCharsets;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipFile;
import java.util.zip.ZipInputStream;

import static org.assertj.core.api.Assertions.assertThat;


public class FileTest {

    @Test
    void JsonFileTest () throws Exception {
        File file = new File("src/test/resources/student.json");
        ObjectMapper objectMapper = new ObjectMapper();
        Student student = objectMapper.readValue(file, Student.class);
        assertThat(student.firstname).isEqualTo("Alexey");
        assertThat(student.lastname).isEqualTo("Orlov");
        assertThat(student.age).isEqualTo(26);
        assertThat(student.email).isEqualTo("aorlov@example.ru");
        assertThat(student.homework[1]).isEqualTo("Lesson2");
        assertThat(student.teacher).isEqualTo("Dmitrii Tuchs");
    }


    ClassLoader cl = FileTest.class.getClassLoader();
    @Test
    void ReadingArchiveTest () throws Exception {
        ZipFile zf = new ZipFile(new File("src/test/resources/Desktop.zip"));
        try (ZipInputStream is = new ZipInputStream(cl.getResourceAsStream("Desktop.zip"))) {
            ZipEntry entry;
            while ((entry = is.getNextEntry()) != null) {
                switch (entry.getName()) {
                    case "import_empl.csv":
                        try (InputStream inputStream = zf.getInputStream(entry);
                             CSVReader reader = new CSVReader(new InputStreamReader(inputStream, StandardCharsets.UTF_8))) {
                            List<String[]> content = reader.readAll();
                            String[] row = content.get(0);
                            String searchWords = row[0];
                            assertThat(searchWords).isEqualTo("Name;ID;email");
                        }
                        break;
                    case "import_ou.xlsx":
                        try (InputStream inputStream = zf.getInputStream(entry)) {
                            XLS xls = new XLS(inputStream);
                            assertThat(
                                    xls.excel.getSheetAt(0)
                                            .getRow(0)
                                            .getCell(0)
                                            .getStringCellValue()
                            ).isEqualTo("Name");
                        }
                        break;
                    case "test.pdf":
                        try (InputStream inputStream = zf.getInputStream(entry)) {
                            PDF pdf = new PDF(inputStream);
                            assertThat(pdf.author).isEqualTo("Yukon Department of Education");
                        }
                        break;
                }
            }
        }
    }
}

