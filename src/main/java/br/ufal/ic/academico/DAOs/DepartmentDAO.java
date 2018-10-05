package br.ufal.ic.academico.DAOs;

import br.ufal.ic.academico.models.Department;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.util.List;

public class DepartmentDAO extends AbstractDAO<Department> {
    public DepartmentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Department> list() throws HibernateException {
        return super.list(query("from Department"));
    }
}
