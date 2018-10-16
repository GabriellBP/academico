package br.ufal.ic.academico.models;

import br.ufal.ic.academico.DAOs.StudentDAO;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;

@ExtendWith(DropwizardExtensionsSupport.class)
class StudentTest {
    private DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
            .addEntityClass(Student.class)
            .build();

    private StudentDAO dao;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        System.out.println("setUp");
        dao = new StudentDAO(dbTesting.getSessionFactory());
    }

    @Test
    void student() {
        System.out.println("student CRUD test");

//        create
        final Student student = create();
//        read
        read(student.getId(), student);
//        update
        update(student);
//        delete
        delete(student);

        assertEquals(0, dbTesting.inTransaction(dao::list).size(), "Student não foi deletado corretamente");

        final Student discipline1 = create();
        final Student discipline2 = create();

        assertEquals(2, dbTesting.inTransaction(dao::list).size(), "Há estudantes a mais ou a menos do que o esperado");

    }

    private Student create() {
        final Student student = new Student("Guilherme");
        final Student saved = dbTesting.inTransaction(() -> dao.persist(student));

        assertNotNull(saved, "Student não foi salvo");
        assertNotNull(saved.getId(), "Student criado não recebeu um ID");
        assertEquals(student.getId(), saved.getId(), "Id do Student diferente do Id salvo");
        assertEquals(student.getName(), saved.getName(), "Nome do Student diferente do Nome salvo");
        assertEquals(student.getCredits(), saved.getCredits(), "Creditos do Student diferente dos creditos salvos");
        assertEquals(student.getFinalizedDisciplines(), saved.getFinalizedDisciplines(), "Disciplinas finalidas do Student diferente das Disciplinas finalidas salvas");

        return student;
    }

    private void read(Long studentId, Student student) {
        Student recovered = dbTesting.inTransaction(() -> dao.get(studentId));

        assertEquals(student.getId(), recovered.getId(), "ID do estudante não corresponde ao id recuperado");
        assertEquals(student.getName(), recovered.getName(), "Nome do estudante não corresponde ao nome recuperado");
        assertEquals(student.getCredits(), recovered.getCredits(), "Creditos do Student não corresponde ao Creditos recuperado");
        assertEquals(student.getFinalizedDisciplines(), recovered.getFinalizedDisciplines(), "Disciplinas finalidas do Student não corresponde às Disciplinas finalidas recuperado");
    }

    private void update(Student student) {
        Course course = new Course();
        student.enrollmentCourse(course);

        final Student updated = dbTesting.inTransaction(() -> dao.persist(student));
        assertEquals(student.getId(), updated.getId(), "Id de student alterado ao ser atualizado");
        assertEquals(student.getCourse(), updated.getCourse(), "Após ser alterado, Curso recuperada não corresponde ao esperado");
    }

    private void delete(Student student) {
        dbTesting.inTransaction(() -> dao.delete(student));
        assertNull(dbTesting.inTransaction(() -> dao.get(student.getId())), "Student não foi corretamente removido");
    }


}
