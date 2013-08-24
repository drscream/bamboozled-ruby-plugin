package com.alienfast.bamboozled.ruby.rt.rvm;

import org.apache.commons.lang.builder.EqualsBuilder;
import org.apache.commons.lang.builder.ToStringBuilder;

/**
 * Ruby Version Manager (RVM) Installation details.
 */
public class RvmInstallation {

    private Type installType;
    private String installPath;
    private String rubiesPath;
    private String gemsPath;

    public RvmInstallation(String installPath, Type installType, String rubiesPath, String gemsPath) {

        this.installPath = installPath;
        this.installType = installType;
        this.rubiesPath = rubiesPath;
        this.gemsPath = gemsPath;
    }

    public boolean isReadOnly() {

        return this.installType.equals( Type.SYSTEM );
    }

    public String getInstallPath() {

        return this.installPath;
    }

    public boolean isSystemInstall() {

        return this.installType.equals( Type.SYSTEM );
    }

    public boolean isUserInstall() {

        return this.installType.equals( Type.USER );
    }

    public String getGemsPath() {

        return this.gemsPath;
    }

    public String getRubiesPath() {

        return this.rubiesPath;
    }

    public enum Type {
        USER,
        SYSTEM
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

        int result = this.installType != null ? this.installType.hashCode() : 0;
        result = 31 * result + ( this.installPath != null ? this.installPath.hashCode() : 0 );
        result = 31 * result + ( this.rubiesPath != null ? this.rubiesPath.hashCode() : 0 );
        result = 31 * result + ( this.gemsPath != null ? this.gemsPath.hashCode() : 0 );
        return result;
    }
}
