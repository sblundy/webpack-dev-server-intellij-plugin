package net.sblundy.plugins.webpack.devServer.config;

import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.ConfigurationTypeBase;
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterRef;
import com.intellij.openapi.project.Project;
import net.sblundy.plugins.webpack.devServer.Icons;
import org.jetbrains.annotations.NotNull;

/**
 */
public class WebPackDevServerConfigType extends ConfigurationTypeBase {

    private final WebPackDevServerRunConfigurationFactory defaultFactory;

    public WebPackDevServerConfigType() {
        super("webpack-dev-server", "WebPack", "Webpack Dev Server", Icons.WebPackIcon);
        this.defaultFactory = new WebPackDevServerRunConfigurationFactory();
        addFactory(defaultFactory);
    }

    public ConfigurationFactoryEx<WebpackDevServerRunConfiguration> getDefaultFactory() {
        return this.defaultFactory;
    }

    private class WebPackDevServerRunConfigurationFactory extends ConfigurationFactoryEx<WebpackDevServerRunConfiguration> {
        WebPackDevServerRunConfigurationFactory() {
            super(WebPackDevServerConfigType.this);
        }

        @NotNull
        @Override
        public WebpackDevServerRunConfiguration createTemplateConfiguration(@NotNull Project project) {
            WebpackDevServerRunConfiguration templateConfig = new WebpackDevServerRunConfiguration(project, this, "Webpack");
            templateConfig.setPortNumber("9080");
            templateConfig.setInterpreterRef(NodeJsInterpreterRef.createProjectRef());
            return templateConfig;
        }
    }
}
