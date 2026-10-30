package devtrack.utils;

import java.util.UUID;

import devtrack.model.Enums.Priority;
import devtrack.model.Enums.Status;

public class MainUtils {

    ///Methods

	public static String generateId() {
        return UUID.randomUUID().toString();
    }

    public boolean isStatusValid(Status status) {
        boolean isStatusValid = false;
        for (Status s : Status.values()) {
            if (s == status) {
                isStatusValid = true;
                break;
            }
        }

        return isStatusValid;
    }

    public boolean isPriorityValid(Priority status) {
        boolean isPriorityValid = false;
        for (Priority s : Priority.values()) {
            if (s == status) {
                isPriorityValid = true;
                break;
            }
        }

        return isPriorityValid;
    }
}
