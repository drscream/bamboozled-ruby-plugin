package com.alienfast.bamboozled.ruby.tasks.capistrano;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.Mock;
import org.mockito.runners.MockitoJUnitRunner;

import com.alienfast.bamboozled.ruby.fixtures.RvmFixtures;
import com.alienfast.bamboozled.ruby.rt.RubyRuntime;
import com.alienfast.bamboozled.ruby.rt.rvm.RvmRubyLocator;
import com.alienfast.bamboozled.ruby.tasks.AbstractBundleExecCommandBuilder;
import com.alienfast.bamboozled.ruby.tasks.capistrano.CapistranoCommandBuilder;
import com.google.common.collect.Lists;

/**
 * Test the Capistrano command builder.
 */
@RunWith( MockitoJUnitRunner.class )
public class CapistranoCommandBuilderTest {

    private static final String TEST_TASK = "cold:deploy";

    @Mock
    private RvmRubyLocator rvmRubyLocator;

    private final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
    private final String rubyExecutablePath = RvmFixtures.getMRIRubyRuntimeDefaultGemSet().getRubyExecutablePath();

    private CapistranoCommandBuilder capistranoCommandBuilder;

    @Before
    public void setUp() throws Exception {

        this.capistranoCommandBuilder = new CapistranoCommandBuilder( this.rvmRubyLocator, this.rubyRuntime, RvmFixtures
                .getMRIRubyRuntimeDefaultGemSet()
                .getRubyExecutablePath() );

        when(
                this.rvmRubyLocator.buildExecutablePath(
                        this.rubyRuntime.getRubyRuntimeName(),
                        this.rubyExecutablePath,
                        CapistranoCommandBuilder.CAP_COMMAND ) ).thenReturn( RvmFixtures.CAP_PATH );
        when(
                this.rvmRubyLocator.buildExecutablePath(
                        this.rubyRuntime.getRubyRuntimeName(),
                        this.rubyExecutablePath,
                        AbstractBundleExecCommandBuilder.BUNDLE_COMMAND ) ).thenReturn( RvmFixtures.BUNDLER_PATH );

    }

    @Test
    public void testAddRubyExecutable() throws Exception {

        this.capistranoCommandBuilder.addRubyExecutable();
        assertThat( this.capistranoCommandBuilder.build(), hasItem( this.rubyRuntime.getRubyExecutablePath() ) );
    }

    @Test
    public void testAddIfBundleExec() throws Exception {

        this.capistranoCommandBuilder.addIfBundleExec( "true" );
        assertThat( this.capistranoCommandBuilder.build(), hasItem( RvmFixtures.BUNDLER_PATH ) );
        assertThat( this.capistranoCommandBuilder.build(), hasItem( "exec" ) );
        assertThat( this.capistranoCommandBuilder.build().size(), is( 2 ) );

    }

    @Test
    public void testAddCapExecutable() throws Exception {

        this.capistranoCommandBuilder.addCapistranoExecutable( "false" );
        assertThat( this.capistranoCommandBuilder.build(), hasItem( RvmFixtures.CAP_PATH ) );
    }

    @Test
    public void testAddCapExecutableWithBundleExec() throws Exception {

        this.capistranoCommandBuilder.addCapistranoExecutable( "true" );
        assertThat( this.capistranoCommandBuilder.build(), hasItem( "cap" ) );
    }

    @Test
    public void testAddIfVerbose() throws Exception {

        this.capistranoCommandBuilder.addIfVerbose( "true" );
        assertThat( this.capistranoCommandBuilder.build(), hasItem( "--verbose" ) );
    }

    @Test
    public void testAddIfDebug() throws Exception {

        this.capistranoCommandBuilder.addIfDebug( "true" );
        assertThat( this.capistranoCommandBuilder.build(), hasItem( "--debug" ) );

    }

    @Test
    public void testAddTasks() throws Exception {

        this.capistranoCommandBuilder.addTasks( Lists.newArrayList( TEST_TASK ) );
        assertThat( this.capistranoCommandBuilder.build(), hasItem( TEST_TASK ) );
    }

    @Test
    public void testBuild() throws Exception {

        String bundleExecFlag = "true";

        this.capistranoCommandBuilder
                .addRubyExecutable()
                .addIfBundleExec( bundleExecFlag )
                .addCapistranoExecutable( bundleExecFlag )
                .addIfDebug( "true" )
                .addIfVerbose( "true" )
                .addTasks( Lists.newArrayList( TEST_TASK ) );
        assertThat( this.capistranoCommandBuilder.build().size(), is( 7 ) );
    }
}
