public class ExitException extends Exception {
    public ExitException(String msg) {
        super(msg);
    }

    public ExitException() {
        super();
    }
}