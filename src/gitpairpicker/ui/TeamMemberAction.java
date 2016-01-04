/*
 * Copyright (C) 2016 Robert A. Wallis, All Rights Reserved
 */
package gitpairpicker.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.project.DumbAwareAction;
import com.intellij.ui.popup.KeepingPopupOpenAction;
import com.intellij.util.PlatformIcons;
import com.intellij.util.ui.EmptyIcon;
import com.sun.istack.internal.NotNull;
import gitpairpicker.pairing.TeamMember;

import javax.swing.*;

/**
 * Represents a row in the {@link PairsPopupList}, an individual that could be pairing right now.
 */
@SuppressWarnings("ComponentNotRegistered") // It's dynamic, so registering it won't help.
public class TeamMemberAction extends DumbAwareAction implements KeepingPopupOpenAction {

    private final static Icon ON = PlatformIcons.CHECK_ICON;
    private final static Icon ON_SELECTED = PlatformIcons.CHECK_ICON_SELECTED;
    private final static Icon OFF = EmptyIcon.create(ON.getIconHeight());

    private TeamMember teamMember;
    private boolean isPaired;
    private TeamMemberActionPerformer teamMemberActionPerformer;

    /**
     * Create a row for a team member.
     *
     * @param teamMember model representing a team member.
     * @param isPaired   currently paired state.
     */
    public TeamMemberAction(@NotNull TeamMember teamMember, boolean isPaired, @NotNull TeamMemberActionPerformer actionPerformer) {
        super(teamMember.getName());
        this.teamMember = teamMember;
        this.isPaired = isPaired;
        this.teamMemberActionPerformer = actionPerformer;
        getTemplatePresentation().setIcon(isPaired ? ON : OFF);
        getTemplatePresentation().setSelectedIcon(ON_SELECTED);
    }

    @Override
    public void actionPerformed(AnActionEvent anActionEvent) {
        teamMemberActionPerformer.onTeamMemberActionPerformed(teamMember);
    }

    /**
     * Handle events from a TeamMemberAction.
     */
    public interface TeamMemberActionPerformer {
        /**
         * Event when a team member is clicked.
         *
         * @param teamMember that was clicked.
         */
        void onTeamMemberActionPerformed(TeamMember teamMember);
    }
}
