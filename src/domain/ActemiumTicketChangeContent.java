package domain;

import java.io.Serial;
import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.ManyToOne;

/**
 * The type Actemium ticket change.
 */
@Entity
//@Access(AccessType.FIELD)
public class ActemiumTicketChangeContent implements Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ticketChangeContentId;

	@ManyToOne
	private ActemiumTicketChange ticketChange;

	private String changeContent;

	/**
	 * Instantiates a new Actemium ticket change.
	 */
	public ActemiumTicketChangeContent() {
		super();
	}

	public ActemiumTicketChangeContent(ActemiumTicketChange ticketChange,
			String changeContent) {
		super();
		this.ticketChange = ticketChange;
		this.changeContent = changeContent;
	}

	/**
	 * Gets ticketChange.
	 *
	 * @return the ticketChange
	 */
	public ActemiumTicketChange getTicketChange() {
		return ticketChange;
	}

	/**
	 * Sets ticketChange.
	 *
	 * @param ticketChange the ticketChange
	 */
	public void setTicket(ActemiumTicketChange ticketChange) {
		this.ticketChange = ticketChange;
	}

	/**
	 * @return the changeContent
	 */
	public String getChangeContent() {
		return changeContent;
	}

	/**
	 * @param changeContent the changeContent to set
	 */
	public void setChangeContent(String changeContent) {
		this.changeContent = changeContent;
	}

}
