package com.alienfast.bamboozled.ruby.tasks;

import static org.mockito.Mockito.when;

import java.util.Map;

import org.mockito.Mock;

import com.alienfast.bamboozled.ruby.config.RubyBuildConfigurationPlugin;
import com.alienfast.bamboozled.ruby.rt.RuntimeLocatorException;
import com.atlassian.bamboo.build.BuildDefinition;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.configuration.ConfigurationMapImpl;
import com.atlassian.bamboo.v2.build.BuildContext;

/**
 * 
 */
public abstract class AbstractTaskTest extends AbstractRubyTest {

    private final ConfigurationMap configurationMap = new ConfigurationMapImpl();

    @Mock
    private BuildContext buildContext;

    @Mock
    private BuildDefinition buildDefinition;

    public abstract void testBuildCommandList() throws RuntimeLocatorException;

    public abstract void testBuildEnvironment() throws RuntimeLocatorException;

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

        task.setBuildContext( getBuildContext() );
        task.setBuildDefinition( getBuildDefinition() );
    }
}
