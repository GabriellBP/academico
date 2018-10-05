package br.ufal.ic.academico.resources;

import br.ufal.ic.academico.DAOs.CourseDAO;
import br.ufal.ic.academico.DAOs.DisciplineDAO;
import br.ufal.ic.academico.DAOs.SecretaryDAO;
import br.ufal.ic.academico.DAOs.StudentDAO;
import br.ufal.ic.academico.DTOs.DisciplineDTO;
import br.ufal.ic.academico.DTOs.StudentDTO;
import br.ufal.ic.academico.models.*;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.hibernate.SessionFactory;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Path("student")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class StudentResource{

    private final StudentDAO studentDAO;
    private final SecretaryDAO secretaryDAO;
    private final CourseDAO courseDAO;
    private final DisciplineDAO disciplineDAO;

    @GET
    @UnitOfWork
    public Response getAll() {

        log.info("get all students");

        return Response.ok(studentDAO.list()).build();
    }

    @GET
    @Path("/{id}/disciplines")
    @UnitOfWork
    public Response getDisciplinesFromStudentCourseDepartment(@PathParam("id") Long id) {
        log.info("get the disciplines of the student's course department by StudentId = {}", id);

        Student student = studentDAO.get(id);
        if (student == null)
            return Response.status(404).entity("StudentId not found.").build();

        Course course = student.getCourse();
        if (course == null)
            return Response.status(412).entity("Student is not enrolled in any course.").build();

        Secretary secretary = courseDAO.getMySecretary(course);
        if (secretary == null)
            return Response.status(412).entity("course is not linked to any secretary.").build();

        Department department = secretaryDAO.getMyDepartment(secretary);
        if (department == null)
            return Response.status(412).entity("secretary is not linked to any department.").build();

        Secretary underGraduate  = department.getUnderGraduateSecretary();
        Secretary postGraduate =  department.getPostGraduateSecretary();

        List<Course> courses = underGraduate.getCourses();
        courses.addAll(postGraduate.getCourses());

        List<Discipline> disciplines = new LinkedList<>();

        for (Course c : courses) {
            disciplines.addAll(c.getDiscElective());
            disciplines.addAll(c.getDiscRequired());
        }

        return Response.ok(disciplines).build();
    }

    @POST
    @Path("/{id}/enrollment/discipline/{disciplineId}")
    @UnitOfWork
    public Response enrollment(@PathParam("id") Long id, @PathParam("disciplineId") Long disciplineId) {
        log.info("enrolling the student with id {} in the discipline with id {}", id, disciplineId);

        Student student = studentDAO.get(id);
        if (student == null)
            return Response.status(404).entity("StudentId not found.").build();

        Discipline discipline = disciplineDAO.get(disciplineId);
        if (discipline == null)
            return Response.status(404).entity("disciplineId not found.").build();

        if (student.getCredits() < discipline.getPrerequisiteCredits())
            return Response.status(412).entity("student doesn't have the minimum number of credits").build();

        for (Discipline d : discipline.getPrerequisiteDisciplines())
            if (!student.getFinalizedDisciplines().contains(d))
                return Response.status(412).entity("student doesn't have all prerequisite disciplines").build();

        discipline.enrollStudent(student);

        return Response.ok(disciplineDAO.persist(discipline)).build();

//        List<Discipline> possibleDisciplines = (List<Discipline>) this.getDisciplinesFromStudentCourseDepartment(id);

//        if (!possibleDisciplines.contains(discipline)) {
//            return Response.status(412).entity("student").build();
//        }
    }

}
