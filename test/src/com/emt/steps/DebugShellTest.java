package com.emt.steps;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assume.assumeTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.junit.After;
import org.junit.experimental.theories.FromDataPoints;
import org.junit.experimental.theories.Theory;
import org.mockito.ArgumentCaptor;
import org.mockito.stubbing.OngoingStubbing;

import com.emt.util.StateVar;
import com.google.common.collect.Lists;

public class DebugShellTest extends StepTestFixture {

    @StateVar List<String> commands;

    static final String CMD_CONTINUE = "continue";
    static final String CMD_DIV_BY_ZERO = "1 / 0";
    static final String CMD_SIMPLE_ARITHMETIC = "1 + 1";
    static final String CMD_GET_ENV_VAR = "env.MY_VAR";

    static final Map<String, String> expected_output;
    static {
        expected_output = new HashMap<>();
        expected_output.put(CMD_CONTINUE, ".*Resuming.*");
        expected_output.put(CMD_DIV_BY_ZERO, ".*Error:.*");
        expected_output.put(CMD_SIMPLE_ARITHMETIC, ".*2$");
    }

    public static Object[] commands_values() {
        return new Object[] {
                Lists.newArrayList(CMD_CONTINUE),
                Lists.newArrayList(CMD_DIV_BY_ZERO, CMD_CONTINUE),
                Lists.newArrayList(CMD_SIMPLE_ARITHMETIC, CMD_CONTINUE),
                Lists.newArrayList(CMD_GET_ENV_VAR, CMD_CONTINUE),
        };
    }

    private Map _state, _args;
    private List<String> _command_list;
    private ArgumentCaptor<String> _captor;

    protected void commonSetup(Map args, Map state) {
        _args = args;
        _state = state;
        _command_list = (List<String>) _state.get("commands");
        _captor = ArgumentCaptor.forClass(String.class);
        String var_value = Double.toString(Math.random());
        _steps.env.put("MY_VAR", var_value);
        expected_output.put(CMD_GET_ENV_VAR, ".*" + var_value + "$");
    }

    @Override
    public void postInst(BaseStep inst) {
        OngoingStubbing<Object> o = when(_steps.input(any(Map.class)));
        for (String command: _command_list) { o = o.thenReturn(command); }
    }

    // Invariant
    private void executesAllCommandsBeforeTheFirstContinueStatement() {
        // Executes all the commands preceding the first continue statement,
        // and only those.
        final int n_executed_commands = _command_list.indexOf(CMD_CONTINUE) + 1;
        verify(_steps, times(n_executed_commands)).echo(_captor.capture());
        
        List<String> command_outputs = _captor.getAllValues();
        for (int i = 0; i < n_executed_commands; ++i) {
            String cmd = _command_list.get(i);
            String cmd_output = command_outputs.get(i);
            assertThat(cmd_output, matchesRegex(expected_output.get(cmd)));
        }
    }
    
    @After
    public void check_invariants() {
        if (_executed_successfully) {
            executesAllCommandsBeforeTheFirstContinueStatement();
        }
    }

    @Theory
    public void baseCase(@FromDataPoints("args") Map args,
                         @FromDataPoints("state") Map state) {
        commonSetup(args, state);
        execute(args);
    }

    @Theory
    public void canAccessPipelineScope(@FromDataPoints("args") Map args,
                                       @FromDataPoints("state") Map state) {
        commonSetup(args, state);
        assumeTrue(_command_list.contains(CMD_GET_ENV_VAR));
        execute(args);
    }

    //@Theory
    //public void displaysErrorOnException(@FromDataPoints("args") Map args,
    //                                     @FromDataPoints("state") Map state) {
    //    commonSetup(args, state);
    //    assumeTrue(_command_list.contains(CMD_DIV_BY_ZERO));

    //    execute(args);
    //}

}
