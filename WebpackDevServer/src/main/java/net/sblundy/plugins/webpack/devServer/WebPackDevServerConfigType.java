package net.sblundy.plugins.webpack.devServer;

import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterRef;
import com.intellij.openapi.project.Project;
import org.jetbrains.annotations.NotNull;

/**
 */
public class WebPackDevServerConfigType extends ConfigurationTypeBase {
    public WebPackDevServerConfigType() {
        super("webpack-dev-server", "WebPack", "Webpack Dev Server", Icons.WebPackIcon);
        addFactory(new WebPackDevServerRunConfigurationFactory());
    }

    private class WebPackDevServerRunConfigurationFactory extends ConfigurationFactoryEx<RunConfiguration> {
        public WebPackDevServerRunConfigurationFactory() {
            super(WebPackDevServerConfigType.this);
        }

        @NotNull
        @Override
        public RunConfiguration createTemplateConfiguration(@NotNull Project project) {
            WebpackDevServerRunConfiguration templateConfig = new WebpackDevServerRunConfiguration(project, this, "Webpack");
            templateConfig.setPortNumber("9080");
            templateConfig.setInterpreterRef(NodeJsInterpreterRef.createProjectRef());
            return templateConfig;
        }
    }
}
