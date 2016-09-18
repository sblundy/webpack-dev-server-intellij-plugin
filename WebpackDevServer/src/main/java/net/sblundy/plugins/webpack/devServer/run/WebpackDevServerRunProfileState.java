package net.sblundy.plugins.webpack.devServer.run;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.Executor;
import com.intellij.execution.configurations.CommandLineState;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.configurations.ParametersList;
import com.intellij.execution.configurations.RunProfileState;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.process.KillableColoredProcessHandler;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.runners.ExecutionEnvironment;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.openapi.vfs.CharsetToolkit;
import net.sblundy.plugins.webpack.devServer.config.WebpackDevServerRunConfiguration;
import net.sblundy.plugins.webpack.devServer.server.ServerMonitorFactory;
import net.sblundy.plugins.webpack.devServer.server.ServerStatusMonitor;
import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.*;

/**
 */
public class WebpackDevServerRunProfileState extends CommandLineState implements RunProfileState {
    private final WebpackDevServerRunConfiguration configuration;
    private final ServerMonitorFactory serverMonitorFactory;
    private ServerStatusMonitor monitor;

    public WebpackDevServerRunProfileState(ExecutionEnvironment environment, WebpackDevServerRunConfiguration configuration) {
        super(environment);
        this.configuration = configuration;

        this.serverMonitorFactory = environment.getProject().getComponent(ServerMonitorFactory.class);
    }

    @NotNull
    @Override
    protected ProcessHandler startProcess() throws ExecutionException {
        monitor = this.serverMonitorFactory.createStatusMonitor();
        GeneralCommandLine commandLine = createCommandLine();
        return new KillableColoredProcessHandler(commandLine, true);
    }

    @NotNull
    private GeneralCommandLine createCommandLine() throws ExecutionException {
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.withEnvironment("NODE_PATH", this.serverMonitorFactory.getLibraryCache(configuration.getInterpreterRef()).getAbsolutePath() + File.pathSeparator + new File(configuration.getNodeModulesDir()).getAbsolutePath());
        commandLine.withCharset(CharsetToolkit.UTF8_CHARSET);
        if (StringUtils.isNotBlank(configuration.getWorkingDir())) {
            commandLine.setWorkDirectory(configuration.getWorkingDir());
        }

        commandLine.setExePath(configuration.getInterpreterRef().resolveAsLocal(getEnvironment().getProject()).getInterpreterSystemDependentPath());
        if (StringUtils.isNotBlank(configuration.getNodeOptions())) {
            commandLine.addParameters(ParametersList.parse(configuration.getNodeOptions().trim()));
        }
        File script = this.serverMonitorFactory.getScriptFile();
        commandLine.addParameter(script.getAbsolutePath());
        commandLine.addParameter(new File(configuration.getWebPackConfigFile()).getAbsolutePath());
        commandLine.addParameter(configuration.getPortNumber());
        commandLine.addParameter(String.valueOf(monitor.getPort()));
        commandLine.addParameter(StringUtils.isBlank(configuration.getBasePath()) ? "/" : configuration.getBasePath());
        return commandLine;
    }

    @Nullable
    @Override
    protected ConsoleView createConsole(@NotNull Executor executor) throws ExecutionException {
        TextConsoleBuilder builder = this.getConsoleBuilder();

        return new WebpackServerView(getEnvironment().getProject(), builder, monitor);
    }
}
