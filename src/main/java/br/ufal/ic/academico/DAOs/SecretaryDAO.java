package br.ufal.ic.academico.DAOs;

import br.ufal.ic.academico.models.Department;
import br.ufal.ic.academico.models.Secretary;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

@Slf4j
public class SecretaryDAO extends AbstractDAO<Secretary> {
    public SecretaryDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Secretary> list() throws HibernateException {
        return super.list(query("from Secretary"));
    }

    @Override
    public Secretary get(Serializable id) throws HibernateException {
        log.info("getting department: id={}", id);
        return super.get(id);
    }

    @Override
    public Secretary persist(Secretary entity) throws HibernateException {
        return super.persist(entity);
    }

    public void delete(Secretary entity) throws HibernateException {
        super.currentSession().delete(entity);
    }

    public Department getMyDepartment(Secretary secretary) {
        DepartmentDAO departmentDAO = new DepartmentDAO(currentSession().getSessionFactory());

        for (Department department : departmentDAO.list()) {
            if (department.getPostGraduateSecretary().equals(secretary))
                return department;
            if (department.getUnderGraduateSecretary().equals(secretary))
                return department;
        }

        return null;
    }

    public String secretaryType(Secretary secretary) {
        DepartmentDAO departmentDAO = new DepartmentDAO(currentSession().getSessionFactory());

        for (Department department : departmentDAO.list()) {
            if (department.getPostGraduateSecretary().equals(secretary))
                return "POSTGRADUATE";
            if (department.getUnderGraduateSecretary().equals(secretary))
                return "UNDERGRADUATE";
        }

        return "NONE";
    }
}
