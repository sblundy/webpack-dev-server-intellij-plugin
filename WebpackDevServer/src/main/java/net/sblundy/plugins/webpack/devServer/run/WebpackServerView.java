package net.sblundy.plugins.webpack.devServer.run;

import com.intellij.execution.filters.Filter;
import com.intellij.execution.filters.HyperlinkInfo;
import com.intellij.execution.filters.TextConsoleBuilder;
import com.intellij.execution.process.ProcessHandler;
import com.intellij.execution.ui.ConsoleView;
import com.intellij.execution.ui.ConsoleViewContentType;
import com.intellij.openapi.actionSystem.*;
import com.intellij.openapi.diagnostic.Logger;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Disposer;
import com.intellij.ui.tabs.JBTabs;
import com.intellij.ui.tabs.TabInfo;
import com.intellij.ui.tabs.impl.JBTabsImpl;
import net.sblundy.plugins.webpack.devServer.WebpackDevServerBundle;
import net.sblundy.plugins.webpack.devServer.server.Asset;
import net.sblundy.plugins.webpack.devServer.server.CompileStatusListener;
import net.sblundy.plugins.webpack.devServer.server.ServerStatusMonitor;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;
import java.awt.*;
import java.util.List;

/**
 */
class WebpackServerView implements ConsoleView {
    private static final Logger LOGGER = Logger.getInstance(WebpackServerView.class);

    private final ServerStatusMonitor monitor;
    private ConsoleView myConsoleView;
    private JPanel myComponent;
    private StatusPanel statusPanel;
    private AssetsPanel assetsPanel;
    private Project project;

    WebpackServerView(Project project, TextConsoleBuilder builder, ServerStatusMonitor monitor) {
        this.myConsoleView = builder.getConsole();
        this.monitor = monitor;
        this.project = project;
        Disposer.register(this, this.myConsoleView);
        Disposer.register(this, monitor);
    }

    @Override
    public void print(@NotNull String str, @NotNull ConsoleViewContentType contentType) {
        myConsoleView.print(str, contentType);
    }

    @Override
    public void clear() {
        myConsoleView.clear();
    }

    @Override
    public void scrollTo(int offset) {
        myConsoleView.scrollTo(offset);
    }

    @Override
    public void attachToProcess(ProcessHandler processHandler) {
        myConsoleView.attachToProcess(processHandler);
    }

    @Override
    public void setOutputPaused(boolean value) {
        myConsoleView.setOutputPaused(value);
    }

    @Override
    public boolean isOutputPaused() {
        return myConsoleView.isOutputPaused();
    }

    @Override
    public boolean hasDeferredOutput() {
        return myConsoleView.hasDeferredOutput();
    }

    @Override
    public void performWhenNoDeferredOutput(Runnable runnable) {
        myConsoleView.performWhenNoDeferredOutput(runnable);
    }

    @Override
    public void setHelpId(String helpId) {
        myConsoleView.setHelpId(helpId);
    }

    @Override
    public void addMessageFilter(Filter filter) {
        myConsoleView.addMessageFilter(filter);
    }

    @Override
    public void printHyperlink(String hyperlinkText, HyperlinkInfo info) {
        myConsoleView.printHyperlink(hyperlinkText, info);
    }

    @Override
    public int getContentSize() {
        return myConsoleView.getContentSize();
    }

    @Override
    public boolean canPause() {
        return myConsoleView.canPause();
    }

    @NotNull
    @Override
    public AnAction[] createConsoleActions() {
        return AnAction.EMPTY_ARRAY;
    }

    @Override
    public void allowHeavyFilters() {
        myConsoleView.allowHeavyFilters();
    }

    @Override
    public JComponent getComponent() {
        if (null == this.myComponent) {
            this.myComponent = new JPanel(new BorderLayout());
            this.assetsPanel = new AssetsPanel();
            Disposer.register(this, this.assetsPanel);
            JBTabs tabs = new JBTabsImpl(this.project);
            TabInfo consoleTab = new TabInfo(this.myConsoleView.getComponent());
            consoleTab.setPreferredFocusableComponent(this.myConsoleView.getPreferredFocusableComponent());
            consoleTab.setText(WebpackDevServerBundle.message("view.console.title"));
            TabInfo statsTab = new TabInfo(this.assetsPanel.getComponent());
            statsTab.setPreferredFocusableComponent(this.assetsPanel.getPreferredFocusableComponent());
            statsTab.setText(WebpackDevServerBundle.message("view.assets.title"));
            tabs.addTab(consoleTab);
            tabs.addTab(statsTab);
            this.myComponent.add(tabs.getComponent(), BorderLayout.CENTER);

            final ActionToolbar toolbar = ActionManager.getInstance().createActionToolbar(ActionPlaces.UNKNOWN, new DefaultActionGroup(this.myConsoleView.createConsoleActions()), false);
            this.myComponent.add(toolbar.getComponent(), BorderLayout.WEST);
            statusPanel = new StatusPanel();
            monitor.addListener(new CompileStatusListener() {
                @Override
                public void onInvalidated() {
                    statusPanel.setWarnMessage(WebpackDevServerBundle.message("status.message.invalidated"));
                }

                @Override
                public void onStart() {
                    statusPanel.setSuccessMessage(WebpackDevServerBundle.message("status.message.started"));
                }

                @Override
                public void onProgress(double progress, String message) {
                    statusPanel.setProgress(message, progress);
                }

                @Override
                public void onComplete(Boolean errors, List<Asset> assets) {
                    if (errors == Boolean.TRUE) {
                        statusPanel.setErrorMessage(WebpackDevServerBundle.message("status.message.complete.errors"));
                    } else {
                        statusPanel.setSuccessMessage(WebpackDevServerBundle.message("status.message.complete"));
                        assetsPanel.setAssets(assets);
                    }
                }

                @Override
                public void onFailure() {
                    statusPanel.setErrorMessage(WebpackDevServerBundle.message("status.message.error"));

                }
            });
            this.myComponent.add(statusPanel, BorderLayout.EAST);
        }
        return this.myComponent;
    }

    @Override
    public JComponent getPreferredFocusableComponent() {
        return myConsoleView.getPreferredFocusableComponent();
    }

    @Override
    public void dispose() {
        this.myConsoleView.dispose();
        this.assetsPanel.dispose();
        this.monitor.dispose();
    }
}
