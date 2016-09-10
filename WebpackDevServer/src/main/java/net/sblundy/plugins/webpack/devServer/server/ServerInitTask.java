package net.sblundy.plugins.webpack.devServer.server;

import com.google.common.io.Files;
import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterRef;
import com.intellij.javascript.nodejs.interpreter.local.NodeJsLocalInterpreter;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressIndicator;
import com.intellij.openapi.progress.Task;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.io.FileUtil;
import com.intellij.openapi.vfs.CharsetToolkit;
import com.intellij.util.PathUtil;
import net.sblundy.plugins.webpack.devServer.WebpackDevServerBundle;
import net.sblundy.plugins.webpack.devServer.WebpackDevServerRunProfileState;
import org.jetbrains.annotations.NotNull;

import java.io.File;
import java.io.IOException;

/**
 */
public class ServerInitTask extends Task.WithResult<File, ExecutionException> {
    private static final Logger LOGGER = Logger.getInstance(ServerInitTask.class);

    private final NodeJsInterpreterRef interpreterRef;

    public ServerInitTask(Project project, NodeJsInterpreterRef interpreterRef) {
        super(project, WebpackDevServerBundle.message("setup.title"), true);
        this.interpreterRef = interpreterRef;
    }

    @Override
    protected File compute(@NotNull ProgressIndicator indicator) throws ExecutionException {
        File target = Files.createTempDir();
        LOGGER.debug("Installing at " + target.getAbsolutePath());
        indicator.setText(WebpackDevServerBundle.message("setup.step.initial"));
        indicator.setIndeterminate(true);
        try {
            FileUtil.copy(new File(getScriptDir(), "package.json"), new File(target, "package.json"));
        } catch (IOException e) {
            throw new ExecutionException("Error prepping server directory", e);
        }
        GeneralCommandLine commandLine = createCommandLine(target);
        CapturingProcessHandler handler = new CapturingProcessHandler(commandLine);
        indicator.setText(WebpackDevServerBundle.message("setup.step.downloading"));
        ProcessOutput output = handler.runProcessWithProgressIndicator(indicator);

        if (output.isCancelled()) {
            throw new ExecutionException("Install cancelled");
        } else if (output.getExitCode() != 0) {
            LOGGER.warn("Install failed\n" + output.getStdout() + "\n" + output.getStderr());
            throw new ExecutionException("Install failed");
        } else {
            indicator.setText(WebpackDevServerBundle.message("setup.step.cleanup"));
            File webpack = new File(target, "node_modules/webpack");
            if (!FileUtil.delete(webpack)) {
                throw new ExecutionException("Install cleanup failed");
            } else {
                return target;
            }
        }
    }

    @NotNull
    private GeneralCommandLine createCommandLine(File target) throws ExecutionException {
        GeneralCommandLine commandLine = new GeneralCommandLine();
        commandLine.withCharset(CharsetToolkit.UTF8_CHARSET);
        commandLine.setWorkDirectory(target);

        NodeJsLocalInterpreter interpreter = interpreterRef.resolveAsLocal(getProject());
        commandLine.setExePath(interpreter.getInterpreterSystemDependentPath());
        commandLine.addParameter(interpreter.getValidNpmCliJsFilePath());
        commandLine.addParameter("install");
        return commandLine;
    }

    @NotNull
    private File getScriptDir() {
        String jarPath = PathUtil.getJarPathForClass(WebpackDevServerRunProfileState.class);
        File pluginClassesDir = new File(jarPath);

        return new File(pluginClassesDir.getParent(), "server");
    }
}
