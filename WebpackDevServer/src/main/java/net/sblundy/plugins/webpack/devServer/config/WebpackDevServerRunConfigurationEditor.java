package net.sblundy.plugins.webpack.devServer.config;

import com.intellij.javascript.nodejs.interpreter.NodeJsInterpreterField;
import com.intellij.openapi.fileChooser.FileChooserDescriptor;
import com.intellij.openapi.fileChooser.FileChooserDescriptorFactory;
import com.intellij.openapi.options.ConfigurationException;
import com.intellij.openapi.options.SettingsEditor;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.TextComponentAccessor;
import com.intellij.openapi.ui.TextFieldWithBrowseButton;
import com.intellij.ui.RawCommandLineEditor;
import com.intellij.util.ui.FormBuilder;
import com.intellij.util.ui.SwingHelper;
import net.sblundy.plugins.webpack.devServer.WebpackDevServerBundle;
import org.jetbrains.annotations.NotNull;

import javax.swing.*;

/**
 */
class WebpackDevServerRunConfigurationEditor extends SettingsEditor<WebpackDevServerRunConfiguration> {
    private JPanel myComponent;

    private final NodeJsInterpreterField nodeJsInterpreterField;
    private final RawCommandLineEditor nodeJsOptions;
    private final TextFieldWithBrowseButton nodeModulesDirField;
    private final TextFieldWithBrowseButton workingDirField;
    private final TextFieldWithBrowseButton configFileName;
    private final JTextField portNumber;
    private final JTextField basePath;

    WebpackDevServerRunConfigurationEditor(@NotNull Project project) {
        this.nodeJsInterpreterField = new NodeJsInterpreterField(project, false);
        this.nodeJsOptions = new RawCommandLineEditor();
        this.nodeJsOptions.setDialogCaption(WebpackDevServerBundle.message("editor.node.options.caption"));
        this.nodeModulesDirField = createNodeModulesDirTextField(project);
        this.workingDirField = createWorkingDirTextField(project);
        this.portNumber = new JTextField();
        this.basePath = new JTextField();
        this.configFileName = createConfigFilePathTextField(project, this.nodeModulesDirField.getTextField());
        FormBuilder builder = new FormBuilder();
        builder.addLabeledComponent(WebpackDevServerBundle.message("editor.interpreter.label"), this.nodeJsInterpreterField);
        builder.addLabeledComponent(WebpackDevServerBundle.message("editor.node.options.label"), this.nodeJsOptions);
        builder.addLabeledComponent(WebpackDevServerBundle.message("editor.node_modules.label"), this.nodeModulesDirField);
        builder.addLabeledComponent(WebpackDevServerBundle.message("editor.working.dir.label"), this.workingDirField);
        builder.addLabeledComponent(WebpackDevServerBundle.message("editor.config.file.label"), this.configFileName);
        builder.addLabeledComponent(WebpackDevServerBundle.message("editor.port.label"), this.portNumber);
        builder.addLabeledComponent(WebpackDevServerBundle.message("editor.base.path.label"), this.basePath);
        this.myComponent = builder.getPanel();
    }


    @NotNull
    private static TextFieldWithBrowseButton createConfigFilePathTextField(@NotNull Project project, @NotNull JTextField workingDir) {
        FileChooserDescriptor descriptor = FileChooserDescriptorFactory.createSingleFileNoJarsDescriptor();
        TextFieldWithBrowseButton pathToJSFile = new TextFieldWithBrowseButton();
        pathToJSFile.addBrowseFolderListener(null, null, project, descriptor, TextComponentAccessor.TEXT_FIELD_WHOLE_TEXT);
        return pathToJSFile;
    }

    @Override
    protected void resetEditorFrom(@NotNull WebpackDevServerRunConfiguration config) {
        this.nodeJsInterpreterField.setInterpreterRef(config.getInterpreterRef());
        this.nodeJsOptions.setText(config.getNodeOptions());
        this.nodeModulesDirField.setText(config.getNodeModulesDir());
        this.workingDirField.setText(config.getWorkingDir());
        this.configFileName.setText(config.getWebPackConfigFile());
        this.portNumber.setText(config.getPortNumber());
        this.basePath.setText(config.getBasePath());
    }


    @Override
    protected void applyEditorTo(@NotNull WebpackDevServerRunConfiguration config) throws ConfigurationException {
        config.setInterpreterRef(this.nodeJsInterpreterField.getInterpreterRef());
        config.setNodeOptions(this.nodeJsOptions.getText());
        config.setNodeModulesDir(this.nodeModulesDirField.getText());
        config.setWorkingDir(this.workingDirField.getText());
        config.setWebPackConfigFile(this.configFileName.getText());
        config.setPortNumber(this.portNumber.getText());
        config.setBasePath(this.basePath.getText());
    }

    @NotNull
    private static TextFieldWithBrowseButton createNodeModulesDirTextField(@NotNull Project project) {
        TextFieldWithBrowseButton textFieldWithBrowseButton = new TextFieldWithBrowseButton();
        SwingHelper.installFileCompletionAndBrowseDialog(project, textFieldWithBrowseButton, "Working Directory", FileChooserDescriptorFactory.createSingleFolderDescriptor());
        return textFieldWithBrowseButton;
    }

    @NotNull
    private static TextFieldWithBrowseButton createWorkingDirTextField(@NotNull Project project) {
        TextFieldWithBrowseButton textFieldWithBrowseButton = new TextFieldWithBrowseButton();
        SwingHelper.installFileCompletionAndBrowseDialog(project, textFieldWithBrowseButton, "Working Directory", FileChooserDescriptorFactory.createSingleFolderDescriptor());
        return textFieldWithBrowseButton;
    }

    @NotNull
    @Override
    protected JComponent createEditor() {
        return this.myComponent;
    }
}
