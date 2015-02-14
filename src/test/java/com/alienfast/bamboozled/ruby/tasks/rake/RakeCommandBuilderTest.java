package com.alienfast.bamboozled.ruby.tasks.rake;

import static org.junit.Assert.assertEquals;
import static org.mockito.Mockito.when;

import java.util.Iterator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.alienfast.bamboozled.ruby.fixtures.RvmFixtures;
import com.alienfast.bamboozled.ruby.rt.RubyRuntime;
import com.alienfast.bamboozled.ruby.rt.rvm.RvmRubyLocator;
import com.alienfast.bamboozled.ruby.tasks.AbstractBundleExecCommandBuilder;
import com.alienfast.bamboozled.ruby.tasks.rake.RakeCommandBuilder;

/**
 * Test the rake command builder.
 */
@RunWith( MockitoJUnitRunner.class )
public class RakeCommandBuilderTest {

    @Mock
    RvmRubyLocator rvmRubyLocator;

    final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
    final String rubyExecutablePath = RvmFixtures.getMRIRubyRuntimeDefaultGemSet().getRubyExecutablePath();

    RakeCommandBuilder rakeCommandBuilder;

    @Before
    public void setUp() throws Exception {

        when(
                this.rvmRubyLocator.buildExecutablePath(
                        this.rubyRuntime.getRubyRuntimeName(),
                        this.rubyExecutablePath,
                        RakeCommandBuilder.RAKE_COMMAND ) ).thenReturn( RvmFixtures.RAKE_PATH );
        when(
                this.rvmRubyLocator.buildExecutablePath(
                        this.rubyRuntime.getRubyRuntimeName(),
                        this.rubyExecutablePath,
                        AbstractBundleExecCommandBuilder.BUNDLE_COMMAND ) ).thenReturn( RvmFixtures.BUNDLER_PATH );

        this.rakeCommandBuilder = new RakeCommandBuilder( this.rvmRubyLocator, this.rubyRuntime, 
                RvmFixtures.getMRIRubyRuntimeDefaultGemSet().getRubyExecutablePath(), 
                "/usr/bin/xvfb-run" );
    }

    @Test
    public void testAddRubyExecutable() throws Exception {

        this.rakeCommandBuilder.addRubyExecutable();
        assertEquals( 1, this.rakeCommandBuilder.build().size() );

        Iterator<String> commandsIterator = this.rakeCommandBuilder.build().iterator();

        assertEquals( this.rubyRuntime.getRubyExecutablePath(), commandsIterator.next() );
    }

    @Test
    public void testAddIfRakefile() throws Exception {

        this.rakeCommandBuilder.addIfRakeFile( null );
        assertEquals( 0, this.rakeCommandBuilder.build().size() );

        this.rakeCommandBuilder.addIfRakeFile( "Rakefile" );

        assertEquals( 2, this.rakeCommandBuilder.build().size() );

        Iterator<String> commandsIterator = this.rakeCommandBuilder.build().iterator();

        assertEquals( RakeCommandBuilder.RAKEFILE_ARG, commandsIterator.next() );
        assertEquals( "Rakefile", commandsIterator.next() );
    }

    @Test
    public void testAddRakeExecutableWithBundleExec() {

        final String bundleExecFlag = "true";

        this.rakeCommandBuilder.addIfBundleExec( bundleExecFlag ).addRakeExecutable( bundleExecFlag );

        Iterator<String> commandsIterator = this.rakeCommandBuilder.build().iterator();

        assertEquals( RvmFixtures.BUNDLER_PATH, commandsIterator.next() );
        assertEquals( AbstractBundleExecCommandBuilder.BUNDLE_EXEC_ARG, commandsIterator.next() );
        assertEquals( RakeCommandBuilder.RAKE_COMMAND, commandsIterator.next() );

    }

    @Test
    public void testAddRakeExecutable() {

        final String bundleExecFlag = "false";

        this.rakeCommandBuilder.addRakeExecutable( bundleExecFlag );

        Iterator<String> commandsIterator = this.rakeCommandBuilder.build().iterator();

        assertEquals( RvmFixtures.RAKE_PATH, commandsIterator.next() );

    }

    @Test
    public void testAddIfRakeLibDir() throws Exception {

        this.rakeCommandBuilder.addIfRakeLibDir( null );
        assertEquals( 0, this.rakeCommandBuilder.build().size() );

        this.rakeCommandBuilder.addIfRakeLibDir( "./rakelib" );

        assertEquals( 2, this.rakeCommandBuilder.build().size() );

        Iterator<String> commandsIterator = this.rakeCommandBuilder.build().iterator();

        assertEquals( RakeCommandBuilder.RAKELIBDIR_ARG, commandsIterator.next() );
        assertEquals( "./rakelib", commandsIterator.next() );
    }

    @Test
    public void testAddIfBundleExec() throws Exception {

        this.rakeCommandBuilder.addIfBundleExec(  null );
        assertEquals( 0, this.rakeCommandBuilder.build().size() );

        this.rakeCommandBuilder.addIfBundleExec( "false" );
        assertEquals( 0, this.rakeCommandBuilder.build().size() );

        this.rakeCommandBuilder.addIfBundleExec( "true" );

        assertEquals( 2, this.rakeCommandBuilder.build().size() );

        Iterator<String> commandsIterator = this.rakeCommandBuilder.build().iterator();

        commandsIterator.next(); // bundle
        assertEquals( AbstractBundleExecCommandBuilder.BUNDLE_EXEC_ARG, commandsIterator.next() );
    }    
    
    @Test
    public void testAddIfXvfbRun() throws Exception {

        this.rakeCommandBuilder.addIfXvfbRun(  null );
        assertEquals( 0, this.rakeCommandBuilder.build().size() );

        this.rakeCommandBuilder.addIfXvfbRun( "false" );
        assertEquals( 0, this.rakeCommandBuilder.build().size() );

        this.rakeCommandBuilder.addIfXvfbRun( "true" );

        assertEquals( 2, this.rakeCommandBuilder.build().size() );

        Iterator<String> commandsIterator = this.rakeCommandBuilder.build().iterator();

        assertEquals( "/usr/bin/xvfb-run", commandsIterator.next() );
        assertEquals( RakeCommandBuilder.XVFB_RUN_ARG, commandsIterator.next() );
    }        
    
    
    @Test
    public void testAddIfVerbose() throws Exception {

        this.rakeCommandBuilder.addIfVerbose( null );
        assertEquals( 0, this.rakeCommandBuilder.build().size() );

        this.rakeCommandBuilder.addIfVerbose( "false" );
        assertEquals( 0, this.rakeCommandBuilder.build().size() );

        this.rakeCommandBuilder.addIfVerbose( "true" );

        assertEquals( 1, this.rakeCommandBuilder.build().size() );

        Iterator<String> commandsIterator = this.rakeCommandBuilder.build().iterator();

        assertEquals( AbstractBundleExecCommandBuilder.VERBOSE_ARG, commandsIterator.next() );
    }

    @Test
    public void testAddIfTrace() throws Exception {

        this.rakeCommandBuilder.addIfTrace( null );
        assertEquals( 0, this.rakeCommandBuilder.build().size() );

        this.rakeCommandBuilder.addIfTrace( "false" );
        assertEquals( 0, this.rakeCommandBuilder.build().size() );

        this.rakeCommandBuilder.addIfTrace( "true" );
        assertEquals( 1, this.rakeCommandBuilder.build().size() );

        Iterator<String> commandsIterator = this.rakeCommandBuilder.build().iterator();

        assertEquals( RakeCommandBuilder.TRACE_ARG, commandsIterator.next() );
    }
}
