package com.alienfast.bamboozled.ruby.rt.windows;

import org.apache.commons.lang.SystemUtils;

import com.alienfast.bamboozled.ruby.rt.RubyLocator;
import com.alienfast.bamboozled.ruby.rt.RubyRuntimeLocatorService;
import com.alienfast.bamboozled.ruby.util.ExecHelper;
import com.alienfast.bamboozled.ruby.util.FileSystemHelper;

/**
 * This service assists with detection of ruby installations in windows.
 */
public class WindowsRubyRuntimeLocatorService implements RubyRuntimeLocatorService {

    public static final String MANAGER_LABEL = "WINDOWS";

    private final WindowsRubyLocator windowsRubyLocator;

    public WindowsRubyRuntimeLocatorService() {

        this.windowsRubyLocator = new WindowsRubyLocator( new FileSystemHelper(), new ExecHelper() );
    }

    public WindowsRubyRuntimeLocatorService(FileSystemHelper fileSystemHelper, ExecHelper execHelper) {

        this.windowsRubyLocator = new WindowsRubyLocator( fileSystemHelper, execHelper );
    }

    @Override
    public RubyLocator getRubyLocator() {

        return this.windowsRubyLocator;
    }

    @Override
    public boolean isInstalled() {

        return SystemUtils.IS_OS_WINDOWS;
    }

    @Override
    public String getRuntimeManagerName() {

        return MANAGER_LABEL;
    }
}
