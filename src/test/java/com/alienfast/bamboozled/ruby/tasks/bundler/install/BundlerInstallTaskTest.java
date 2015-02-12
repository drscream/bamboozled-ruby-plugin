package com.alienfast.bamboozled.ruby.tasks.bundler.install;

import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
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
import com.alienfast.bamboozled.ruby.tasks.AbstractTaskTest;
import com.google.common.collect.Maps;

/**
 * Do some basic checking of the bundler task.
 */
@RunWith( MockitoJUnitRunner.class )
public class BundlerInstallTaskTest extends AbstractTaskTest {

    private BundlerInstallTask bundlerInstallTask = new BundlerInstallTask();

    @Before
    public void setUp() throws Exception {

        this.bundlerInstallTask.setEnvironmentVariableAccessor( this.environmentVariableAccessor );
        this.bundlerInstallTask.setProcessService( this.processService );
        this.bundlerInstallTask.setRubyLocatorServiceFactory( this.rubyLocatorServiceFactory );
        this.bundlerInstallTask.setCapabilityContext( this.capabilityContext );

        when( this.capability.getValue() ).thenReturn( getRubyRuntime().getRubyExecutablePath() );
        when( this.capabilitySet.getCapability( getRubyLabel().toCapabilityLabel() ) ).thenReturn( this.capability );
        when( this.capabilityContext.getCapabilitySet() ).thenReturn( this.capabilitySet );

        getConfigurationMap().put( "ruby", getRubyRuntime().getRubyRuntimeName() );

        when( this.rubyLocatorServiceFactory.acquireRubyLocator( eq( "RVM" ) ) ).thenReturn( this.rvmRubyLocator );

        setupBuildContext( this.bundlerInstallTask );
    }

    @Override
    @Test
    public void testBuildCommandList() throws RuntimeLocatorException {

        when( this.rvmRubyLocator.getRubyRuntime( getRubyRuntime().getRubyRuntimeName() ) ).thenReturn( getRubyRuntime() );
        when(
                this.rvmRubyLocator.buildExecutablePath(
                        getRubyRuntime().getRubyRuntimeName(),
                        getRubyExecutablePath(),
                        BundlerInstallCommandBuilder.BUNDLE_COMMAND ) ).thenReturn( RvmFixtures.BUNDLER_PATH );

        List<String> commandList = this.bundlerInstallTask.buildCommandList( getRubyLabel(), getConfigurationMap() );

        Iterator<String> commandTokens = commandList.iterator();

        assertThat( commandTokens.next(), is( getRubyRuntime().getRubyExecutablePath() ) );
        assertThat( commandTokens.next(), is( RvmFixtures.BUNDLER_PATH ) );
        assertThat( commandTokens.next(), is( "install" ) );

    }

    @Test
    public void testBuildCommandListWithPathAndBinStubs() throws RuntimeLocatorException {

        when( this.rvmRubyLocator.getRubyRuntime( getRubyRuntime().getRubyRuntimeName() ) ).thenReturn( getRubyRuntime() );
        when(
                this.rvmRubyLocator.buildExecutablePath(
                        getRubyRuntime().getRubyRuntimeName(),
                        getRubyExecutablePath(),
                        BundlerInstallCommandBuilder.BUNDLE_COMMAND ) ).thenReturn( RvmFixtures.BUNDLER_PATH );

        getConfigurationMap().put( "path", "gems" );
        getConfigurationMap().put( "binstubs", "true" );

        List<String> commandList = this.bundlerInstallTask.buildCommandList( getRubyLabel(), getConfigurationMap() );

        Iterator<String> commandTokens = commandList.iterator();

        assertThat( commandTokens.next(), is( getRubyRuntime().getRubyExecutablePath() ) );
        assertThat( commandTokens.next(), is( RvmFixtures.BUNDLER_PATH ) );
        assertThat( commandTokens.next(), is( "install" ) );
        assertThat( commandTokens.next(), is( "--path" ) );
        assertThat( commandTokens.next(), is( "gems" ) );
        assertThat( commandTokens.next(), is( "--binstubs" ) );
    }

    @Override
    @Test
    public void testBuildEnvironment() throws RuntimeLocatorException {

        when(
                this.rvmRubyLocator.buildEnv(
                        getRubyRuntime().getRubyRuntimeName(),
                        getRubyExecutablePath(),
                        Maps.<String, String> newHashMap() ) ).thenReturn( Maps.<String, String> newHashMap() );

        Map<String, String> envVars = this.bundlerInstallTask.buildEnvironment( getRubyLabel(), getConfigurationMap() );

        assertThat( envVars.size(), is( 0 ) );

    }

}
