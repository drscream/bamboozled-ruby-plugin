package com.alienfast.bamboozled.ruby.capability;

import java.util.List;
import java.util.Map;

import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alienfast.bamboozled.ruby.rt.RubyLabel;
import com.alienfast.bamboozled.ruby.rt.RubyLocatorServiceFactory;
import com.alienfast.bamboozled.ruby.rt.RubyRuntime;
import com.alienfast.bamboozled.ruby.rt.RubyRuntimeLocatorService;
import com.atlassian.bamboo.util.BambooFilenameUtils;
import com.atlassian.bamboo.v2.build.agent.capability.AbstractExecutableCapabilityTypeModule;
import com.atlassian.bamboo.v2.build.agent.capability.Capability;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityImpl;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilitySet;
import com.google.common.collect.Lists;

public class RubyCapabilityTypeModule extends AbstractExecutableCapabilityTypeModule {

    public static final String RUBY_CAPABILITY_KEY = "system.executable.ruby";
    public static final String RUBY_EXECUTABLE_KEY = "rubyExecutable";
    public static final String RUBY_EXECUTABLE = "ruby";
    private static final String FIELD_RUBY_LABEL = "rubyLabel";
    private static final String FIELD_RUBY_PATH = "rubyPath";

    private static final String CAPABILITY_TYPE_ERROR_UNDEFINED_EXECUTABLE = AGENT_CAPABILITY_TYPE_PREFIX + RUBY_CAPABILITY_KEY
            + ".undefinedExecutable";
    private final RubyLocatorServiceFactory rubyLocatorServiceFactory;
    private static final Logger log = LoggerFactory.getLogger( RubyCapabilityTypeModule.class );

    public RubyCapabilityTypeModule() {

        this( new RubyLocatorServiceFactory() );
    }

    public RubyCapabilityTypeModule(RubyLocatorServiceFactory rubyLocatorServiceFactory) {

        //        log.info( "loading rubyLocatorServiceFactory = " + rubyLocatorServiceFactory );
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

        log.info( "Resolving ruby runtime managers." );

        for (RubyRuntimeLocatorService rubyRuntimeLocatorService : this.rubyLocatorServiceFactory.getLocatorServices()) {

            log.info( "Loading ruby locator service - {}", rubyRuntimeLocatorService.getRuntimeManagerName() );

            if ( rubyRuntimeLocatorService.isInstalled() ) {

                List<RubyRuntime> rubyRuntimeList = rubyRuntimeLocatorService.getRubyLocator().listRubyRuntimes();

                for (RubyRuntime rubyRuntime : rubyRuntimeList) {

                    final RubyLabel rubyLabel = new RubyLabel(
                            rubyRuntimeLocatorService.getRuntimeManagerName(),
                            rubyRuntime.getRubyRuntimeName() );
                    log.info( "Adding " + rubyLabel.toCapabilityKey() );
                    final Capability capability = new CapabilityImpl( rubyLabel.toCapabilityKey(), rubyRuntime.getRubyExecutablePath() );
                    log.info( "Added {}", capability );
                    capabilitySet.addCapability( capability );
                }
            }
        }

        return capabilitySet;
    }

    @Override
    @NotNull
    public Capability getCapability( @NotNull Map<String, String[]> params ) {

        //        this is for when you create/save one

        log.info( "getCapability for {} keys {}", params, params.keySet().toString() );

        //        TODO: override this to provide the rubylable capability?
        //        final String value = getParamValue( params, getExecutableKey() );
        //        log.info( "Value {} for {}", value, getExecutableKey() );
        //        return new CapabilityImpl( getMandatoryCapabilityKey(), value );

        final String labelString = params.get( FIELD_RUBY_LABEL )[0].trim();
        final RubyLabel rubyLabel = RubyLabel.fromString( labelString );
        // transform the path so that it works properly
        final String path = BambooFilenameUtils.stripTrailingSlashes( params.get( FIELD_RUBY_PATH )[0].trim() );

        return new CapabilityImpl( rubyLabel.toCapabilityKey(), path );
    }

    @Override
    @NotNull
    public String getLabel( @NotNull String key ) {

        //        probably need to override this to provide the RVM like label
        //        String label = getText( getMandatoryCapabilityKey() );
        RubyLabel rubyLabel = RubyLabel.fromKey( key );
        String label = rubyLabel.toString();
        log.info( "Returning {} for key {}", label, key );
        // Returning ruby for key system.executable.ruby.RVM ruby-2.2.0@af_core
        return label;
    }

    @Override
    public String getMandatoryCapabilityKey() {

        return RUBY_CAPABILITY_KEY;
    }

    @Override
    public String getExecutableKey() {

        return RUBY_EXECUTABLE_KEY;
    }

    @Override
    public String getCapabilityUndefinedKey() {

        return CAPABILITY_TYPE_ERROR_UNDEFINED_EXECUTABLE;
    }

    @Override
    public List<String> getDefaultWindowPaths() {

        return Lists.newArrayList();
    }

    @Override
    public String getExecutableFilename() {

        return RUBY_EXECUTABLE;
    }

    //    public String getExecutableDescription() {
    //
    //        return getText( AGENT_CAPABILITY_TYPE_PREFIX + RUBY_CAPABILITY_KEY + ".description" );
    //    }

}
