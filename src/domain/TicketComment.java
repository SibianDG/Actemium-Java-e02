package domain;

import java.time.LocalDateTime;

public interface TicketComment {
	
	public UserModel getUser();

	public String getUserRole();

	public LocalDateTime getDateTimeOfComment();

	public String getCommentText(); 

}
