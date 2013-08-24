package com.alienfast.bamboozled.ruby.util;

import static org.hamcrest.CoreMatchers.equalTo;
import static org.hamcrest.MatcherAssert.assertThat;

import org.junit.Assert;
import org.junit.Test;

/**
 * 
 */
public class FileSystemHelperTest {

    private FileSystemHelper fileSystemHelper = new FileSystemHelper();

    @Test
    public void testResolve() throws Exception {

        String userHome = this.fileSystemHelper.getUserHome();
        Assert.assertNotNull( userHome );

        assertThat( this.fileSystemHelper.resolve( "~/.ssh" ), equalTo( userHome + "/.ssh" ) );
        assertThat( this.fileSystemHelper.resolve( "~\\.ssh" ), equalTo( userHome + "\\.ssh" ) );

        assertResolveSamePath( "/var/log/xyz.log" );
        assertResolveSamePath( "\\var\\log\\xyz.log" );
        assertResolveSamePath( "C:\\temp\\log\\xyz.log" );
    }

    private void assertResolveSamePath( String absolutePath ) {

        assertThat( this.fileSystemHelper.resolve( absolutePath ), equalTo( absolutePath ) );
    }
}
