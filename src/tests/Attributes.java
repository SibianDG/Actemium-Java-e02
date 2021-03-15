package tests;

import domain.ActemiumCompany;
import domain.ActemiumCustomer;
import domain.ActemiumTicket;
import domain.enums.TicketPriority;
import domain.enums.TicketType;

public interface Attributes {


    int MAX_NUMBER = 5;

    ActemiumCompany google = new ActemiumCompany("Google", "United States", "Mountain View, CA 94043", "1600 Amphitheatre Parkway", "+1-650-253-0000");
    ActemiumCustomer customer = new ActemiumCustomer("customer123", "PassWd123&", "John", "Smith", google);
    ActemiumTicket actemiumTicket = new ActemiumTicket(TicketPriority.P1, TicketType.SOFTWARE,"Printer Broken", "Cannot print labels",customer);


}
