package br.ufal.ic.academico.models;

import br.ufal.ic.academico.DAOs.CourseDAO;
import io.dropwizard.testing.junit5.DAOTestExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import lombok.SneakyThrows;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class CourseTest {
    private DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
            .addEntityClass(Course.class)
            .build();

    private CourseDAO dao;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        System.out.println("setUp");
        dao = new CourseDAO(dbTesting.getSessionFactory());
    }

    @Test
    void course() {
        System.out.println("course CRUD test");

//        create
        final Course course = create();
//        read
        read(course.getId(), course);
//        update
        update(course);
//        delete
        delete(course);

        assertEquals(0, dbTesting.inTransaction(dao::list).size(), "Curso não foi deletado corretamente");

        final Course course1 = create();
        final Course course2 = create();

        assertEquals(2, dbTesting.inTransaction(dao::list).size(), "Há cursos a mais ou a menos do que o esperado");

    }

    private Course create() {
        final Course course = new Course();
        final Course saved = dbTesting.inTransaction(() -> dao.persist(course));

        assertNotNull(saved, "Curso não foi salvo");
        assertNotNull(saved.getId(), "Curso criado não recebeu um ID");
        assertEquals(course.getId(), saved.getId(), "Id do Course diferente do Id salvo");
        assertNotNull(saved.getDiscElective(), "Lista de disciplinas eletivas não foi iniciada em curso");
        assertNotNull(saved.getDiscRequired(), "Lista de disciplinas requeridas não foi iniciada em curso");
        assertEquals(0, saved.getDiscRequired().size(), "Course foi criado lista não vazia em disciplinas requeridas");
        assertEquals(0, saved.getDiscElective().size(), "Course foi criado lista não vazia em disciplinas eletivas");

        return course;
    }

    private void read(Long courseId, Course course) {
        Course recovered = dbTesting.inTransaction(() -> dao.get(courseId));

        assertEquals(course.getId(), recovered.getId(), "ID do curso não corresponde ao id recuperado");
        assertEquals(course.getDiscElective(), recovered.getDiscElective(), "Disciplinas eletivas recuperadas não correspondem oa esperado");
        assertEquals(course.getDiscRequired(), recovered.getDiscRequired(), "Disciplinas requeridas recuperadas não correspondem oa esperado");
    }

    private void update(Course course) {
        final Discipline discipline = new Discipline("123", "Matemática", 100, 0);
        course.addCourse(true, discipline);
        course.addCourse(false, discipline);
        final Course updated = dbTesting.inTransaction(() -> dao.persist(course));
        assertEquals(course.getId(), updated.getId(), "Id de curso alterado ao ser atualizado");
        assertEquals(course.getDiscElective(), updated.getDiscElective(), "Após ser alterado, Disciplinas eletivas recuperadas não correspondem oa esperado");
        assertEquals(course.getDiscRequired(), updated.getDiscRequired(), "Após ser alterado, Disciplinas requeridas recuperadas não correspondem oa esperado");
    }

    private void delete(Course course) {
        dbTesting.inTransaction(() -> dao.delete(course));
        assertNull(dbTesting.inTransaction(() -> dao.get(course.getId())), "Course não foi corretamente removido");
    }

}
