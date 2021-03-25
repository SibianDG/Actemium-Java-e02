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

/**
 * The type Actemium ticket change.
 */
@Entity
@Access(AccessType.FIELD)
public class ActemiumTicketChange implements TicketChange, Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ticketChangeId;

	@ManyToOne
	private ActemiumTicket ticket;

	@ManyToOne
	private UserModel user;
	private String userRole;

	private LocalDateTime dateTimeOfChange;

	private String changeDescription;

	private List<String> changeContent;

	/**
	 * Instantiates a new Actemium ticket change.
	 */
	public ActemiumTicketChange() {
		super();
	}

	/**
	 * Instantiates a new Actemium ticket change via the builder pattern.
	 *
	 * @param builder the builder
	 */
	private ActemiumTicketChange(TicketChangeBuilder builder) {
		this.ticket = builder.ticket;
		this.user = builder.user;
		this.userRole = builder.userRole;
		this.dateTimeOfChange = builder.dateTimeOfChange;
		this.changeDescription = builder.changeDescription;
		this.changeContent = builder.changeContent;
	}

	/**
	 * Gets ticket.
	 *
	 * @return the ticket
	 */
	public ActemiumTicket getTicket() {
		return ticket;
	}

	/**
	 * Sets ticket.
	 *
	 * @param ticket the ticket
	 */
	public void setTicket(ActemiumTicket ticket) {
		this.ticket = ticket;
	}

	/**
	 * Gets the user of the ticket change.
	 *
	 * @return user
	 */
	public UserModel getUser() {
		return user;
	}

	/**
	 * Sets user.
	 *
	 * @param user the user
	 */
	public void setUser(UserModel user) {
		this.user = user;
	}

	/**
	 * Gets the role of the user of the ticket change.
	 *
	 * @return user role
	 */
	public String getUserRole() {
		return userRole;
	}

	/**
	 * Sets user role.
	 *
	 * @param userRole the user role
	 */
	public void setUserRole(String userRole) {
		this.userRole = userRole;
	}

	/**
	 * Gets the date time of the change.
	 *
	 * @return date time of change
	 */
	public LocalDateTime getDateTimeOfChange() {
		return dateTimeOfChange;
	}

	/**
	 * Sets date time of change.
	 *
	 * @param dateTimeOfChange the date time of change
	 */
	public void setDateTimeOfChange(LocalDateTime dateTimeOfChange) {
		this.dateTimeOfChange = dateTimeOfChange;
	}

	/**
	 *	Gets the change description string.
	 *
	 * @return change description string
	 */
	public String getChangeDescription() {
		return changeDescription;
	}

	/**
	 * Sets change description.
	 *
	 * @param changeDescription the change description
	 */
	public void setChangeDescription(String changeDescription) {
		this.changeDescription = changeDescription;
	}

	/**
	 *	Gets the changed content as string list.
	 *
	 * @return List of strings of the changed content
	 */
	public List<String> getChangeContent() {
		return changeContent;
	}

	/**
	 * Sets change content.
	 *
	 * @param changeContent the change content
	 */
	public void setChangeContent(List<String> changeContent) {
		this.changeContent = changeContent;
	}

	/**
	 * Check attributes.
	 *
	 * @throws InformationRequiredException the information required exception
	 */
	public void checkAttributes() throws InformationRequiredException {
		new TicketChangeBuilder().ticket(this.getTicket())
				.user(this.getUser())
				.userRole(this.getUserRole())
				.dateTimeOfChange(this.getDateTimeOfChange())
				.changeDescription(this.getChangeDescription())
				.changeContent(this.getChangeContent())
				.build();
	}

	/**
	 * The type Ticket change builder.
	 */
	public static class TicketChangeBuilder {
		private ActemiumTicket ticket;
		private UserModel user;
		private String userRole;
		private LocalDateTime dateTimeOfChange;
		private String changeDescription;
		private List<String> changeContent;

		private Set<RequiredElement> requiredElements;

		/**
		 * Ticket ticket change builder.
		 *
		 * @param ticket the ticket
		 * @return the ticket change builder
		 */
		public TicketChangeBuilder ticket(ActemiumTicket ticket) {
			this.ticket = ticket;
			return this;
		}

		/**
		 * User ticket change builder.
		 *
		 * @param user the user
		 * @return the ticket change builder
		 */
		public TicketChangeBuilder user(UserModel user) {
			this.user = user;
			return this;
		}

		/**
		 * User role ticket change builder.
		 *
		 * @param userRole the user role
		 * @return the ticket change builder
		 */
		public TicketChangeBuilder userRole(String userRole) {
			this.userRole = userRole;
			return this;
		}

		/**
		 * Date time of change ticket change builder.
		 *
		 * @param dateTimeOfChange the date time of change
		 * @return the ticket change builder
		 */
		public TicketChangeBuilder dateTimeOfChange(LocalDateTime dateTimeOfChange) {
			this.dateTimeOfChange = dateTimeOfChange;
			return this;
		}

		/**
		 * Change description ticket change builder.
		 *
		 * @param changeDescription the change description
		 * @return the ticket change builder
		 */
		public TicketChangeBuilder changeDescription(String changeDescription) {
			this.changeDescription = changeDescription;
			return this;
		}

		/**
		 * Change content ticket change builder.
		 *
		 * @param changeContent the change content
		 * @return the ticket change builder
		 */
		public TicketChangeBuilder changeContent(List<String> changeContent) {
			this.changeContent = changeContent;
			return this;
		}

		/**
		 * Build actemium ticket change.
		 *
		 * @return the actemium ticket change
		 * @throws InformationRequiredException the information required exception
		 */
		public ActemiumTicketChange build() throws InformationRequiredException {
			requiredElements = new HashSet<>();
			checkAttributesKbItemBuiler();
			return new ActemiumTicketChange(this);
		}

		private void checkAttributesKbItemBuiler() throws InformationRequiredException {
			if (ticket == null)
				requiredElements.add(RequiredElement.TicketTitleRequired);
			if (user == null)
				requiredElements.add(RequiredElement.UserRequired);
			if (userRole == null || userRole.isBlank())
				requiredElements.add(RequiredElement.EmployeeRoleRequired);
			if(dateTimeOfChange == null)
				requiredElements.add(RequiredElement.DateTimeofChangeRequired);
			if(changeDescription == null || changeDescription.isBlank())
				requiredElements.add(RequiredElement.ChangeDescriptionRequired);

			if (!requiredElements.isEmpty())
				throw new InformationRequiredException(requiredElements);
		}
	}

	/**
	 * This clones an actemium ticket change.
	 *
	 * @return actmium ticket change
	 * @throws CloneNotSupportedException throws a clone not supported exception
	 */
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
			e.printStackTrace();
		}
		return cloned;
	}

	@Override
	public String toString() {
		final DateTimeFormatter formatDateTime = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

		StringBuilder ticketChange = new StringBuilder();
		ticketChange.append(String.format("%s: %s %s%n", getUserRole(), user.getFirstName(), user.getLastName()));
		ticketChange.append(String.format("%s: %s%n", LanguageResource.getString("date"), getDateTimeOfChange().format(formatDateTime)));
		ticketChange.append(String.format("%s: %s%n", LanguageResource.getString("time"), getDateTimeOfChange().format(formatDateTime)));
		ticketChange.append(String.format("%s: %s%n", LanguageResource.getString("description"), getChangeDescription()));
		ticketChange.append(String.format("%s: %n", LanguageResource.getString("change_content")));
		getChangeContent().forEach(c -> ticketChange.append(String.format("%s%n", c)));
		return ticketChange.toString();
	}

}
