package br.ufal.ic.academico.resources;

import br.ufal.ic.academico.DAOs.DisciplineDAO;
import br.ufal.ic.academico.DTOs.DisciplineDTO;
import br.ufal.ic.academico.DTOs.StudentDTO;
import br.ufal.ic.academico.models.Discipline;
import br.ufal.ic.academico.models.Student;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.*;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;
import java.util.LinkedList;
import java.util.List;

@Path("discipline")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class DisciplineResource {
    private final DisciplineDAO disciplineDAO;

    @GET
    @UnitOfWork
    public Response getAll() {

        log.info("get all discipline");

        return Response.ok(disciplineDAO.list()).build();
    }

    @GET
    @Path("/{id}")
    @UnitOfWork
    public Response getDiscipline(@PathParam("id") Long id) {
        log.info("get the discipline with id = {}", id);
        Discipline discipline = disciplineDAO.get(id);
        if (discipline == null)
            return Response.status(404).entity("DisciplineId not found.").build();

        List<String> prerequisiteDisciplinescode = new LinkedList<>();
        for (Discipline pd : discipline.getPrerequisiteDisciplines())
            prerequisiteDisciplinescode.add(pd.getCode());

        List<StudentDTO> enrolledStudents = new LinkedList<>();
        for (Student st : discipline.getEnrolledStudents())
            enrolledStudents.add(new StudentDTO(st.getId(), st.getName()));

        return Response.ok(new DisciplineDTO(discipline.getCode(), discipline.getName(), discipline.getCredits(), discipline.getPrerequisiteCredits(), prerequisiteDisciplinescode, discipline.getProfessor().getName(), enrolledStudents)).build();
    }

    @POST
    @UnitOfWork
    @Consumes("application/json")
    public Response save(Discipline entity) {

        log.info("save: {}", entity);

        Discipline p = new Discipline(entity.getCode(), entity.getName(), entity.getCredits(), entity.getPrerequisiteCredits(), entity.getPrerequisiteDisciplines(), entity.getProfessor());

        return Response.ok(disciplineDAO.persist(p)).build();
    }
}
