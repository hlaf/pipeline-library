package com.emt.steps

@groovy.transform.InheritConstructors
class DockerImageExists extends BaseStep {

    def required_parameters = ['image_name']

    Object execute(Map parameters=[:]) {
        validateParameters(parameters)
        String image_name = parameters.image_name

        def ret = _steps.sh returnStatus:true, script: """
             docker inspect --type=image ${image_name} > /dev/null 2>&1
        """
        return ret == 0
    }
}
