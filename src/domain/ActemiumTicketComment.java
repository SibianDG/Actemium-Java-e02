package domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashSet;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

import domain.enums.RequiredElement;
import exceptions.InformationRequiredException;
import languages.LanguageResource;

/**
 * The type Actemium ticket comment.
 */
@Entity
@Access(AccessType.FIELD)
public class ActemiumTicketComment implements TicketComment, Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private long ticketCommentId;

	@ManyToOne
	private ActemiumTicket ticket;

	@ManyToOne
	private UserModel user;
//	private String username;
	private String userRole;

	private LocalDateTime dateTimeOfComment;

	@Lob
	@Column
	private String commentText;

	/**
	 * Instantiates a new Actemium ticket comment.
	 */
	public ActemiumTicketComment() {
		super();
	}

	/**
	 * Instantiates a new Actemium ticket comment via the builder pattern.
	 *
	 * @param builder the builder
	 */
	private ActemiumTicketComment(TicketCommentBuilder builder) {
		this.ticket = builder.ticket;
		this.user = builder.user;
		this.userRole = builder.userRole;
		this.dateTimeOfComment = builder.dateTimeOfComment;
		this.commentText = builder.commentText;
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
	 * Gets the user of the ticket comment.
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
	 * Gets the role of the user if the ticket comment.
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
	 * Gets the date time of the comment
	 *
	 * @return date time of comment
	 */
	public LocalDateTime getDateTimeOfComment() {
		return dateTimeOfComment;
	}

	/**
	 * Sets date time of comment.
	 *
	 * @param dateTimeOfComment the date time of comment
	 */
	public void setDateTimeOfComment(LocalDateTime dateTimeOfComment) {
		this.dateTimeOfComment = dateTimeOfComment;
	}

	/**
	 * Gets the commented text
	 *
	 * @return comment text
	 */
	public String getCommentText() {
		return commentText;
	}

	/**
	 * Sets comment text.
	 *
	 * @param commentText the comment text
	 */
	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	/**
	 * Check attributes.
	 *
	 * @throws InformationRequiredException the information required exception
	 */
	public void checkAttributes() throws InformationRequiredException {
		new TicketCommentBuilder().ticket(this.getTicket())
				.user(this.getUser())
				.userRole(this.getUserRole())
				.dateTimeOfComment(this.getDateTimeOfComment())
				.commentText(this.getCommentText())
				.build();
	}

	/**
	 * The type Ticket comment builder.
	 */
	public static class TicketCommentBuilder {
		private ActemiumTicket ticket;
		private UserModel user;
		private String userRole;
		private LocalDateTime dateTimeOfComment;
		private String commentText;

		private Set<RequiredElement> requiredElements;

		/**
		 * Ticket ticket comment builder.
		 *
		 * @param ticket the ticket
		 * @return the ticket comment builder
		 */
		public TicketCommentBuilder ticket(ActemiumTicket ticket) {
			this.ticket = ticket;
			return this;
		}

		/**
		 * User ticket comment builder.
		 *
		 * @param user the user
		 * @return the ticket comment builder
		 */
		public TicketCommentBuilder user(UserModel user) {
			this.user = user;
			return this;
		}

		/**
		 * User role ticket comment builder.
		 *
		 * @param userRole the user role
		 * @return the ticket comment builder
		 */
		public TicketCommentBuilder userRole(String userRole) {
			this.userRole = userRole;
			return this;
		}

		/**
		 * Date time of comment ticket comment builder.
		 *
		 * @param dateTimeOfComment the date time of comment
		 * @return the ticket comment builder
		 */
		public TicketCommentBuilder dateTimeOfComment(LocalDateTime dateTimeOfComment) {
			this.dateTimeOfComment = dateTimeOfComment;
			return this;
		}

		/**
		 * Comment text ticket comment builder.
		 *
		 * @param commentText the comment text
		 * @return the ticket comment builder
		 */
		public TicketCommentBuilder commentText(String commentText) {
			this.commentText = commentText;
			return this;
		}

		/**
		 * Build actemium ticket comment.
		 *
		 * @return the actemium ticket comment
		 * @throws InformationRequiredException the information required exception
		 */
		public ActemiumTicketComment build() throws InformationRequiredException {
			requiredElements = new HashSet<>();
			checkAttributesKbItemBuiler();
			return new ActemiumTicketComment(this);
		}

		private void checkAttributesKbItemBuiler() throws InformationRequiredException {
			// we dont really need to check anything here
			if (user == null)
				requiredElements.add(RequiredElement.UserRequired);
			if(dateTimeOfComment == null)
				requiredElements.add(RequiredElement.DateTimeOfCommentRequired);
			//TODO
			if(commentText == null || commentText.isBlank())
				requiredElements.add(RequiredElement.CommentTextRequired);

			if (!requiredElements.isEmpty())
				throw new InformationRequiredException(requiredElements);
		}
	}

	/**
	 * This clones an actemium ticket comment.
	 *
	 * @return actmium ticket comment
	 * @throws CloneNotSupportedException throws a clone not supported exception
	 */
	@Override
	public ActemiumTicketComment clone() throws CloneNotSupportedException {

		ActemiumTicketComment cloned = null;
		try {
			cloned = new ActemiumTicketComment.TicketCommentBuilder().ticket(this.getTicket())
					.user(this.getUser())
					.userRole(this.getUserRole())
					.dateTimeOfComment(this.getDateTimeOfComment())
					.commentText(this.getCommentText())
					.build();
		} catch (InformationRequiredException e) {
			// this should be a good Employee
			e.printStackTrace();
		}
		return cloned;
	}

	@Override
	public String toString() {
		StringBuilder ticketComment = new StringBuilder();
		ticketComment.append(String.format("%s: %s %s%n", getUserRole(), user.getFirstName(), user.getLastName()));
		ticketComment.append(String.format("%s: %s%n", LanguageResource.getString("date"), getDateTimeOfComment().format(DateTimeFormatter.ISO_DATE)));
		ticketComment.append(String.format("%s: %s%n", LanguageResource.getString("time"), getDateTimeOfComment().format(DateTimeFormatter.ISO_TIME)));
		ticketComment.append(String.format("%s: %s%n", LanguageResource.getString("text"), getCommentText()));
		return ticketComment.toString();
	}

}
