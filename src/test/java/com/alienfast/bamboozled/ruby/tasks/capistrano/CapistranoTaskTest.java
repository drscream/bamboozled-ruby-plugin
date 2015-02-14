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

    @Before
    public void setUp() throws Exception {

        this.capistranoTask.setEnvironmentVariableAccessor( this.environmentVariableAccessor );
        this.capistranoTask.setProcessService( this.processService );
        this.capistranoTask.setRubyLocatorServiceFactory( this.rubyLocatorServiceFactory );
        this.capistranoTask.setCapabilityContext( this.capabilityContext );

        when( this.capability.getValue() ).thenReturn( this.getRubyRuntime().getRubyExecutablePath() );
        when( this.capabilitySet.getCapability( this.getRubyLabel().toCapabilityKey() ) ).thenReturn( this.capability );
        when( this.capabilityContext.getCapabilitySet() ).thenReturn( this.capabilitySet );

        setupBuildContext( this.capistranoTask );
    }

    @Test
    @Override
    public void testBuildCommandList() throws RuntimeLocatorException {

        this.getConfigurationMap().put( "ruby", this.getRubyRuntime().getRubyRuntimeName() );
        this.getConfigurationMap().put( "tasks", DEPLOY_SETUP_TASKS );
        this.getConfigurationMap().put( "bundleexec", "true" );
        this.getConfigurationMap().put( "verbose", "false" );
        this.getConfigurationMap().put( "debug", "false" );

        when( this.rubyLocatorServiceFactory.acquireRubyLocator( eq( "RVM" ) ) ).thenReturn( this.rvmRubyLocator );

        when( this.rvmRubyLocator.getRubyRuntime( this.getRubyRuntime().getRubyRuntimeName() ) ).thenReturn( this.getRubyRuntime() );
        when(
                this.rvmRubyLocator.buildExecutablePath(
                        this.getRubyRuntime().getRubyRuntimeName(),
                        this.getRubyExecutablePath(),
                        CapistranoCommandBuilder.CAP_COMMAND ) ).thenReturn( RvmFixtures.CAP_PATH );
        when(
                this.rvmRubyLocator.buildExecutablePath(
                        this.getRubyRuntime().getRubyRuntimeName(),
                        this.getRubyExecutablePath(),
                        AbstractBundleExecCommandBuilder.BUNDLE_COMMAND ) ).thenReturn( RvmFixtures.BUNDLER_PATH );

        final List<String> commandList = this.capistranoTask.buildCommandList( this.getRubyLabel(), this.getConfigurationMap() );

        final Iterator<String> commandsIterator = commandList.iterator();

        assertThat( commandsIterator.next(), is( this.getRubyRuntime().getRubyExecutablePath() ) );
        assertThat( commandsIterator.next(), is( RvmFixtures.BUNDLER_PATH ) );
        assertThat( commandsIterator.next(), is( "exec" ) );
        assertThat( commandsIterator.next(), is( "cap" ) );
        assertThat( commandsIterator.next(), is( DEPLOY_SETUP_TASKS ) );

        assertThat( commandList.size(), is( 5 ) );
    }

    @Test
    @Override
    public void testBuildEnvironment() throws RuntimeLocatorException {

        this.getConfigurationMap().put( "ruby", this.getRubyRuntime().getRubyRuntimeName() );

        when( this.rubyLocatorServiceFactory.acquireRubyLocator( eq( "RVM" ) ) ).thenReturn( this.rvmRubyLocator );

        when(
                this.rvmRubyLocator.buildEnv(
                        this.getRubyRuntime().getRubyRuntimeName(),
                        this.getRubyExecutablePath(),
                        Maps.<String, String> newHashMap() ) ).thenReturn( Maps.<String, String> newHashMap() );

        final Map<String, String> envVars = this.capistranoTask.buildEnvironment( this.getRubyLabel(), this.getConfigurationMap() );

        assertThat( envVars.size(), is( 0 ) );

    }
}
