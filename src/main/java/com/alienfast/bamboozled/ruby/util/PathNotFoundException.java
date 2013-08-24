package com.alienfast.bamboozled.ruby.util;

/**
 * This exception is thrown if there is an issue locating a path.
 */
public class PathNotFoundException extends RuntimeException {

    private static final long serialVersionUID = 9109584043725307152L;

    public PathNotFoundException(String message) {

        super( message );
    }
}
