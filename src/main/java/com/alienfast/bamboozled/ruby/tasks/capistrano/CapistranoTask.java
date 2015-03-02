package com.alienfast.bamboozled.ruby.tasks.capistrano;

import java.util.List;

import com.alienfast.bamboozled.ruby.rt.RubyLabel;
import com.alienfast.bamboozled.ruby.rt.RubyLocator;
import com.alienfast.bamboozled.ruby.rt.RubyRuntime;
import com.alienfast.bamboozled.ruby.rt.RuntimeLocatorException;
import com.alienfast.bamboozled.ruby.rt.rvm.RvmUtils;
import com.alienfast.bamboozled.ruby.tasks.AbstractRubyTask;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.google.common.base.Preconditions;

/**
 * Task which interacts with Capistrano.
 */
public class CapistranoTask extends AbstractRubyTask {

    public static final String TASKS = "tasks";
    public static final String BUNDLE_EXEC = "bundleexec";
    public static final String VERBOSE = "verbose";
    public static final String TRACE = "trace";

    @Override
    protected List<String> buildCommandList( RubyLabel rubyRuntimeLabel, ConfigurationMap config ) throws RuntimeLocatorException {

        final RubyLocator rubyLocator = getRubyLocator( rubyRuntimeLabel.getRubyRuntimeManager() ); // TODO Fix Error handling

        final String tasks = config.get( TASKS );
        Preconditions.checkArgument( tasks != null ); // TODO Fix Error handling

        final String bundleExecFlag = config.get( BUNDLE_EXEC );
        final String verboseFlag = config.get( VERBOSE );
        final String traceFlag = config.get( TRACE );

        final List<String> tasksList = RvmUtils.splitTokens( tasks );

        final RubyRuntime rubyRuntime = rubyLocator.getRubyRuntime( rubyRuntimeLabel.getRubyRuntime() ); // TODO Fix Error handling

        final String rubyExecutablePath = getRubyExecutablePath( rubyRuntimeLabel );

        return new CapistranoCommandBuilder(getCapabilityContext(), rubyLocator, rubyRuntime, rubyExecutablePath )
                .addRubyExecutable()
                .addIfBundleExec( bundleExecFlag )
                .addCapistranoExecutable( bundleExecFlag )
                .addIfTrace( traceFlag )
                .addIfVerbose( verboseFlag )
                .addTasks( tasksList )
                .build();
    }

}
