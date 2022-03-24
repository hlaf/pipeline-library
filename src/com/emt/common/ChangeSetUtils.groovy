package com.emt.common;

import java.util.logging.Logger

import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper;

import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.AffectedFile;
import hudson.scm.ChangeLogSet.Entry;

class ChangeSetUtils implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger("com.emt.common.ChangeSetUtils");

    @CoverageIgnoreGenerated
    static List<AffectedFile> getChangedFiles(RunWrapper build) {
        List<ChangeLogSet<? extends Entry>> changeLogSets;
        try {
            changeLogSets = build.getChangeSets();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.config("Found " + changeLogSets.size() + " change sets");
        List<AffectedFile> changed_files = new ArrayList();

        for (ChangeLogSet change_set: changeLogSets) {
            logger.config("The change set contains " + 
                          change_set.getItems().length + " entries.");
            for (Object entry: change_set) {
                List<AffectedFile> tmp = new ArrayList();
                tmp.addAll(((ChangeLogSet.Entry)entry).getAffectedFiles());
                logger.config("The entry contains " + tmp.size() + " changed files");
                for (AffectedFile f: tmp) {
                    logger.config("Affected file: " + f.getPath());
                    changed_files.add(f);
                }
            }
        }

        return changed_files;
    }

}
