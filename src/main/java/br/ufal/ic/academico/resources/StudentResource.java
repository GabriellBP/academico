package br.ufal.ic.academico.resources;

import br.ufal.ic.academico.DAOs.CourseDAO;
import br.ufal.ic.academico.DAOs.DisciplineDAO;
import br.ufal.ic.academico.DAOs.SecretaryDAO;
import br.ufal.ic.academico.DAOs.StudentDAO;
import br.ufal.ic.academico.models.*;

import io.dropwizard.hibernate.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

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
            return Response.status(404).entity("studentId not found.").build();

        Discipline discipline = disciplineDAO.get(disciplineId);
        if (discipline == null)
            return Response.status(404).entity("disciplineId not found.").build();

        if (student.getFinalizedDisciplines().contains(discipline))
            return Response.status(412).entity("student can't be enrolled in disciplines already finalized.").build();

        if (discipline.getEnrolledStudents().contains(student))
            return Response.status(412).entity("student can't be enrolled in disciplines already enrolled.").build();

//        ------------------------
        /*Getting possible disciplines*/
        Course studentCourse = student.getCourse();
        if (studentCourse == null)
            return Response.status(412).entity("student is not enrolled in any course.").build();

        Secretary studentSecretary = courseDAO.getMySecretary(studentCourse);
        if (studentSecretary == null)
            return Response.status(412).entity("student course is not linked to any secretary.").build();

        Department department = secretaryDAO.getMyDepartment(studentSecretary);
        if (department == null)
            return Response.status(412).entity("secretary course of student is not linked to any department.").build();

        Secretary underGraduate  = department.getUnderGraduateSecretary();
        Secretary postGraduate =  department.getPostGraduateSecretary();

        List<Course> courses = underGraduate.getCourses();
        courses.addAll(postGraduate.getCourses());

        List<Discipline> possibleDisciplines = new LinkedList<>();

        for (Course c : courses) {
            possibleDisciplines.addAll(c.getDiscElective());
            possibleDisciplines.addAll(c.getDiscRequired());
        }
//        ------------------------

        if (!possibleDisciplines.contains(discipline))
            return Response.status(412).entity("discipline doesn't belong to the student course department.").build();

//        ------------------------
        /*Getting discipline Secretary*/
        Course disciplineCourse = disciplineDAO.getMyCourse(discipline);
        if (disciplineCourse == null)
            return Response.status(412).entity("discipline is not linked to any course.").build();

        Secretary disciplineSecretary = courseDAO.getMySecretary(disciplineCourse);
        if (disciplineSecretary == null)
            return Response.status(412).entity("discipline course is not linked to any secretary.").build();
//        ------------------------

        if (secretaryDAO.secretaryType(studentSecretary).equals("POSTGRADUATE") && secretaryDAO.secretaryType(disciplineSecretary).equals("UNDERGRADUATE"))
            return Response.status(412).entity("postgraduate students can not take undergraduate disciplines.").build();

        if (secretaryDAO.secretaryType(studentSecretary).equals("UNDERGRADUATE") && secretaryDAO.secretaryType(disciplineSecretary).equals("POSTGRADUATE") && student.getCredits() < 170)
            return Response.status(412).entity("undergraduate students can not take postgraduate disciplines if they doesn't have at least 170 credits.").build();

        if (student.getCredits() < discipline.getPrerequisiteCredits())
            return Response.status(412).entity("student doesn't have the minimum number of required credits").build();

        for (Discipline d : discipline.getPrerequisiteDisciplines())
            if (!student.getFinalizedDisciplines().contains(d))
                return Response.status(412).entity("student doesn't have all prerequisite disciplines").build();

        discipline.enrollStudent(student);

        return Response.ok(disciplineDAO.persist(discipline)).build();
    }



}
