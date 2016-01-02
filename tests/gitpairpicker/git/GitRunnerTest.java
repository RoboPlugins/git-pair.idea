package gitpairpicker.git;

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;

/**
 * Make sure the git commands work.
 */
public class GitRunnerTest extends LightPlatformCodeInsightFixtureTestCase {

    public void testRunGitConfigUserEmail() throws Exception {
        // GIVEN a system with git installed and a project configured with git
        GitRunner gitRunner = new GitRunner(getProject().getBasePath());

        // WHEN we run `git config user.email`
        // THEN it should return the current user's email
        assertNotNull(gitRunner.runGitConfigUserEmail());
    }

}