package domain;

import domain.enums.KbItemType;
import javafx.beans.property.StringProperty;
import javafx.collections.ObservableList;

public interface KbItem {

	public String getTitle();

	public String getTypeAsString();

	public KbItemType getType();
	
	public String getKeywords();

	public String getText();
	
	public ObservableList<Ticket> giveTicketsOfSameType();

	public StringProperty titleProperty();

	public StringProperty typeProperty();

}
