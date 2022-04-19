package com.emt.common;

import java.util.logging.Logger

import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper;

import com.cloudbees.groovy.cps.NonCPS

import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.AffectedFile;
import hudson.scm.ChangeLogSet.Entry;

class ChangeSetUtils implements Serializable {

    private static final long serialVersionUID = 2L;
    private static final transient Logger logger = Logger.getLogger("com.emt.common.ChangeSetUtils");

    Object script = null;
    
    public ChangeSetUtils(Object script) { this.script = script; }

    Collection<String> getChangeLog() {
        List<String> changed_files = getChangedFiles(script.currentBuild);
        logger.fine("Changed files: " + changed_files);

        // Find files that are not mapped to the pipeline workspace
        List<String> unmapped_file_paths = changed_files.findAll { !script.fileExists(it) }
        logger.fine("Unmapped file paths: " + unmapped_file_paths);
        if (unmapped_file_paths.size() > 0) {
            return script.error("The change log contains unmapped files: " + unmapped_file_paths.join(', '))
        }
        return changed_files
    }

    @NonCPS
    private static List<String> getChangedFiles(RunWrapper build) {
        List<ChangeLogSet<? extends Entry>> changeLogSets = build.getChangeSets();
        logger.fine("Found " + changeLogSets.size() + " change sets");
        List<AffectedFile> changed_files = new ArrayList();

        for (ChangeLogSet change_set: changeLogSets) {
            int n_entries = 0;
            for (Object entry: change_set) ++n_entries;
            logger.fine("The change set contains " + n_entries + " entries.");
            for (Object entry: change_set) {
                List<AffectedFile> tmp = new ArrayList();
                tmp.addAll(((ChangeLogSet.Entry)entry).getAffectedFiles());
                logger.fine("The entry contains " + tmp.size() + " changed files");
                for (AffectedFile f: tmp) {
                    logger.fine("Affected file: " + f.getPath());
                    changed_files.add(f);
                }
            }
        }

        return changed_files.collect { it.path };
    }

}
