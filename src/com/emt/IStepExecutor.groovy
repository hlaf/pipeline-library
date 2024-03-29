package com.emt

import org.jenkinsci.plugins.workflow.support.steps.build.RunWrapper

import com.emt.common.CoverageIgnoreGenerated

/**
 * Interface for calling any necessary Jenkins steps. This will be mocked in unit tests.
 */
@CoverageIgnoreGenerated
abstract class IStepExecutor {
	
	public IDockerNamespace docker
	public RunWrapper currentBuild;
	public Map<String, String> env;
	
	abstract void archiveArtifacts(Map);
	abstract int bat(String command)
	abstract void checkout(Map);
	abstract void copyArtifacts(Map);
	abstract void deleteDir();
    Object dir(String path, Closure body) { return body() };
    abstract void echo(String message)
    abstract void emailext(Map);
    abstract void error(String message);
	abstract boolean fileExists(String file_path);
	abstract boolean isUnix();
	abstract void junit(Map);
	Object node(String name, Closure body) { return body() }
	abstract String pwd(Map);
	abstract Object readFile(String)
	abstract Object readJSON(Map);
	abstract int sh(String command)
	abstract Object sh(Map params)
	abstract specific(String);
	Object sshagent(List list, Closure body) { return body() }
	abstract void step(Map);
    abstract Object string(Map);
	Object tempDir(Closure body) { return body() }
	abstract void unstash(String stash_name);
	abstract void unstash(Map) throws hudson.AbortException;
	abstract Object usernamePassword(Map);
	Object withCredentials(List, Closure body) { return body() }
	abstract void writeFile(Map);
    @CoverageIgnoreGenerated
	void writeJSON(Map) { throw new RuntimeException("Step requires approval") };

	// TODO: Find a way to get rid of these declarations
	abstract void deletePuppetCertificate(Map params);
	abstract void echo(String);
	abstract String getDnsDomainName();
	abstract Map getPipelineConfig();
	abstract String getPackageVersion(Map params);
	abstract boolean dockerImageExists(String name);
	abstract void createPuppetDockerfile(Map params=[:]);
	abstract Object initializeVirtualEnv(Map params=[:]);
    abstract Object input(Map);
	abstract void saferStash(Map);
	abstract void saferUnstash(Map);
	abstract void setPackageVersion(Map);
	abstract void stashCoverageResult(Map);
	abstract void stash(Map);
	abstract boolean stashExists(String);
	abstract String unstashCoverageResult(Map) throws hudson.AbortException;
    abstract void writeAsJson(Map);
	abstract void publishJUnitReport();

    public IChangeSetUtils changeSetUtils;
}

interface IDockerNamespace {
	
	Object build(String image_name, String extra_options)
	Object image(String image_name)

}

interface IChangeSetUtils {
    Object getChangeLog()
}
