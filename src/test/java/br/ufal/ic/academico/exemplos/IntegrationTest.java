package br.ufal.ic.academico.exemplos;

import br.ufal.ic.academico.AcademicoApp;
import br.ufal.ic.academico.ConfigApp;
import ch.qos.logback.classic.Level;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;

import javax.ws.rs.client.Entity;
import javax.ws.rs.core.GenericType;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
//import static org.mockito.Mockito.mock;

/**
 *
 * @author willy
 */
@ExtendWith(DropwizardExtensionsSupport.class)
public class IntegrationTest {
    
    
    static {
        BootstrapLogging.bootstrap(Level.DEBUG);
    }

//    private PersonDAO dao = mock(PersonDAO.class);

//    private final MyResource resource = new MyResource(dao);

    public static DropwizardAppExtension<ConfigApp> RULE = new DropwizardAppExtension(AcademicoApp.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));
    
    @Test
    public void testSave() {
        
        Person p = new Person("p1");
        
        Person saved = RULE.client().target(
             String.format("http://localhost:%d/%s/exemplos", RULE.getLocalPort(), "academicotest"))
            .request()
            .post(Entity.json(p), Person.class);

        assertNotNull(saved.getId());
        
        List<Person> list = RULE.client().target(
             String.format("http://localhost:%d/%s/exemplos", RULE.getLocalPort(), "academicotest"))
            .request()
            .get(new GenericType<List<Person>>() {});

        assertEquals(1, list.size());
    }
    
    @Test
    public void testList() {
        
        List<Person> saved = RULE.client().target(
             String.format("http://localhost:%d/%s/exemplos", RULE.getLocalPort(), "academicotest"))
            .request()
            .get(new GenericType<List<Person>>() {});

        assertEquals(0, saved.size());
    }
}
