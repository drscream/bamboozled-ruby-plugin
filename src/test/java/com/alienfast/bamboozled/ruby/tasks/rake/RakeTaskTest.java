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

    @Override
    @Before
    public void setUp() throws Exception {

        super.setUp();
        this.rakeTask.setEnvironmentVariableAccessor( getEnvironmentVariableAccessor() );
        this.rakeTask.setProcessService( getProcessService() );
        this.rakeTask.setCapabilityContext( getCapabilityContext() );
        this.rakeTask.setRubyLocatorServiceFactory( getRubyLocatorServiceFactory() );
        this.rakeTask.setCapabilityContext( getCapabilityContext() );

        // setup xvfb-run
        //        when( this.xvfbRunCapability.getValue() ).thenReturn( "/usr/bin/xvfb-run");
        //        when( this.capabilitySet.getCapability( RubyCapabilityDefaultsHelper.XVFB_RUN_CAPABILITY ) ).thenReturn( this.xvfbRunCapability );

        setupBuildContext( this.rakeTask );
    }

    @Override
    @Test
    public void testBuildCommandList() throws RuntimeLocatorException {

        getConfigurationMap().put( "ruby", getRubyRuntime().getRubyRuntimeName() );
        getConfigurationMap().put( "targets", DB_MIGRATE_TARGET );
        getConfigurationMap().put( "bundleexec", "true" );
        getConfigurationMap().put( "verbose", "false" );
        getConfigurationMap().put( "trace", "false" );

        when( getRubyLocatorServiceFactory().acquireRubyLocator( eq( "RVM" ) ) ).thenReturn( getRvmRubyLocator() );

        when( getRvmRubyLocator().getRubyRuntime( getRubyRuntime().getRubyRuntimeName() ) ).thenReturn( getRubyRuntime() );

        when(
                getRvmRubyLocator().buildExecutablePath(
                        getRubyRuntime().getRubyRuntimeName(),
                        getRubyExecutablePath(),
                        RakeCommandBuilder.RAKE_COMMAND ) ).thenReturn( RvmFixtures.RAKE_PATH );
        when(
                getRvmRubyLocator().buildExecutablePath(
                        getRubyRuntime().getRubyRuntimeName(),
                        getRubyExecutablePath(),
                        AbstractBundleExecCommandBuilder.BUNDLE_COMMAND ) ).thenReturn( RvmFixtures.BUNDLER_PATH );

        final List<String> commandList = this.rakeTask.buildCommandList( getRubyLabel(), getConfigurationMap() );

        assertEquals( 5, commandList.size() );

        final Iterator<String> commandsIterator = commandList.iterator();

        assertEquals( getRubyRuntime().getRubyExecutablePath(), commandsIterator.next() );
        assertEquals( RvmFixtures.BUNDLER_PATH, commandsIterator.next() );
        assertEquals( BUNDLE_EXEC_ARG, commandsIterator.next() );
        assertEquals( RakeCommandBuilder.RAKE_COMMAND, commandsIterator.next() );
        assertEquals( DB_MIGRATE_TARGET, commandsIterator.next() );
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

        final Map<String, String> envVars = this.rakeTask.buildEnvironment( getRubyLabel(), getConfigurationMap() );

        assertTrue( envVars.size() == 0 );
    }
}
