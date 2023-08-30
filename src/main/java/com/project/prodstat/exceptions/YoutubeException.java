package com.project.prodstat.exceptions;

public class YoutubeException extends Exception{
    /**
     * Constructor for YoutubeException.
     *
     * @param err - Error Message.
     */
    public YoutubeException(String err) {
        super(err);
    }
}
