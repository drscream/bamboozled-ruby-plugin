package com.alienfast.bamboozled.ruby.tasks;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alienfast.bamboozled.ruby.rt.RubyLocator;
import com.alienfast.bamboozled.ruby.rt.RubyRuntime;
import com.atlassian.bamboo.v2.build.agent.capability.CapabilityContext;
import com.google.common.collect.Lists;

public abstract class AbstractCommandBuilder<T extends AbstractCommandBuilder<?>> {

    protected final Logger log = LoggerFactory.getLogger( getClass() );

    private final CapabilityContext capabilityContext;
    private final RubyLocator rubyLocator;
    private final RubyRuntime rubyRuntime;
    private final String rubyExecutablePath;
    private List<String> commandList = Lists.newLinkedList();

    public AbstractCommandBuilder(CapabilityContext capabilityContext, RubyLocator rubyLocator, RubyRuntime rubyRuntime,
            String rubyExecutablePath) {

        this.capabilityContext = capabilityContext;
        this.rubyLocator = rubyLocator;
        this.rubyRuntime = rubyRuntime;
        this.rubyExecutablePath = rubyExecutablePath;
    }

    //    public AbstractCommandBuilder(RubyLabel rubyRuntimeLabel) {
    //
    //        final RubyLocator rubyLocator = getRubyLocator( rubyRuntimeLabel.getRubyRuntimeManager() ); // TODO Fix Error handling
    //        final RubyRuntime rubyRuntime = rubyLocator.getRubyRuntime( rubyRuntimeLabel.getRubyRuntime() ); // TODO Fix Error handling
    //        final String rubyExecutablePath = getRubyExecutablePath( rubyRuntimeLabel );
    //
    //        this.rubyLocator = rubyLocator;
    //        this.rubyRuntime = rubyRuntime;
    //        this.rubyExecutablePath = rubyExecutablePath;
    //    }

    /**
     * Append the ruby executable to the command list.
     *
     * @return T command builder.
     */
    @SuppressWarnings( "unchecked" )
    public T addRubyExecutable() {

        getCommandList().add( getRubyExecutablePath() );
        return (T) this;
    }

    protected List<String> getCommandList() {

        return this.commandList;
    }

    /**
     * Builds the list of commands.
     *
     * @return The list of commands.
     */
    public List<String> build() {

        this.log.info( "commandList {}", getCommandList().toString() );
        return getCommandList();
    }

    protected RubyLocator getRubyLocator() {

        return this.rubyLocator;
    }

    protected RubyRuntime getRubyRuntime() {

        return this.rubyRuntime;
    }

    protected String getRubyExecutablePath() {

        return this.rubyExecutablePath;
    }

    protected CapabilityContext getCapabilityContext() {

        return this.capabilityContext;
    }
}
