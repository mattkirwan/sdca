package com.sdca.api.exception;

public class WorldNotFoundException extends RuntimeException {
    public WorldNotFoundException(Long worldId) {
        super(String.format("World ID:%s not found", worldId));
    }
}
