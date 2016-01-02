/**
 * Copyright (C) 2016 Robert A. Wallis.  All Rights Reserved.
 */
package gitpairpicker.ui;

import com.intellij.openapi.actionSystem.ActionGroup;
import com.intellij.openapi.actionSystem.AnAction;
import com.intellij.openapi.actionSystem.DefaultActionGroup;
import com.intellij.openapi.actionSystem.impl.SimpleDataContext;
import com.intellij.openapi.project.Project;
import com.intellij.openapi.util.Condition;
import com.intellij.ui.popup.PopupFactoryImpl;
import org.jetbrains.annotations.NotNull;

/**
 * Menu with list of pairs to choose.
 */
public class PairsPopupList extends PopupFactoryImpl.ActionGroupPopup {

    private static final String POPUP_TITLE = "Git Pair";

    /**
     * Make a new popup menu with a list of pairs.
     *
     * @param project                  IntelliJ Project.
     * @param preselectActionCondition Which rows should be selected, which pairs are paired.
     * @param actionGroup              List of pairs/actions.
     */
    private PairsPopupList(@NotNull Project project, @NotNull Condition<AnAction> preselectActionCondition, ActionGroup actionGroup) {
        super(POPUP_TITLE, actionGroup, SimpleDataContext.getProjectContext(project), false, false, true, false, null, -1, preselectActionCondition, null);
    }

    /**
     * Construct a PairsPopupList.  Adds the rows for you.
     *
     * @param project IntelliJ Project.
     * @return a new popup menu.
     */
    public static PairsPopupList createPairsPopup(@NotNull Project project) {
        Condition<AnAction> preselectActionCondition = new GitPairPreselectCondition();
        ActionGroup actionGroup = createActions();
        return new PairsPopupList(project, preselectActionCondition, actionGroup);
    }

    /**
     * Create a list of IntelliJ actions that the user can choose, to change their pairs.
     *
     * @return list of actions.
     */
    private static ActionGroup createActions() {
        // TODO: WIP: make an action for each team member
        return new DefaultActionGroup(null, false);
    }

    /**
     * Handles selecting users (Action rows) that are currently paired.
     */
    private static class GitPairPreselectCondition implements Condition<AnAction> {
        @Override
        public boolean value(AnAction anAction) {
            return false;
        }
    }
}