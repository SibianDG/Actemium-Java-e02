package domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

import domain.enums.RequiredElement;
import exceptions.InformationRequiredException;
import languages.LanguageResource;

@Entity
@Access(AccessType.FIELD)
public class ActemiumTicketChange implements TicketChange, Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long ticketChangeId;

	@ManyToOne
	private ActemiumTicket ticket;
	
	private UserModel user;
//	private String username;
	private String userRole;

	private LocalDateTime dateTimeOfChange;

	private String changeDescription;
//	@Lob
//	@Column
	private List<String> changeContent;

	public ActemiumTicketChange() {
		super();
	}

	private ActemiumTicketChange(TicketChangeBuilder builder) throws InformationRequiredException {
		this.ticket = builder.ticket;
		this.user = builder.user;
		this.userRole = builder.userRole;
		this.dateTimeOfChange = builder.dateTimeOfChange;
		this.changeDescription = builder.changeDescription;
		this.changeContent = builder.changeContent;
	}

	public ActemiumTicket getTicket() {
		return ticket;
	}

	public void setTicket(ActemiumTicket ticket) {
		this.ticket = ticket;
	}

	public UserModel getUser() {
		return user;
	}

	public void setUser(UserModel user) {
		this.user = user;
	}

	public String getUserRole() {
		return userRole;
	}

	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	public LocalDateTime getDateTimeOfChange() {
		return dateTimeOfChange;
	}

	public void setDateTimeOfChange(LocalDateTime dateTimeOfChange) {
		this.dateTimeOfChange = dateTimeOfChange;
	}

	public String getChangeDescription() {
		return changeDescription;
	}

	public void setChangeDescription(String changeDescription) {
		this.changeDescription = changeDescription;
	}

	public List<String> getChangeContent() {
		return changeContent;
	}

	public void setChangeContent(List<String> changeContent) {
		this.changeContent = changeContent;
	}

	public void checkAttributes() throws InformationRequiredException {
		new TicketChangeBuilder().ticket(this.getTicket())
				.user(this.getUser())
				.userRole(this.getUserRole())
				.dateTimeOfChange(this.getDateTimeOfChange())
				.changeDescription(this.getChangeDescription())
				.changeContent(this.getChangeContent())
				.build();
	}

	public static class TicketChangeBuilder {
		private ActemiumTicket ticket;
		private UserModel user;
		private String userRole;
		private LocalDateTime dateTimeOfChange;
		private String changeDescription;
		private List<String> changeContent;

		private Set<RequiredElement> requiredElements;

		public TicketChangeBuilder ticket(ActemiumTicket ticket) {
			this.ticket = ticket;
			return this;
		}

		public TicketChangeBuilder user(UserModel user) {
			this.user = user;
			return this;
		}

		public TicketChangeBuilder userRole(String userRole) {
			this.userRole = userRole;
			return this;
		}

		public TicketChangeBuilder dateTimeOfChange(LocalDateTime dateTimeOfChange) {
			this.dateTimeOfChange = dateTimeOfChange;
			return this;
		}

		public TicketChangeBuilder changeDescription(String changeDescription) {
			this.changeDescription = changeDescription;
			return this;
		}

		public TicketChangeBuilder changeContent(List<String> changeContent) {
			this.changeContent = changeContent;
			return this;
		}

		public ActemiumTicketChange build() throws InformationRequiredException {
			requiredElements = new HashSet<>();
			checkAttributesKbItemBuiler();
			return new ActemiumTicketChange(this);
		}

		private void checkAttributesKbItemBuiler() throws InformationRequiredException {
			// we dont really need to check anything here
			if (user == null)
				requiredElements.add(RequiredElement.UserRequired);
			if(dateTimeOfChange == null)
				requiredElements.add(RequiredElement.DateTimeofChangeRequired);
			if(changeDescription == null || changeDescription.isBlank())
				requiredElements.add(RequiredElement.ChangeDescriptionRequired);
			// changeContent is optional

			if (!requiredElements.isEmpty())
				throw new InformationRequiredException(requiredElements);
		}
	}

	@Override
	public ActemiumTicketChange clone() throws CloneNotSupportedException {

		ActemiumTicketChange cloned = null;
		try {
			cloned = new ActemiumTicketChange.TicketChangeBuilder().ticket(this.getTicket())
					.user(this.getUser())
					.userRole(this.getUserRole())
					.dateTimeOfChange(this.getDateTimeOfChange())
					.changeDescription(this.getChangeDescription())
					.changeContent(this.getChangeContent())
					.build();
		} catch (InformationRequiredException e) {
			// this should be a good Employee
			e.printStackTrace();
		}
		return cloned;
	}

	@Override
	public String toString() {
		StringBuilder ticketChange = new StringBuilder();
		ticketChange.append(String.format("%s: %s %s%n", getUserRole(), user.getFirstName(), user.getLastName()));
		ticketChange.append(String.format("%s: %s%n", LanguageResource.getString("date"), getDateTimeOfChange().format(DateTimeFormatter.ISO_DATE)));
		ticketChange.append(String.format("%s: %s%n", LanguageResource.getString("time"), getDateTimeOfChange().format(DateTimeFormatter.ISO_TIME)));
		ticketChange.append(String.format("%s: %s%n", LanguageResource.getString("description"), getChangeDescription()));
		ticketChange.append(String.format("%s: %n", LanguageResource.getString("change_content")));
		getChangeContent().forEach(c -> ticketChange.append(String.format("%s%n", c)));
		return ticketChange.toString();
	}

}
