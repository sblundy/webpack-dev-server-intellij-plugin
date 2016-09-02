package net.sblundy.plugins.webpack.devServer.server;

import com.intellij.execution.ExecutionException;
import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterRef;
import com.intellij.openapi.components.AbstractProjectComponent;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.progress.ProgressManager;
import com.intellij.openapi.project.Project;
import com.intellij.util.PathUtil;
import net.sblundy.plugins.webpack.devServer.WebpackDevServerRunProfileState;
import org.jetbrains.annotations.NotNull;

import java.io.File;

/**
 */
public class ServerImpl extends AbstractProjectComponent implements Server, ProjectComponent {
    private static final Logger LOGGER = Logger.getInstance(ServerImpl.class);

    private File libCache;

    public ServerImpl(Project project) {
        super(project);
    }

    @Override
    @NotNull
    public File getScriptFile() {
        String jarPath = PathUtil.getJarPathForClass(WebpackDevServerRunProfileState.class);
        File pluginClassesDir = new File(jarPath);

        File script = new File(pluginClassesDir.getParent(), "js/server.js");

        LOGGER.assertTrue(script.exists(), "Script file not found:" + script.getAbsolutePath());
        return script;
    }

    @Override
    public File getLibraryCache(NodeJsInterpreterRef nodeInterpreterRef) throws ExecutionException {
        if (null == this.libCache) {
            ServerInitTask initTask = new ServerInitTask(super.myProject, nodeInterpreterRef);
            this.libCache = new File(ProgressManager.getInstance().run(initTask), "node_modules");
        }
        return this.libCache;
    }
}
