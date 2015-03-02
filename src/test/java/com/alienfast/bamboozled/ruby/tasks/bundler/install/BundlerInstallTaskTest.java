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

    @Override
    @Before
    public void setUp() throws Exception {

        this.bundlerInstallTask.setEnvironmentVariableAccessor( getEnvironmentVariableAccessor() );
        this.bundlerInstallTask.setProcessService( getProcessService() );
        this.bundlerInstallTask.setRubyLocatorServiceFactory( getRubyLocatorServiceFactory() );
        this.bundlerInstallTask.setCapabilityContext( getCapabilityContext() );

        when( getCapability().getValue() ).thenReturn( getRubyRuntime().getRubyExecutablePath() );
        when( getCapabilitySet().getCapability( getRubyLabel().toCapabilityKey() ) ).thenReturn( getCapability() );
        when( getCapabilityContext().getCapabilitySet() ).thenReturn( getCapabilitySet() );

        getConfigurationMap().put( "ruby", getRubyRuntime().getRubyRuntimeName() );

        when( getRubyLocatorServiceFactory().acquireRubyLocator( eq( "RVM" ) ) ).thenReturn( getRvmRubyLocator() );

        setupBuildContext( this.bundlerInstallTask );
    }

    @Override
    @Test
    public void testBuildCommandList() throws RuntimeLocatorException {

        when( getRvmRubyLocator().getRubyRuntime( getRubyRuntime().getRubyRuntimeName() ) ).thenReturn( getRubyRuntime() );
        when(
                getRvmRubyLocator().buildExecutablePath(
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

        when( getRvmRubyLocator().getRubyRuntime( getRubyRuntime().getRubyRuntimeName() ) ).thenReturn( getRubyRuntime() );
        when(
                getRvmRubyLocator().buildExecutablePath(
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
                getRvmRubyLocator().buildEnv(
                        getRubyRuntime().getRubyRuntimeName(),
                        getRubyExecutablePath(),
                        Maps.<String, String> newHashMap() ) ).thenReturn( Maps.<String, String> newHashMap() );

        Map<String, String> envVars = this.bundlerInstallTask.buildEnvironment( getRubyLabel(), getConfigurationMap() );

        assertThat( envVars.size(), is( 0 ) );

    }

}
