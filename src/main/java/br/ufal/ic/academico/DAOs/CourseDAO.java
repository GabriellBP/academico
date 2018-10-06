package br.ufal.ic.academico.DAOs;

import br.ufal.ic.academico.models.Secretary;
import lombok.extern.slf4j.Slf4j;
import io.dropwizard.hibernate.AbstractDAO;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import br.ufal.ic.academico.models.Course;

import java.util.List;

@Slf4j
public class CourseDAO extends AbstractDAO<Course> {
    public CourseDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Course> list() throws HibernateException {
        return super.list(query("from Course"));
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
