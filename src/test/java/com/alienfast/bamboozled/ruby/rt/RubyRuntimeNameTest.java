package com.alienfast.bamboozled.ruby.rt;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Test;

/**
 * Test the ruby runtime name parsing method
 */
public class RubyRuntimeNameTest {

    final String rubyRuntimeNameString = "ruby-1.9.3-p286@rails32";
    final RubyRuntimeName rubyRuntimeName = new RubyRuntimeName( "ruby-1.9.3-p286", "rails32" );

    @Test
    public void testParseString() throws Exception {

        assertThat( RubyRuntimeName.parseString( this.rubyRuntimeNameString ), equalTo( this.rubyRuntimeName ) );

    }

}
