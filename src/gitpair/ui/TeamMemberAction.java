/*
 * Copyright (C) 2016 Robert A. Wallis, All Rights Reserved
 */
package gitpair.ui;

import com.intellij.openapi.actionSystem.AnActionEvent;
import com.intellij.openapi.actionSystem.ToggleAction;
import com.sun.istack.internal.NotNull;
import gitpair.pairing.TeamMember;

/**
 * Represents a row in the {@link PairsPopupList}, an individual that could be pairing right now.
 */
@SuppressWarnings("ComponentNotRegistered") // It's dynamic, so registering it won't help.
public class TeamMemberAction extends ToggleAction {

    private TeamMember teamMember;
    private TeamMemberActionPerformer teamMemberActionPerformer;

    /**
     * Create a row for a team member.
     *
     * @param teamMember model representing a team member.
     */
    public TeamMemberAction(@NotNull TeamMember teamMember, @NotNull TeamMemberActionPerformer actionPerformer) {
        super(teamMember.getName());
        this.teamMember = teamMember;
        this.teamMemberActionPerformer = actionPerformer;
    }

    @Override
    public boolean isSelected(AnActionEvent anActionEvent) {
        return teamMemberActionPerformer.isTeamMemberSelected(teamMember);
    }

    @Override
    public void setSelected(AnActionEvent anActionEvent, boolean b) {
        if (b) {
            teamMemberActionPerformer.onSelectTeamMember(teamMember);
        } else {
            teamMemberActionPerformer.onDeselectTeamMember(teamMember);
        }
    }

    /**
     * Handle events from a TeamMemberAction.
     */
    public interface TeamMemberActionPerformer {
        /**
         * @return true if the team member is selected.
         */
        boolean isTeamMemberSelected(TeamMember teamMember);

        /**
         * Make sure team member is selected.
         *
         * @param teamMember that was clicked.
         */
        void onSelectTeamMember(TeamMember teamMember);

        /**
         * Make sure team member is selected.
         *
         * @param teamMember that was clicked.
         */
        void onDeselectTeamMember(TeamMember teamMember);
    }
}
