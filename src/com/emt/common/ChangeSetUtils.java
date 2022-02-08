package com.emt.common;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.List;

import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper;

import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.AffectedFile;
import hudson.scm.ChangeLogSet.Entry;

class ChangeSetUtils implements Serializable {

    private static final long serialVersionUID = 1L;

    @CoverageIgnoreGenerated
    static List<AffectedFile> getChangedFiles(RunWrapper build) {
        List<ChangeLogSet<? extends Entry>> changeLogSets;
        try {
            changeLogSets = build.getChangeSets();
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
        List<AffectedFile> res = new ArrayList();

        for (ChangeLogSet change_set: changeLogSets) {
            for (Object entry: change_set) {
                res.addAll(((ChangeLogSet.Entry)entry).getAffectedFiles());
            }
        }

        return res;
    }

}
