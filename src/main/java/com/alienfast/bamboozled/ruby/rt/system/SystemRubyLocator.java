package com.alienfast.bamboozled.ruby.rt.system;

import static com.alienfast.bamboozled.ruby.rt.system.SystemRubyUtils.buildPath;
import static com.alienfast.bamboozled.ruby.rt.system.SystemRubyUtils.parseRubyVersionString;
import static com.alienfast.bamboozled.ruby.util.EnvUtils.filterList;
import static com.alienfast.bamboozled.ruby.util.ExecUtils.getGemPathString;
import static com.alienfast.bamboozled.ruby.util.ExecUtils.getRubyVersionString;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import org.apache.commons.lang.SystemUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alienfast.bamboozled.ruby.rt.BaseRubyLocator;
import com.alienfast.bamboozled.ruby.rt.RubyLocator;
import com.alienfast.bamboozled.ruby.rt.RubyRuntime;
import com.alienfast.bamboozled.ruby.util.FileSystemHelper;
import com.alienfast.bamboozled.ruby.util.PathNotFoundException;
import com.google.common.collect.ImmutableList;
import com.google.common.collect.Lists;
import com.google.common.collect.Maps;

/**
 * Locates system ruby installations.
 * <p/>
 * This assumes that
 * - the shell environment MAY be tainted by RVM so it is avoided.
 * - the 'gem' executable is in the same path as the 'ruby' executable.
 * - at the moment I will locate only one installation to keep it simple.
 */
public class SystemRubyLocator extends BaseRubyLocator implements RubyLocator {

    private static final Logger log = LoggerFactory.getLogger( SystemRubyLocator.class );

    private static final List<String> searchPaths = ImmutableList.of( "/usr/bin", "/usr/local/bin" );

    public SystemRubyLocator(FileSystemHelper fileSystemHelper) {

        this.fileSystemHelper = fileSystemHelper;
    }

    @Override
    public Map<String, String> buildEnv( String rubyRuntimeName, String rubyExecutablePath, Map<String, String> currentEnv ) {

        Map<String, String> filteredRubyEnv = Maps.newHashMap();

        // As everything is static with the system ruby install we just need to clean this stuff
        // out of the environment and let ruby do it's thing.
        for (Map.Entry<String, String> entry : currentEnv.entrySet()) {
            if ( !filterList.contains( entry.getKey() ) ) {
                filteredRubyEnv.put( entry.getKey(), entry.getValue() );
            }
        }

        return filteredRubyEnv;
    }

    @Override
    public RubyRuntime getRubyRuntime( String rubyName, String gemSetName ) {

        final String rubyRuntimeName = String.format( "%s@%s", rubyName, gemSetName );

        return getRubyRuntime( rubyRuntimeName );
    }

    @Override
    public RubyRuntime getRubyRuntime( String rubyRuntimeName ) {

        final List<RubyRuntime> rubyRuntimeList = listRubyRuntimes();

        for (RubyRuntime rubyRuntime : rubyRuntimeList) {
            if ( rubyRuntime.getRubyRuntimeName().equals( rubyRuntimeName ) ) {
                return rubyRuntime;
            }
        }

        throw new PathNotFoundException( "Ruby runtime not found for - " + rubyRuntimeName );
    }

    @Override
    public List<RubyRuntime> listRubyRuntimes() {

        List<RubyRuntime> rubyRuntimeList = Lists.newLinkedList();

        // only currently supports *nix
        if ( SystemUtils.IS_OS_UNIX ) {
            for (String path : searchPaths) {

                if ( this.fileSystemHelper.pathExists( path, "ruby" ) && this.fileSystemHelper.pathExists( path, "gem" ) ) {
                    try {

                        final String rubyExecutablePath = buildPath( path, "ruby" );
                        final String rubyVersionString = getRubyVersionString( rubyExecutablePath );
                        final String version = parseRubyVersionString( rubyVersionString );
                        final String gemPathString = getGemPathString( buildPath( path, "gem" ) );

                        rubyRuntimeList.add( new RubyRuntime( version, "default", rubyExecutablePath, gemPathString ) );

                    }
                    catch (IOException e) {
                        log.error( "IO Exception occurred trying to build Ruby Runtime - " + e.getMessage() );
                    }
                    catch (InterruptedException e) {
                        log.error( "Interrupted Exception occurred trying to build Ruby Runtime - " + e.getMessage() );
                    }
                }

            }
        }
        else {
            log.warn( "This plugin currently only supports UNIX based operating systems." );
        }

        return rubyRuntimeList;
    }

    @Override
    public boolean hasRuby( String rubyNamePattern ) {

        final List<RubyRuntime> rubyRuntimeList = listRubyRuntimes();

        for (RubyRuntime rubyRuntime : rubyRuntimeList) {
            if ( rubyRuntime.getName().equals( rubyNamePattern ) ) {
                return true;
            }
        }

        return false;
    }

    @Override
    public boolean isReadOnly() {

        return true;
    }

}
