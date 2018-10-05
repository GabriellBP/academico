package br.ufal.ic.academico.DAOs;

import io.dropwizard.hibernate.AbstractDAO;
import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import lombok.extern.slf4j.Slf4j;
import org.hibernate.HibernateException;
import org.hibernate.SessionFactory;

import br.ufal.ic.academico.models.Student;

@Slf4j
public class StudentDAO extends AbstractDAO<Student> {
    public StudentDAO(SessionFactory sessionFactory) {
        super(sessionFactory);
    }

    public List<Student> list() throws HibernateException {
//        uniqueResult()
//        return list(query("from Student as s where s.id = :gid")
//                .setParameter("gid", id)
//                .setParameter("name", null));
        return super.list(query("from Student"));
    }

    @Override
    public Student get(Serializable id) throws HibernateException {
        log.info("getting student: id={}", id);

        return super.get(id);
    }
//
//    @Override
//    public Student persist(Student entity) throws HibernateException {
//        return super.persist(entity);
//    }
}
