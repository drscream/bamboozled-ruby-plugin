package com.alienfast.bamboozled.ruby.config;

import java.util.Map;

import org.apache.commons.lang.StringUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.bamboo.build.BuildDefinition;
import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.cache.ImmutablePlan;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.v2.build.BaseBuildConfigurationAwarePlugin;
import com.atlassian.bamboo.v2.build.configuration.MiscellaneousBuildConfigurationPlugin;
import com.atlassian.bamboo.ww2.actions.build.admin.create.BuildConfiguration;

/**
 * This adds the configuration of ruby environment to the miscellaneous tab.
 * 
 * @author kross
 */
public class RubyBuildConfigurationPlugin extends BaseBuildConfigurationAwarePlugin implements MiscellaneousBuildConfigurationPlugin {

    private static final Logger log = LoggerFactory.getLogger( RubyBuildConfigurationPlugin.class );
    public static String RUBY_CONFIG_RUNTIME = "custom.ruby-config-runtime";
    public static String RUBY_CONFIG_ENVIRONMENT_VARIABLES = "custom.ruby-config-environmentVariables";

    @Override
    protected void populateContextForView( @NotNull final Map<String, Object> context, @NotNull final Plan plan ) {

        super.populateContextForView( context, plan );

        populateContext( context );
    }

    public static void populateContext( final Map<String, Object> context ) {

        ImmutablePlan plan = (ImmutablePlan) context.get( "plan" );
        if ( plan == null ) {

            // in case it is a deployment plan
            plan = (ImmutablePlan) context.get( "relatedPlan" );
        }
        BuildDefinition buildDefinition = plan.getBuildDefinition();

        String runtime = getRubyRuntime( buildDefinition );
        context.put( "invalidRuntime", StringUtils.isEmpty( runtime ) );

        String environmentVariables = getRubyEnvironmentVariables( buildDefinition );
        if ( StringUtils.isEmpty( environmentVariables ) ) {
            environmentVariables = "(none)";
        }

        context.put( RUBY_CONFIG_RUNTIME, runtime );
        context.put( RUBY_CONFIG_ENVIRONMENT_VARIABLES, environmentVariables );
    }

    @Override
    public boolean isApplicableTo( Plan plan ) {

        // always make this available, regardless of context
        return true;
        //only top level Plans see this option.
        //        return plan instanceof TopLevelPlan || Job;
    }

    @Override
    public boolean isConfigurationMissing( BuildConfiguration buildConfiguration ) {

        return super.isConfigurationMissing( buildConfiguration );
    }

    @Override
    public ErrorCollection validate( BuildConfiguration buildConfiguration ) {

        ErrorCollection errorCollection = super.validate( buildConfiguration );

        if ( StringUtils.isEmpty( buildConfiguration.getString( RUBY_CONFIG_RUNTIME ) ) ) {
            errorCollection.addError( RUBY_CONFIG_RUNTIME, "Ruby Runtime Configuration required (on the Miscellaneous tab)" );
        }

        return errorCollection;
    }

    public static String getRubyRuntime( BuildDefinition buildDefinition ) {

        return getValue( buildDefinition, RUBY_CONFIG_RUNTIME );
    }

    public static String getRubyEnvironmentVariables( BuildDefinition buildDefinition ) {

        return getValue( buildDefinition, RUBY_CONFIG_ENVIRONMENT_VARIABLES );
    }

    private static String getValue( BuildDefinition buildDefinition, String name ) {

        Map<String, String> customConfig = buildDefinition.getCustomConfiguration();
        if ( log.isDebugEnabled() ) {
            for (String key : customConfig.keySet()) {
                log.debug( "Key: " + key );
                log.debug( customConfig.get( key ).toString() );
            }
        }
        String result = (String) customConfig.get( name );
        return result;
    }
}
