/*
 * Copyright (C) 2016 Robert A. Wallis, All Rights Reserved
 */

package gitpairpicker.git;

import com.intellij.testFramework.fixtures.LightPlatformCodeInsightFixtureTestCase;

/**
 * Make sure the git commands work.
 */
public class GitRunnerTest extends LightPlatformCodeInsightFixtureTestCase {

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
        gitRunner.setUserEmail("testSetUserEmail@example.com");

        // THEN it should set current user's email
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