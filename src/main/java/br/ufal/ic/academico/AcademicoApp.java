package br.ufal.ic.academico;

import br.ufal.ic.academico.DAOs.*;
import br.ufal.ic.academico.exemplos.MyResource;
import br.ufal.ic.academico.exemplos.Person;
import br.ufal.ic.academico.exemplos.PersonDAO;
import br.ufal.ic.academico.models.*;
import br.ufal.ic.academico.resources.DepartmentResource;
import br.ufal.ic.academico.resources.DisciplineResource;
import br.ufal.ic.academico.resources.StudentResource;
import io.dropwizard.Application;
import io.dropwizard.db.DataSourceFactory;
import io.dropwizard.hibernate.HibernateBundle;
import io.dropwizard.setup.Bootstrap;
import io.dropwizard.setup.Environment;
import lombok.extern.slf4j.Slf4j;

/**
 *
 * @author Willy
 */
@Slf4j
public class AcademicoApp extends Application<ConfigApp> {

    public static void main(String[] args) throws Exception {
        new AcademicoApp().run(args);
    }

    @Override
    public String getName() {
        return "hello-world";
    }

    @Override
    public void initialize(Bootstrap<ConfigApp> bootstrap) {
        log.info("initialize");
        bootstrap.addBundle(hibernate);
    }

    @Override
    public void run(ConfigApp config, Environment environment) {
        
        final PersonDAO dao = new PersonDAO(hibernate.getSessionFactory()); // example

        final DepartmentDAO departmentDAO = new DepartmentDAO(hibernate.getSessionFactory());
        final SecretaryDAO secretaryDAO  = new SecretaryDAO(hibernate.getSessionFactory());
        final CourseDAO courseDAO  = new CourseDAO(hibernate.getSessionFactory());
        final DisciplineDAO disciplineDAO  = new DisciplineDAO(hibernate.getSessionFactory());
        final StudentDAO studentDAO = new StudentDAO(hibernate.getSessionFactory());

        final MyResource resource = new MyResource(dao);
        final StudentResource studentResource = new StudentResource(studentDAO, secretaryDAO, courseDAO, disciplineDAO);
        final DepartmentResource departmentResource = new DepartmentResource(departmentDAO);
        final DisciplineResource disciplineResource = new DisciplineResource(disciplineDAO);

        environment.jersey().register(resource);
        environment.jersey().register(studentResource);
        environment.jersey().register(departmentResource);
        environment.jersey().register(disciplineResource);
    }

    private final HibernateBundle<ConfigApp> hibernate
            = new HibernateBundle<ConfigApp>(Person.class, Student.class, Professor.class, Department.class, Secretary.class, Course.class, Discipline.class) {
        
        @Override
        public DataSourceFactory getDataSourceFactory(ConfigApp configuration) {
            return configuration.getDatabase();
        }
    };
}
