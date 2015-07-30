package com.alienfast.bamboozled.ruby.tasks;

import static org.mockito.Mockito.when;

import org.mockito.Mock;

import com.alienfast.bamboozled.ruby.capability.XvfbRunCapabilityTypeModule;
import com.alienfast.bamboozled.ruby.fixtures.RvmFixtures;
import com.alienfast.bamboozled.ruby.rt.RubyLabel;
import com.alienfast.bamboozled.ruby.rt.RubyLocatorServiceFactory;
import com.alienfast.bamboozled.ruby.rt.RubyRuntime;
import com.alienfast.bamboozled.ruby.rt.rvm.RvmRubyLocator;
import com.atlassian.bamboo.process.EnvironmentVariableAccessor;
import com.atlassian.bamboo.process.ProcessService;
import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilitySet;

/**
 * 
 */
public abstract class AbstractRubyTest {

    @Mock
    private ProcessService processService;
    @Mock
    private CapabilityContext capabilityContext;
    @Mock
    private Capability capability;
    @Mock
    private Capability xvfbRunCapability;
    @Mock
    private CapabilitySet capabilitySet;
    @Mock
    private RubyLocatorServiceFactory rubyLocatorServiceFactory;
    @Mock
    private EnvironmentVariableAccessor environmentVariableAccessor;
    @Mock
    private RvmRubyLocator rvmRubyLocator;

    private final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
    private final String rubyExecutablePath = this.rubyRuntime.getRubyExecutablePath();
    private final RubyLabel rubyLabel = new RubyLabel( "RVM", getRubyRuntime().getRubyRuntimeName() );

    public void setUp() throws Exception {

        when( getCapability().getValue() ).thenReturn( getRubyRuntime().getRubyExecutablePath() );
        when( getCapabilitySet().getCapability( getRubyLabel().toCapabilityKey() ) ).thenReturn( getCapability() );
        //        when( this.capabilityContext.getCapabilitySet() ).thenReturn( this.capabilitySet );

        // setup xvfb-run
        when( getXvfbRunCapability().getValue() ).thenReturn( "/usr/bin/xvfb-run" );
        when( getCapabilitySet().getCapability( XvfbRunCapabilityTypeModule.XVFB_RUN_CAPABILITY_KEY ) ).thenReturn( getXvfbRunCapability() );
        when( getCapabilityContext().getCapabilitySet() ).thenReturn( getCapabilitySet() );
    }

    protected RubyRuntime getRubyRuntime() {

        return this.rubyRuntime;
    }

    protected RubyLabel getRubyLabel() {

        return this.rubyLabel;
    }

    protected String getRubyExecutablePath() {

        return this.rubyExecutablePath;
    }

    protected RvmRubyLocator getRvmRubyLocator() {

        return this.rvmRubyLocator;
    }

    protected void setRvmRubyLocator( RvmRubyLocator rvmRubyLocator ) {

        this.rvmRubyLocator = rvmRubyLocator;
    }

    protected CapabilitySet getCapabilitySet() {

        return this.capabilitySet;
    }

    protected void setCapabilitySet( CapabilitySet capabilitySet ) {

        this.capabilitySet = capabilitySet;
    }

    protected CapabilityContext getCapabilityContext() {

        return this.capabilityContext;
    }

    protected void setCapabilityContext( CapabilityContext capabilityContext ) {

        this.capabilityContext = capabilityContext;
    }

    protected ProcessService getProcessService() {

        return this.processService;
    }

    protected void setProcessService( ProcessService processService ) {

        this.processService = processService;
    }

    protected Capability getCapability() {

        return this.capability;
    }

    protected void setCapability( Capability capability ) {

        this.capability = capability;
    }

    protected Capability getXvfbRunCapability() {

        return this.xvfbRunCapability;
    }

    protected void setXvfbRunCapability( Capability xvfbRunCapability ) {

        this.xvfbRunCapability = xvfbRunCapability;
    }

    protected RubyLocatorServiceFactory getRubyLocatorServiceFactory() {

        return this.rubyLocatorServiceFactory;
    }

    protected void setRubyLocatorServiceFactory( RubyLocatorServiceFactory rubyLocatorServiceFactory ) {

        this.rubyLocatorServiceFactory = rubyLocatorServiceFactory;
    }

    protected EnvironmentVariableAccessor getEnvironmentVariableAccessor() {

        return this.environmentVariableAccessor;
    }

    protected void setEnvironmentVariableAccessor( EnvironmentVariableAccessor environmentVariableAccessor ) {

        this.environmentVariableAccessor = environmentVariableAccessor;
    }
}
