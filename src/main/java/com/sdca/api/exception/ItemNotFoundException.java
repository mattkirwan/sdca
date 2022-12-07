package com.sdca.api.exception;

public class ItemNotFoundException extends RuntimeException {
    public ItemNotFoundException(Long itemId) {
        super(String.format("Item ID:%s not found", itemId));
    }
}
