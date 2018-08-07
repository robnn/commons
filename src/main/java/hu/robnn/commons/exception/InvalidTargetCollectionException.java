package hu.robnn.commons.exception;

public class InvalidTargetCollectionException extends RuntimeException{
    public InvalidTargetCollectionException() {
        super("Invalid target collection, please check if the target collection is null!");
    }
}
