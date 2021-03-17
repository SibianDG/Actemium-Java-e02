package tests;

import domain.ActemiumCompany;
import domain.ActemiumCustomer;
import domain.ActemiumTicket;
import domain.enums.TicketPriority;
import domain.enums.TicketType;
import exceptions.InformationRequiredException;

public interface Attributes {


    int MAX_NUMBER = 5;

    ActemiumCompany google = new ActemiumCompany("Google", "United States", "Mountain View, CA 94043", "1600 Amphitheatre Parkway", "+1-650-253-0000");

    ActemiumTicket getActemiumTicket() throws InformationRequiredException;


}
