package com.emt.steps;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;

import java.util.Map;

import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;

import com.emt.util.Parameter;


public class CheckoutFromGitTest extends StepTestFixture {

    @Parameter(values={"https://some.url.com/path/to/repo"}) String repo_url;
    @Parameter(values={"some_credentials_id"}, optional=true) String repo_creds;
    @Parameter(values={"my_branch"}, optional=true) String branch;
    @Parameter(values={"some/relative/target/dir"}, optional=true) String target_dir;

    @Theory
    public void callsCheckout(@FromDataPoints("args") Map args) {
        execute(args);
        verify(_steps).checkout(any(Map.class));
    }

}
