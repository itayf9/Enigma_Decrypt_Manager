public enum Operation {
    READ_SYSTEM_DETAILS_FROM_XML, DISPLAY_SPECS, CHOOSE_INIT_CONFIG_MANUAL,
    CHOOSE_INIT_CONFIG_AUTO, PROCESS_INPUT, RESET_CURRENT_CODE,
    HISTORY_AND_STATISTICS, EXIT_SYSTEM,
    READ_EXISTING_MACHINE_FROM_FILE, WRITE_EXISTING_MACHINE_TO_FILE, NONE;

    public static final int INVALID_OPERATION = 0;

    public static Operation getOperation(int choice) {
        Operation selectedOperation;

        switch (choice) {
            case 1:
                selectedOperation = READ_SYSTEM_DETAILS_FROM_XML;
                break;
            case 2:
                selectedOperation = DISPLAY_SPECS;
                break;
            case 3:
                selectedOperation = CHOOSE_INIT_CONFIG_MANUAL;
                break;
            case 4:
                selectedOperation = CHOOSE_INIT_CONFIG_AUTO;
                break;
            case 5:
                selectedOperation = PROCESS_INPUT;
                break;
            case 6:
                selectedOperation = RESET_CURRENT_CODE;
                break;
            case 7:
                selectedOperation = HISTORY_AND_STATISTICS;
                break;
            case 8:
                selectedOperation = EXIT_SYSTEM;
                break;
            case 9:
                selectedOperation = READ_EXISTING_MACHINE_FROM_FILE;
                break;
            case 10:
                selectedOperation = WRITE_EXISTING_MACHINE_TO_FILE;
                break;
            default:
                selectedOperation = NONE;
                break;
        }
        return selectedOperation;
    }
}
