package hanium.videoMeeting.advice.exception;

public class DuplicateNameException extends RuntimeException {
    public DuplicateNameException() {
        super();
    }

    public DuplicateNameException(String message) {
        super(message);
    }
}
