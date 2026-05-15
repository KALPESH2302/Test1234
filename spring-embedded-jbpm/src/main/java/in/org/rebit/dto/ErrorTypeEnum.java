package in.org.rebit.dto;

public enum ErrorTypeEnum {

    BUSINESS, TECHNICAL;

    public static ErrorTypeEnum fromString(String value) {
        for (ErrorTypeEnum status : ErrorTypeEnum.values()) {
            if (status.name().equalsIgnoreCase(value)) {
                return status;
            }
        }
        return ErrorTypeEnum.TECHNICAL;
    }
}

