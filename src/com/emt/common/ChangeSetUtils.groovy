package com.emt.common;

import java.util.logging.Logger

import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper;

import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.AffectedFile;
import hudson.scm.ChangeLogSet.Entry;

class ChangeSetUtils implements Serializable {

    private static final long serialVersionUID = 1L;
    private static final Logger logger = Logger.getLogger(ChangeSetUtils.class.getName());

    @CoverageIgnoreGenerated
    static List<AffectedFile> getChangedFiles(RunWrapper build) {
        List<ChangeLogSet<? extends Entry>> changeLogSets;
        try {
            changeLogSets = build.getChangeSets();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        logger.severe("Found " + changeLogSets.size() + " change sets");
        List<AffectedFile> res = new ArrayList();

        for (ChangeLogSet change_set: changeLogSets) {
            for (Object entry: change_set) {
                res.addAll(((ChangeLogSet.Entry)entry).getAffectedFiles());
            }
        }

        return res;
    }

}
