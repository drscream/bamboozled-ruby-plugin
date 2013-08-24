package com.alienfast.bamboozled.ruby.rt;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

import com.alienfast.bamboozled.ruby.rt.rvm.RvmUtils;

/**
 * Represents a ruby runtime installation on a host.
 */
public class RubyRuntime {

    private final String name;
    private final String gemSetName;

    private final String rubyExecutablePath;
    private final String gemPath;

    public RubyRuntime(String name, String gemSetName, String rubyExecutablePath, String gemPath) {

        this.name = name;
        this.gemSetName = gemSetName;
        this.rubyExecutablePath = rubyExecutablePath;
        this.gemPath = gemPath;
    }

    public String getName() {

        return this.name;
    }

    public String getGemSetName() {

        return this.gemSetName;
    }

    public String getRubyExecutablePath() {

        return this.rubyExecutablePath;
    }

    public String getGemPath() {

        return this.gemPath;
    }

    public String getRubyRuntimeName() {

        return this.name + RvmUtils.DEFAULT_GEMSET_SEPARATOR + this.gemSetName;
    }

    @Override
    public String toString() {

        return ToStringBuilder.reflectionToString( this );
    }

    @Override
    public boolean equals( Object o ) {

        return EqualsBuilder.reflectionEquals( this, o );
    }

    @Override
    public int hashCode() {

        int result = this.name != null ? this.name.hashCode() : 0;
        result = 31 * result + ( this.gemSetName != null ? this.gemSetName.hashCode() : 0 );
        result = 31 * result + ( this.rubyExecutablePath != null ? this.rubyExecutablePath.hashCode() : 0 );
        result = 31 * result + ( this.gemPath != null ? this.gemPath.hashCode() : 0 );
        return result;
    }
}
