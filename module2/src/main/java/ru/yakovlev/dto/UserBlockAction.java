package ru.yakovlev.dto;

public record UserBlockAction(Long userId, Long blockedUser, Boolean isBlocked) {

}
