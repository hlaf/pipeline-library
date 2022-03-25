package com.emt.common;

import java.util.logging.Logger
import java.util.stream.StreamSupport

import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper;

import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.AffectedFile;
import hudson.scm.ChangeLogSet.Entry;

class ChangeSetUtils implements Serializable {

    private static final long serialVersionUID = 2L;
    private static final transient Logger logger = Logger.getLogger("com.emt.common.ChangeSetUtils");

    static List<AffectedFile> getChangedFiles(RunWrapper build) {
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

        return changed_files;
    }

}
