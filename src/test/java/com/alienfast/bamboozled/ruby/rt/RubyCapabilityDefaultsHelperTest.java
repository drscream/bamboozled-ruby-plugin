//package com.alienfast.bamboozled.ruby.rt;
//
//import static java.util.Arrays.asList;
//import static org.mockito.Mockito.verify;
//import static org.mockito.Mockito.when;
//
//import org.junit.Before;
//import org.junit.Test;
//import org.junit.runner.RunWith;
//import org.mockito.Mock;
//import org.mockito.runners.MockitoJUnitRunner;
//
//import com.alienfast.bamboozled.ruby.fixtures.RvmFixtures;
//import com.alienfast.bamboozled.ruby.rt.rvm.RvmRubyLocator;
//import com.atlassian.bamboo.v2.build.agent.capability.CapabilityImpl;
//import com.atlassian.bamboo.v2.build.agent.capability.CapabilitySet;
//import com.google.common.collect.Lists;
//
///**
// * Test the ruby capabilities helper.
// */
//@RunWith( MockitoJUnitRunner.class )
//public class RubyCapabilityDefaultsHelperTest {
//
//    @Mock
//    RubyLocatorServiceFactory rubyLocatorServiceFactory;
//
//    @Mock
//    RubyRuntimeLocatorService rubyRuntimeLocatorService;
//
//    @Mock
//    RvmRubyLocator rvmRubyLocator;
//
//    @Mock
//    CapabilitySet capabilitySet;
//
//    RubyCapabilityDefaultsHelper rubyCapabilityDefaultsHelper;
//
//    @Before
//    public void setUp() throws Exception {
//
//        this.rubyCapabilityDefaultsHelper = new RubyCapabilityDefaultsHelper( this.rubyLocatorServiceFactory );
//
//        when( this.rubyLocatorServiceFactory.getLocatorServices() ).thenReturn( Lists.newArrayList( this.rubyRuntimeLocatorService ) );
//
//    }
//
//    @Test
//    public void testAddDefaultCapabilities() throws Exception {
//
//        final RubyRuntime rubyRuntimeMRI = RvmFixtures.getMRIRubyRuntimeDefaultGemSet();
//        final RubyRuntime rubyRuntimeJRuby = RvmFixtures.getJRubyRuntimeDefaultGemSet();
//
//        when( this.rubyRuntimeLocatorService.getRuntimeManagerName() ).thenReturn( "RVM" );
//        when( this.rubyRuntimeLocatorService.getRubyLocator() ).thenReturn( this.rvmRubyLocator );
//        when( this.rubyRuntimeLocatorService.isInstalled() ).thenReturn( true );
//        when( this.rvmRubyLocator.listRubyRuntimes() ).thenReturn( asList( rubyRuntimeMRI, rubyRuntimeJRuby ) );
//
//        this.rubyCapabilityDefaultsHelper.addDefaultCapabilities( this.capabilitySet );
//
//        verify( this.capabilitySet ).addCapability(
//                new CapabilityImpl( "system.builder.ruby.RVM " + rubyRuntimeMRI.getRubyRuntimeName(), rubyRuntimeMRI
//                        .getRubyExecutablePath() ) );
//        verify( this.capabilitySet ).addCapability(
//                new CapabilityImpl( "system.builder.ruby.RVM " + rubyRuntimeJRuby.getRubyRuntimeName(), rubyRuntimeJRuby
//                        .getRubyExecutablePath() ) );
//    }
//}
