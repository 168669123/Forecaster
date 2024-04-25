package wjh.projects.common.exception;

public class ResourceNotFoundException extends RuntimeException {

    public ResourceNotFoundException(String reason) {
        super(reason);
    }
}
