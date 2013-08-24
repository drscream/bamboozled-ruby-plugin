package com.alienfast.bamboozled.ruby.rt.windows;

import static org.hamcrest.Matchers.equalTo;
import static org.hamcrest.Matchers.hasItems;
import static org.junit.Assert.assertThat;
import static org.mockito.Matchers.any;
import static org.mockito.Matchers.eq;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.alienfast.bamboozled.ruby.rt.windows.WindowsRubyLocator;
import com.alienfast.bamboozled.ruby.util.ExecHelper;
import com.alienfast.bamboozled.ruby.util.FileSystemHelper;

/**
 *
 *
 */
@RunWith( MockitoJUnitRunner.class )
public class WindowsRubyLocatorTest {

    private final String whereOutput = "C:\\Windows\\System32\\notepad.exe\r\n" + "C:\\Windows\\notepad.exe\r\n" + "\r\n";

    @Mock
    ExecHelper execHelper;

    @Mock
    FileSystemHelper fileSystemHelper;

    WindowsRubyLocator windowsRubyLocator;

    @Before
    public void setup() throws Exception {

        when( this.execHelper.getExecutablePath( any( String.class ), eq( true ) ) ).thenReturn( this.whereOutput );
        this.windowsRubyLocator = new WindowsRubyLocator( this.fileSystemHelper, this.execHelper );

    }

    @Test
    public void testDetectExecutableOnPath() throws Exception {

        String path = this.windowsRubyLocator.detectExecutableOnPath( "notepad.exe" );

        assertThat( path, equalTo( "C:\\Windows\\System32\\notepad.exe" ) );
    }

    @Test
    public void testDetectExecutablesOnPath() throws Exception {

        List<String> path = this.windowsRubyLocator.detectExecutablesOnPath( "notepad.exe" );

        assertThat( path, hasItems( "C:\\Windows\\System32\\notepad.exe", "C:\\Windows\\notepad.exe" ) );
    }

}
