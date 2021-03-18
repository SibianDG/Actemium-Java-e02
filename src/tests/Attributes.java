package tests;

import domain.ActemiumCompany;
import domain.ActemiumCustomer;
import domain.ActemiumTicket;
import domain.enums.TicketPriority;
import domain.enums.TicketType;
import exceptions.InformationRequiredException;

public interface Attributes {

    int MAX_NUMBER = 5;

    ActemiumTicket getActemiumTicket();


}
