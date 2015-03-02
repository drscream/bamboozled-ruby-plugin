package com.alienfast.bamboozled.ruby.rt.rbenv;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

import com.alienfast.bamboozled.ruby.rt.RubyRuntimeName;
import com.alienfast.bamboozled.ruby.rt.rbenv.RbenvUtils;

/**
 * Tests for the rbenv util methods.
 */
public class RbenvUtilsTest {

    @Test
    public void testBuildRbenvRubiesPath() throws Exception {

        String rubiesPath = RbenvUtils.buildRbenvRubiesPath( "/Users/kross/.rbenv" );

        assertThat( rubiesPath, equalTo( "/Users/kross/.rbenv/versions" ) );
    }

    @Test
    public void testParseRubyRuntimeName() throws Exception {

        RubyRuntimeName rubyRuntimeName = RbenvUtils.parseRubyRuntimeName( "1.9.3-p194@default" );

        assertThat( rubyRuntimeName.getVersion(), equalTo( "1.9.3-p194" ) );
        assertThat( rubyRuntimeName.getGemSet(), equalTo( "default" ) );
    }

    @Test
    public void testBuildRubyExecutablePath() throws Exception {

        // /Users/kross/.rbenv/versions/1.9.3-p194/bin/ruby

        String rubyBinPath = RbenvUtils.buildRubyExecutablePath( "/Users/kross/.rbenv", "1.9.3-p194" );

        assertThat( rubyBinPath, equalTo( "/Users/kross/.rbenv/versions/1.9.3-p194/bin/ruby" ) );

    }

}
