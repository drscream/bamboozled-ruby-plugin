package com.alienfast.bamboozled.ruby.tasks.capistrano;

import java.util.List;

import org.apache.commons.lang.BooleanUtils;
import org.jetbrains.annotations.Nullable;

import com.alienfast.bamboozled.ruby.rt.RubyLocator;
import com.alienfast.bamboozled.ruby.rt.RubyRuntime;
import com.alienfast.bamboozled.ruby.tasks.AbstractBundleExecCommandBuilder;

/**
 * Assemble the command list for cap.
 */
public class CapistranoCommandBuilder extends AbstractBundleExecCommandBuilder<CapistranoCommandBuilder> {

    public static final String CAP_COMMAND = "cap";
    public static final String TRACE_ARG = "--trace";

    public CapistranoCommandBuilder(RubyLocator rvmRubyLocator, RubyRuntime rubyRuntime, String rubyExecutablePath) {

        super( rvmRubyLocator, rubyRuntime, rubyExecutablePath );
    }

    /**
     * Append the rake executable to the command list.
     *
     * @return Capistrano command builder.
     */
    public CapistranoCommandBuilder addCapistranoExecutable( @Nullable String bundleFlag ) {

        if ( BooleanUtils.toBoolean( bundleFlag ) ) {
            getCommandList().add( CAP_COMMAND );
        }
        else {
            getCommandList().add(
                    getRubyLocator().buildExecutablePath( getRubyRuntime().getRubyRuntimeName(), getRubyExecutablePath(), CAP_COMMAND ) );
        }
        return this;
    }

    /**
     * Will conditionally append the verbose switch if verbose flag is "true".
     *
     * @param traceFlag String which takes null or "true".
     * @return Capistrano command builder.
     */
    public CapistranoCommandBuilder addIfTrace( @Nullable String traceFlag ) {

        if ( BooleanUtils.toBoolean( traceFlag ) ) {
            getCommandList().add( TRACE_ARG );
        }
        return this;
    }

    /**
     * Will append the supplied list of tasks to the command list.
     *
     * @param tasks List of targets.
     * @return Capistrano command builder.
     */
    public CapistranoCommandBuilder addTasks( List<String> tasks ) {

        getCommandList().addAll( tasks );
        return this;
    }

}
