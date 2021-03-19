package domain;

import java.time.LocalDateTime;
import java.util.List;

public interface TicketChange {

	public UserModel getUser();

	public String getUserRole();

	public LocalDateTime getDateTimeOfChange();

	public String getChangeDescription();

	public List<String> getChangeContent();

}
