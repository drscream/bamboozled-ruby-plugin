package com.alienfast.bamboozled.ruby.tasks;

import static org.mockito.Mockito.when;

import java.util.Map;

import org.mockito.Mock;

import com.alienfast.bamboozled.ruby.config.RubyBuildConfigurationPlugin;
import com.alienfast.bamboozled.ruby.fixtures.RvmFixtures;
import com.alienfast.bamboozled.ruby.rt.RubyLabel;
import com.alienfast.bamboozled.ruby.rt.RubyLocatorServiceFactory;
import com.alienfast.bamboozled.ruby.rt.RubyRuntime;
import com.alienfast.bamboozled.ruby.rt.rvm.RvmRubyLocator;
import com.atlassian.bamboo.build.BuildDefinition;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.configuration.ConfigurationMapImpl;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilitySet;

/**
 * Helper class to house common data.
 */
public abstract class AbstractTaskTest {

    private final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
    private final String rubyExecutablePath = RvmFixtures.getMRIRubyRuntimeDefaultGemSet().getRubyExecutablePath();
    private final RubyLabel rubyLabel = new RubyLabel( "RVM", getRubyRuntime().getRubyRuntimeName() );
    private final ConfigurationMap configurationMap = new ConfigurationMapImpl();

    @Mock
    private BuildContext buildContext;

    @Mock
    private BuildDefinition buildDefinition;

    @Mock
    protected ProcessService processService;

    @Mock
    protected CapabilityContext capabilityContext;

    @Mock
    protected Capability capability;

    @Mock
    protected CapabilitySet capabilitySet;

    @Mock
    protected RubyLocatorServiceFactory rubyLocatorServiceFactory;

    @Mock
    protected EnvironmentVariableAccessor environmentVariableAccessor;

    @Mock
    protected RvmRubyLocator rvmRubyLocator;

    public abstract void testBuildCommandList();

    public abstract void testBuildEnvironment();

    protected RubyRuntime getRubyRuntime() {

        return this.rubyRuntime;
    }

    protected String getRubyExecutablePath() {

        return this.rubyExecutablePath;
    }

    protected RubyLabel getRubyLabel() {

        return this.rubyLabel;
    }

    protected ConfigurationMap getConfigurationMap() {

        return this.configurationMap;
    }

    protected BuildContext getBuildContext() {

        return this.buildContext;
    }

    protected BuildDefinition getBuildDefinition() {

        return this.buildDefinition;
    }

    protected void setupBuildContext( AbstractRubyTask task ) {

        // now setup the proper build context and build definition

        // buildDefinition.getCustomConfiguration()
        Map<String, String> customConfig = getBuildDefinition().getCustomConfiguration();
        customConfig.put( RubyBuildConfigurationPlugin.RUBY_CONFIG_RUNTIME, getRubyRuntime().getRubyRuntimeName() );
        customConfig.put( RubyBuildConfigurationPlugin.RUBY_CONFIG_ENVIRONMENT_VARIABLES, "" );
        when( getBuildDefinition().getCustomConfiguration() ).thenReturn( customConfig );

        // buildContext.getBuildDefinition
        when( getBuildContext().getBuildDefinition() ).thenReturn( getBuildDefinition() );

        task.init( getBuildContext() );
    }
}
