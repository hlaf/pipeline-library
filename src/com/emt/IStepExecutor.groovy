package com.emt

/**
 * Interface for calling any necessary Jenkins steps. This will be mocked in unit tests.
 */
abstract class IStepExecutor {
	
	public IDockerNamespace docker
	public ICurrentBuildNamespace currentBuild;
	public Map<String, String> env;
	
	abstract void archiveArtifacts(Map);
	abstract int bat(String command)
	abstract void checkout(Map);
	abstract void deleteDir();
    Object dir(String path, Closure body) { return body() };
    abstract void echo(String message)
    abstract void emailext(Map);
    void error(String message) { throw new RuntimeException(message); }
	abstract boolean fileExists(String file_path);
	abstract boolean isUnix();
	abstract void junit(Map);
	Object node(String name, Closure body) { return body() }
	abstract String pwd(Map);
	abstract Object readJSON(Map);
	abstract int sh(String command)
	abstract String sh(Map params)
	Object sshagent(List list, Closure body) { return body() }
	abstract void step(Map);
	Object tempDir(Closure body) { return body() }
	abstract void unstash(String stash_name);
	abstract void unstash(Map) throws hudson.AbortException;
	abstract void writeJSON(Map);

	// TODO: Find a way to get rid of these declarations
	abstract void deletePuppetCertificate(String cert_name, String manager_node);
	abstract String getDnsDomainName();
	abstract Map getPipelineConfig();
	abstract boolean dockerImageExists(String name);
	abstract void createPuppetDockerfile(Map params=[:]);
	abstract Object initializeVirtualEnv();
	abstract void saferUnstash(Map);
	abstract void stashCoverageResult(Map);
	abstract void stash(Map);
	abstract boolean stashExists(String);
	abstract String unstashCoverageResult(Map) throws hudson.AbortException;
	abstract void publishJUnitReport();
}

interface ICurrentBuildNamespace {
	List<String> getBuildCauses(String cause)
}

interface IDockerNamespace {
	
	Object build(String image_name, String extra_options)
	Object image(String image_name)

}