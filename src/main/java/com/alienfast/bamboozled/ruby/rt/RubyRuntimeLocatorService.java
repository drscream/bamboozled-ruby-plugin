package com.alienfast.bamboozled.ruby.rt;

/**
 * ruby locator services.
 */
public interface RubyRuntimeLocatorService {

    RubyLocator getRubyLocator();

    /**
     * Check if the ruby runtime manager is installed.
     *
     * @return boolean, true indicates ruby runtime manager is installed.
     */
    boolean isInstalled();

    String getRuntimeManagerName();
}
