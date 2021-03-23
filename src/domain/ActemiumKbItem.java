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
	// using the same type as ticketType
	@Transient
	private StringProperty type = new SimpleStringProperty();
	
	private String keywords;
	@Lob
	@Column
	private String text;
	
	public ActemiumKbItem() {
		super();
	}
	
	private ActemiumKbItem(KbItemBuilder builder) throws InformationRequiredException {
		this.title.set(builder.title);
		this.type.set(String.valueOf(builder.type));
		this.keywords = builder.keywords;
		this.text = builder.text;
	}
		
	@Access(AccessType.PROPERTY)
	public String getTitle() {
		return title.get();
	}

	public void setTitle(String title) {		
		this.title.set(title);
	}

	public String getTypeAsString() {
		return type.get();
	}

	@Access(AccessType.PROPERTY)
	@Enumerated(EnumType.STRING)
	public KbItemType getType() {
		return  KbItemType.valueOf(type.get());
	}

	public void setType(KbItemType type) {
		this.type.set(String.valueOf(type));
	}
	
	public String getKeywords() {
		return keywords;
	}
	
	public void setKeywords(String keywords) {
		this.keywords = keywords;
	}
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}
	
	public StringProperty titleProperty() {
		return title;
	}
	
	public StringProperty typeProperty() {
		return type;
	}
	
	public void checkAttributes() throws InformationRequiredException {
		new KbItemBuilder()
			.title(this.getTitle())
			.type(this.getType())
			.keywords(this.getKeywords())
			.text(this.getText())
			.build();
	}
	
	public static class KbItemBuilder {
		private String title;
		private KbItemType type;
		private String keywords;
		private String text;

		private Set<RequiredElement> requiredElements;

		public KbItemBuilder title(String title) {
			this.title = title;
			return this;
		}
		public KbItemBuilder type(KbItemType type) {
			this.type = type;
			return this;
		}
		public KbItemBuilder keywords(String keywords) {
			this.keywords = keywords;
			return this;
		}
		public KbItemBuilder text(String text) {
			this.text = text;
			return this;
		}

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
			//this should be a good Employee
			e.printStackTrace();
		}
		return cloned;
	}

}
