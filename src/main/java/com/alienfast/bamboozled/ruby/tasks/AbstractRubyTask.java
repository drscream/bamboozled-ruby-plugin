package com.alienfast.bamboozled.ruby.tasks;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alienfast.bamboozled.ruby.config.RubyBuildConfigurationPlugin;
import com.alienfast.bamboozled.ruby.rt.RubyLabel;
import com.alienfast.bamboozled.ruby.rt.RubyLocator;
import com.alienfast.bamboozled.ruby.rt.RubyLocatorServiceFactory;
import com.alienfast.bamboozled.ruby.util.PathNotFoundException;
import com.atlassian.bamboo.build.BuildDefinition;
import com.atlassian.bamboo.configuration.ConfigurationMap;
import com.atlassian.bamboo.deployments.execution.DeploymentTaskContext;
import com.atlassian.bamboo.deployments.projects.DeploymentProject;
import com.atlassian.bamboo.deployments.projects.service.DeploymentProjectService;
import com.atlassian.bamboo.plan.Plan;
import com.atlassian.bamboo.plan.PlanManager;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ExternalProcessBuilder;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.task.CommonTaskContext;
import com.atlassian.bamboo.task.CommonTaskType;
import com.atlassian.bamboo.task.TaskContext;
import com.atlassian.bamboo.task.TaskException;
import com.atlassian.bamboo.task.TaskResult;
import com.atlassian.bamboo.task.TaskResultBuilder;
import com.atlassian.bamboo.util.Narrow;
import com.atlassian.bamboo.v2.build.BuildContext;
import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.utils.process.ExternalProcess;
import com.google.common.base.Preconditions;
import com.google.common.collect.ImmutableMap;

/**
 * Basis for ruby tasks.
 */
public abstract class AbstractRubyTask implements CommonTaskType {

    public static final String ENVIRONMENT = "environmentVariables";
    protected final Logger log = LoggerFactory.getLogger( AbstractRubyTask.class );
    private ProcessService processService;
    private RubyLocatorServiceFactory rubyLocatorServiceFactory;
    private EnvironmentVariableAccessor environmentVariableAccessor;
    private CapabilityContext capabilityContext;

    private BuildContext buildContext;
    private BuildDefinition buildDefinition;

    private DeploymentProjectService deploymentProjectService;
    private PlanManager planManager;

    public AbstractRubyTask() {

    }

    @Override
    @NotNull
    public TaskResult execute( @NotNull CommonTaskContext commonTaskContext ) throws TaskException {

        final TaskResultBuilder taskResultBuilder = TaskResultBuilder.newBuilder( commonTaskContext );
        final TaskContext taskContext = Narrow.to( commonTaskContext, TaskContext.class );

        // move this crazy resolution out of here.
        resolveContext(taskContext, commonTaskContext);

        final String rubyRuntimeLabel = RubyBuildConfigurationPlugin.getRubyRuntime( getBuildDefinition() );

        try {

            final RubyLabel rubyLabel = RubyLabel.fromString( rubyRuntimeLabel );

            final ConfigurationMap config = commonTaskContext.getConfigurationMap();
            Map<String, String> envVars = buildEnvironment( rubyLabel, config );

            List<String> commandsList = buildCommandList( rubyLabel, config );

            ExternalProcess externalProcess = getProcessService().createExternalProcess(
                    commonTaskContext,
                    new ExternalProcessBuilder()
                            .env( envVars )
                            .command( commandsList )
                            .workingDirectory( commonTaskContext.getWorkingDirectory() ) );

            externalProcess.execute();

            taskResultBuilder.checkReturnCode( externalProcess, 0 );

        }
        catch (IllegalArgumentException e) {
            logError( e, taskContext );
            return taskResultBuilder.failedWithError().build();
        }
        catch (PathNotFoundException e) {
            logError( e, taskContext );
            return taskResultBuilder.failedWithError().build();
        }

        return taskResultBuilder.build();
    }

    private void resolveContext( TaskContext taskContext, CommonTaskContext commonTaskContext ) {

        // This is a hack to ultimately resolve the BuildDefinition.  Unfortunately this is not well encapsulated and the lack of
        //      information provided to the deploy task makes it difficult to navigate back to.
        if ( taskContext != null ) {
            // taskContext is null for deploy tasks
            setBuildContext( taskContext.getBuildContext() );
            setBuildDefinition( getBuildContext().getBuildDefinition() );
        }
        else {

            // this is a deploy task
            final DeploymentTaskContext deploymentTaskContext = Narrow.to( commonTaskContext, DeploymentTaskContext.class );
            long environmentId = deploymentTaskContext.getDeploymentContext().getEnvironmentId();

            DeploymentProject deploymentProject = getDeploymentProjectService().getDeploymentProjectForEnvironment( environmentId );
            Plan plan = getPlanManager().getPlanByKey( deploymentProject.getPlanKey() );
            setBuildDefinition( plan.getBuildDefinition() );
        }
    }

    private void logError( Throwable e, TaskContext taskContext ) {

        this.log.error( e.getMessage(), e );
        if ( taskContext != null ) {
            taskContext.getBuildContext().getErrorCollection().addErrorMessage( e.getMessage() );
        }
    }

    /**
     * Invoked as a part of the task execute routine to enable building of a list of elements which will be executed.
     *
     * @param rubyRuntimeLabel The ruby runtime label.
     * @param config           The configuration map.
     * @return List of command elements to be executed.
     */
    protected abstract List<String> buildCommandList( RubyLabel rubyRuntimeLabel, ConfigurationMap config );

    protected RubyLocator getRubyLocator( String rubyRuntimeManager ) {

        if ( this.rubyLocatorServiceFactory == null ) {
            this.rubyLocatorServiceFactory = new RubyLocatorServiceFactory();
        }
        return this.rubyLocatorServiceFactory.acquireRubyLocator( rubyRuntimeManager );
    }

    protected String getRubyExecutablePath( final RubyLabel rubyRuntimeLabel ) {

        final Capability capability = this.getCapabilityContext().getCapabilitySet().getCapability( rubyRuntimeLabel.toCapabilityLabel() );
        Preconditions.checkNotNull( capability, "Capability" );
        final String rubyRuntimeExecutable = capability.getValue();
        Preconditions.checkNotNull( rubyRuntimeExecutable, "rubyRuntimeExecutable" );
        return rubyRuntimeExecutable;
    }

    public Map<String, String> buildEnvironment( RubyLabel rubyRuntimeLabel, ConfigurationMap config ) {

        this.log.info( "Using manager {} runtime {}", rubyRuntimeLabel.getRubyRuntimeManager(), rubyRuntimeLabel.getRubyRuntime() );

        final Map<String, String> currentEnvVars = this.getEnvironmentVariableAccessor().getEnvironment();

        // Get the variables from our configuration
        final String taskEnvironmentVariables = config.get( ENVIRONMENT );
        final String globalEnvironmentVariables = RubyBuildConfigurationPlugin.getRubyEnvironmentVariables( getBuildDefinition() );

        Map<String, String> configEnvVars = this.getEnvironmentVariableAccessor().splitEnvironmentAssignments(
                globalEnvironmentVariables + " " + taskEnvironmentVariables );

        final RubyLocator rubyLocator = getRubyLocator( rubyRuntimeLabel.getRubyRuntimeManager() );

        return rubyLocator.buildEnv( rubyRuntimeLabel.getRubyRuntime(), getRubyExecutablePath( rubyRuntimeLabel ), ImmutableMap
                .<String, String> builder()
                .putAll( currentEnvVars )
                .putAll( configEnvVars )
                .build() );
    }

    protected BuildContext getBuildContext() {

        return this.buildContext;
    }

    public void setBuildContext( BuildContext buildContext ) {

        this.buildContext = buildContext;
    }

    protected EnvironmentVariableAccessor getEnvironmentVariableAccessor() {

        return environmentVariableAccessor;
    }

    protected CapabilityContext getCapabilityContext() {

        return capabilityContext;
    }

    protected ProcessService getProcessService() {

        return processService;
    }

    public DeploymentProjectService getDeploymentProjectService() {

        return deploymentProjectService;
    }

    public void setDeploymentProjectService( DeploymentProjectService deploymentProjectService ) {

        this.deploymentProjectService = deploymentProjectService;
    }

    public PlanManager getPlanManager() {

        return planManager;
    }

    public void setPlanManager( PlanManager planManager ) {

        this.planManager = planManager;
    }

    public void setProcessService( ProcessService processService ) {

        this.processService = processService;
    }

    public void setCapabilityContext( CapabilityContext capabilityContext ) {

        this.capabilityContext = capabilityContext;
    }

    public void setRubyLocatorServiceFactory( RubyLocatorServiceFactory rubyLocatorServiceFactory ) {

        this.rubyLocatorServiceFactory = rubyLocatorServiceFactory;
    }

    public void setEnvironmentVariableAccessor( EnvironmentVariableAccessor environmentVariableAccessor ) {

        this.environmentVariableAccessor = environmentVariableAccessor;
    }

    public BuildDefinition getBuildDefinition() {

        return buildDefinition;
    }

    public void setBuildDefinition( BuildDefinition buildDefinition ) {

        this.buildDefinition = buildDefinition;
    }
}
