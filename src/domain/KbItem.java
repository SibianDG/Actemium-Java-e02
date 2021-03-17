package domain;

import domain.enums.TicketType;
import javafx.beans.property.StringProperty;

public interface KbItem {

	public String getTitle();

	public String getTicketType();

	public TicketType getTypeAsEnum();

	public String getText();

	public StringProperty titleProperty();

	public StringProperty typeProperty();

}
