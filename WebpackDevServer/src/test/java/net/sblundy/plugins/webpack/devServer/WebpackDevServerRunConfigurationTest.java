package net.sblundy.plugins.webpack.devServer;

import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterRef;
import com.intellij.openapi.command.impl.DummyProject;
import com.intellij.openapi.project.Project;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.junit.Test;

import static org.junit.Assert.*;

/**
 */
public class WebpackDevServerRunConfigurationTest {

    @Test
    public void roundTripConfigSave() throws Exception {
        Project mock = DummyProject.getInstance();
        ConfigurationFactoryEx<RunConfiguration> factory = new ConfigurationFactoryEx<RunConfiguration>(new WebPackDevServerConfigType()) {
            @NotNull
            @Override
            public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
                return null;
            }
        };
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

        assertEquals(source.getInterpreterRef().getReferenceName(), target.getInterpreterRef().getReferenceName());
        assertEquals(source.getPortNumber(), target.getPortNumber());
        assertEquals(source.getNodeOptions(), target.getNodeOptions());
        assertEquals(source.getWebPackConfigFile(), target.getWebPackConfigFile());
        assertEquals(source.getNodeModulesDir(), target.getNodeModulesDir());
        assertEquals(source.getWorkingDir(), target.getWorkingDir());
    }
}