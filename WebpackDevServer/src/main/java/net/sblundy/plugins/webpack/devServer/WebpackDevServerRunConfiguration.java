package net.sblundy.plugins.webpack.devServer;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configuration.ConfigurationFactoryEx;
import com.intellij.execution.configurations.RunConfiguration;
import com.intellij.execution.configurations.RunConfigurationBase;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.configurations.RuntimeConfigurationException;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterRef;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.InvalidDataException;
import com.intellij.openapi.util.WriteExternalException;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.util.text.StringUtil;
import org.apache.commons.lang.StringUtils;
import org.jdom.Element;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

/**
 */
public class WebpackDevServerRunConfiguration extends RunConfigurationBase implements RunConfiguration {
    private String portNumber;
    private String webPackConfigFile;
    private NodeJsInterpreterRef interpreterRef;
    private String nodeOptions;
    private String nodeModulesDir;
    private String workingDir;

    public WebpackDevServerRunConfiguration(Project project, ConfigurationFactoryEx<RunConfiguration> factory, String name) {
        super(project, factory, name);
    }

    @NotNull
    @Override
    public SettingsEditor<? extends RunConfiguration> getConfigurationEditor() {
        return new WebpackDevServerRunConfigurationEditor(getProject());
    }

    @Nullable
    @Override
    public RunProfileState getState(@NotNull Executor executor, @NotNull ExecutionEnvironment executionEnvironment) throws ExecutionException {
        return new WebpackDevServerRunProfileState(executionEnvironment, this);
    }

    @Override
    public void readExternal(Element element) throws InvalidDataException {
        super.readExternal(element);
        this.portNumber = element.getChildText("port-number");
        this.webPackConfigFile = toSystemDependentNameOrNull(element.getChildText("config-file"));
        this.interpreterRef = NodeJsInterpreterRef.create(StringUtil.notNullize(element.getChildText("node-interpreter")));
        this.nodeOptions = element.getChildText("node-options");
        this.nodeModulesDir = toSystemDependentNameOrNull(element.getChildText("node-modules-dir"));
        this.workingDir = toSystemDependentNameOrNull(element.getChildText("working-dir"));
    }

    @Nullable
    private static String toSystemDependentNameOrNull(String path) {
        return StringUtil.isNotEmpty(path) ? FileUtil.toSystemDependentName(path) : null;
    }

    @Override
    public void writeExternal(Element element) throws WriteExternalException {
        super.writeExternal(element);
        if (StringUtils.isNotBlank(this.portNumber)) {
            element.addContent(createElement("port-number", this.portNumber));
        }
        if (StringUtils.isNotBlank(this.webPackConfigFile)) {
            element.addContent(createElement("config-file", toSystemIndependentNameOrEmpty(this.webPackConfigFile)));
        }
        if (null != this.interpreterRef) {
            element.addContent(createElement("node-interpreter", this.interpreterRef.getReferenceName()));
        }
        if (StringUtils.isNotBlank(this.nodeOptions)) {
            element.addContent(createElement("node-options", this.nodeOptions));
        }
        if (StringUtils.isNotBlank(this.nodeModulesDir)) {
            element.addContent(createElement("node-modules-dir", toSystemIndependentNameOrEmpty(this.nodeModulesDir)));
        }
        if (StringUtils.isNotBlank(this.workingDir)) {
            element.addContent(createElement("working-dir", toSystemIndependentNameOrEmpty(this.workingDir)));
        }
    }

    @NotNull
    private String toSystemIndependentNameOrEmpty(String path) {
        return StringUtil.isNotEmpty(path) ? FileUtil.toSystemIndependentName(path) : "";
    }

    @NotNull
    private Element createElement(String name, String value) {
        Element element = new Element(name);
        element.setText(value);
        return element;
    }

    @Override
    public void checkConfiguration() throws RuntimeConfigurationException {
        //TODO
    }

    public void setPortNumber(String portNumber) {
        this.portNumber = portNumber;
    }

    public String getPortNumber() {
        return portNumber;
    }

    public void setWebPackConfigFile(String webPackConfigFile) {
        this.webPackConfigFile = webPackConfigFile;
    }

    public String getWebPackConfigFile() {
        return webPackConfigFile;
    }

    public NodeJsInterpreterRef getInterpreterRef() {
        return interpreterRef;
    }

    public void setInterpreterRef(NodeJsInterpreterRef interpreterRef) {
        this.interpreterRef = interpreterRef;
    }

    public String getNodeOptions() {
        return nodeOptions;
    }

    public void setNodeOptions(String nodeOptions) {
        this.nodeOptions = nodeOptions;
    }

    public String getNodeModulesDir() {
        return nodeModulesDir;
    }

    public void setNodeModulesDir(String nodeModulesDir) {
        this.nodeModulesDir = nodeModulesDir;
    }

    public NodeJsInterpreterRef getNodeInterpreterRef() {
        return interpreterRef;
    }

    public String getWorkingDir() {
        return workingDir;
    }

    public void setWorkingDir(String workingDir) {
        this.workingDir = workingDir;
    }

    @Override
    public String toString() {
        return "WebpackDevServerRunConfiguration{" +
                "portNumber='" + portNumber + '\'' +
                ", webPackConfigFile='" + webPackConfigFile + '\'' +
                ", interpreterRef=" + interpreterRef +
                ", nodeOptions='" + nodeOptions + '\'' +
                ", nodeModulesDir='" + nodeModulesDir + '\'' +
                ", workingDir='" + workingDir + '\'' +
                '}';
    }
}
