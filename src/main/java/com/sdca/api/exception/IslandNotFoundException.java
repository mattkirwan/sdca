package com.sdca.api.exception;

public class IslandNotFoundException extends RuntimeException {
    public IslandNotFoundException(Long islandId) {
        super(String.format("Island ID:%s not found", islandId));
    }
}
