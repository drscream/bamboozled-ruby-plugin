package com.alienfast.bamboozled.ruby.rt;

import java.util.List;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityDefaultsHelper;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityImpl;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilitySet;

/**
 * Detects ruby runtimes which were installed using rvm.
 */
public class RubyCapabilityDefaultsHelper implements CapabilityDefaultsHelper {

    private static final Logger log = LoggerFactory.getLogger( RubyCapabilityDefaultsHelper.class );

    private final RubyLocatorServiceFactory rubyLocatorServiceFactory;

    public RubyCapabilityDefaultsHelper() {

        this( new RubyLocatorServiceFactory() );
    }

    public RubyCapabilityDefaultsHelper(RubyLocatorServiceFactory rubyLocatorServiceFactory) {

        log.info( "loading rubyLocatorServiceFactory = " + rubyLocatorServiceFactory );
        this.rubyLocatorServiceFactory = rubyLocatorServiceFactory;
    }

    /**
     * Adds default Capabilities for this module. Used during auto-detecting server capabilities.
     *
     * @param capabilitySet to add the capability to
     * @return the supplied capabilitySet with the new capabilities included if found, else the given capabilitySet
     */
    @NotNull
    @Override
    public CapabilitySet addDefaultCapabilities( @NotNull CapabilitySet capabilitySet ) {

        log.info( "Retrieving a list of runtime managers." );

        for (RubyRuntimeLocatorService rubyRuntimeLocatorService : this.rubyLocatorServiceFactory.getLocatorServices()) {

            log.info( "Loading ruby locator service - {}", rubyRuntimeLocatorService.getRuntimeManagerName() );

            if ( rubyRuntimeLocatorService.isInstalled() ) {

                List<RubyRuntime> rubyRuntimeList = rubyRuntimeLocatorService.getRubyLocator().listRubyRuntimes();

                for (RubyRuntime rubyRuntime : rubyRuntimeList) {

                    final RubyLabel rubyLabel = new RubyLabel(
                            rubyRuntimeLocatorService.getRuntimeManagerName(),
                            rubyRuntime.getRubyRuntimeName() );
                    final String capabilityLabel = rubyLabel.toCapabilityLabel();
                    final Capability capability = new CapabilityImpl( capabilityLabel, rubyRuntime.getRubyExecutablePath() );

                    log.info( "Adding {}", capability );
                    capabilitySet.addCapability( capability );
                }
            }
        }

        return capabilitySet;
    }
}
