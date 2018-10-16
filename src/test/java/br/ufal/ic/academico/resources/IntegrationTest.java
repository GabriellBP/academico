package br.ufal.ic.academico.resources;

import br.ufal.ic.academico.AcademicoApp;
import br.ufal.ic.academico.ConfigApp;
import ch.qos.logback.classic.Level;
import io.dropwizard.logging.BootstrapLogging;
import io.dropwizard.testing.ResourceHelpers;
import io.dropwizard.testing.junit5.DropwizardAppExtension;
import io.dropwizard.testing.junit5.DropwizardExtensionsSupport;
import org.junit.jupiter.api.extension.ExtendWith;

@ExtendWith(DropwizardExtensionsSupport.class)
class IntegrationTest {
    final String url = "http://localhost:3000/academico/";

    static {
        BootstrapLogging.bootstrap(Level.DEBUG);
    }

    static DropwizardAppExtension<ConfigApp> RULE = new DropwizardAppExtension(AcademicoApp.class,
            ResourceHelpers.resourceFilePath("config-test.yml"));
}
