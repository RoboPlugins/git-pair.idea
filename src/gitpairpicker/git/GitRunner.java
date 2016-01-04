/*
 * Copyright (C) 2016 Robert A. Wallis, All Rights Reserved
 */

package gitpairpicker.git;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.CapturingProcessHandler;
import com.intellij.execution.process.ProcessOutput;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.EnvironmentUtil;
import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.util.Map;

/**
 * Run git commands.
 */
public class GitRunner {

    private static final String TAG = GitRunner.class.getSimpleName();
    private String projectBasePath;
    private String gitPathCache;

    /**
     * Initialize with the root path for the project.
     *
     * @param projectBasePath git root path, folder that contains .git.
     */
    public GitRunner(String projectBasePath) {
        this.projectBasePath = projectBasePath;
    }

    /**
     * Run `git config user.email` and return the current configured user.
     *
     * @return returns the current configured user email or null on error.
     */
    @Nullable
    public String getUserEmail() {
        String output = runGitCommand("config", "user.email");
        if (output != null) {
            return output.trim();
        }
        return null;
    }

    /**
     * Run `git config user.email example@example.com` and return the current configured user.
     *
     * @param fullEmail the current email of the user, for example "example@example.com".
     */
    public void setUserEmail(@NotNull String fullEmail) {
        runGitCommand("config", "user.email", fullEmail);
    }

    /**
     * Run `git config user.name` and return the current configured user.
     *
     * @return returns the current configured user email or null on error.
     */
    @Nullable
    public String getUserName() {
        String output = runGitCommand("config", "user.name");
        if (output != null) {
            return output.trim();
        }
        return null;
    }

    /**
     * Run `git config user.name Bubba` and return the current configured user.
     *
     * @param fullName the current name of the user, for example "Bubba".
     */
    public void setUserName(@NotNull String fullName) {
        runGitCommand("config", "user.name", fullName);
    }

    /**
     * Calculate git's path.
     *
     * @return path of the git executable, including the executable name.
     */
    @Nullable
    private String findGitExePath() {

        if (StringUtil.isNotEmpty(gitPathCache)) {
            return gitPathCache;
        }

        String path = EnvironmentUtil.getValue("PATH");
        if (!StringUtil.isNotEmpty(path)) {
            return null;
        }

        String[] dirs;
        if (SystemInfo.isWindows) {
            dirs = path.split(";");
        } else {
            dirs = path.split(":");
        }

        for (String dir : dirs) {
            String filename;
            if (SystemInfo.isWindows) {
                filename = dir + "/git.exe";
            } else {
                filename = dir + "/git";
            }
            File file = new File(filename);
            if (file.exists()) {
                gitPathCache = filename;
                return gitPathCache;
            }
        }

        return null;
    }

    /**
     * Run a git command.  Finds git, and then runs the parameters.
     * For example, parameters "config", "user.email" will run `git config user.email`.
     *
     * @param parameters List of parameters after `git` to run.
     * @return output of the git command.
     */
    @Nullable
    String runGitCommand(String... parameters) {
        String gitPath = findGitExePath();
        if (!StringUtil.isNotEmpty(gitPath)) {
            // git will fail, because we can't find it, so we exit early
            return null;
        }

        // prepare executable command
        GeneralCommandLine gitConfigCommand = new GeneralCommandLine();
        Map<String, String> env = EnvironmentUtil.getEnvironmentMap();
        gitConfigCommand.getEnvironment().clear();
        gitConfigCommand.getEnvironment().putAll(env);
        gitConfigCommand.setWorkDirectory(projectBasePath);
        gitConfigCommand.setExePath(gitPath);
        gitConfigCommand.addParameters(parameters);

        // execute command
        CapturingProcessHandler processHandler;
        try {
            processHandler = new CapturingProcessHandler(gitConfigCommand);
        } catch (ExecutionException e) {
            System.out.println(TAG + " OS error: " + e.getMessage());
            return null;
        }

        ProcessOutput processOutput = processHandler.runProcess(1000);
        if (processOutput.isTimeout()) {
            System.out.println(TAG + " " + gitConfigCommand.toString() + " Timed out after 1 second.");
            return null;
        }

        String output = processOutput.getStdout();

        if (processOutput.getExitCode() != 0) {
            System.out.println(TAG + " " + gitConfigCommand.toString() + " caused Git error: " + processOutput.getStderr());
            return null;
        }

        return output;
    }
}
