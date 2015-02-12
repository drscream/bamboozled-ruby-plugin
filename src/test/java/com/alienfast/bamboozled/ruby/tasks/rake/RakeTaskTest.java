package com.alienfast.bamboozled.ruby.tasks.rake;

import static com.alienfast.bamboozled.ruby.tasks.AbstractBundleExecCommandBuilder.BUNDLE_EXEC_ARG;
import static org.junit.Assert.assertEquals;
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
public class RakeTaskTest extends AbstractTaskTest {

    private static final String DB_MIGRATE_TARGET = "db:migrate";

    private RakeTask rakeTask = new RakeTask();

    @Before
    public void setUp() throws Exception {

        this.rakeTask.setEnvironmentVariableAccessor( this.environmentVariableAccessor );
        this.rakeTask.setProcessService( this.processService );
        this.rakeTask.setCapabilityContext( this.capabilityContext );
        this.rakeTask.setRubyLocatorServiceFactory( this.rubyLocatorServiceFactory );
        this.rakeTask.setCapabilityContext( this.capabilityContext );

        when( this.capability.getValue() ).thenReturn( this.getRubyRuntime().getRubyExecutablePath() );
        when( this.capabilitySet.getCapability( this.getRubyLabel().toCapabilityLabel() ) ).thenReturn( this.capability );
        when( this.capabilityContext.getCapabilitySet() ).thenReturn( this.capabilitySet );
        
        setupBuildContext( this.rakeTask );
    }

    @Override
    @Test
    public void testBuildCommandList() throws RuntimeLocatorException {

        this.getConfigurationMap().put( "ruby", this.getRubyRuntime().getRubyRuntimeName() );
        this.getConfigurationMap().put( "targets", DB_MIGRATE_TARGET );
        this.getConfigurationMap().put( "bundleexec", "true" );
        this.getConfigurationMap().put( "verbose", "false" );
        this.getConfigurationMap().put( "trace", "false" );

        when( this.rubyLocatorServiceFactory.acquireRubyLocator( eq( "RVM" ) ) ).thenReturn( this.rvmRubyLocator );

        when( this.rvmRubyLocator.getRubyRuntime( this.getRubyRuntime().getRubyRuntimeName() ) ).thenReturn( this.getRubyRuntime() );

        when(
                this.rvmRubyLocator.buildExecutablePath(
                        this.getRubyRuntime().getRubyRuntimeName(),
                        this.getRubyExecutablePath(),
                        RakeCommandBuilder.RAKE_COMMAND ) ).thenReturn( RvmFixtures.RAKE_PATH );
        when(
                this.rvmRubyLocator.buildExecutablePath(
                        this.getRubyRuntime().getRubyRuntimeName(),
                        this.getRubyExecutablePath(),
                        AbstractBundleExecCommandBuilder.BUNDLE_COMMAND ) ).thenReturn( RvmFixtures.BUNDLER_PATH );

        final List<String> commandList = this.rakeTask.buildCommandList( this.getRubyLabel(), this.getConfigurationMap() );

        assertEquals( 5, commandList.size() );

        final Iterator<String> commandsIterator = commandList.iterator();

        assertEquals( this.getRubyRuntime().getRubyExecutablePath(), commandsIterator.next() );
        assertEquals( RvmFixtures.BUNDLER_PATH, commandsIterator.next() );
        assertEquals( BUNDLE_EXEC_ARG, commandsIterator.next() );
        assertEquals( RakeCommandBuilder.RAKE_COMMAND, commandsIterator.next() );
        assertEquals( DB_MIGRATE_TARGET, commandsIterator.next() );
    }

    @Override
    @Test
    public void testBuildEnvironment() throws RuntimeLocatorException {

        this.getConfigurationMap().put( "ruby", this.getRubyRuntime().getRubyRuntimeName() );

        when( this.rubyLocatorServiceFactory.acquireRubyLocator( eq( "RVM" ) ) ).thenReturn( this.rvmRubyLocator );

        when(
                this.rvmRubyLocator.buildEnv(
                        this.getRubyRuntime().getRubyRuntimeName(),
                        this.getRubyExecutablePath(),
                        Maps.<String, String> newHashMap() ) ).thenReturn( Maps.<String, String> newHashMap() );

        final Map<String, String> envVars = this.rakeTask.buildEnvironment( this.getRubyLabel(), this.getConfigurationMap() );

        assertTrue( envVars.size() == 0 );
    }
}
