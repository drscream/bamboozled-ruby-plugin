package com.alienfast.bamboozled.ruby.rt;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.alienfast.bamboozled.ruby.rt.rbenv.RbenvRubyRuntimeLocatorService;
import com.alienfast.bamboozled.ruby.rt.rvm.RvmRubyRuntimeLocatorService;
import com.alienfast.bamboozled.ruby.rt.system.SystemRubyRuntimeLocatorService;
import com.alienfast.bamboozled.ruby.rt.windows.WindowsRubyRuntimeLocatorService;
import com.google.common.collect.Lists;

/**
 * Manages the various Ruby Runtime Managers.
 */
public class RubyLocatorServiceFactory {

    private static final Logger log = LoggerFactory.getLogger( RubyLocatorServiceFactory.class );

    private final List<RubyRuntimeLocatorService> rubyRuntimeLocatorServices;

    public RubyLocatorServiceFactory() {

        this.rubyRuntimeLocatorServices = Lists.newArrayList(
                new SystemRubyRuntimeLocatorService(),
                new RbenvRubyRuntimeLocatorService(),
                new RvmRubyRuntimeLocatorService(),
                new WindowsRubyRuntimeLocatorService() );

        //        log.info( "Loaded ruby runtime managers {} ", this.rubyRuntimeLocatorServices );
    }

    public RubyLocatorServiceFactory(SystemRubyRuntimeLocatorService systemRubyRuntimeLocatorService,
            RvmRubyRuntimeLocatorService rvmRubyRuntimeLocatorService, RbenvRubyRuntimeLocatorService rbenvRubyRuntimeLocatorService,
            WindowsRubyRuntimeLocatorService windowsRubyRuntimeLocatorService) {

        this.rubyRuntimeLocatorServices = Lists.newArrayList(
                systemRubyRuntimeLocatorService,
                rbenvRubyRuntimeLocatorService,
                rvmRubyRuntimeLocatorService,
                windowsRubyRuntimeLocatorService );

        //        log.info( "Loaded ruby runtime managers {} ", this.rubyRuntimeLocatorServices );
    }

    public List<RubyRuntimeLocatorService> getLocatorServices() {

        return this.rubyRuntimeLocatorServices;
    }

    public RubyLocator acquireRubyLocator( String rubyRuntimeManager ) throws RuntimeLocatorException {

        // default to RVM in the case of a missing runtime manager tag.
        if ( rubyRuntimeManager == null || rubyRuntimeManager.equals( "" ) ) {
            rubyRuntimeManager = RvmRubyRuntimeLocatorService.MANAGER_LABEL;
        }

        for (RubyRuntimeLocatorService rubyRuntimeLocatorService : this.rubyRuntimeLocatorServices) {
            if ( rubyRuntimeLocatorService.getRuntimeManagerName().equals( rubyRuntimeManager ) ) {
                if ( rubyRuntimeLocatorService.isInstalled() ) {
                    return rubyRuntimeLocatorService.getRubyLocator();
                }
                else {
                    throw new RuntimeLocatorException( "Unable to locate Runtime Manager installation for - " + rubyRuntimeManager
                            + ".  Please make sure to detect server capabilities and ensure that those paths still exist." );
                }
            }
        }

        throw new RuntimeLocatorException( "Unable to locate Runtime Manager for - " + rubyRuntimeManager
                + ".  Please make sure to detect server capabilities and ensure that those paths still exist." );
    }
}
