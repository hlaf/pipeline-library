package com.emt.steps

import java.lang.annotation.Retention
import java.lang.annotation.RetentionPolicy

import hudson.model.Result

@Retention(RetentionPolicy.RUNTIME)
@interface CoverageIgnore2 {}

@groovy.transform.InheritConstructors
class VerifyCoverage extends BaseStep {
	
	public static final double DEFAULT_BASELINE = 0.85
	
	Object execute(Map parameters=[:]) {
		
		String key = parameters.key ?: ''
		
		// Copy the latest successful build's coverage.xml
		def previous_build = getLatestSuccessfulBuildWithArtifacts(_steps)
		def branch_rate_old
		def line_rate_old
		def branch_rate_new
		def line_rate_new

		if (previous_build == null) {
			_steps.echo "Couldn't find prior coverage results - using the default baseline for new projects"
	
			branch_rate_old = new BigDecimal(DEFAULT_BASELINE).setScale(2, java.math.RoundingMode.HALF_UP)
			line_rate_old = new BigDecimal(DEFAULT_BASELINE).setScale(2, java.math.RoundingMode.HALF_UP)
	
		} else {
			_steps.copyArtifacts(projectName: "${_steps.env.JOB_NAME}",
						         selector: _steps.specific("${previous_build.number}"),
						         target: 'previous_build', filter: '**/coverage.xml',
						         flatten: true);
	
			String xml_path_old = "${_steps.env.WORKSPACE}/previous_build/coverage.xml"
			(branch_rate_old, line_rate_old) = parseCoverageXml(_steps, xml_path_old)
		}
	
		String results_path_new = _steps.unstashCoverageResult(key: key)
		String xml_path_new = "${results_path_new}/coverage.xml"
		(branch_rate_new, line_rate_new) = parseCoverageXml(_steps, xml_path_new)
	
		_steps.echo "Baseline coverage metrics:"
		_steps.echo "  Branch rate: ${branch_rate_old}"
		_steps.echo "  Line rate  : ${line_rate_old}"
		
		_steps.echo "Current build's coverage metrics:"
		_steps.echo "  Branch rate: ${branch_rate_new}"
		_steps.echo "  Line rate  : ${line_rate_new}"
	
		// Fail the job if coverage decreases
		if (branch_rate_new < branch_rate_old || line_rate_new < line_rate_old) {
			if (previous_build != null) {
				_steps.error "Code coverage decreased from build ${previous_build.number} to build ${_steps.env.BUILD_NUMBER}"
			} else {
				_steps.error "Code coverage is too low"
			}
		}
	}
	
	@CoverageIgnore
	//@NonCPS
	def getLatestSuccessfulBuildWithArtifacts(context) {
		def b = context.currentBuild
		while (b.getPreviousBuild() != null) {
			b = b.getPreviousBuild()
			if (b.getRawBuild().result == Result.SUCCESS && b.getRawBuild().getHasArtifacts()) {
				return b;
			}
		};
		return null;
	}
	
	@CoverageIgnore2
	def parseCoverageXml(context, String xml_path) {
		context.echo "Reading ${xml_path}"
		def xml_text = context.readFile xml_path
		def p = new XmlParser()
		p.setFeature('http://apache.org/xml/features/disallow-doctype-decl', false)
		def cov_data = p.parseText(xml_text)
		def branch_rate = new BigDecimal(cov_data['@branch-rate']).setScale(2, java.math.RoundingMode.HALF_UP)
		def line_rate = new BigDecimal(cov_data['@line-rate']).setScale(2, java.math.RoundingMode.HALF_UP)
		return [branch_rate, line_rate]
	}
}

