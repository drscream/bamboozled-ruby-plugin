package com.alienfast.bamboozled.ruby.rt;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

/**
 * Tests for the ruby label parsing logic.
 */
public class RubyLabelTest {

    @Test
    public void testFromKey() throws Exception {

        RubyLabel label = RubyLabel.fromKey( "system.builder.ruby.RVM ruby-2.2.0@af_core" );

        assertThat( label.getRubyRuntimeManager(), equalTo( RubyLabel.DEFAULT_RUNTIME_MANAGER ) );
        assertThat( label.getRubyRuntime(), equalTo( "ruby-2.2.0@af_core" ) );
        System.out.println( label.toString() );
    }

    @Test
    public void testFromString() throws Exception {

        RubyLabel legacyRubyLabel = RubyLabel.fromString( "rbenv 1.9.2-p0@rails31" );

        assertThat( legacyRubyLabel.getRubyRuntimeManager(), equalTo( "rbenv" ) );
        assertThat( legacyRubyLabel.getRubyRuntime(), equalTo( "1.9.2-p0@rails31" ) );
    }

    @Test( expected = IllegalArgumentException.class )
    public void testFromStringError() throws Exception {

        RubyLabel.fromString( "rbenv:1.9.2-p0" );
    }
}
