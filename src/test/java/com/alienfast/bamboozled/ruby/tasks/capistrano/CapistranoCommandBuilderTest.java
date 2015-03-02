package com.alienfast.bamboozled.ruby.tasks.capistrano;

import static org.hamcrest.MatcherAssert.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.hamcrest.Matchers.is;
import static org.mockito.Mockito.when;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.runners.MockitoJUnitRunner;

import com.alienfast.bamboozled.ruby.fixtures.RvmFixtures;
import com.alienfast.bamboozled.ruby.tasks.AbstractBuilderTest;
import com.alienfast.bamboozled.ruby.tasks.AbstractBundleExecCommandBuilder;
import com.google.common.collect.Lists;

/**
 * Test the Capistrano command builder.
 */
@RunWith( MockitoJUnitRunner.class )
public class CapistranoCommandBuilderTest extends AbstractBuilderTest {

    private static final String TEST_TASK = "cold:deploy";

    private CapistranoCommandBuilder capistranoCommandBuilder;

    @Override
    @Before
    public void setUp() throws Exception {

        this.capistranoCommandBuilder = new CapistranoCommandBuilder(
                getCapabilityContext(),
                getRvmRubyLocator(),
                getRubyRuntime(),
                getRubyExecutablePath() );

        when(
                getRvmRubyLocator().buildExecutablePath(
                        getRubyRuntime().getRubyRuntimeName(),
                        getRubyExecutablePath(),
                        CapistranoCommandBuilder.CAP_COMMAND ) ).thenReturn( RvmFixtures.CAP_PATH );
        when(
                getRvmRubyLocator().buildExecutablePath(
                        getRubyRuntime().getRubyRuntimeName(),
                        getRubyExecutablePath(),
                        AbstractBundleExecCommandBuilder.BUNDLE_COMMAND ) ).thenReturn( RvmFixtures.BUNDLER_PATH );

    }

    @Test
    public void testAddRubyExecutable() throws Exception {

        this.capistranoCommandBuilder.addRubyExecutable();
        assertThat( this.capistranoCommandBuilder.build(), hasItem( getRubyRuntime().getRubyExecutablePath() ) );
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
    public void testAddIfTrace() throws Exception {

        this.capistranoCommandBuilder.addIfTrace( "true" );
        assertThat( this.capistranoCommandBuilder.build(), hasItem( "--trace" ) );

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
                .addIfTrace( "true" )
                .addIfVerbose( "true" )
                .addTasks( Lists.newArrayList( TEST_TASK ) );
        assertThat( this.capistranoCommandBuilder.build().size(), is( 7 ) );
    }
}
