package pt.up.fe.android.mosaico.Exceptions;

public class NoPhotosFoundException extends Exception {

	private static final long serialVersionUID = -5548245773296495823L;

	private String message;
	public NoPhotosFoundException(String message)
	{
		this.message = message;
	}
	
	public NoPhotosFoundException()
	{
		this.message = "No Photos were found. Try again";
	}
	public String getMessage()
	{
		return message;
	}
}
