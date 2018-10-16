package br.ufal.ic.academico.resources;

import br.ufal.ic.academico.DTOs.StudentDTO;
import br.ufal.ic.academico.models.*;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.platform.commons.util.PreconditionViolationException;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class StudentResourceTest extends IntegrationTest{
    @Test
    void getAllTest() {
        List<Student> saved = RULE.client().target(url + "student")
                .request()
                .get(new GenericType<List<Student>>() {});

        assertEquals(0, saved.size(), "Testes não iniciaram com Banco de dados vazio");
    }

    @Test
    void saveTest() {
        Student d = new Student("Gabriel");

        StudentDTO saved = RULE.client().target(url + "student")
                .request()
                .post(Entity.json(d), StudentDTO.class);

        assertNotNull(saved.getId(), "estudante salvo não contém id");

        List<Student> list = RULE.client().target(url + "student")
                .request()
                .get(new GenericType<List<Student>>() {});

        assertEquals(1, list.size());
    }

    @Test
    void getStudentTest() {
        assertThrows(NotFoundException.class, () -> RULE.client().target(url + "student/1").request()
                .get(Student.class), "API deveria retornar erro 404, com id inválido porém não retornou");

        assertThrows(PreconditionViolationException.class, () -> RULE.client().target(url + "student/0").request()
                .get(Student.class), "API deveria retornar erro 412, com nenhuma disciplina cadastrada");

        Discipline d = new Discipline("17", "Teste", 60, 220);
        Discipline savedDiscipline = RULE.client().target(url + "discipline")
                .request()
                .post(Entity.json(d), Discipline.class);
        assertNotNull(savedDiscipline.getId(), "disciplina salva não contém id");

        StudentResource.StudentDTO saved =  RULE.client().target(url + "student/0")
                .request()
                .get(StudentResource.StudentDTO.class);

        assertNotNull(saved.getId(), "estudante recuperado não contém id");

        assertEquals(new Long(0), saved.getId(), "Id esperado é diferente do id retornado");
        assertEquals("Gabriel", saved.getName(), "Nome esperado é diferente do nome retornado");
    }

    @Test
    void getDisciplinesFromStudentCourseDepartmentTest() {
        assertThrows(NotFoundException.class, () -> RULE.client().target(url + "student/1/disciplines").request()
                .get(new GenericType<List<Discipline>>() {}), "API deveria retornar erro 404, com id inválido porém não retornou");

        assertThrows(PreconditionViolationException.class, () -> RULE.client().target(url + "student/0/disciplines").request()
                .get(new GenericType<List<Discipline>>() {}), "API deveria retornar erro 412, estudante não está matriculado em nenhuma disciplina");

        Student saved =  RULE.client().target(url + "student/0")
                .request()
                .get(Student.class);
        assertNotNull(saved.getId(), "estudante recuperado não contém id");

        Discipline savedDiscipline = RULE.client().target(url + "discipline/0")
                .request()
                .get(Discipline.class);

        Course course = new Course();
        course.addCourse(true, savedDiscipline);
        saved.enrollmentCourse(course);
        saved = RULE.client().target(url + "student")
                .request()
                .post(Entity.json(new Student()), Student.class);
        assertNotNull(saved.getId(), "estudante salvo após matriculado em um curso não contém id");

        assertThrows(PreconditionViolationException.class, () -> RULE.client().target(url + "student/0/disciplines").request()
                .get(new GenericType<List<Discipline>>() {}), "API deveria retornar erro 412, curso do estudante não está ligado a nenhuma secretaria");

        Secretary secretary = new Secretary();
        secretary.addCourse(course);
        secretary = RULE.client().target(url + "secretary")
                .request()
                .post(Entity.json(new Secretary()), Secretary.class);

        assertThrows(PreconditionViolationException.class, () -> RULE.client().target(url + "student/0/disciplines").request()
                .get(new GenericType<List<Discipline>>() {}), "API deveria retornar erro 412, secretaaria do curso do estudante não está ligado a nenhum departamento");

        Department department = new Department();
        department.setUnderGraduateSecretary(secretary);
        RULE.client().target(url + "department")
                .request()
                .post(Entity.json(new Department()), Department.class);

        List<Discipline> list = RULE.client().target(url + "student/0/disciplines")
                .request()
                .get(new GenericType<List<Discipline>>() {});

        assertEquals(0, list.size(), "Há uma disciplina porém esta não foi reconhecida");
        assertEquals(1, list.size(), "Há mais  disciplinas encontradas do que o esperado");

    }

    @Test
    void enrollmentTest() {// student/0/enrollment/discipline/0
        assertThrows(NotFoundException.class, () -> RULE.client().target(url + "student/1/enrollment/discipline/0").request()
                .post(Entity.json(new Discipline()), Discipline.class), "API deveria retornar erro 404, com id de estudante inválido porém não retornou");

        assertThrows(NotFoundException.class, () -> RULE.client().target(url + "student/0/enrollment/discipline/1").request()
                .post(Entity.json(new Discipline()), Discipline.class), "API deveria retornar erro 404, com id de disciplina inválido porém não retornou");

        Student recoveredStudent =  RULE.client().target(url + "student/0")
                .request()
                .get(Student.class);
        assertNotNull(recoveredStudent.getId(), "estudante recuperado não contém id");

        Discipline recoveredDiscipline = RULE.client().target(url + "discipline/0")
                .request()
                .get(Discipline.class);
        assertNotNull(recoveredDiscipline.getId(), "disciplina recuperada não contém id");

        recoveredDiscipline.enrollStudent(recoveredStudent);

        Discipline saved = RULE.client().target(url + "student/0/enrollment/discipline/0")
                .request()
                .post(Entity.json(recoveredDiscipline), Discipline.class);

        assertNotNull(saved.getId(), "ao ser matriculado o estudante, disciplina salva perdeu o id");

        assertEquals(recoveredDiscipline.getEnrolledStudents(), saved.getEnrolledStudents(), "Estudantes matriculaos na disciplina salva divergem do esperado");

    }
}
