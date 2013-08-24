package com.alienfast.bamboozled.ruby.rt.rvm;

import static org.junit.Assert.assertTrue;

import java.util.List;

import org.junit.Test;

import com.alienfast.bamboozled.ruby.rt.RubyRuntimeName;
import com.alienfast.bamboozled.ruby.rt.rvm.RvmUtils;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

/**
 * Test the RVM utility functions class.
 */
public class RvmUtilsTest {

    @Test
    public void testParseRubyRuntimeName() throws Exception {

        RubyRuntimeName rubyRuntimeTokens = RvmUtils.parseRubyRuntimeName( "ruby-1.9.3-p0@rails31" );

        assertThat( "ruby-1.9.3-p0", equalTo( rubyRuntimeTokens.getVersion() ) );
        assertThat( "rails31", equalTo( rubyRuntimeTokens.getGemSet() ) );

    }

    @Test
    public void testBuildGemSetName() throws Exception {

        String gemSetName = RvmUtils.buildGemSetName( "ruby-1.9.3-p0", "ruby-1.9.3-p0" );

        assertThat( "default", equalTo( gemSetName ) );
    }

    @Test
    public void testBuildGemSetDirectoryName() {

        String gemSetDirectoryName;
        gemSetDirectoryName = RvmUtils.buildGemSetDirectoryName( "ruby-1.9.3-p0", "default" );
        assertThat( "ruby-1.9.3-p0", equalTo( gemSetDirectoryName ) );

        gemSetDirectoryName = RvmUtils.buildGemSetDirectoryName( "ruby-1.9.3-p0", "rails31" );
        assertThat( "ruby-1.9.3-p0@rails31", equalTo( gemSetDirectoryName ) );

    }

    @Test
    public void testBuildGemHomePath() {

        String gemSetDirectoryName;
        gemSetDirectoryName = RvmUtils.buildGemHomePath( "/home/markw/.rvm/gems", "ruby-1.9.3-p0", "default" );
        assertThat( "/home/markw/.rvm/gems/ruby-1.9.3-p0", equalTo( gemSetDirectoryName ) );

        gemSetDirectoryName = RvmUtils.buildGemHomePath( "/home/markw/.rvm/gems", "ruby-1.9.3-p0", "rails31" );
        assertThat( "/home/markw/.rvm/gems/ruby-1.9.3-p0@rails31", equalTo( gemSetDirectoryName ) );
    }

    @Test
    public void testBuildBinPath() {

        String binPath;

        binPath = RvmUtils.buildBinPath( "/home/markw/.rvm/rubies", "/home/markw/.rvm/gems", "ruby-1.9.3-p0", "default" );
        assertThat(
                "/home/markw/.rvm/gems/ruby-1.9.3-p0/bin:/home/markw/.rvm/gems/ruby-1.9.3-p0@global/bin:/home/markw/.rvm/rubies/ruby-1.9.3-p0/bin",
                equalTo( binPath ) );

        binPath = RvmUtils.buildBinPath( "/home/markw/.rvm/rubies", "/home/markw/.rvm/gems", "ruby-1.9.3-p0", "rails31" );
        assertThat(
                "/home/markw/.rvm/gems/ruby-1.9.3-p0@rails31/bin:/home/markw/.rvm/gems/ruby-1.9.3-p0@global/bin:/home/markw/.rvm/rubies/ruby-1.9.3-p0/bin",
                equalTo( binPath ) );
    }

    @Test
    public void testBuildRubyBinPath() {

        String rubyBinPath;

        rubyBinPath = RvmUtils.buildRubyBinPath( "/home/markw/.rvm/rubies", "ruby-1.9.3-p0" );
        assertThat( "/home/markw/.rvm/rubies/ruby-1.9.3-p0/bin", equalTo( rubyBinPath ) );
    }

    @Test
    public void testSplitRakeTargets() {

        String targets = "db:migrate spec";

        List<String> targetList = RvmUtils.splitTokens( targets );

        assertThat( 2, equalTo( targetList.size() ) );

        assertTrue( targetList.contains( "db:migrate" ) );
        assertTrue( targetList.contains( "spec" ) );

    }
}
