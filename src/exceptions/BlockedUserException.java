package exceptions;

public class BlockedUserException extends RuntimeException
{
    public BlockedUserException()
    {
    }
    
    public BlockedUserException(String message)
    {
        super(message);
    }

    public BlockedUserException(String message, Throwable cause)
    {
        super(message, cause);
    }

    public BlockedUserException(Throwable cause)
    {
        super(cause);
    }

    public BlockedUserException(String message, Throwable cause, boolean enableSuppression, boolean writableStackTrace)
    {
        super(message, cause, enableSuppression, writableStackTrace);
    }
}
