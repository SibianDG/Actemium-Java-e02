package tests;

import domain.ActemiumCompany;
import domain.ActemiumCustomer;
import domain.ActemiumTicket;
import domain.enums.TicketPriority;
import domain.enums.TicketType;

public interface Attributes {


    int MAX_NUMBER = 5;

    ActemiumCompany company = new ActemiumCompany("Actemium", "Mainway 99", "0470099874");
    ActemiumCustomer customer = new ActemiumCustomer("customer123", "PassWd123&", "John", "Smith", company);
    ActemiumTicket actemiumTicket = new ActemiumTicket(TicketPriority.P1, TicketType.SOFTWARE,"Printer Broken", "Cannot print labels",customer);


}
