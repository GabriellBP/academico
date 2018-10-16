package br.ufal.ic.academico.models;

import br.ufal.ic.academico.DAOs.DepartmentDAO;
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
class DepartmentTest {
    private DAOTestExtension dbTesting = DAOTestExtension.newBuilder()
            .addEntityClass(Department.class)
            .build();

    private DepartmentDAO dao;

    @BeforeEach
    @SneakyThrows
    void setUp() {
        System.out.println("setUp");
        dao = new DepartmentDAO(dbTesting.getSessionFactory());
    }

    @Test
    void department() {
        System.out.println("department CRUD test");

//        create
        final Department department = create();
//        read
        read(department.getId(), department);
//        update
        update(department);
//        delete
        delete(department);

        assertEquals(0, dbTesting.inTransaction(dao::list).size(), "Departamento não foi deletado corretamente");

        final Department department1 = create();
        final Department department2 = create();

        assertEquals(2, dbTesting.inTransaction(dao::list).size(), "Há departamentos a mais ou a menos do que o esperado");

    }

    private Department create() {
        final Department department = new Department();
        final Department saved = dbTesting.inTransaction(() -> dao.persist(department));

        assertNotNull(saved, "Departmento não foi salvo");
        assertNotNull(saved.getId(), "Departmento criado não recebeu um ID");
        assertEquals(department.getId(), saved.getId(), "Id do Department diferente do Id salvo");
        assertNull(saved.getPostGraduateSecretary(), "Secretaria de pós graduação não atribuida diferente de null");
        assertNull(saved.getUnderGraduateSecretary(), "Secretaria de graduação não atribuida diferente de null");

        return department;
    }

    private void read(Long departmentId, Department department) {
        Department recovered = dbTesting.inTransaction(() -> dao.get(departmentId));

        assertEquals(department.getId(), recovered.getId(), "ID do departamento não corresponde ao id recuperado");
        assertEquals(department.getPostGraduateSecretary(), recovered.getPostGraduateSecretary(), "Secretaria de pós recuperada não corresponde ao esperado");
        assertEquals(department.getUnderGraduateSecretary(), recovered.getUnderGraduateSecretary(), "Secretaria de graduação recuperada recuperada não corresponde ao esperado");
    }

    private void update(Department department) {
        final Secretary postSecretary = new Secretary();
        department.setPostGraduateSecretary(postSecretary);
        final Secretary underSecretary = new Secretary();
        department.setUnderGraduateSecretary(underSecretary);

        final Department updated = dbTesting.inTransaction(() -> dao.persist(department));
        assertEquals(department.getId(), updated.getId(), "Id de departamento alterado ao ser atualizado");
        assertEquals(department.getPostGraduateSecretary(), updated.getPostGraduateSecretary(), "Após ser alterado, Secretaria de pós recuperada não corresponde ao esperado");
        assertEquals(department.getUnderGraduateSecretary(), updated.getUnderGraduateSecretary(), "Após ser alterado, Secretaria de graduação recuperada recuperada não corresponde ao esperado");
    }

    private void delete(Department department) {
        dbTesting.inTransaction(() -> dao.delete(department));
        assertNull(dbTesting.inTransaction(() -> dao.get(department.getId())), "Departmento não foi corretamente removido");
    }

}
