package com.alienfast.bamboozled.ruby.capability;

import java.util.List;

import com.atlassian.bamboo.v2.build.agent.capability.AbstractExecutableCapabilityTypeModule;
import com.google.common.collect.Lists;

public class XvfbRunCapabilityTypeModule extends AbstractExecutableCapabilityTypeModule {

    public static final String XVFB_RUN_CAPABILITY_KEY = "system.executable.xvfbrun";
    public static final String XVFB_RUN_EXECUTABLE_KEY = "xvfbRunExecutable";
    public static final String XVFB_RUN_EXECUTABLE = "xvfb-run";

    private static final String CAPABILITY_TYPE_ERROR_UNDEFINED_EXECUTABLE = AGENT_CAPABILITY_TYPE_PREFIX + XVFB_RUN_CAPABILITY_KEY
            + ".undefinedExecutable";

    @Override
    public String getMandatoryCapabilityKey() {

        return XVFB_RUN_CAPABILITY_KEY;
    }

    @Override
    public String getExecutableKey() {

        return XVFB_RUN_EXECUTABLE_KEY;
    }

    @Override
    public String getCapabilityUndefinedKey() {

        return CAPABILITY_TYPE_ERROR_UNDEFINED_EXECUTABLE;
    }

    @Override
    public List<String> getDefaultWindowPaths() {

        return Lists.newArrayList();
    }

    @Override
    public String getExecutableFilename() {

        return XVFB_RUN_EXECUTABLE;
    }

    public String getExecutableDescription() {

        return getText( AGENT_CAPABILITY_TYPE_PREFIX + XVFB_RUN_CAPABILITY_KEY + ".description" );
    }

}
