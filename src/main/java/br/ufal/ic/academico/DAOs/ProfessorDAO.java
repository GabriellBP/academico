package br.ufal.ic.academico.DAOs;

import br.ufal.ic.academico.models.Professor;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

@Slf4j
public class ProfessorDAO extends AbstractDAO<Professor> {

    public ProfessorDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Professor> list() throws HibernateException {
        return super.list(query("from Professor"));
    }

    @Override
    public Professor get(Serializable id) throws HibernateException {
        log.info("getting discipline: id={}", id);

        return super.get(id);
    }

    @Override
    public Professor persist(Professor entity) throws HibernateException {
        log.info("persisting professor: code={}", entity.getId());

        return super.persist(entity);
    }

    public void delete(Professor entity) throws HibernateException {
        super.currentSession().delete(entity);
    }

}
