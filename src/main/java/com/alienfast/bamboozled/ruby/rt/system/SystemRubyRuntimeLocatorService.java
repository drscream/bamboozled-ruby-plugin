package com.alienfast.bamboozled.ruby.rt.system;

import org.apache.commons.lang.SystemUtils;

import com.alienfast.bamboozled.ruby.rt.RubyLocator;
import com.alienfast.bamboozled.ruby.rt.RubyRuntimeLocatorService;
import com.alienfast.bamboozled.ruby.util.FileSystemHelper;

/**
 * Minimal ruby runtime manager designed to locate system installations.
 */
public class SystemRubyRuntimeLocatorService implements RubyRuntimeLocatorService {

    public static final String MANAGER_LABEL = "SYSTEM";

    private final SystemRubyLocator systemRubyLocator;

    public SystemRubyRuntimeLocatorService() {

        this.systemRubyLocator = new SystemRubyLocator( new FileSystemHelper() );
    }

    public SystemRubyRuntimeLocatorService(FileSystemHelper fileSystemHelper) {

        this.systemRubyLocator = new SystemRubyLocator( fileSystemHelper );
    }

    @Override
    public RubyLocator getRubyLocator() {

        return this.systemRubyLocator;
    }

    @Override
    public boolean isInstalled() {

        return SystemUtils.IS_OS_UNIX;
    }

    @Override
    public String getRuntimeManagerName() {

        return MANAGER_LABEL;
    }
}
