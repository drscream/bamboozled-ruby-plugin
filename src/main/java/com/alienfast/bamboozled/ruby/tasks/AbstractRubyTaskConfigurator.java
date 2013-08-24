package com.alienfast.bamboozled.ruby.tasks;

import java.util.Map;
import java.util.Set;

import org.jetbrains.annotations.NotNull;
import org.jetbrains.annotations.Nullable;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alienfast.bamboozled.ruby.config.RubyBuildConfigurationPlugin;
import com.atlassian.bamboo.collections.ActionParametersMap;
import com.atlassian.bamboo.task.AbstractTaskConfigurator;
import com.atlassian.bamboo.task.TaskDefinition;
import com.atlassian.bamboo.utils.error.ErrorCollection;
import com.atlassian.bamboo.ww2.actions.build.admin.create.UIConfigSupport;

/**
 * Adds ruby specific helpers to the abstract task configurator to get rid of duplication.
 */
public abstract class AbstractRubyTaskConfigurator extends AbstractTaskConfigurator {

    protected static final Logger log = LoggerFactory.getLogger( AbstractRubyTaskConfigurator.class );

    protected static final String CREATE_MODE = "create";
    protected static final String EDIT_MODE = "edit";
    protected static final String MODE = "mode";

    protected static final String RUBY_KEY = "ruby";

    protected static final String CTX_UI_CONFIG_BEAN = "uiConfigBean";
    private UIConfigSupport uiConfigBean;

    public void setUiConfigBean( final UIConfigSupport uiConfigBean ) {

        this.uiConfigBean = uiConfigBean;
    }

    protected abstract Set<String> getFieldsToCopy();

    @NotNull
    @Override
    public Map<String, String> generateTaskConfigMap( @NotNull ActionParametersMap params, @Nullable TaskDefinition previousTaskDefinition ) {

        final Map<String, String> map = super.generateTaskConfigMap( params, previousTaskDefinition );
        this.taskConfiguratorHelper.populateTaskConfigMapWithActionParameters( map, params, getFieldsToCopy() );

        return map;
    }

    @Override
    public void populateContextForCreate( @NotNull Map<String, Object> context ) {

        log.debug( "populateContextForCreate" );
        context.put( MODE, CREATE_MODE );
        context.put( CTX_UI_CONFIG_BEAN, this.uiConfigBean ); // NOTE: This is not normally necessary and will be fixed in 3.3.3

        RubyBuildConfigurationPlugin.populateContext( context );
    }

    @Override
    public void populateContextForEdit( @NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition ) {

        log.debug( "populateContextForEdit" );
        this.taskConfiguratorHelper.populateContextWithConfiguration( context, taskDefinition, getFieldsToCopy() );

        context.put( MODE, EDIT_MODE );
        context.put( CTX_UI_CONFIG_BEAN, this.uiConfigBean ); // NOTE: This is not normally necessary and will be fixed in 3.3.3

        RubyBuildConfigurationPlugin.populateContext( context );
    }

    @Override
    public void populateContextForView( @NotNull Map<String, Object> context, @NotNull TaskDefinition taskDefinition ) {

        log.debug( "populateContextForView" );
        this.taskConfiguratorHelper.populateContextWithConfiguration( context, taskDefinition, getFieldsToCopy() );

        RubyBuildConfigurationPlugin.populateContext( context );
    }

    @Override
    public void validate( @NotNull ActionParametersMap params, @NotNull ErrorCollection errorCollection ) {

        super.validate( params, errorCollection );
        //        String ruby = params.getString( RUBY_KEY );
        //
        //        if ( StringUtils.isEmpty( ruby ) ) {
        //            errorCollection.addError( RUBY_KEY, "You must specify a ruby runtime" );
        //        }
    }
}
