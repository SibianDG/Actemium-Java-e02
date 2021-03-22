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
	
	private UserModel user;
//	private String username;
	private String userRole;

	private LocalDateTime dateTimeOfComment;

	@Lob
	@Column
	private String commentText;

	public ActemiumTicketComment() {
		super();
	}

	private ActemiumTicketComment(TicketCommentBuilder builder) throws InformationRequiredException {
		this.ticket = builder.ticket;
		this.user = builder.user;
		this.userRole = builder.userRole;
		this.dateTimeOfComment = builder.dateTimeOfComment;
		this.commentText = builder.commentText;
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

	public LocalDateTime getDateTimeOfComment() {
		return dateTimeOfComment;
	}

	public void setDateTimeOfComment(LocalDateTime dateTimeOfComment) {
		this.dateTimeOfComment = dateTimeOfComment;
	}

	public String getCommentText() {
		return commentText;
	}

	public void setCommentText(String commentText) {
		this.commentText = commentText;
	}

	public void checkAttributes() throws InformationRequiredException {
		new TicketCommentBuilder().ticket(this.getTicket())
				.user(this.getUser())
				.userRole(this.getUserRole())
				.dateTimeOfComment(this.getDateTimeOfComment())
				.commentText(this.getCommentText())
				.build();
	}

	public static class TicketCommentBuilder {
		private ActemiumTicket ticket;
		private UserModel user;
		private String userRole;
		private LocalDateTime dateTimeOfComment;
		private String commentText;

		private Set<RequiredElement> requiredElements;

		public TicketCommentBuilder ticket(ActemiumTicket ticket) {
			this.ticket = ticket;
			return this;
		}

		public TicketCommentBuilder user(UserModel user) {
			this.user = user;
			return this;
		}

		public TicketCommentBuilder userRole(String userRole) {
			this.userRole = userRole;
			return this;
		}

		public TicketCommentBuilder dateTimeOfComment(LocalDateTime dateTimeOfComment) {
			this.dateTimeOfComment = dateTimeOfComment;
			return this;
		}

		public TicketCommentBuilder commentText(String commentText) {
			this.commentText = commentText;
			return this;
		}

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
