package demo.qa;

import com.fasterxml.jackson.databind.ObjectMapper;
import model.Student;
import org.junit.jupiter.api.Test;

import java.io.File;

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
}
