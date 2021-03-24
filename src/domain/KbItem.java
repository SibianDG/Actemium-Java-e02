package domain;

import domain.enums.KbItemType;
import javafx.beans.property.StringProperty;

/**
 * The interface Kb item.
 */
public interface KbItem {

	/**
	 * Gets title.
	 *
	 * @return the title
	 */
	String getTitle();

	/**
	 * Gets type as string.
	 *
	 * @return the type as string
	 */
	String getTypeAsString();

	/**
	 * Gets type.
	 *
	 * @return the type
	 */
	KbItemType getType();

	/**
	 * Gets keywords.
	 *
	 * @return the keywords
	 */
	String getKeywords();

	/**
	 * Gets text.
	 *
	 * @return the text
	 */
	String getText();

	/**
	 * Title property string property.
	 *
	 * @return the string property
	 */
	StringProperty titleProperty();

	/**
	 * Type property string property.
	 *
	 * @return the string property
	 */
	StringProperty typeProperty();

}
