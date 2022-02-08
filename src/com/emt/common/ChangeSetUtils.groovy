package com.emt.common;

import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper;

import com.cloudbees.groovy.cps.NonCPS

import groovy.transform.CompileStatic
import hudson.scm.ChangeLogSet;
import hudson.scm.ChangeLogSet.AffectedFile;
import hudson.scm.ChangeLogSet.Entry;

@CompileStatic
class ChangeSetUtils implements Serializable {

    private static final long serialVersionUID = 1L;

    @CoverageIgnoreGenerated
    @NonCPS
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
