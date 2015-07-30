package com.alienfast.bamboozled.ruby.rt;

import java.util.StringTokenizer;

import com.alienfast.bamboozled.ruby.capability.RubyCapabilityTypeModule;

/**
 * Used to represent a ruby instance managed by a ruby runtime manager.
 */
public class RubyLabel {

    // This is to support existing deployments where the registered runtimes lack a runtime manager prefix.
    static final String DEFAULT_RUNTIME_MANAGER = "RVM";

    private String rubyRuntimeManager;
    private String rubyRuntime;

    public RubyLabel(String rubyRuntimeManager, String rubyRuntime) {

        this.rubyRuntimeManager = rubyRuntimeManager;
        this.rubyRuntime = rubyRuntime;
    }

    public String getRubyRuntimeManager() {

        return this.rubyRuntimeManager;
    }

    public String getRubyRuntime() {

        return this.rubyRuntime;
    }

    /**
     * Factory method used to build a ruby runtime name.
     *
     * @param rubyRuntimeLabel The label to be parsed.
     * @return RubyLabel object containing the attributes from the rubyRuntimeLabel.
     */
    public static RubyLabel fromString( String rubyRuntimeLabel ) {

        final StringTokenizer stringTokenizer = new StringTokenizer( rubyRuntimeLabel, " " );

        if ( stringTokenizer.countTokens() == 2 ) {
            return new RubyLabel( stringTokenizer.nextToken(), stringTokenizer.nextToken() );
        }
        else {
            if ( rubyRuntimeLabel.matches( ".*@.*" ) ) { // mad or bad regex, not sure time will tell.
                return new RubyLabel( DEFAULT_RUNTIME_MANAGER, rubyRuntimeLabel );
            }
            throw new IllegalArgumentException(
                    "Could not parse rubyRuntime string, expected something like 'RVM ruby-1.9.2@rails31', not " + rubyRuntimeLabel );
        }
    }

    public static RubyLabel fromKey( String key ) {

        return fromString( key.replaceFirst( RubyCapabilityTypeModule.RUBY_CAPABILITY_KEY + ".", "" ) );
    }

    public String toCapabilityKey() {

        return String.format( "%s.%s", RubyCapabilityTypeModule.RUBY_CAPABILITY_KEY, this );
    }

    @Override
    public String toString() {

        return String.format( "%s %s", this.rubyRuntimeManager, this.rubyRuntime );
    }
}
