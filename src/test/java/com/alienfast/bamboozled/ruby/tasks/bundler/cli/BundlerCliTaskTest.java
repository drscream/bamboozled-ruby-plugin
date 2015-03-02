package com.alienfast.bamboozled.ruby.tasks.bundler.cli;

import static com.alienfast.bamboozled.ruby.tasks.AbstractBundleExecCommandBuilder.BUNDLE_EXEC_ARG;
import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.junit.Assert.assertTrue;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.Iterator;
import java.util.List;
import java.util.Map;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.alienfast.bamboozled.ruby.fixtures.RvmFixtures;
import com.alienfast.bamboozled.ruby.rt.RuntimeLocatorException;
import com.alienfast.bamboozled.ruby.tasks.AbstractBundleExecCommandBuilder;
import com.alienfast.bamboozled.ruby.tasks.AbstractTaskTest;
import com.google.common.collect.Maps;

/**
 * Do some basic checking of the rake task.
 */
@RunWith( MockitoJUnitRunner.class )
public class BundlerCliTaskTest extends AbstractTaskTest {

    private static final String ARGS_BINSTUBS = "binstubs";
    private static final String ARGS_ELASTIC_BEANSTALK = "elastic-beanstalk";
    private static final String ARGS_BINSTUBS_ELASTIC_BEANSTALK = ARGS_BINSTUBS + " " + ARGS_ELASTIC_BEANSTALK;

    private BundlerCliTask bundlerCliTask = new BundlerCliTask();

    @Override
    @Before
    public void setUp() throws Exception {

        this.bundlerCliTask.setEnvironmentVariableAccessor( getEnvironmentVariableAccessor() );
        this.bundlerCliTask.setProcessService( getProcessService() );
        this.bundlerCliTask.setCapabilityContext( getCapabilityContext() );
        this.bundlerCliTask.setRubyLocatorServiceFactory( getRubyLocatorServiceFactory() );
        this.bundlerCliTask.setCapabilityContext( getCapabilityContext() );

        when( getCapability().getValue() ).thenReturn( getRubyRuntime().getRubyExecutablePath() );
        when( getCapabilitySet().getCapability( getRubyLabel().toCapabilityKey() ) ).thenReturn( getCapability() );
        when( getCapabilityContext().getCapabilitySet() ).thenReturn( getCapabilitySet() );

        setupBuildContext( this.bundlerCliTask );
    }

    @Override
    @Test
    public void testBuildCommandList() throws RuntimeLocatorException {

        //        this.configurationMap.put( AbstractRubyTask.RUBY, this.rubyRuntime.getRubyRuntimeName() );
        getConfigurationMap().put( BundlerCliTask.ARGUMENTS, ARGS_BINSTUBS_ELASTIC_BEANSTALK );
        getConfigurationMap().put( BundlerCliTask.BUNDLE_EXEC, "true" );
        getConfigurationMap().put( BundlerCliTask.VERBOSE, "false" );
        getConfigurationMap().put( BundlerCliTask.TRACE, "false" );

        when( getRubyLocatorServiceFactory().acquireRubyLocator( eq( "RVM" ) ) ).thenReturn( getRvmRubyLocator() );
        when( getRvmRubyLocator().getRubyRuntime( getRubyRuntime().getRubyRuntimeName() ) ).thenReturn( getRubyRuntime() );
        when(
                getRvmRubyLocator().buildExecutablePath(
                        getRubyRuntime().getRubyRuntimeName(),
                        getRubyExecutablePath(),
                        AbstractBundleExecCommandBuilder.BUNDLE_COMMAND ) ).thenReturn( RvmFixtures.BUNDLER_PATH );

        final List<String> commandList = this.bundlerCliTask.buildCommandList( getRubyLabel(), getConfigurationMap() );

        assertThat( 5, equalTo( commandList.size() ) );

        final Iterator<String> commandsIterator = commandList.iterator();

        assertThat( getRubyRuntime().getRubyExecutablePath(), equalTo( commandsIterator.next() ) );
        assertThat( RvmFixtures.BUNDLER_PATH, equalTo( commandsIterator.next() ) );
        assertThat( BUNDLE_EXEC_ARG, equalTo( commandsIterator.next() ) );
        assertThat( ARGS_BINSTUBS, equalTo( commandsIterator.next() ) );
        assertThat( ARGS_ELASTIC_BEANSTALK, equalTo( commandsIterator.next() ) );
    }

    @Override
    @Test
    public void testBuildEnvironment() throws RuntimeLocatorException {

        getConfigurationMap().put( "ruby", getRubyRuntime().getRubyRuntimeName() );

        when( getRubyLocatorServiceFactory().acquireRubyLocator( eq( "RVM" ) ) ).thenReturn( getRvmRubyLocator() );

        when(
                getRvmRubyLocator().buildEnv(
                        getRubyRuntime().getRubyRuntimeName(),
                        getRubyExecutablePath(),
                        Maps.<String, String> newHashMap() ) ).thenReturn( Maps.<String, String> newHashMap() );

        final Map<String, String> envVars = this.bundlerCliTask.buildEnvironment( getRubyLabel(), getConfigurationMap() );

        assertTrue( envVars.size() == 0 );
    }
}
