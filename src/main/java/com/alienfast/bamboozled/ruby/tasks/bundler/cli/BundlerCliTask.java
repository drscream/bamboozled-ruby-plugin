package com.alienfast.bamboozled.ruby.tasks.bundler.cli;

import java.util.List;

import com.alienfast.bamboozled.ruby.rt.RubyLabel;
import com.alienfast.bamboozled.ruby.rt.RubyLocator;
import com.alienfast.bamboozled.ruby.rt.RubyRuntime;
import com.alienfast.bamboozled.ruby.rt.rvm.RvmUtils;
import com.alienfast.bamboozled.ruby.tasks.AbstractRubyTask;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.google.common.base.Preconditions;

/**
 * Bamboo task which interfaces with RVM and runs ruby make (rake).
 */
public class BundlerCliTask extends AbstractRubyTask {

    public static final String ARGUMENTS = "arguments";
    public static final String BUNDLE_EXEC = "bundleexec";

    public static final String VERBOSE = "verbose";
    public static final String TRACE = "trace";

    @Override
    protected List<String> buildCommandList( RubyLabel rubyRuntimeLabel, ConfigurationMap config ) {

        final String arguments = config.get( ARGUMENTS );
        Preconditions.checkArgument( arguments != null ); // TODO Fix Error handling

        final String bundleExecFlag = config.get( BUNDLE_EXEC );
        final String verboseFlag = config.get( VERBOSE );
        final String traceFlag = config.get( TRACE );

        final List<String> argumentList = RvmUtils.splitTokens( arguments );

        final RubyLocator rubyLocator = getRubyLocator( rubyRuntimeLabel.getRubyRuntimeManager() ); // TODO Fix Error handling
        final RubyRuntime rubyRuntime = rubyLocator.getRubyRuntime( rubyRuntimeLabel.getRubyRuntime() ); // TODO Fix Error handling
        final String rubyExecutablePath = getRubyExecutablePath( rubyRuntimeLabel );

        return new BundlerCliCommandBuilder( rubyLocator, rubyRuntime, rubyExecutablePath )
                .addRubyExecutable()
                .addBundleExecutable()
                .addIfBundleExec( bundleExecFlag )
                .addIfVerbose( verboseFlag )
                .addIfTrace( traceFlag )
                .addArguments( argumentList )
                .build();
    }

//    protected BundlerCliCommandBuilder newBuilder(RubyLabel rubyRuntimeLabel){
//        
//        final RubyLocator rubyLocator = getRubyLocator( rubyRuntimeLabel.getRubyRuntimeManager() ); // TODO Fix Error handling
//        final RubyRuntime rubyRuntime = rubyLocator.getRubyRuntime( rubyRuntimeLabel.getRubyRuntime() ); // TODO Fix Error handling
//        final String rubyExecutablePath = getRubyExecutablePath( rubyRuntimeLabel );
//
//        return (BundlerCliCommandBuilder) BundlerCliCommandBuilder.class.getConstructor( new Object[]{RubyLocator.class,RubyRuntime.class, String.class}).newInstance(new Object[]{rubyLocator, rubyRuntime, rubyExecutablePath});
//    }
}
