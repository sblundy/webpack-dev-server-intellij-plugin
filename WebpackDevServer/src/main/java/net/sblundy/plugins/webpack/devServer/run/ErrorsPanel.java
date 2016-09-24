package net.sblundy.plugins.webpack.devServer.run;

import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.filters.TextConsoleBuilderFactory;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.ComponentContainer;

import javax.swing.*;
import java.util.List;

/**
 */
class ErrorsPanel implements ComponentContainer {

    private Project project;
    private ConsoleView console;

    ErrorsPanel(Project project) {
        this.project = project;
    }

    void setErrors(List<String> errorMessages) {
        console.clear();
        for (String message : errorMessages) {
            console.print(message, ConsoleViewContentType.ERROR_OUTPUT);
        }
    }

    @Override
    public JComponent getComponent() {
        if (console == null) {
            TextConsoleBuilder builder = TextConsoleBuilderFactory.getInstance().createBuilder(this.project);
            builder.setViewer(true);
            this.console = builder.getConsole();
        }
        return console.getComponent();
    }

    @Override
    public JComponent getPreferredFocusableComponent() {
        return console.getPreferredFocusableComponent();
    }

    @Override
    public void dispose() {
        console.dispose();
    }

    void clear() {
        console.clear();
    }
}
