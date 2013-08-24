package com.alienfast.bamboozled.ruby.rt.rvm;

import static com.alienfast.bamboozled.ruby.fixtures.RvmFixtures.USER_HOME;
import static com.alienfast.bamboozled.ruby.fixtures.RvmFixtures.getSystemRvmInstallation;
import static com.alienfast.bamboozled.ruby.fixtures.RvmFixtures.getUserRvmInstallation;
import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertFalse;
import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertNull;
import static org.junit.Assert.assertTrue;
import static org.mockito.Mockito.reset;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.alienfast.bamboozled.ruby.rt.RubyLocator;
import com.alienfast.bamboozled.ruby.rt.rvm.RvmInstallation;
import com.alienfast.bamboozled.ruby.rt.rvm.RvmRubyRuntimeLocatorService;
import com.alienfast.bamboozled.ruby.util.FileSystemHelper;
import com.alienfast.bamboozled.ruby.util.PathNotFoundException;

/**
 * Unit test for RVM related operations.
 */
@RunWith( MockitoJUnitRunner.class )
public class RvmLocatorServiceTest {

    @Mock
    private FileSystemHelper fileSystemHelper;

    private RvmRubyRuntimeLocatorService rvmLocatorService;

    @Before
    public void setup() {

        this.rvmLocatorService = new RvmRubyRuntimeLocatorService( this.fileSystemHelper );
    }

    @Test
    public void testLocateRvmInstallation() {

        when( this.fileSystemHelper.getUserHome() ).thenReturn( USER_HOME );

        RvmInstallation userRvm = getUserRvmInstallation();

        primeMockWithRvmInstallation( userRvm );

        RvmInstallation rvmUserInstallation = this.rvmLocatorService.locateRvmInstallation();

        assertNotNull( rvmUserInstallation );

        assertTrue( rvmUserInstallation.isUserInstall() );

        assertEquals( getUserRvmInstallation(), rvmUserInstallation );

        assertFalse( rvmUserInstallation.isReadOnly() );

        reset( this.fileSystemHelper );

        RvmInstallation systemRvm = getSystemRvmInstallation();

        primeMockWithRvmInstallation( systemRvm );

        RvmInstallation rvmSystemInstallation = this.rvmLocatorService.locateRvmInstallation();

        assertNotNull( rvmSystemInstallation );

        assertEquals( getSystemRvmInstallation(), rvmSystemInstallation );

        assertTrue( rvmSystemInstallation.isReadOnly() );

        reset( this.fileSystemHelper );

        rvmSystemInstallation = this.rvmLocatorService.locateRvmInstallation();

        assertNull( rvmSystemInstallation );

    }

    @Test
    public void testGetRubyLocator() {

        when( this.fileSystemHelper.getUserHome() ).thenReturn( USER_HOME );

        RvmInstallation userRvm = getUserRvmInstallation();

        primeMockWithRvmInstallation( userRvm );

        RubyLocator rvmRubyLocator = this.rvmLocatorService.getRvmRubyLocator();

        assertNotNull( rvmRubyLocator );

    }

    @Test( expected = PathNotFoundException.class )
    public void testGetRubyLocatorWhenNotFound() {

        when( this.fileSystemHelper.getUserHome() ).thenReturn( null );
        when( this.fileSystemHelper.pathExists( "/usr/local/rvm" ) ).thenReturn( false );
        when( this.fileSystemHelper.pathExists( "/opt/local/rvm" ) ).thenReturn( false );

        RvmInstallation userRvm = getUserRvmInstallation();

        primeMockWithRvmInstallation( userRvm );

        this.rvmLocatorService.getRvmRubyLocator();
    }

    private void primeMockWithRvmInstallation( RvmInstallation rvmInstallation ) {

        when( this.fileSystemHelper.pathExists( rvmInstallation.getInstallPath() ) ).thenReturn( true );
        when( this.fileSystemHelper.pathExists( rvmInstallation.getRubiesPath() ) ).thenReturn( true );
        when( this.fileSystemHelper.pathExists( rvmInstallation.getGemsPath() ) ).thenReturn( true );

    }

}
