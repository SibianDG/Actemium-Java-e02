package domain;

import java.io.Serial;
import java.io.Serializable;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Access;
import javax.persistence.AccessType;
import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.EnumType;
import javax.persistence.Enumerated;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Lob;
import javax.persistence.ManyToMany;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.Transient;

import domain.enums.RequiredElement;
import domain.enums.TicketPriority;
import domain.enums.TicketStatus;
import domain.enums.TicketType;
import exceptions.InformationRequiredException;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.beans.property.StringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import languages.LanguageResource;


/**
 * The type Actemium ticket.
 */
@Entity
@Access(AccessType.FIELD)
public class ActemiumTicket implements Ticket, Serializable {
	@Serial
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private int ticketId;
	
	@Transient
	private IntegerProperty ticketIdInt = new SimpleIntegerProperty();

	@Transient
	private StringProperty status = new SimpleStringProperty();
	@Transient
	private StringProperty priority = new SimpleStringProperty();
	@Transient
	private StringProperty completionDate = new SimpleStringProperty();

	@Column(columnDefinition = "DATETIME")
	private LocalDateTime dateAndTimeOfCreation;
	@Column(columnDefinition = "DATETIME")
	private LocalDateTime dateAndTimeOfCompletion;

	@Transient
	private StringProperty title = new SimpleStringProperty();
	@Lob
	@Column
	private String description;
//	@ManyToOne
//	private ActemiumCustomer customer;

	@ManyToOne
	private ActemiumCompany company;
	@ManyToMany(cascade = CascadeType.PERSIST)
	private List<ActemiumTicketComment> comments = new ArrayList<>();
	private String attachments;	
	
	// List of technicians contain all the technicians assigned to the ticket
	@ManyToMany(cascade = CascadeType.PERSIST)
	private List<ActemiumEmployee> technicians = new ArrayList<>();

	@Transient
	private StringProperty ticketType = new SimpleStringProperty();
	
	// Following list of additional data would prove usefull 
	// for further follow up on tickets and reporting:
	// - How was the solution acquired: through the KB or not. 
	//   This allows us to check if the KB must be 
	//   modified and expanded or not
	private String solution;
	// - Quality: the information was sufficient or not. 
	//   This would allow us to decided wether or not additional
	//   (mandatorry) fields are required when creating a ticket
	private String quality;
	// - Support needed: was the ticket created correctly? 
	//   E.g. a ticket was created but the wrong  
	//   department was assigned
	private String supportNeeded;
	
	// List with changes to show history of the ticket
	@OneToMany(mappedBy = "ticket", cascade = CascadeType.PERSIST)
	private List<ActemiumTicketChange> ticketChanges = new ArrayList<>();

	/**
	 * Instantiates a new Actemium ticket.
	 */
	public ActemiumTicket() {
		super();
	}



	/**
	 * Instantiates a new Actemium ticket via the builder pattern.
	 *
	 * @param builder the builder
	 */
	private ActemiumTicket(TicketBuilder builder) {
		this.status.set(TicketStatus.CREATED.toString());
		this.priority.set(String.valueOf(builder.ticketPriority));
		this.ticketType.set(String.valueOf(builder.ticketType));
		this.dateAndTimeOfCreation = builder.dateAndTimeOfCreation;
		this.dateAndTimeOfCompletion = builder.dateAndTimeOfCompletion;
		this.title.set(builder.title);
		this.description = builder.description;
		this.company = builder.company;
//		this.customer = builder.customer;
		this.comments = builder.comments;
		this.solution = builder.solution;
		this.quality = builder.quality;
		this.supportNeeded = builder.supportNeeded;
		this.technicians = builder.technicians;
		this.attachments = builder.attachments;
	}

	/**
	 *	Gets the ticket ID as string.
	 *
	 * @return ticketID as string
	 */
	public String getTicketIdString() {
		return String.valueOf(ticketId);
	}

	/**
	 * Gets ticket id int.
	 *
	 * @return the ticket id int
	 */
	public int getTicketIdInt() {
		return (int) ticketId;
	}

	/**
	 * Sets ticket id int.
	 */
	public void setTicketIdInt() {
		this.ticketIdInt.set((int) ticketId);
	}

	/**
	 * Gets the status as string.
	 *
	 * @return status as string.
	 */
	public String getStatusAsString() {
		return status.get();
	}

	/**
	 * Gets the ticket status as enum.
	 *
	 * @return Ticket Status as Enum
	 */
	@Access(AccessType.PROPERTY)
	@Enumerated(EnumType.STRING)
	public TicketStatus getStatus() {
		return TicketStatus.valueOf(status.get());
	}

	/**
	 * Sets status. If the status is completed or cancelled, the date of compeltion is set to now.
	 *
	 * @param ticketStatus the ticket status
	 * @throws InformationRequiredException the information required exception
	 */
	public void setStatus(TicketStatus ticketStatus) throws InformationRequiredException {
		if (ticketStatus == TicketStatus.COMPLETED
				|| ticketStatus == TicketStatus.CANCELLED) {
			setDateAndTimeOfCompletion(LocalDateTime.now());
		}
		this.status.set(ticketStatus.toString());
		//checkAttributes();
	}

	/**
	 *	Gets the priority as String.
	 *
	 * @return priority as string
	 */
	public String getPriorityAsString() {
		return priority.get();
	}

	/**
	 * Gets the ticket Priority as enum.
	 *
	 * @return Ticket Priority
	 */
	@Access(AccessType.PROPERTY)
	@Enumerated(EnumType.STRING)
	public TicketPriority getPriority() {
		return TicketPriority.valueOf(priority.get());
	}

	/**
	 * Sets priority.
	 *
	 * @param priority the priority
	 * @throws InformationRequiredException the information required exception
	 */
	public void setPriority(TicketPriority priority) throws InformationRequiredException {
		this.priority.set(String.valueOf(priority));
	}

	/**
	 * Gets the ticket type as sting.
	 *
	 * @return ticket type as Sting
	 */
	public String getTicketTypeAsString() {
		return ticketType.get();
	}

	/**
	 * Gets the ticket type as enum
	 *
	 * @return ticket type as enum
	 */
	@Access(AccessType.PROPERTY)
	@Enumerated(EnumType.STRING)
	public TicketType getTicketType() {
		return TicketType.valueOf(ticketType.get());
	}

	/**
	 * Sets ticket type.
	 *
	 * @param ticketType the ticket type
	 * @throws InformationRequiredException the information required exception
	 */
	public void setTicketType(TicketType ticketType) throws InformationRequiredException {
		this.ticketType.set(String.valueOf(ticketType));
	}

	/**
	 * Gets the dat of creation of the ticket.
	 *
	 * @return date of creation
	 */
	public LocalDate getDateOfCreation() {
		return getDateAndTimeOfCreation().toLocalDate();
	}

	/**
	 *	Gets the date and time of creation.
	 *
	 * @return date and time of creation
	 */
	public LocalDateTime getDateAndTimeOfCreation() {
		return dateAndTimeOfCreation;
	}

	/**
	 * Sets date and time of creation.
	 *
	 * @param dateAndTimeOfCreation the date and time of creation
	 */
	public void setDateAndTimeOfCreation(LocalDateTime dateAndTimeOfCreation) {
		this.dateAndTimeOfCreation = dateAndTimeOfCreation;
	}

	/**
	 * Gets the date and time of completion.
	 *
	 * @return date and time of completion
	 */
	public LocalDateTime getDateAndTimeOfCompletion() {
		return dateAndTimeOfCompletion;
	}

	/**
	 * Sets date and time of completion.
	 *
	 * @param dateAndTimeOfCompletion the date and time of completion
	 */
	public void setDateAndTimeOfCompletion(LocalDateTime dateAndTimeOfCompletion) {
		this.dateAndTimeOfCompletion = dateAndTimeOfCompletion;
	}

	/**
	 *	Gets the title as String
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
	 * @throws InformationRequiredException the information required exception
	 */
	public void setTitle(String title) throws InformationRequiredException {
		this.title.set(title);
	}

	/**
	 * Gets the description as string.
	 *
	 * @return description string.
	 */
	public String getDescription() {
		return description;
	}

	/**
	 * Sets description.
	 *
	 * @param description the description
	 * @throws InformationRequiredException the information required exception
	 */
	public void setDescription(String description) throws InformationRequiredException {
		this.description = description;
	}

	/**
	 * Gets customer.
	 *
	 * @return the customer
	 */
	public ActemiumCompany getCompany() {
		return company;
	}

	/**
	 * Gives the customer for the ticker.
	 *
	 * @return customer
	 */
	@Override
	public Company giveCompany() {
		return (Company) company;
	}

	/**
	 * Sets customer.
	 *
	 * @param customer the customer
	 * @throws InformationRequiredException the information required exception
	 */
	public void setCompany(ActemiumCompany company) throws InformationRequiredException {
		this.company = company;
		checkAttributes();
	}
//	/**
//	 * Gets customer.
//	 *
//	 * @return the customer
//	 */
//	public ActemiumCustomer getCustomer() {
//		return customer;
//	}
//	
//	/**
//	 * Gives the customer for the ticker.
//	 *
//	 * @return customer
//	 */
//	@Override
//	public Customer giveCustomer() {
//		return (Customer) customer;
//	}
//	
//	/**
//	 * Sets customer.
//	 *
//	 * @param customer the customer
//	 * @throws InformationRequiredException the information required exception
//	 */
//	public void setCustomer(ActemiumCustomer customer) throws InformationRequiredException {
//		this.customer = customer;
//		checkAttributes();
//	}

	/**
	 * Gives a list of Ticket comments.
	 *
	 * @return ticket comment list
	 */
	public List<TicketComment> giveComments() {
		return (List<TicketComment>)(Object) comments;
	}

	/**
	 * Gets comments.
	 *
	 * @return the comments
	 */
	public List<ActemiumTicketComment> getComments() {
		return comments;
	}

	/**
	 * Sets comments.
	 *
	 * @param comments the comments
	 */
	public void setComments(List<ActemiumTicketComment> comments) {
		// Optional, can be null or blank
		this.comments = comments;
	}

	/**
	 * Add ticket comment.
	 *
	 * @param comment the comment
	 */
	public void addTicketComment(ActemiumTicketComment comment) {
		comments.add(comment);
	}

	/**
	 * Gets the attachments string.
	 *
	 * @return attachment string
	 */
	public String getAttachments() {
		return attachments;
	}

	/**
	 * Sets attachments.
	 *
	 * @param attachments the attachments
	 */
	public void setAttachments(String attachments) {
		// Optional, can be null or blank
		this.attachments = attachments;
	}

	/**
	 * Gets technicians.
	 *
	 * @return the technicians
	 */
	public List<ActemiumEmployee> getTechnicians() {
		return technicians;
	}

	/**
	 *	Gets the employees in an observable list.
	 *
	 * @return ObservableList of employees
	 */
	@Override
	public ObservableList<Employee> giveTechnicians() {
		return FXCollections.observableArrayList((List<Employee>) (Object) technicians);
	}

	/**
	 * Sets technicians.
	 *
	 * @param technicians the technicians
	 */
	public void setTechnicians(List<ActemiumEmployee> technicians) {
		this.technicians = technicians;
	}

	/**
	 * Add technician.
	 *
	 * @param technician the technician
	 */
	public void addTechnician(ActemiumEmployee technician) {
		technicians.add(technician);
	}

	/**
	 *	Gets the solution string.
	 *
	 * @return solution as string
	 */
	public String getSolution() {
		return solution;
	}

	/**
	 * Sets solution.
	 *
	 * @param solution the solution
	 */
	public void setSolution(String solution) {
		this.solution = solution;
	}

	/**
	 * Gets the quality as string.
	 *
	 * @return quality string.
	 */
	public String getQuality() {
		return quality;
	}

	/**
	 * Sets quality.
	 *
	 * @param quality the quality
	 */
	public void setQuality(String quality) {
		this.quality = quality;
	}

	/**
	 * Gets the support needed as string.
	 *
	 * @return support needed as string.
	 */
	public String getSupportNeeded() {
		return supportNeeded;
	}

	/**
	 * Sets support needed.
	 *
	 * @param supportNeeded the support needed
	 */
	public void setSupportNeeded(String supportNeeded) {
		this.supportNeeded = supportNeeded;
	}

	/**
	 *	Gives the ticket changes as list.
	 *
	 * @return List of ticket change
	 */
	public List<TicketChange> giveTicketChanges() {
		return (List<TicketChange>)(Object) ticketChanges;
	}

	/**
	 * Gets ticket changes.
	 *
	 * @return the ticket changes
	 */
	public List<ActemiumTicketChange> getTicketChanges() {
		return ticketChanges;
	}

	/**
	 * Sets ticket changes.
	 *
	 * @param ticketChanges the ticket changes
	 */
	public void setTicketChanges(List<ActemiumTicketChange> ticketChanges) {
		this.ticketChanges = ticketChanges;
	}

	/**
	 * Add ticket change.
	 *
	 * @param ticketChange the ticket change
	 */
	public void addTicketChange(ActemiumTicketChange ticketChange) {
		this.ticketChanges.add(ticketChange);
	}

	/**
	 *	Gets the property title.
	 *
	 * @return title property
	 */
	public StringProperty titleProperty() {
		return title;
	}

	/**
	 *	Gets the property priority.
	 *
	 * @return priority property
	 */
	public StringProperty priorityProperty() {
		return priority;
	}

	/**
	 * Gets the property status.
	 *
	 * @return status property
	 */
	public StringProperty statusProperty() {
		return status;
	}

	/**
	 *	Gets the property of ticket id
	 *
	 * @return ticket id property
	 */
	public IntegerProperty ticketIdProperty() {
		setTicketIdInt();
		return ticketIdInt;
	}

	/**
	 * Gets the property ticket type.
	 *
	 * @return ticket type property
	 */
	public StringProperty ticketTypeProperty() {
		return ticketType;
	}

	/**
	 * Gets the property completion date
	 *
	 * @return completion date property
	 */
	public StringProperty completionDateProperty() {
		this.completionDate.set(getDateAndTimeOfCompletion().format(DateTimeFormatter.ISO_DATE));
		return completionDate;
	}

	/**
	 * Check attributes.
	 *
	 * @throws InformationRequiredException the information required exception
	 */
	public void checkAttributes() throws InformationRequiredException {
		new TicketBuilder()
				.ticketPriority(getPriority())
				.ticketStatus(getStatus())
				.ticketType(getTicketType())
				.title(this.title.get())
				.description(this.description)
				.company(this.company)
//				.customer(this.customer)
				.build();

	}

	/**
	 * The type Ticket builder.
	 */
	public static class TicketBuilder {
		
		private TicketStatus ticketStatus;
		private TicketPriority ticketPriority;
		private TicketType ticketType;
		private LocalDateTime dateAndTimeOfCreation;
		private LocalDateTime dateAndTimeOfCompletion;
		private String title;
		private String description;
		private ActemiumCompany company;
//		private ActemiumCustomer customer;
		private List<ActemiumTicketComment> comments;
		private String attachments;
//		private List<String> attachments;
		private String solution;
		private String quality;
		private String supportNeeded;
		private List<ActemiumEmployee> technicians = new ArrayList<>();

		private Set<RequiredElement> requiredElements;

		/**
		 * Ticket priority ticket builder.
		 *
		 * @param priority the priority
		 * @return the ticket builder
		 */
		public TicketBuilder ticketPriority(TicketPriority priority){
			this.ticketPriority = priority;
			return this;
		}

		/**
		 * Ticket type ticket builder.
		 *
		 * @param ticketType the ticket type
		 * @return the ticket builder
		 */
		public TicketBuilder ticketType(TicketType ticketType){
			this.ticketType = ticketType;
			return this;
		}

		/**
		 * Title ticket builder.
		 *
		 * @param title the title
		 * @return the ticket builder
		 */
		public TicketBuilder title(String title){
			this.title = title;
			return this;
		}

		/**
		 * Description ticket builder.
		 *
		 * @param description the description
		 * @return the ticket builder
		 */
		public TicketBuilder description(String description){
			this.description = description;
			return this;
		}

		/**
		 * Customer ticket builder.
		 *
		 * @param customer the customer
		 * @return the ticket builder
		 */
		public TicketBuilder company(ActemiumCompany company){
			this.company = company;
			return this;
		}
//		/**
//		 * Customer ticket builder.
//		 *
//		 * @param customer the customer
//		 * @return the ticket builder
//		 */
//		public TicketBuilder customer(ActemiumCustomer customer){
//			this.customer = customer;
//			return this;
//		}

		/**
		 * Ticket status ticket builder.
		 *
		 * @param ticketStatus the ticket status
		 * @return the ticket builder
		 */
		public TicketBuilder ticketStatus(TicketStatus ticketStatus){
			this.ticketStatus = ticketStatus;
			return this;
		}

		/**
		 * Date and time of creation ticket builder.
		 *
		 * @param dateAndTimeOfCreation the date and time of creation
		 * @return the ticket builder
		 */
		public TicketBuilder dateAndTimeOfCreation(LocalDateTime dateAndTimeOfCreation){
			this.dateAndTimeOfCreation = dateAndTimeOfCreation;
			return this;
		}

		/**
		 * Date and time of completion ticket builder.
		 *
		 * @param dateAndTimeOfCompletion the date and time of completion
		 * @return the ticket builder
		 */
		public TicketBuilder dateAndTimeOfCompletion(LocalDateTime dateAndTimeOfCompletion){
			this.dateAndTimeOfCompletion = dateAndTimeOfCompletion;
			return this;
		}

		/**
		 * Comments ticket builder.
		 *
		 * @param comments the comments
		 * @return the ticket builder
		 */
		public TicketBuilder comments(List<ActemiumTicketComment> comments){
			this.comments = comments;
			return this;
		}

		/**
		 * Attachments ticket builder.
		 *
		 * @param attachments the attachments
		 * @return the ticket builder
		 */
		public TicketBuilder attachments(String attachments){
			this.attachments = attachments;
			return this;
		}

		/**
		 * Solution ticket builder.
		 *
		 * @param solution the solution
		 * @return the ticket builder
		 */
		public TicketBuilder solution(String solution){
			this.solution = solution;
			return this;
		}

		/**
		 * Quality ticket builder.
		 *
		 * @param quality the quality
		 * @return the ticket builder
		 */
		public TicketBuilder quality(String quality){
			this.quality = quality;
			return this;
		}

		/**
		 * Support needed ticket builder.
		 *
		 * @param supportNeeded the support needed
		 * @return the ticket builder
		 */
		public TicketBuilder supportNeeded(String supportNeeded){
			this.supportNeeded = supportNeeded;
			return this;
		}

		/**
		 * Technicians ticket builder.
		 *
		 * @param technicians the technicians
		 * @return the ticket builder
		 */
		public TicketBuilder technicians(List<ActemiumEmployee> technicians){
			this.technicians = technicians;
			return this;
		}

		/**
		 * Build actemium ticket.
		 *
		 * @return the actemium ticket
		 * @throws InformationRequiredException the information required exception
		 */
		public ActemiumTicket build() throws InformationRequiredException {
			requiredElements = new HashSet<>();

			//ActemiumTicket ticket = ;
			checkAttributesTicketBuilder();
			return new ActemiumTicket(this);
		}

		/**
		 * Check attributes ticket builder.
		 *
		 * @throws InformationRequiredException the information required exception
		 */
		public void checkAttributesTicketBuilder() throws InformationRequiredException {

			if (ticketPriority == null)
				requiredElements.add(RequiredElement.TicketPriorityRequired);
			if (ticketType == null)
				requiredElements.add(RequiredElement.TicketTypeRequired);
			if (title == null || title.isBlank())
				requiredElements.add(RequiredElement.TicketTitleRequired);
			if (description == null || description.isBlank())
				requiredElements.add(RequiredElement.TicketDescriptionRequired);
			if (company == null)
				requiredElements.add(RequiredElement.TicketCustomerIDRequired);
//			if (customer == null)
//				requiredElements.add(RequiredElement.TicketCustomerIDRequired);
			if (ticketStatus == null)
				this.ticketStatus = TicketStatus.CREATED;
			if (dateAndTimeOfCreation == null)
				this.dateAndTimeOfCreation = LocalDateTime.now();
			if (solution == null)
				this.solution = "";
			if (quality == null)
				this.quality = "";
			if (supportNeeded == null)
				this.supportNeeded = "";
			if (comments == null)
				this.comments = new ArrayList<ActemiumTicketComment>();
			if (attachments == null)
				this.attachments = String.format("(%s)", LanguageResource.getString("none"));
			if (!requiredElements.isEmpty()) {
				throw new InformationRequiredException(requiredElements);
			}
		}
	}

	/**
	 * This clones an actemium ticket.
	 *
	 * @return actmium ticket
	 * @throws CloneNotSupportedException throws a clone not supported exception
	 */
	@Override
	public ActemiumTicket clone() throws CloneNotSupportedException {

		ActemiumTicket cloned = null;
		try {
			cloned = new TicketBuilder()
					.ticketStatus(this.getStatus())
					.ticketPriority(this.getPriority())
					.ticketType(this.getTicketType())
					.title(this.getTitle())
					.description(this.getDescription())
					.company(this.getCompany())
//					.customer(this.getCustomer())
					.comments(this.getComments())
					.attachments(this.getAttachments())
					.solution(this.getSolution())
					.quality(this.getQuality())
					.supportNeeded(this.getSupportNeeded())
					.build();
		} catch (InformationRequiredException e) {
			e.printStackTrace();
		}
		return cloned;
	}

}
