package com.alienfast.bamboozled.ruby.rt;

import java.io.File;

import org.codehaus.plexus.util.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alienfast.bamboozled.ruby.util.FileSystemHelper;

/**
 * Common logic used in all implementations of the Ruby Locator.
 */
public abstract class BaseRubyLocator {

    protected final Logger log = LoggerFactory.getLogger( BaseRubyLocator.class );

    protected FileSystemHelper fileSystemHelper;

    public String buildExecutablePath( String rubyRuntimeName, String rubyExecutablePath, String command ) {

        this.log.info( "rubyExecutablePath {}", rubyExecutablePath );

        final String executablePath = FileUtils.dirname( rubyExecutablePath ) + File.separator + command;

        this.log.info( "Checking executable {}", executablePath );

        if ( !this.fileSystemHelper.executableFileExists( executablePath ) ) {
            this.log.error( "Executable " + executablePath + " not found." );
            throw new IllegalArgumentException( "Executable " + executablePath + " not found in ruby bin path." );
        }
        else {
            return executablePath;
        }

    }

}
