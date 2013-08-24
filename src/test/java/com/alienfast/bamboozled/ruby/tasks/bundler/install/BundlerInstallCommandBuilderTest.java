package com.alienfast.bamboozled.ruby.tasks.bundler.install;

import static org.hamcrest.Matchers.hasItems;
import static org.hamcrest.core.Is.is;
import static org.junit.Assert.assertThat;
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
import com.alienfast.bamboozled.ruby.tasks.bundler.install.BundlerInstallCommandBuilder;

/**
 * Test the bundler command builder.
 */
@RunWith( MockitoJUnitRunner.class )
public class BundlerInstallCommandBuilderTest {

    @Mock
    private RvmRubyLocator rvmRubyLocator;

    private final RubyRuntime rubyRuntime = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
    private final String rubyExecutablePath = RvmFixtures.getMRIRubyRuntimeDefaultGemSet().getRubyExecutablePath();

    private BundlerInstallCommandBuilder bundlerCommandBuilder;

    @Before
    public void setUp() throws Exception {

        when(
                this.rvmRubyLocator.buildExecutablePath(
                        this.rubyRuntime.getRubyRuntimeName(),
                        this.rubyExecutablePath,
                        BundlerInstallCommandBuilder.BUNDLE_COMMAND ) ).thenReturn( RvmFixtures.BUNDLER_PATH );
        this.bundlerCommandBuilder = new BundlerInstallCommandBuilder( this.rvmRubyLocator, this.rubyRuntime, this.rubyExecutablePath );

    }

    @Test
    public void testAddRubyExecutable() throws Exception {

        this.bundlerCommandBuilder.addRubyExecutable();
        assertThat( this.bundlerCommandBuilder.build().size(), is( 1 ) );
        assertThat( this.bundlerCommandBuilder.build(), hasItems( this.rubyRuntime.getRubyExecutablePath() ) );

    }

    @Test
    public void testAddBundleExecutable() throws Exception {

        this.bundlerCommandBuilder.addBundleExecutable();
        assertThat( this.bundlerCommandBuilder.build(), hasItems( RvmFixtures.BUNDLER_PATH ) );

    }

    @Test
    public void testAddInstall() throws Exception {

        this.bundlerCommandBuilder.addInstall();
        assertThat( this.bundlerCommandBuilder.build(), hasItems( "install" ) );

    }

    @Test
    public void testAddPath() throws Exception {

        this.bundlerCommandBuilder.addPath( "gems" );
        assertThat( this.bundlerCommandBuilder.build(), hasItems( "--path" ) );
        assertThat( this.bundlerCommandBuilder.build(), hasItems( "gems" ) );

    }

    @Test
    public void testAddIfBinStubs() throws Exception {

        this.bundlerCommandBuilder.addIfBinStubs( "true" );
        assertThat( this.bundlerCommandBuilder.build(), hasItems( "--binstubs" ) );

    }

    @Test
    public void testBuild() throws Exception {

        this.bundlerCommandBuilder.addRubyExecutable().addBundleExecutable().addInstall().addPath( "gems" ).addIfBinStubs( "true" ).build();

        assertThat( this.bundlerCommandBuilder.build().size(), is( 6 ) );

        Iterator<String> commandTokens = this.bundlerCommandBuilder.build().iterator();

        assertThat( commandTokens.next(), is( this.rubyRuntime.getRubyExecutablePath() ) );
        assertThat( commandTokens.next(), is( RvmFixtures.BUNDLER_PATH ) );
        assertThat( commandTokens.next(), is( "install" ) );
        assertThat( commandTokens.next(), is( "--path" ) );
        assertThat( commandTokens.next(), is( "gems" ) );
        assertThat( commandTokens.next(), is( "--binstubs" ) );

    }
}
