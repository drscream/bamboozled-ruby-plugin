package com.alienfast.bamboozled.ruby.rt.system;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.alienfast.bamboozled.ruby.rt.RubyRuntime;
import com.alienfast.bamboozled.ruby.rt.system.SystemRubyLocator;
import com.alienfast.bamboozled.ruby.util.FileSystemHelper;

/**
 * Test the system ruby locator
 */
@RunWith( MockitoJUnitRunner.class )
public class SystemRubyLocatorTest {

    @Mock
    FileSystemHelper fileSystemHelper;

    @Mock
    SystemRubyLocator systemRubyLocator;

    @Before
    public void setup() {

        this.systemRubyLocator = new SystemRubyLocator( this.fileSystemHelper );
    }

    @Test
    public void testListRubyRuntimes() throws Exception {

        when( this.fileSystemHelper.pathExists( "/usr/bin", "ruby" ) ).thenReturn( true );
        when( this.fileSystemHelper.pathExists( "/usr/bin", "gem" ) ).thenReturn( true );

        List<RubyRuntime> rubyRuntimeList = this.systemRubyLocator.listRubyRuntimes();

        assertThat( rubyRuntimeList.size(), equalTo( 1 ) );

        assertThat( rubyRuntimeList.get( 0 ).getName(), equalTo( "1.8.7-p358" ) );

    }
}
