package br.ufal.ic.academico.DAOs;

import br.ufal.ic.academico.models.Department;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

@Slf4j
public class DepartmentDAO extends AbstractDAO<Department> {
    public DepartmentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Department> list() throws HibernateException {
        return super.list(query("from Department"));
    }

    @Override
    public Department get(Serializable id) throws HibernateException {
        log.info("getting department: id={}", id);
        return super.get(id);
    }

    @Override
    public Department persist(Department entity) throws HibernateException {
        return super.persist(entity);
    }

    public void delete(Department entity) throws HibernateException {
        super.currentSession().delete(entity);
    }
}
