package tests;

import domain.ActemiumTicket;
import domain.Company;
import domain.Customer;
import domain.enums.TicketPriority;
import domain.enums.TicketType;

public interface Attributes {


    int MAX_NUMBER = 5;

    Company company = new Company("Actemium", "Mainway 99", "0470099874");
    Customer customer = new Customer("customer123", "PassWd123&", "John", "Smith", company);
    ActemiumTicket actemiumTicket = new ActemiumTicket(TicketPriority.P1, TicketType.SOFTWARE,"Printer Broken", "Cannot print labels",customer);


}
