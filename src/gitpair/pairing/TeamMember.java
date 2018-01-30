/*
 * Copyright (C) 2018 Robert A. Wallis, All Rights Reserved.
 */

package gitpair.pairing;

/**
 * An individual team member that can pair.
 */
public class TeamMember {

    private String initials;
    private String name;
    private String email;

    /**
     * Represents a programmer contributor that can check-in to git.
     *
     * @param initials usually 2-3 letter initials, ex. "raw" for "Robert A. Wallis".
     * @param name     human readable name for the commit, ex. "Robert A. Wallis".
     * @param email    email username, ex. "robert.wallis" for "robert.wallis@walnutcube.com".
     *                 When email is concatenated with + GMail will mail only the first person.
     */
    TeamMember(String initials, String name, String email) {
        this.initials = initials;
        this.name = name;
        this.email = email;
    }

    public String getInitials() {
        return initials;
    }

    public void setInitials(String initials) {
        this.initials = initials;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }
}
