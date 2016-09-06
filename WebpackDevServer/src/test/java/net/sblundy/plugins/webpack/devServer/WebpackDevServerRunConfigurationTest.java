package net.sblundy.plugins.webpack.devServer;

import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterRef;
import com.intellij.openapi.command.impl.DummyProject;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.junit.Before;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 */
public class WebpackDevServerRunConfigurationTest {

    private ConfigurationFactoryEx<RunConfiguration> factory;
    private Project dummyProject;

    @Before
    public void setUp(){
        factory = new ConfigurationFactoryEx<RunConfiguration>(new WebPackDevServerConfigType()) {
            @NotNull
            @Override
            public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
                return null;
            }
        };
        dummyProject = DummyProject.getInstance();
    }

    @Test
    public void roundTripConfigSave() throws WriteExternalException, InvalidDataException {
        Project mock = DummyProject.getInstance();
        WebpackDevServerRunConfiguration source = new WebpackDevServerRunConfiguration(mock, factory, "test");

        source.setInterpreterRef(NodeJsInterpreterRef.create("dummy"));
        source.setPortNumber("1234");
        source.setNodeOptions("-N D");
        source.setWebPackConfigFile("dev/null");
        source.setNodeModulesDir("test/node_modules");
        source.setWorkingDir("temp");
        Element config = new Element("config");

        source.writeExternal(config);
        WebpackDevServerRunConfiguration target = new WebpackDevServerRunConfiguration(mock, factory, "test");
        target.readExternal(config);

        assertEqual(source, target);
    }

    @Test
    public void roundTripConfigSaveMinimal() throws WriteExternalException, InvalidDataException {
        WebpackDevServerRunConfiguration source = new WebpackDevServerRunConfiguration(dummyProject, factory, "test");
        source.setInterpreterRef(NodeJsInterpreterRef.create(""));

        Element config = new Element("config");

        source.writeExternal(config);
        WebpackDevServerRunConfiguration target = new WebpackDevServerRunConfiguration(dummyProject, factory, "test");
        target.readExternal(config);

        assertEqual(source, target);
    }

    private static void assertEqual(WebpackDevServerRunConfiguration expected, WebpackDevServerRunConfiguration actual) {
        if (expected.getInterpreterRef() == null) {
            assertNull(actual.getInterpreterRef());
        } else {
            assertNotNull(actual.getInterpreterRef());
            assertEquals(expected.getInterpreterRef().getReferenceName(), actual.getInterpreterRef().getReferenceName());
        }
        assertEquals(expected.getPortNumber(), actual.getPortNumber());
        assertEquals(expected.getNodeOptions(), actual.getNodeOptions());
        assertEquals(expected.getWebPackConfigFile(), actual.getWebPackConfigFile());
        assertEquals(expected.getNodeModulesDir(), actual.getNodeModulesDir());
        assertEquals(expected.getWorkingDir(), actual.getWorkingDir());
    }
}