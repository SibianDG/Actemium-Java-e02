package domain;

import java.io.Serial;
import java.io.Serializable;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.Transient;

import domain.enums.KbItemType;
import domain.enums.RequiredElement;
import exceptions.InformationRequiredException;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;

/**
 * The type Actemium kb item.
 */
@Entity
@Access(AccessType.FIELD)
public class ActemiumKbItem implements KbItem, Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long kbItemId;

	@Transient
	private StringProperty title = new SimpleStringProperty();
	@Transient
	private StringProperty type = new SimpleStringProperty();
	
	private String keywords;
	@Lob
	@Column
	private String text;

	/**
	 * Instantiates a new Actemium kb item.
	 */
	public ActemiumKbItem() {
		super();
	}

	/**
	 * Instantiates a new Actemium kb item via the builder pattern.
	 *
	 * @param builder the builder
	 */
	private ActemiumKbItem(KbItemBuilder builder) {
		this.title.set(builder.title);
		this.type.set(String.valueOf(builder.type));
		this.keywords = builder.keywords;
		this.text = builder.text;
	}

	/**
	 * Getst the title as String.
	 *
	 * @return title string
	 */
	@Access(AccessType.PROPERTY)
	public String getTitle() {
		return title.get();
	}

	/**
	 * Sets title.
	 *
	 * @param title the title
	 */
	public void setTitle(String title) {
		this.title.set(title);
	}

	/**
	 * Gets the type as string.
	 *
	 * @return type string
	 */
	public String getTypeAsString() {
		return type.get();
	}

	/**
	 * Gets the type of the kb item.
	 *
	 * @return Kb item type
	 */
	@Access(AccessType.PROPERTY)
	@Enumerated(EnumType.STRING)
	public KbItemType getType() {
		return KbItemType.valueOf(type.get());
	}

	/**
	 * Sets type.
	 *
	 * @param type the type
	 */
	public void setType(KbItemType type) {
		this.type.set(String.valueOf(type));
	}

	/**
	 * Gets the keywords
	 *
	 * @return keyword string
	 */
	public String getKeywords() {
		return keywords;
	}

	/**
	 * Sets keywords.
	 *
	 * @param keywords the keywords
	 */
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}

	/**
	 * Gets the text string.
	 *
	 * @return text string
	 */
	public String getText() {
		return text;
	}

	/**
	 * Sets text.
	 *
	 * @param text the text
	 */
	public void setText(String text) {
		this.text = text;
	}

	/**
	 * Gets the property title.
	 *
	 * @return title property
	 */
	public StringProperty titleProperty() {
		return title;
	}

	/**
	 *	Gets the property type.
	 *
	 * @return type property
	 */
	public StringProperty typeProperty() {
		return type;
	}

	/**
	 * Check attributes.
	 *
	 * @throws InformationRequiredException the information required exception
	 */
	public void checkAttributes() throws InformationRequiredException {
		new KbItemBuilder()
			.title(this.getTitle())
			.type(this.getType())
			.keywords(this.getKeywords())
			.text(this.getText())
			.build();
	}

	/**
	 * The type Kb item builder.
	 */
	public static class KbItemBuilder {
		private String title;
		private KbItemType type;
		private String keywords;
		private String text;

		private Set<RequiredElement> requiredElements;

		/**
		 * Title kb item builder.
		 *
		 * @param title the title
		 * @return the kb item builder
		 */
		public KbItemBuilder title(String title) {
			this.title = title;
			return this;
		}

		/**
		 * Type kb item builder.
		 *
		 * @param type the type
		 * @return the kb item builder
		 */
		public KbItemBuilder type(KbItemType type) {
			this.type = type;
			return this;
		}

		/**
		 * Keywords kb item builder.
		 *
		 * @param keywords the keywords
		 * @return the kb item builder
		 */
		public KbItemBuilder keywords(String keywords) {
			this.keywords = keywords;
			return this;
		}

		/**
		 * Text kb item builder.
		 *
		 * @param text the text
		 * @return the kb item builder
		 */
		public KbItemBuilder text(String text) {
			this.text = text;
			return this;
		}

		/**
		 * Build actemium kb item.
		 *
		 * @return the actemium kb item
		 * @throws InformationRequiredException the information required exception
		 */
		public ActemiumKbItem build() throws InformationRequiredException {
			requiredElements = new HashSet<>();
			checkAttributesKbItemBuiler();
			return new ActemiumKbItem(this);
		}

		private void checkAttributesKbItemBuiler() throws InformationRequiredException {
			if (title == null || title.isBlank())
				requiredElements.add(RequiredElement.KbItemTitleRequired);
			if(type == null)
				requiredElements.add(RequiredElement.KbItemTypeRequired);
			if(keywords == null || keywords.isBlank())
				requiredElements.add(RequiredElement.KbItemKeywordsRequired);
			if(text == null || text.isBlank())
				requiredElements.add(RequiredElement.KbItemTextRequired);

			if (!requiredElements.isEmpty())
				throw new InformationRequiredException(requiredElements);
		}
	}

	/**
	 * This clones an actemium kb item.
	 *
	 * @return actmium kb item
	 * @throws CloneNotSupportedException throws a clone not supported exception
	 */
	@Override
	public ActemiumKbItem clone() throws CloneNotSupportedException {

		ActemiumKbItem cloned = null;
		try {
			cloned = new ActemiumKbItem.KbItemBuilder()
					.title(this.getTitle())
					.type(this.getType())
					.keywords(this.getKeywords())
					.text(this.getText())
					.build();
		} catch (InformationRequiredException e) {
			e.printStackTrace();
		}
		return cloned;
	}

}
