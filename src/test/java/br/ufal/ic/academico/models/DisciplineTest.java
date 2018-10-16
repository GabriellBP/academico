package br.ufal.ic.academico.models;

import br.ufal.ic.academico.DAOs.DisciplineDAO;
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
class DisciplineTest {
    private DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
            .addEntityClass(Discipline.class)
            .build();

    private DisciplineDAO dao;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        System.out.println("setUp");
        dao = new DisciplineDAO(dbTesting.getSessionFactory());
    }

    @Test
    void discipline() {
        System.out.println("discipline CRUD test");

//        create
        final Discipline discipline = create();
//        read
        read(discipline.getId(), discipline);
//        update
        update(discipline);
//        delete
        delete(discipline);

        assertEquals(0, dbTesting.inTransaction(dao::list).size(), "Disciplina não foi deletado corretamente");

        final Discipline discipline1 = create();
        final Discipline discipline2 = create();

        assertEquals(2, dbTesting.inTransaction(dao::list).size(), "Há disciplinas a mais ou a menos do que o esperado");

    }

    private Discipline create() {
        final Discipline discipline = new Discipline("555", "Teste de Software", 80, 0);
        final Discipline saved = dbTesting.inTransaction(() -> dao.persist(discipline));

        assertNotNull(saved, "Disciplina não foi salvo");
        assertNotNull(saved.getId(), "Disciplina criado não recebeu um ID");
        assertEquals(discipline.getId(), saved.getId(), "Id do Discipline diferente do Id salvo");
        assertEquals(discipline.getName(), saved.getName(), "Nome do Discipline diferente do Nome salvo");
        assertEquals(discipline.getCredits(), saved.getCredits(), "Creditos do Discipline diferente do Creditos salvo");
        assertEquals(discipline.getCode(), saved.getCode(), "Código do Discipline diferente do Código salvo");
        assertEquals(discipline.getPrerequisiteCredits(), saved.getPrerequisiteCredits(), "Créditos pré-requisito do Discipline diferente do Créditos pré-requisito salvo");


        return discipline;
    }

    private void read(Long departmentId, Discipline discipline) {
        Discipline recovered = dbTesting.inTransaction(() -> dao.get(departmentId));

        assertEquals(discipline.getId(), recovered.getId(), "ID do departamento não corresponde ao id recuperado");
        assertEquals(discipline.getName(), recovered.getName(), "Nome recuperada não corresponde ao esperado");
        assertEquals(discipline.getCredits(), recovered.getCredits(), "Creditos recuperada não corresponde ao esperado");
        assertEquals(discipline.getCode(), recovered.getCode(), "Código recuperada não corresponde ao esperado");
        assertEquals(discipline.getPrerequisiteCredits(), recovered.getPrerequisiteCredits(), "Créditos pré-requisito recuperada não corresponde ao esperado");
    }

    private void update(Discipline discipline) {
        discipline.setName("Teste de software");

        final Discipline updated = dbTesting.inTransaction(() -> dao.persist(discipline));
        assertEquals(discipline.getId(), updated.getId(), "Id de disciplina alterado ao ser atualizado");
        assertEquals(discipline.getName(), updated.getName(), "Após ser alterado, Nome recuperada não corresponde ao esperado");
        assertEquals(discipline.getCredits(), updated.getCredits(), "Após ser alterado, Creditos recuperada não corresponde ao esperado");
        assertEquals(discipline.getCode(), updated.getCode(), "Após ser alterado, Código recuperada não corresponde ao esperado");
        assertEquals(discipline.getPrerequisiteCredits(), updated.getPrerequisiteCredits(), "Após ser alterado, Créditos pré-requisito recuperada não corresponde ao esperado");
    }

    private void delete(Discipline discipline) {
        dbTesting.inTransaction(() -> dao.delete(discipline));
        assertNull(dbTesting.inTransaction(() -> dao.get(discipline.getId())), "Disciplina não foi corretamente removido");
    }

}
