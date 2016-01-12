/*
 * Copyright (C) 2016 Robert A. Wallis, All Rights Reserved
 */

package gitpair.git;

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;

/**
 * Make sure the git commands work.
 */
public class GitRunnerTest extends LightPlatformCodeInsightFixtureTestCase {

    private String oldGlobalEmail;
    private String oldGlobalName;

    @Override
    public void setUp() throws Exception {
        super.setUp();
        GitRunner gitRunner = new GitRunner(".");
        String globalName = gitRunner.runGitCommand("config", "--global", "user.name");
        if (globalName != null) {
            oldGlobalName = globalName.trim();
        }
        String globalEmail = gitRunner.runGitCommand("config", "--global", "user.email");
        if (globalEmail != null) {
            oldGlobalEmail = globalEmail.trim();
        }
    }

    @Override
    public void tearDown() throws Exception {
        super.tearDown();
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
    }

    public void testGetUserEmail() throws Exception {
        // GIVEN a system with git installed and a project configured with git
        GitRunner gitRunner = new GitRunner(getProject().getBasePath());
        gitRunner.runGitCommand("init");
        gitRunner.runGitCommand("config", "user.email", "setup@example.com");

        // WHEN we run `git config user.email`
        // THEN it should return the current user's email
        String configuredEmail = gitRunner.getUserEmail();
        assertNotNull(configuredEmail);
        assertEquals("setup@example.com", configuredEmail);
    }

    public void testSetUserEmail() throws Exception {
        // GIVEN a system with git installed and a project configured with git
        GitRunner gitRunner = new GitRunner(getProject().getBasePath());
        gitRunner.runGitCommand("init");
        gitRunner.runGitCommand("config", "user.email", "setup@example.com");

        // WHEN we run `git config user.email testSetUserEmail@example.com`
        gitRunner.setUserEmail("testSetUserEmail@example.com", false);

        // THEN it should set current user's email
        assertEquals("testSetUserEmail@example.com", gitRunner.getUserEmail());
    }

    public void testSetUserEmailGlobal() throws Exception {
        // GIVEN a system with git installed and a project configured with git
        GitRunner gitRunner = new GitRunner(getProject().getBasePath());
        gitRunner.runGitCommand("init");
        gitRunner.runGitCommand("config", "user.email", "setup@example.com");

        // WHEN we run `git config user.email testSetUserEmail@example.com`
        gitRunner.setUserEmail("testSetUserEmail@example.com", true);

        // THEN it should set current user's email, and the local should have been unset
        assertEquals("testSetUserEmail@example.com", gitRunner.getUserEmail());
    }

    public void testGetUserName() throws Exception {
        // GIVEN a system with git installed and a project configured with git
        GitRunner gitRunner = new GitRunner(getProject().getBasePath());
        gitRunner.runGitCommand("init");
        gitRunner.runGitCommand("config", "user.name", "TestUserName");

        // WHEN we run `git config user.name`
        // THEN it should return the current user's name
        String configuredName = gitRunner.getUserName();
        assertNotNull(configuredName);
        assertEquals("TestUserName", configuredName);
    }

    public void testSetUserName() throws Exception {
        // GIVEN a system with git installed and a project configured with git
        GitRunner gitRunner = new GitRunner(getProject().getBasePath());
        gitRunner.runGitCommand("init");
        gitRunner.runGitCommand("config", "user.name", "setup");

        // WHEN we run `git config user.name "Test User Name & Stuff"`
        gitRunner.setUserName("Test User Name & Stuff");

        // THEN it should set current user's name
        assertEquals("Test User Name & Stuff", gitRunner.getUserName());
    }
}