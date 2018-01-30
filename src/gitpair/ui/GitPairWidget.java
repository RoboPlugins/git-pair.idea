/*
 * Copyright (C) 2018 Robert A. Wallis, All Rights Reserved.
 */

package gitpair.ui;

import com.intellij.openapi.application.ApplicationManager;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.ui.popup.ListPopup;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.openapi.wm.StatusBar;
import com.intellij.openapi.wm.StatusBarWidget;
import com.intellij.openapi.wm.WindowManager;
import com.intellij.openapi.wm.impl.status.EditorBasedWidget;
import com.intellij.util.Consumer;
import com.intellij.util.ObjectUtils;
import gitpair.git.GitRunner;
import gitpair.pairing.PairConfig;
import gitpair.pairing.PairController;
import gitpair.pairing.TeamMember;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.awt.event.MouseEvent;
import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;

/**
 * Visible menu to be shown in the bottom left to indicate which pair is active, and allow a choice of new pair.
 */
public class GitPairWidget extends EditorBasedWidget implements StatusBarWidget.MultipleTextValuesPresentation, StatusBarWidget.Multiframe, TeamMemberAction.TeamMemberActionPerformer {

    @Nullable
    private String selectedPair;

    private PairController pairController;

    /**
     * Widget to be shown in the bottom left to indicate which pair is active, and allow a choice of new pair.
     *
     * @param project from idea.
     */
    public GitPairWidget(@NotNull Project project) {
        super(project);
    }

    /**
     * Ask git who is the current user, and update our internal state.
     */
    public boolean updateState() {
        Project project = getProject();
        if (project == null) {
            return false;
        }

        String projectPath = project.getBasePath();
        if (projectPath == null) {
            return false;
        }

        String configFile = projectPath.concat("/.pairs");
        StringBuilder configYaml = new StringBuilder();
        try {
            BufferedReader br = new BufferedReader(new FileReader(configFile));
            String line;
            while ((line = br.readLine()) != null) {
                configYaml.append(line).append("\n");
            }
        } catch (IOException e) {
            System.out.println("Git Pair plugin couldn't open " + configFile + ": " + e.getMessage());
            return false;
        }
        PairConfig pairConfig = new PairConfig(configYaml.toString());
        GitRunner gitRunner = new GitRunner(projectPath);
        pairController = new PairController(pairConfig, gitRunner);
        pairController.init();
        updateWidgetView();

        return true;
    }

    @Override
    public StatusBarWidget copy() {
        return new GitPairWidget(ObjectUtils.assertNotNull(getProject()));
    }

    @Override
    @Nullable
    public ListPopup getPopupStep() {
        updateState(); // make sure we have the latest info that should have changed in the background
        return PairsPopupList.createPairsPopup(myProject, pairController, this);
    }

    @Override
    @Nullable
    public String getSelectedValue() {
        if (StringUtil.isEmpty(selectedPair))
            return "";
        return selectedPair;
    }

    @Override
    @NotNull
    public String getMaxValue() {
        return "";
    }

    @Override
    @NotNull
    public String ID() {
        return this.getClass().getName();
    }

    @Override
    @Nullable
    public WidgetPresentation getPresentation(@NotNull PlatformType platformType) {
        return this;
    }

    @Override
    @Nullable
    public String getTooltipText() {
        return selectedPair;
    }

    @Override
    @Nullable
    public Consumer<MouseEvent> getClickConsumer() {
        // has no effect since the click opens a list popup, and the consumer is not called for the MultipleTextValuesPresentation
        return null;
    }

    @Override
    public boolean isTeamMemberSelected(TeamMember teamMember) {
        return pairController.isPaired(teamMember);
    }

    @Override
    public void onSelectTeamMember(TeamMember teamMember) {
        if (!pairController.isPaired(teamMember)) {
            pairController.toggleTeamMember(teamMember);
        }
        updateWidgetView();
    }

    @Override
    public void onDeselectTeamMember(TeamMember teamMember) {
        if (pairController.isPaired(teamMember)) {
            pairController.toggleTeamMember(teamMember);
        }
        updateWidgetView();
    }

    /**
     * Insert this widget into the status bar in the correct position.
     *
     * @param project idea Project.
     */
    public void installWidgetToStatusBar(@NotNull final Project project) {
        ApplicationManager.getApplication().invokeLater(new Runnable() {
            @Override
            public void run() {
                StatusBar statusBar = WindowManager.getInstance().getStatusBar(project);
                if (statusBar != null && !isDisposed()) {
                    statusBar.addWidget(GitPairWidget.this, "after " + (SystemInfo.isMac ? "Encoding" : "InsertOverwrite"), project);
                    updateWidgetView();
                }
            }
        });
    }

    /**
     * Refresh the view on the status bar.
     */
    private void updateWidgetView() {
        selectedPair = pairController.getPairDisplayName();
        if (myStatusBar != null) {
            myStatusBar.updateWidget(ID());
        }
    }
}
