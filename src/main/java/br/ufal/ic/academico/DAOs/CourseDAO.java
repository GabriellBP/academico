package br.ufal.ic.academico.DAOs;

import br.ufal.ic.academico.models.Secretary;
import lombok.extern.slf4j.Slf4j;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import br.ufal.ic.academico.models.Course;

import java.io.Serializable;
import java.util.List;

@Slf4j
public class CourseDAO extends AbstractDAO<Course> {
    public CourseDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Course> list() throws HibernateException {
        return super.list(query("from Course"));
    }

    @Override
    public Course get(Serializable id) throws HibernateException {
        log.info("getting course: id={}", id);
        return super.get(id);
    }

    @Override
    public Course persist(Course entity) throws HibernateException {
        return super.persist(entity);
    }

    public void delete(Course entity) throws HibernateException {
        super.currentSession().delete(entity);
    }

    public Secretary getMySecretary(Course course) {
        SecretaryDAO secretaryDAO = new SecretaryDAO(currentSession().getSessionFactory());

        for (Secretary secretary : secretaryDAO.list()) {
            if (secretary.getCourses().contains(course))
                return secretary;
        }

        return null;
    }
}
