package com.alienfast.bamboozled.ruby.tasks.rake;

import java.util.List;

import com.alienfast.bamboozled.ruby.rt.RubyCapabilityDefaultsHelper;
import com.alienfast.bamboozled.ruby.rt.RubyLabel;
import com.alienfast.bamboozled.ruby.rt.RubyLocator;
import com.alienfast.bamboozled.ruby.rt.RubyRuntime;
import com.alienfast.bamboozled.ruby.rt.RuntimeLocatorException;
import com.alienfast.bamboozled.ruby.rt.rvm.RvmUtils;
import com.alienfast.bamboozled.ruby.tasks.AbstractRubyTask;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.google.common.base.Preconditions;

/**
 * Bamboo task which interfaces with RVM and runs ruby make (rake).
 */
public class RakeTask extends AbstractRubyTask {

    public static final String RAKE_FILE = "rakefile";
    public static final String RAKE_LIB_DIR = "rakelibdir";
    public static final String TARGETS = "targets";
    public static final String XVFB_RUN = "xvfbrun";
    public static final String BUNDLE_EXEC = "bundleexec";

    public static final String VERBOSE = "verbose";
    public static final String TRACE = "trace";

    @Override
    protected List<String> buildCommandList( RubyLabel rubyRuntimeLabel, ConfigurationMap config ) throws RuntimeLocatorException {

        final RubyLocator rvmRubyLocator = getRubyLocator( rubyRuntimeLabel.getRubyRuntimeManager() ); // TODO Fix Error handling

        final String rakeFile = config.get( RAKE_FILE );
        final String rakeLibDir = config.get( RAKE_LIB_DIR );

        final String targets = config.get( TARGETS );
        Preconditions.checkArgument( targets != null ); // TODO Fix Error handling

        final String xvfbRunFlag = config.get( XVFB_RUN );
        final String bundleExecFlag = config.get( BUNDLE_EXEC );
        final String verboseFlag = config.get( VERBOSE );
        final String traceFlag = config.get( TRACE );

        final List<String> targetList = RvmUtils.splitTokens( targets );
        final RubyRuntime rubyRuntime = rvmRubyLocator.getRubyRuntime( rubyRuntimeLabel.getRubyRuntime() ); // TODO Fix Error handling
        final String rubyExecutablePath = getRubyExecutablePath( rubyRuntimeLabel );
        final String xvfbRunExecutablePath = getXvfbRunExecutablePath();
        

        // RAILS_ENV=test xvfb-run -a bundle exec rake parallel:features
        return new RakeCommandBuilder( rvmRubyLocator, rubyRuntime, rubyExecutablePath, xvfbRunExecutablePath )               
                .addIfXvfbRun( xvfbRunFlag )
                .addRubyExecutable()
                .addIfBundleExec( bundleExecFlag )
                .addRakeExecutable( bundleExecFlag )
                .addIfRakeFile( rakeFile )
                .addIfRakeLibDir( rakeLibDir )
                .addIfVerbose( verboseFlag )
                .addIfTrace( traceFlag )
                .addTargets( targetList )
                .build();
    }
    
    
    protected String getXvfbRunExecutablePath() {

        final Capability capability = this.getCapabilityContext().getCapabilitySet().getCapability( RubyCapabilityDefaultsHelper.XVFB_RUN_CAPABILITY );
        Preconditions.checkNotNull( capability, "Capability" );
        final String exe = capability.getValue();
        Preconditions.checkNotNull( exe, "xvfbRunExecutable" );
        return exe;
    }
}
