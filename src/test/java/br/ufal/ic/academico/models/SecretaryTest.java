package br.ufal.ic.academico.models;

import br.ufal.ic.academico.DAOs.SecretaryDAO;
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
class SecretaryTest {
    private DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
            .addEntityClass(Secretary.class)
            .build();

    private SecretaryDAO dao;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        System.out.println("setUp");
        dao = new SecretaryDAO(dbTesting.getSessionFactory());
    }

    @Test
    void secretary() {
        System.out.println("secretary CRUD test");

//        create
        final Secretary secretary = create();
//        read
        read(secretary.getId(), secretary);
//        update
        update(secretary);
//        delete
        delete(secretary);

        assertEquals(0, dbTesting.inTransaction(dao::list).size(), "Secretary não foi deletado corretamente");

        final Secretary discipline1 = create();
        final Secretary discipline2 = create();

        assertEquals(2, dbTesting.inTransaction(dao::list).size(), "Há secretarias a mais ou a menos do que o esperado");

    }

    private Secretary create() {
        final Secretary secretary = new Secretary();
        final Secretary saved = dbTesting.inTransaction(() -> dao.persist(secretary));

        assertNotNull(saved, "Secretary não foi salvo");
        assertNotNull(saved.getId(), "Secretary criado não recebeu um ID");
        assertEquals(secretary.getId(), saved.getId(), "Id do Secretary diferente do Id salvo");

        return secretary;
    }

    private void read(Long secretaryId, Secretary secretary) {
        Secretary recovered = dbTesting.inTransaction(() -> dao.get(secretaryId));

        assertEquals(secretary.getId(), recovered.getId(), "ID da secretaria não corresponde ao id recuperado");
        assertEquals(secretary.getCourses(), recovered.getCourses(), "Cursos da secretaria não corresponde ao curso recuperado");
    }

    private void update(Secretary secretary) {
        Course course = new Course();
        secretary.addCourse(course);

        final Secretary updated = dbTesting.inTransaction(() -> dao.persist(secretary));
        assertEquals(secretary.getId(), updated.getId(), "Id de secretary alterado ao ser atualizado");
        assertEquals(secretary.getCourses(), updated.getCourses(), "Após ser alterado, Cursos recuperada não corresponde ao esperado");
    }

    private void delete(Secretary secretary) {
        dbTesting.inTransaction(() -> dao.delete(secretary));
        assertNull(dbTesting.inTransaction(() -> dao.get(secretary.getId())), "Secretary não foi corretamente removido");
    }

}
