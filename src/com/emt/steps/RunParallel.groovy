package com.emt.steps

@groovy.transform.InheritConstructors
class RunParallel extends BaseStep {
    Object execute(Map params=[:]) {
        Map<String, Closure> tasks = params.tasks

        def branches = [:]

        // setup a latch
        int max_concurrent = 2
        def latch = new java.util.concurrent.LinkedBlockingDeque(max_concurrent)
        // put a number of items into the queue to allow that number of branches to run
        for (int i=0;i<max_concurrent;i++) {
            latch.offer("$i")
        }

        for (String name: tasks.keySet()) {
            branches[name] = {
                def thing = null
                // this will not allow proceeding until there is something in the queue.
                _steps.waitUntil {
                    thing = latch.pollFirst();
                    return thing != null;
                }
                try {
                    tasks.get(name).call()
                }
                finally {
                    // put something back into the queue to allow others to proceed
                    latch.offer(thing)
                }
            }
        }
        _steps.parallel(branches)
    }
}
