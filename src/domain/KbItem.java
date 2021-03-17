package domain;

import domain.enums.KbItemType;
import javafx.beans.property.StringProperty;

public interface KbItem {

	public String getTitle();

	public String getTicketType();

	public KbItemType getTypeAsEnum();

	public String getText();

	public StringProperty titleProperty();

	public StringProperty typeProperty();

}
