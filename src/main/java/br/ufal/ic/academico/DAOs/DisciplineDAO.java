package br.ufal.ic.academico.DAOs;

import br.ufal.ic.academico.models.Course;
import br.ufal.ic.academico.models.Discipline;
import io.dropwizard.hibernate.AbstractDAO;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import java.io.Serializable;
import java.util.List;

@Slf4j
public class DisciplineDAO extends AbstractDAO<Discipline> {
    public DisciplineDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Discipline> list() throws HibernateException {
        return super.list(query("from Discipline"));
    }

    @Override
    public Discipline get(Serializable id) throws HibernateException {
        log.info("getting discipline: id={}", id);

        return super.get(id);
    }

    @Override
    public Discipline persist(Discipline entity) throws HibernateException {
        log.info("persisting discipline: code={}", entity.getCode());

        return super.persist(entity);
    }

    public Course getMyCourse(Discipline discipline) {
        CourseDAO courseDAO = new CourseDAO(currentSession().getSessionFactory());

        for (Course course : courseDAO.list()) {
            if (course.getDiscElective().contains(discipline))
                return course;
            if (course.getDiscRequired().contains(discipline))
                return course;
        }

        return null;
    }
}
