package wang.tengp.exceptions;

/**
 * An exception which corresponds to an HTTP 400 error.
 */
public final class BadRequestException extends PlatformException {
  private static final long serialVersionUID = 1L;

  public BadRequestException() {
    super();
  }
  
  public BadRequestException(String e) {
    super(e);
  }
  
  public BadRequestException(Exception e) {
    super(e);
  }
}