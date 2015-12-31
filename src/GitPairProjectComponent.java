import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.components.ProjectComponent;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.WindowManager;
import gitpairpicker.ui.GitPairWidget;
import org.jetbrains.annotations.NotNull;

/**
 * Project Component to register into the IntelliJ plugin system.
 */
public class GitPairProjectComponent implements ProjectComponent {

    private Project mProject;

    public GitPairProjectComponent(Project project) {
        mProject = project;
    }

    @Override
    public void initComponent() {
        // TODO: insert component initialization logic here
    }

    @Override
    public void disposeComponent() {
        // TODO: insert component disposal logic here
    }

    @Override
    @NotNull
    public String getComponentName() {
        return "GitPairProjectComponent";
    }

    @Override
    public void projectOpened() {
        // called when project is opened
        GitPairWidget gitPairWidget = new GitPairWidget(mProject);
        gitPairWidget.installWidgetToStatusBar(mProject);
    }

    @Override
    public void projectClosed() {
        // called when project is being closed
    }
}
