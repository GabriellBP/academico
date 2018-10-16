package br.ufal.ic.academico.resources;

import br.ufal.ic.academico.DTOs.DisciplineDTO;
import br.ufal.ic.academico.models.Discipline;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.util.LinkedList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class DisciplineResourceTest extends IntegrationTest{
    @Test
    void getAllTest() {
        List<Discipline> saved = RULE.client().target(url + "discipline")
                .request()
                .get(new GenericType<List<Discipline>>() {});

        assertEquals(0, saved.size(), "Testes não iniciaram com Banco de dados vazio");
    }

    @Test
    void saveTest() {
        Discipline d = new Discipline("17", "Teste", 60, 220);

        Discipline saved = RULE.client().target(url + "discipline")
                .request()
                .post(Entity.json(d), Discipline.class);

        assertNotNull(saved.getId(), "disciplina salva não contém id");

        List<Discipline> list = RULE.client().target(url + "discipline")
                .request()
                .get(new GenericType<List<Discipline>>() {});

        assertEquals(1, list.size());
    }

    @Test
    void getDisciplineTest() {
        assertThrows(NotFoundException.class, () -> RULE.client().target(url + "discipline/1").request()
                .get(Discipline.class), "API deveria retornar erro 404, com id inválido porém não retornou");

        DisciplineDTO saved =  RULE.client().target(url + "discipline/0")
                .request()
                .get(DisciplineDTO.class);

        assertNotNull(saved.getCode(), "disciplina recuperado não contém código");

        assertEquals("17", saved.getCode(), "Código esperado é diferente do código retornado");
        assertEquals("Teste", saved.getName(), "Nome esperado é diferente do nome retornado");
        assertEquals(60, saved.getCredits(), "Creditos esperado é diferente dos creditos retornado");
        assertNull(saved.getPrerequisiteDisciplinesCode(), "Disciplinas pré-requisito esperado é diferente do retornado");
        assertNull(saved.getProfessor(), "Professor esperado é diferente do retornado");
        assertSame(new LinkedList<>(), saved.getEnrolledStudents(), "Disciplina criada não está recebendo uma lista de estudantes");
        assertEquals(0, saved.getEnrolledStudents().size(), "Disciplina está recebendo uma lista não vazia de estudantes");
    }
}
