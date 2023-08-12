package com.project.prodstat.exceptions;

public class SpotifyException extends Exception{
    //private static final long serialVersionUID = 42L;

    /**
     * Constructor for SpotifyException.
     *
     * @param err - Error Message.
     */
    public SpotifyException(String err) {
        super(err);
    }
}