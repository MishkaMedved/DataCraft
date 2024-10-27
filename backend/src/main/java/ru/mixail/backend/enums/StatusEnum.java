package ru.mixail.backend.enums;

public enum StatusEnum {
    PUBLISHED("Опубликовано"),
    DRAFT("Черновик");

    private final String name;

    StatusEnum(String name) {
        this.name = name;
    }

    public static StatusEnum fromString(String value) {
        for (StatusEnum status : StatusEnum.values()) {
            if (status.name.equalsIgnoreCase(value)) {
                return status;
            }
        }
        throw new IllegalArgumentException("No enum constant for value: " + value);
    }
}
