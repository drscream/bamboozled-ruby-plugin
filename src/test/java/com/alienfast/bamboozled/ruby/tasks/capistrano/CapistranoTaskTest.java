package com.alienfast.bamboozled.ruby.tasks.capistrano;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.is;
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
 * Test the Capistrano Ruby task.
 */
@RunWith( MockitoJUnitRunner.class )
public class CapistranoTaskTest extends AbstractTaskTest {

    private static final String DEPLOY_SETUP_TASKS = "deploy:setup";

    private CapistranoTask capistranoTask = new CapistranoTask();

    @Override
    @Before
    public void setUp() throws Exception {

        this.capistranoTask.setEnvironmentVariableAccessor( getEnvironmentVariableAccessor() );
        this.capistranoTask.setProcessService( getProcessService() );
        this.capistranoTask.setRubyLocatorServiceFactory( getRubyLocatorServiceFactory() );
        this.capistranoTask.setCapabilityContext( getCapabilityContext() );

        when( getCapability().getValue() ).thenReturn( getRubyRuntime().getRubyExecutablePath() );
        when( getCapabilitySet().getCapability( getRubyLabel().toCapabilityKey() ) ).thenReturn( getCapability() );
        when( getCapabilityContext().getCapabilitySet() ).thenReturn( getCapabilitySet() );

        setupBuildContext( this.capistranoTask );
    }

    @Test
    @Override
    public void testBuildCommandList() throws RuntimeLocatorException {

        getConfigurationMap().put( "ruby", getRubyRuntime().getRubyRuntimeName() );
        getConfigurationMap().put( "tasks", DEPLOY_SETUP_TASKS );
        getConfigurationMap().put( "bundleexec", "true" );
        getConfigurationMap().put( "verbose", "false" );
        getConfigurationMap().put( "debug", "false" );

        when( getRubyLocatorServiceFactory().acquireRubyLocator( eq( "RVM" ) ) ).thenReturn( getRvmRubyLocator() );

        when( getRvmRubyLocator().getRubyRuntime( getRubyRuntime().getRubyRuntimeName() ) ).thenReturn( getRubyRuntime() );
        when(
                getRvmRubyLocator().buildExecutablePath(
                        getRubyRuntime().getRubyRuntimeName(),
                        getRubyExecutablePath(),
                        CapistranoCommandBuilder.CAP_COMMAND ) ).thenReturn( RvmFixtures.CAP_PATH );
        when(
                getRvmRubyLocator().buildExecutablePath(
                        getRubyRuntime().getRubyRuntimeName(),
                        getRubyExecutablePath(),
                        AbstractBundleExecCommandBuilder.BUNDLE_COMMAND ) ).thenReturn( RvmFixtures.BUNDLER_PATH );

        final List<String> commandList = this.capistranoTask.buildCommandList( getRubyLabel(), getConfigurationMap() );

        final Iterator<String> commandsIterator = commandList.iterator();

        assertThat( commandsIterator.next(), is( getRubyRuntime().getRubyExecutablePath() ) );
        assertThat( commandsIterator.next(), is( RvmFixtures.BUNDLER_PATH ) );
        assertThat( commandsIterator.next(), is( "exec" ) );
        assertThat( commandsIterator.next(), is( "cap" ) );
        assertThat( commandsIterator.next(), is( DEPLOY_SETUP_TASKS ) );

        assertThat( commandList.size(), is( 5 ) );
    }

    @Test
    @Override
    public void testBuildEnvironment() throws RuntimeLocatorException {

        getConfigurationMap().put( "ruby", getRubyRuntime().getRubyRuntimeName() );

        when( getRubyLocatorServiceFactory().acquireRubyLocator( eq( "RVM" ) ) ).thenReturn( getRvmRubyLocator() );

        when(
                getRvmRubyLocator().buildEnv(
                        getRubyRuntime().getRubyRuntimeName(),
                        getRubyExecutablePath(),
                        Maps.<String, String> newHashMap() ) ).thenReturn( Maps.<String, String> newHashMap() );

        final Map<String, String> envVars = this.capistranoTask.buildEnvironment( getRubyLabel(), getConfigurationMap() );

        assertThat( envVars.size(), is( 0 ) );

    }
}
