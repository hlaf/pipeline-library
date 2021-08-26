package com.emt.steps

@groovy.transform.InheritConstructors
class RunParallel extends BaseStep {
    Object execute(Map params=[:]) {
        Map<String, Closure> tasks = params.tasks
        int max_concurrent = params.n_workers

        def branches = [:]

        // setup a latch
        def latch = new java.util.concurrent.LinkedBlockingDeque(max_concurrent)
        // put a number of items into the queue to allow that number of branches to run
        for (int i=0;i<max_concurrent;i++) {
            latch.offer("$i")
        }

        for (String name: tasks.keySet()) {
            String name_copy = name
            branches[name_copy] = {
                def thing = null
                // this will not allow proceeding until there is something in the queue.
                _steps.waitUntil {
                    thing = latch.pollFirst();
                    return thing != null;
                }
                try {
                    tasks.get(name_copy).call()
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
