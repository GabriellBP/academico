package br.ufal.ic.academico.resources;

import br.ufal.ic.academico.models.Department;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.NotFoundException;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@ExtendWith(DropwizardExtensionsSupport.class)
class DepartmentResourceTest extends IntegrationTest{
    @Test
    void getAllTest() {
        List<Department> saved = RULE.client().target(url + "department")
                .request()
                .get(new GenericType<List<Department>>() {});

        assertEquals(0, saved.size(), "Testes não iniciaram com Banco de dados vazio");
    }

    @Test
    void saveTest() {
        Department d = new Department();

        Department saved = RULE.client().target(url + "department")
                .request()
                .post(Entity.json(d), Department.class);

        assertNotNull(saved.getId(), "departamento salvo não contém id");

        List<Department> list = RULE.client().target(url + "department")
                .request()
                .get(new GenericType<List<Department>>() {});

        assertEquals(1, list.size());
    }

    @Test
    void getByIdTest() {
        assertThrows(NotFoundException.class, () -> RULE.client().target(url + "department/1").request()
                .get(Department.class), "API deveria retornar erro 404, com id inválido porém não retornou");

        Department saved =  RULE.client().target(url + "department/0")
                .request()
                .get(Department.class);

        assertNotNull(saved.getId(), "departamento recuperado não contém id");

        assertEquals(new Long(0), saved.getId(), "ID esperado é diferente do id retornado");
        assertNull(saved.getUnderGraduateSecretary(), "Secretaria de graduação não está inicializando como null");
        assertNull(saved.getPostGraduateSecretary(), "Secretaria de pós não está inicializando como null");
    }

    @Test
    void getDisciplineOfferBySecretaryTest() {
        List<DepartmentResource.DepartmentDTO> saved = RULE.client().target(url + "department/offers")
                .request()
                .get(new GenericType<List<DepartmentResource.DepartmentDTO>>() {});

        assertEquals(0, saved.size(), "Secretarias não iniciaram nulas");
    }

}
