package gitpairpicker.pairing;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * The team members that can pair.
 */
public class PairConfig {

    /**
     * Initialize the pair configuration.
     *
     * @param projectRootDir the project folder where the .pairs file is located.
     */
    public PairConfig(String projectRootDir) {

    }

    public List<TeamMember> findAllTeamMembers() {
        // TODO: WIP return all team members
        return null;
    }

    /**
     * Calculate what the git user.name should be given some team members.
     *
     * @param teamMembers list of team members.
     * @return human readable list of names.
     */
    public String generatePairName(TeamMember... teamMembers) {
        if (teamMembers.length == 0) {
            return null;
        }

        ArrayList<TeamMember> teamArray = new ArrayList<>();
        Collections.addAll(teamArray, teamMembers);
        Collections.sort(teamArray, new AlphebeticalName());

        StringBuilder sb = new StringBuilder();
        int size = teamArray.size();
        for (int i = 0; i < size; i++) {
            if (size == 2 && i == 1) {
                sb.append(" & ");
            } else if (i > 0 && i == size - 1) {
                // oxford comma
                sb.append(", and ");
            } else if (i > 0) {
                sb.append(", ");
            }
            sb.append(teamArray.get(i).getName());
        }

        return sb.toString();
    }

    /**
     * Calculate what the git user.email should be given some team members.
     *
     * @param teamMembers list of team emmbers.
     * @return emailable email for the team.
     */
    public String generatePairEmail(TeamMember... teamMembers) {
        return null;
    }

    /**
     * Compare two TeamMembers by name.
     */
    private static class AlphebeticalName implements Comparator<TeamMember> {
        @Override
        public int compare(TeamMember o1, TeamMember o2) {
            if (o1 == null) {
                return 1; // null at end
            }
            if (o2 == null) {
                return -1; // null at end
            }
            return o1.getName().compareTo(o2.getName());
        }
    }

}
