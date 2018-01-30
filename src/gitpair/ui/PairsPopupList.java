/*
 * Copyright (C) 2018 Robert A. Wallis, All Rights Reserved.
 */

package gitpair.ui;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.project.Project;
import com.intellij.ui.popup.PopupFactoryImpl;
import gitpair.pairing.PairConfig;
import gitpair.pairing.PairController;
import gitpair.pairing.TeamMember;
import org.jetbrains.annotations.NotNull;

/**
 * Menu with list of pairs to choose.
 */
public class PairsPopupList extends PopupFactoryImpl.ActionGroupPopup {

    private static final String POPUP_TITLE = "git pair";

    /**
     * Make a new popup menu with a list of pairs.
     *
     * @param project     idea Project.
     * @param actionGroup List of pairs/actions.
     */
    private PairsPopupList(@NotNull Project project, ActionGroup actionGroup) {
        super(POPUP_TITLE, actionGroup, SimpleDataContext.getProjectContext(project), false, false, true, false, null, -1, null, null);
    }

    /**
     * Construct a PairsPopupList.  Adds the rows for you.
     *
     * @param project        idea Project.
     * @param pairController Pair logic controller.
     * @return a new popup menu.
     */
    public static PairsPopupList createPairsPopup(@NotNull Project project, @NotNull PairController pairController, @NotNull TeamMemberAction.TeamMemberActionPerformer teamMemberActionPerformer) {
        ActionGroup actionGroup = createActions(pairController, teamMemberActionPerformer);
        return new PairsPopupList(project, actionGroup);
    }

    /**
     * Create a list of idea actions that the user can choose, to change their pairs.
     *
     * @param pairController Pair logic controller for list of members.
     * @return list of actions.
     */
    private static ActionGroup createActions(@NotNull PairController pairController, @NotNull TeamMemberAction.TeamMemberActionPerformer teamMemberActionPerformer) {
        DefaultActionGroup defaultActionGroup = new DefaultActionGroup(null, false);
        PairConfig pairConfig = pairController.getPairConfig();

        for (TeamMember teamMember : pairConfig.getTeamMembers()) {
            defaultActionGroup.add(new TeamMemberAction(teamMember, teamMemberActionPerformer));
        }

        return defaultActionGroup;
    }
}
