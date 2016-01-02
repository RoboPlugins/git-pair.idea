/**
 * Copyright (C) 2016 Robert A. Wallis.  All Rights Reserved.
 */
package gitpairpicker.git;

import com.intellij.execution.ExecutionException;
import com.intellij.execution.configurations.GeneralCommandLine;
import com.intellij.execution.process.OSProcessHandler;
import com.intellij.openapi.util.SystemInfo;
import com.intellij.openapi.util.text.StringUtil;
import com.intellij.util.EnvironmentUtil;
import org.jetbrains.annotations.Nullable;

import java.io.File;
import java.io.InputStream;
import java.util.Map;

/**
 * Run git commands.
 */
public class GitRunner {

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
     * Run `git config user.email` and return the current configured user.
     *
     * @return returns the current configured user email or null on error.
     */
    @Nullable
    public String runGitConfigUserEmail() {
        return runGitCommand("config", "user.email");
    }

    /**
     * Run a git command.  Finds git, and then runs the parameters.
     * For example, parameters "config", "user.email" will run `git config user.email`.
     *
     * @param parameters List of parameters after `git` to run.
     * @return output of the git command.
     */
    @Nullable
    public String runGitCommand(String... parameters) {
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
        OSProcessHandler processHandler;
        try {
            processHandler = new OSProcessHandler(gitConfigCommand);
        } catch (ExecutionException e) {
            System.out.println("Couldn't find current git user: " + e.getMessage());
            return null;
        }

        Process process = processHandler.getProcess();
        InputStream inputStream = process.getInputStream();
        try (java.util.Scanner scanner = new java.util.Scanner(inputStream)) {
            String output = scanner.useDelimiter("\\A").hasNext() ? scanner.next() : "";
            return output.trim();
        } catch (Exception e) {
            System.out.println("Error processing git output: " + e.getMessage());
            return null;
        }
    }
}
