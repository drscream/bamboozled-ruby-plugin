package com.alienfast.bamboozled.ruby.rt.rbenv;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.notNullValue;
import static org.hamcrest.core.IsEqual.equalTo;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.alienfast.bamboozled.ruby.rt.RubyLocator;
import com.alienfast.bamboozled.ruby.rt.rbenv.RbenvRubyRuntimeLocatorService;
import com.alienfast.bamboozled.ruby.util.FileSystemHelper;

/**
 * Unit test for rbenv locator service.
 */
@RunWith( MockitoJUnitRunner.class )
public class RbenvRubyRuntimeLocatorServiceTest {

    private static final String TEST_HOME_DIR = "/Users/markw";
    private static final String RBENV_DIR = TEST_HOME_DIR + "/.rbenv";

    private RbenvRubyRuntimeLocatorService rbenvRubyRuntimeLocatorService;

    @Mock
    private FileSystemHelper fileSystemHelper;

    @Before
    public void setUp() throws Exception {

        this.rbenvRubyRuntimeLocatorService = new RbenvRubyRuntimeLocatorService( this.fileSystemHelper );
        when( this.fileSystemHelper.getUserHome() ).thenReturn( TEST_HOME_DIR );

    }

    @Test
    public void testGetRubyLocator() throws Exception {

        when( this.fileSystemHelper.pathExists( RBENV_DIR ) ).thenReturn( true );
        RubyLocator rubyLocator = this.rbenvRubyRuntimeLocatorService.getRubyLocator();
        assertThat( rubyLocator, notNullValue() );

    }

    @Test
    public void testIsInstalled() throws Exception {

        when( this.fileSystemHelper.pathExists( RBENV_DIR ) ).thenReturn( true );
        assertThat( this.rbenvRubyRuntimeLocatorService.isInstalled(), equalTo( true ) );

    }

    @Test
    public void testGetRuntimeManagerName() throws Exception {

        assertThat( this.rbenvRubyRuntimeLocatorService.getRuntimeManagerName(), equalTo( "rbenv" ) );

    }
}
