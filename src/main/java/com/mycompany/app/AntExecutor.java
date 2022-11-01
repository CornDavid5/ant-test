package com.mycompany.app;

import java.io.File;

import org.apache.tools.ant.BuildException;
import org.apache.tools.ant.DefaultLogger;
import org.apache.tools.ant.Project;
import org.apache.tools.ant.ProjectHelper;

public class AntExecutor {
    /**
     * To execute the default target specified in the Ant build.xml file
     *
     * @param buildXmlFileFullPath
     */
    public static boolean executeAntTask(String buildXmlFileFullPath) {
        return executeAntTask(buildXmlFileFullPath, null);
    }

    /**
     * To execute a target specified in the Ant build.xml file
     *
     * @param buildFilePath
     * @param target
     */
    public static boolean executeAntTask(String buildFilePath, String target) {
        boolean success = false;
        DefaultLogger consoleLogger = getConsoleLogger();

        // Prepare Ant project
        Project project = new Project();
        File buildFile = new File(buildFilePath);
        // project.setUserProperty("ant.file", buildFile.getAbsolutePath());
        // project.setUserProperty("inheritAll", "false");
        project.setUserProperty("test.name", "org.apache.cassandra.hints.HintsCatalogTest");
        project.setUserProperty("test.methods", "deleteHintsTest");
        project.setUserProperty("use.jdk11", "true");

        project.addBuildListener(consoleLogger);

        // Capture event for Ant script build start / stop / failure
        try {
            project.fireBuildStarted();
            project.init();
            ProjectHelper projectHelper = ProjectHelper.getProjectHelper();
            project.addReference("ant.projectHelper", projectHelper);
            projectHelper.parse(project, buildFile);

            // If no target specified then default target will be executed.
            String targetToExecute = (target != null && target.trim().length() > 0) ? target.trim()
                    : project.getDefaultTarget();

            project.executeTarget(targetToExecute);
            project.fireBuildFinished(null);
            success = true;
        } catch (BuildException buildException) {
            project.fireBuildFinished(buildException);
            throw new RuntimeException("!!! Unable to restart the IEHS App !!!", buildException);
        }

        return success;
    }

    /**
     * Logger to log output generated while executing ant script in console
     *
     * @return
     */
    private static DefaultLogger getConsoleLogger() {
        DefaultLogger consoleLogger = new DefaultLogger();
        consoleLogger.setErrorPrintStream(System.err);
        consoleLogger.setOutputPrintStream(System.out);
        consoleLogger.setMessageOutputLevel(Project.MSG_INFO);

        return consoleLogger;
    }

    /**
     * Main method to test code
     *
     * @param args
     */
    public static void main(String[] args) {
        executeAntTask("../cassandra/build.xml", "testsome");
    }
}