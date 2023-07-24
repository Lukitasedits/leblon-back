package com.leblon.app.Exceptions;

public class MovieHasNotFoundException extends RuntimeException{
    public MovieHasNotFoundException(String message){
        super(message);
    }
}
