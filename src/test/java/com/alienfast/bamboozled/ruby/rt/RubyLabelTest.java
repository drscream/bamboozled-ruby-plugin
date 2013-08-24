package com.alienfast.bamboozled.ruby.rt;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

/**
 * Tests for the ruby label parsing logic.
 */
public class RubyLabelTest {

    String legacyLabel = "1.9.2-p0@rails31";
    String newLabel = "rbenv 1.9.2-p0@rails31";
    String badLabel = "rbenv:1.9.2-p0";

    @Test
    public void testGetLabelFromStringShouldParseLegacyLabel() throws Exception {

        RubyLabel legacyRubyLabel = RubyLabel.fromString( this.legacyLabel );

        assertThat( legacyRubyLabel.getRubyRuntimeManager(), equalTo( RubyLabel.DEFAULT_RUNTIME_MANAGER ) );
        assertThat( legacyRubyLabel.getRubyRuntime(), equalTo( "1.9.2-p0@rails31" ) );

    }

    @Test
    public void testGetLabelFromStringShouldParseLabel() throws Exception {

        RubyLabel legacyRubyLabel = RubyLabel.fromString( this.newLabel );

        assertThat( legacyRubyLabel.getRubyRuntimeManager(), equalTo( "rbenv" ) );
        assertThat( legacyRubyLabel.getRubyRuntime(), equalTo( "1.9.2-p0@rails31" ) );

    }

    @Test( expected = IllegalArgumentException.class )
    public void testGetLabelFromStringShouldThrowExceptionOnForLabel() throws Exception {

        RubyLabel.fromString( this.badLabel );
    }
}
