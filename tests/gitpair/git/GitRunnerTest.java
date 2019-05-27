/*
 * Copyright (C) 2018 Robert A. Wallis, All Rights Reserved.
 */

package gitpair.git;

import junit.framework.TestCase;

/**
 * Make sure the git commands work.
 */
public class GitRunnerTest extends TestCase {

    private GitConfigSettings gitConfigSettings = new GitConfigSettings();

    @Override
    public void setUp() throws Exception {
        gitConfigSettings.save();
        super.setUp();
    }

    @Override
    public void tearDown() throws Exception {
        gitConfigSettings.restore();
        super.tearDown();
    }

    public void testGetUserEmail() {
        // GIVEN a system with git installed and a project configured with git
        GitRunner gitRunner = new GitRunner(".");
        gitRunner.runGitCommand("init");
        gitRunner.runGitCommand("config", "user.email", "setup@example.com");

        // WHEN we run `git config user.email`
        // THEN it should return the current user's email
        String configuredEmail = gitRunner.getUserEmail();
        assertNotNull(configuredEmail);
        assertEquals("setup@example.com", configuredEmail);
    }

    public void testSetUserEmail() {
        // GIVEN a system with git installed and a project configured with git
        GitRunner gitRunner = new GitRunner(".");
        gitRunner.runGitCommand("init");
        gitRunner.runGitCommand("config", "user.email", "setup@example.com");

        // WHEN we run `git config user.email testSetUserEmail@example.com`
        gitRunner.setUserEmail("testSetUserEmail@example.com", false);

        // THEN it should set current user's email
        assertEquals("testSetUserEmail@example.com", gitRunner.getUserEmail());
    }

    public void testSetUserEmailGlobal() {
        // GIVEN a system with git installed and a project configured with git
        GitRunner gitRunner = new GitRunner(".");
        gitRunner.runGitCommand("init");
        gitRunner.runGitCommand("config", "user.email", "setup@example.com");

        // WHEN we run `git config user.email testSetUserEmail@example.com`
        gitRunner.setUserEmail("testSetUserEmail@example.com", true);

        // THEN it should set current user's email, and the local should have been unset
        assertEquals("testSetUserEmail@example.com", gitRunner.getUserEmail());
    }

    public void testGetUserName() {
        // GIVEN a system with git installed and a project configured with git
        GitRunner gitRunner = new GitRunner(".");
        gitRunner.runGitCommand("init");
        gitRunner.runGitCommand("config", "user.name", "TestUserName");

        // WHEN we run `git config user.name`
        // THEN it should return the current user's name
        String configuredName = gitRunner.getUserName();
        assertNotNull(configuredName);
        assertEquals("TestUserName", configuredName);
    }

    public void testSetUserName() {
        // GIVEN a system with git installed and a project configured with git
        GitRunner gitRunner = new GitRunner(".");
        gitRunner.runGitCommand("init");
        gitRunner.runGitCommand("config", "user.name", "setup");

        // WHEN we run `git config user.name "Test User Name & Stuff"`
        gitRunner.setUserName("Test User Name & Stuff", false);

        // THEN it should set current user's name
        assertEquals("Test User Name & Stuff", gitRunner.getUserName());
    }

    public void testSetUserNameGlobal() {
        // GIVEN a system with git installed and a project configured with git
        GitRunner gitRunner = new GitRunner(".");
        gitRunner.runGitCommand("init");
        gitRunner.runGitCommand("config", "user.name", "setup");

        // WHEN we run `git config user.name "Test User Name & Stuff"`
        gitRunner.setUserName("Test Global User Name", true);

        // THEN it should set current user's name
        assertEquals("Test Global User Name", gitRunner.getUserName());
    }
}