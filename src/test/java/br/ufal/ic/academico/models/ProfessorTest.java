package br.ufal.ic.academico.models;

import br.ufal.ic.academico.DAOs.ProfessorDAO;
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
class ProfessorTest {
    private DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
            .addEntityClass(Professor.class)
            .build();

    private ProfessorDAO dao;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        System.out.println("setUp");
        dao = new ProfessorDAO(dbTesting.getSessionFactory());
    }

    @Test
    void professor() {
        System.out.println("professor CRUD test");

//        create
        final Professor professor = create();
//        read
        read(professor.getId(), professor);
//        update
        update(professor);
//        delete
        delete(professor);

        assertEquals(0, dbTesting.inTransaction(dao::list).size(), "Professor não foi deletado corretamente");

        final Professor discipline1 = create();
        final Professor discipline2 = create();

        assertEquals(2, dbTesting.inTransaction(dao::list).size(), "Há Professores a mais ou a menos do que o esperado");

    }

    private Professor create() {
        final Professor professor = new Professor("Gabriel");
        final Professor saved = dbTesting.inTransaction(() -> dao.persist(professor));

        assertNotNull(saved, "Professor não foi salvo");
        assertNotNull(saved.getId(), "Professor criado não recebeu um ID");
        assertEquals(professor.getId(), saved.getId(), "Id do Professor diferente do Id salvo");
        assertEquals(professor.getName(), saved.getName(), "Nome do Professor diferente do Nome salvo");

        return professor;
    }

    private void read(Long professorId, Professor professor) {
        Professor recovered = dbTesting.inTransaction(() -> dao.get(professorId));

        assertEquals(professor.getId(), recovered.getId(), "ID do departamento não corresponde ao id recuperado");
        assertEquals(professor.getName(), recovered.getName(), "Nome recuperada não corresponde ao esperado");
    }

    private void update(Professor professor) {
        professor.setName("Gabriel Barbosa");

        final Professor updated = dbTesting.inTransaction(() -> dao.persist(professor));
        assertEquals(professor.getId(), updated.getId(), "Id de professor alterado ao ser atualizado");
        assertEquals(professor.getName(), updated.getName(), "Após ser alterado, Nome recuperada não corresponde ao esperado");
    }

    private void delete(Professor professor) {
        dbTesting.inTransaction(() -> dao.delete(professor));
        assertNull(dbTesting.inTransaction(() -> dao.get(professor.getId())), "Professor não foi corretamente removido");
    }

}
