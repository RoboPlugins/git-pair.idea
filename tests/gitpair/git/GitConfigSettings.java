package gitpair.git;

public class GitConfigSettings {
    private String oldGlobalName;
    private String oldGlobalEmail;
    private String oldLocalName;
    private String oldLocalEmail;

    public void save() {
        GitRunner gitRunner = new GitRunner(".");
        String globalName = gitRunner.runGitCommand("config", "--global", "user.name");
        if (globalName != null) {
            oldGlobalName = globalName.trim();
        }
        String globalEmail = gitRunner.runGitCommand("config", "--global", "user.email");
        if (globalEmail != null) {
            oldGlobalEmail = globalEmail.trim();
        }
        String localName = gitRunner.runGitCommand("config", "--local", "user.name");
        if (localName != null) {
            oldLocalName = localName.trim();
        }
        String localEmail = gitRunner.runGitCommand("config", "--local", "user.email");
        if (localEmail != null) {
            oldLocalEmail = localEmail.trim();
        }
    }

    public void restore() {
        GitRunner gitRunner = new GitRunner(".");
        if (oldGlobalName != null) {
            gitRunner.runGitCommand("config", "--global", "user.name", oldGlobalName);
        } else {
            gitRunner.runGitCommand("config", "--unset", "--global", "user.name");
        }
        if (oldGlobalEmail != null) {
            gitRunner.runGitCommand("config", "--global", "user.email", oldGlobalEmail);
        } else {
            gitRunner.runGitCommand("config", "--unset", "--global", "user.email");
        }
        if (oldLocalName != null) {
            gitRunner.runGitCommand("config", "user.name", oldLocalName);
        } else {
            gitRunner.runGitCommand("config", "--unset", "--local", "user.name");
        }
        if (oldLocalEmail != null) {
            gitRunner.runGitCommand("config", "user.email", oldLocalEmail);
        } else {
            gitRunner.runGitCommand("config", "--unset", "--local", "user.email");
        }
    }
}
