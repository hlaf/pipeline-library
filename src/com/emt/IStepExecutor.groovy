package com.emt

/**
 * Interface for calling any necessary Jenkins steps. This will be mocked in unit tests.
 */
abstract class IStepExecutor {
	
	public IDockerNamespace docker
	public ICurrentBuildNamespace currentBuild;
	public Map<String, String> env;
	
    abstract int sh(String command)
	abstract String sh(Map params)
    abstract void echo(String message)
    abstract void error(String message)
	Object node(String name, Closure body) { return body() }
	Object sshagent(List list, Closure body) { return body() }
	
	// TODO: Find a way to get rid of these declarations
	abstract void deletePuppetCertificate(String cert_name, String manager_node);
	abstract String getDnsDomainName();
	abstract boolean dockerImageExists(String name);
	abstract void createPuppetDockerfile(Map params=[:]);
	abstract Object initializeVirtualEnv();
}

interface ICurrentBuildNamespace {
	List<String> getBuildCauses(String cause)
}

interface IDockerNamespace {
	
	Object build(String image_name, String extra_options)
	Object image(String image_name)

}