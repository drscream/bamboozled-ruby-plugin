package com.alienfast.bamboozled.ruby.tasks;

import org.apache.commons.lang.BooleanUtils;
import org.jetbrains.annotations.Nullable;

import com.alienfast.bamboozled.ruby.rt.RubyLocator;
import com.alienfast.bamboozled.ruby.rt.RubyRuntime;

public class AbstractBundleExecCommandBuilder<T extends AbstractBundleExecCommandBuilder<?>> extends AbstractCommandBuilder<T> {

    public static final String BUNDLE_EXEC_ARG = "exec";
    public static final String BUNDLE_COMMAND = "bundle";
    public static final String VERBOSE_ARG = "--verbose";

    public AbstractBundleExecCommandBuilder(RubyLocator rubyLocator, RubyRuntime rubyRuntime, String rubyExecutablePath) {

        super( rubyLocator, rubyRuntime, rubyExecutablePath );
    }

    /**
     * Will conditionally add bundle exec if bundle flag is "true".
     *
     * @param bundleFlag String which takes null or "true".
     * @return T command builder.
     */
    @SuppressWarnings( "unchecked" )
    public T addIfBundleExec( @Nullable String bundleFlag ) {

        if ( BooleanUtils.toBoolean( bundleFlag ) ) {
            getCommandList().add(
                    getRubyLocator().buildExecutablePath( getRubyRuntime().getRubyRuntimeName(), getRubyExecutablePath(), BUNDLE_COMMAND ) );
            getCommandList().add( BUNDLE_EXEC_ARG );
        }
        return (T) this;
    }

    /**
     * Will conditionally append the verbose switch if verbose flag is "true".
     *
     * @param verboseFlag String which takes null or "true".
     * @return Rake command builder.
     */
    @SuppressWarnings( "unchecked" )
    public T addIfVerbose( @Nullable String verboseFlag ) {

        if ( BooleanUtils.toBoolean( verboseFlag ) ) {
            getCommandList().add( VERBOSE_ARG );
        }
        return (T) this;
    }
}
