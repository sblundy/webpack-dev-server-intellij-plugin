package net.sblundy.plugins.webpack.devServer;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import net.sblundy.plugins.webpack.devServer.server.Server;
import org.jetbrains.annotations.NotNull;

import java.io.*;

/**
 */
public class WebpackDevServerRunProfileState extends CommandLineState implements RunProfileState {
    private final WebpackDevServerRunConfiguration configuration;
    private final Server server;

    public WebpackDevServerRunProfileState(ExecutionEnvironment environment, WebpackDevServerRunConfiguration configuration) {
        super(environment);
        this.configuration = configuration;

        this.server = environment.getProject().getComponent(Server.class);
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        GeneralCommandLine commandLine = createCommandLine();
        return new KillableColoredProcessHandler(commandLine, true);
    }

    @NotNull
    private GeneralCommandLine createCommandLine() throws ExecutionException {
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.withEnvironment("NODE_PATH", this.server.getLibraryCache(configuration.getNodeInterpreterRef()).getAbsolutePath() + File.pathSeparator + new File(configuration.getNodeModulesDir()).getAbsolutePath());
        commandLine.withCharset(CharsetToolkit.UTF8_CHARSET);
        commandLine.setWorkDirectory(configuration.getWorkingDir());

        commandLine.setExePath(configuration.getNodeInterpreterRef().resolveAsLocal(getEnvironment().getProject()).getInterpreterSystemDependentPath());
        if (StringUtil.isNotEmpty(configuration.getNodeOptions().trim())) {
            commandLine.addParameters(ParametersList.parse(configuration.getNodeOptions().trim()));
        }
        File script = this.server.getScriptFile();
        commandLine.addParameter(script.getAbsolutePath());
        commandLine.addParameter(configuration.getWebPackConfigFile());
        commandLine.addParameter(configuration.getPortNumber());
        return commandLine;
    }

}
