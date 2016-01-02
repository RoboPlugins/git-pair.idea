/**
 * Copyright (C) 2016 Robert A. Wallis.  All Rights Reserved.
 */
package gitpairpicker;

import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import gitpairpicker.ui.GitPairWidget;
import org.jetbrains.annotations.NotNull;

/**
 * Project Component to register into the IntelliJ plugin system.
 */
public class GitPairProjectComponent implements ProjectComponent {

    private Project mProject;

    /**
     * Called by IntelliJ in plugin.xml implementation-class.
     *
     * @param project current project.
     */
    public GitPairProjectComponent(Project project) {
        mProject = project;
    }

    @Override
    public void initComponent() {
    }

    @Override
    public void disposeComponent() {
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "gitpairpicker.GitPairProjectComponent";
    }

    @Override
    public void projectOpened() {
        // called when project is opened
        GitPairWidget gitPairWidget = new GitPairWidget(mProject);
        if (gitPairWidget.updateCurrentEmail()) {
            gitPairWidget.installWidgetToStatusBar(mProject);
        }
    }

    @Override
    public void projectClosed() {
        // called when project is being closed
    }
}
