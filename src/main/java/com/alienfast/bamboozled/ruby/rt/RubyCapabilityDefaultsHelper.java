package com.alienfast.bamboozled.ruby.rt;

import java.io.File;
import java.util.List;

import org.apache.commons.lang.StringUtils;
import org.apache.commons.lang.SystemUtils;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.atlassian.bamboo.utils.SystemProperty;
import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityDefaultsHelper;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityImpl;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilitySet;
import com.google.common.collect.Lists;

/**
 * Detects ruby runtimes which were installed using rvm.
 */
public class RubyCapabilityDefaultsHelper implements CapabilityDefaultsHelper {

    private static final String XVFB_RUN_EXE = "xvfb-run";

    public static final String XVFB_RUN_CAPABILITY = CapabilityDefaultsHelper.CAPABILITY_BUILDER_PREFIX + "." + XVFB_RUN_EXE;

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

        resolveRubyRuntimeCapabilites( capabilitySet );
        resolveExeCapabilites( capabilitySet, XVFB_RUN_EXE, XVFB_RUN_CAPABILITY );

        return capabilitySet;
    }

    protected void resolveExeCapabilites( CapabilitySet capabilitySet, String exeName, String capabilityKey ) {

        List<String> paths = Lists.newArrayList( StringUtils.split( SystemProperty.PATH.getValue(), File.pathSeparator ) );
        //        if (SystemUtils.IS_OS_WINDOWS)
        //        {
        //            paths.addAll(getDefaultWindowPaths());
        //        }
        final String executableName = SystemUtils.IS_OS_WINDOWS ? exeName + ".exe" : exeName;

        for (String path : paths) {
            File file = new File( path, executableName );
            if ( file.exists() ) {
                log.info( "Adding " + capabilityKey + " at `" + file.getAbsolutePath() + "'" );
                final Capability capability = new CapabilityImpl( capabilityKey, file.getAbsolutePath() );
                log.info( "Added {}", capability );
                capabilitySet.addCapability( capability );
            }
        }
    }

    protected void resolveRubyRuntimeCapabilites( CapabilitySet capabilitySet ) {

        log.info( "Resolving ruby runtime managers." );

        for (RubyRuntimeLocatorService rubyRuntimeLocatorService : this.rubyLocatorServiceFactory.getLocatorServices()) {

            log.info( "Loading ruby locator service - {}", rubyRuntimeLocatorService.getRuntimeManagerName() );

            if ( rubyRuntimeLocatorService.isInstalled() ) {

                List<RubyRuntime> rubyRuntimeList = rubyRuntimeLocatorService.getRubyLocator().listRubyRuntimes();

                for (RubyRuntime rubyRuntime : rubyRuntimeList) {

                    final RubyLabel rubyLabel = new RubyLabel(
                            rubyRuntimeLocatorService.getRuntimeManagerName(),
                            rubyRuntime.getRubyRuntimeName() );
                    final String capabilityKey = rubyLabel.toCapabilityKey();
                    log.info( "Adding " + capabilityKey );
                    final Capability capability = new CapabilityImpl( capabilityKey, rubyRuntime.getRubyExecutablePath() );
                    log.info( "Added {}", capability );
                    capabilitySet.addCapability( capability );
                }
            }
        }
    }
}
