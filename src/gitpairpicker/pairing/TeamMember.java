package gitpairpicker.pairing;

/**
 * An individual team member that can pair.
 */
public class TeamMember {

    private String tag;
    private String name;
    private String email;

    /**
     * Represents a programmer contributor that can checkin to git.
     *
     * @param tag   usually 2-3 letter initals, ex. "raw" for "Robert A. Wallis".
     * @param name  human readable name for the commit, ex. "Robert A. Wallis".
     * @param email email username, ex. "smilingrob" for "smilingrob@gmail.com".
     *              If concatenated with + gmail will mail the first person.
     */
    public TeamMember(String tag, String name, String email) {
        this.tag = tag;
        this.name = name;
        this.email = email;
    }

    public String getTag() {
        return tag;
    }

    public void setTag(String tag) {
        this.tag = tag;
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
