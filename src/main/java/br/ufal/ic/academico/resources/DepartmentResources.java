package br.ufal.ic.academico.resources;

import br.ufal.ic.academico.DAOs.DepartmentDAO;

import br.ufal.ic.academico.DTOs.DisciplineDTO;
import br.ufal.ic.academico.models.Course;
import br.ufal.ic.academico.models.Department;
import br.ufal.ic.academico.models.Discipline;
import br.ufal.ic.academico.models.Secretary;
import io.dropwizard.hibernate.UnitOfWork;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;

import javax.ws.rs.GET;
import javax.ws.rs.Path;
import javax.ws.rs.Produces;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import java.util.LinkedList;
import java.util.List;

@Path("department")
@Slf4j
@RequiredArgsConstructor
@Produces(MediaType.APPLICATION_JSON)
public class DepartmentResources {
    private final DepartmentDAO departmentDAO;

    @GET
    @Path("/offers")
    @UnitOfWork
    public Response getDisciplineOfferBySecretary() {
        log.info("get the offer of disciplines from the department by secretary");
        Secretary underGraduateSecretary;
        Secretary postGraduateSecretary;

        List<DepartmentDTO> departments = new LinkedList<>();

        for (Department d : departmentDAO.list()) {
            underGraduateSecretary = d.getUnderGraduateSecretary();
            postGraduateSecretary = d.getPostGraduateSecretary();

            List<DisciplineDTO> underGraduateDisciplines = getDisciplinesFromCourses(underGraduateSecretary.getCourses());
            List<DisciplineDTO> postGraduateDisciplines = getDisciplinesFromCourses(postGraduateSecretary.getCourses());

            departments.add(new DepartmentDTO(underGraduateDisciplines, postGraduateDisciplines));
        }

        return Response.ok(departments).build();
    }

    private List<DisciplineDTO> getDisciplinesFromCourses(List<Course> courses) {
        List<DisciplineDTO> disciplines = new LinkedList<>();

        for (Course c: courses) {
            for (Discipline discipline : c.getDiscRequired()) {
                List<String> prerequisiteDisciplinescode = new LinkedList<>();
                for (Discipline pd : discipline.getPrerequisiteDisciplines())
                    prerequisiteDisciplinescode.add(pd.getCode());
                disciplines.add(new DisciplineDTO(discipline.getCode(), discipline.getName(), discipline.getCredits(), discipline.getPrerequisiteCredits(), prerequisiteDisciplinescode));
            }
            for (Discipline discipline : c.getDiscElective()) {
                List<String> prerequisiteDisciplinescode = new LinkedList<>();
                for (Discipline pd : discipline.getPrerequisiteDisciplines())
                    prerequisiteDisciplinescode.add(pd.getCode());
                disciplines.add(new DisciplineDTO(discipline.getCode(), discipline.getName(), discipline.getCredits(), discipline.getPrerequisiteCredits(), prerequisiteDisciplinescode));
            }
        }
        return disciplines;
    }

    @Getter
    @RequiredArgsConstructor
    @AllArgsConstructor
    @ToString
    private static class DepartmentDTO {
        List<DisciplineDTO> underGraduateDisciplines;
        List<DisciplineDTO> postGraduateDisciplines;
    }

}
