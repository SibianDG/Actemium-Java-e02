package tests;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domain.ActemiumCompany;
import domain.ActemiumContract;
import domain.ActemiumContractType;
import domain.ActemiumCustomer;
import domain.enums.ContractStatus;
import domain.enums.ContractTypeStatus;
import domain.enums.Timestamp;

public class ContractTest {
	
	private static final LocalDate lastWeek = LocalDate.now().minusWeeks(1);
	private static final LocalDate yesterday = LocalDate.now().minusDays(1);
	private static final LocalDate today = LocalDate.now();
	private static final LocalDate tomorrow = LocalDate.now().plusDays(1);
	private static final LocalDate nextWeek = LocalDate.now().plusWeeks(1);
	
	private static final LocalDate nextYear = LocalDate.now().plusYears(1);
	private static final LocalDate inTwoYears = LocalDate.now().plusYears(2);
	private static final LocalDate inThreeYears = LocalDate.now().plusYears(3);
	private static final LocalDate inFourYears = LocalDate.now().plusYears(4);
	private static final LocalDate inFiveYears = LocalDate.now().plusYears(5);
	
	private static final LocalDate nextYearTomorrow = LocalDate.now().plusYears(1).plusDays(1);
	private static final LocalDate inTwoYearsTomorrow = LocalDate.now().plusYears(2).plusDays(1);
	private static final LocalDate inThreeYearsTomorrow = LocalDate.now().plusYears(3).plusDays(1);
	
	private static final LocalDate nextYearNextWeek = LocalDate.now().plusYears(1).plusWeeks(1);
	private static final LocalDate inTwoYearsNextWeek = LocalDate.now().plusYears(2).plusWeeks(1);
	private static final LocalDate inThreeYearsNextWeek = LocalDate.now().plusYears(3).plusWeeks(1);
	
    private static final ActemiumContractType contractType01 = new ActemiumContractType("BasisEmailSupport", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, 2, 999.99);
    private static final ActemiumContractType contractType02 = new ActemiumContractType("FullAllSupport", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.ALWAYS, 3, 1, 3999.99);

    private static final ActemiumCompany facebook = new ActemiumCompany("Facebook", "United States", "Menlo Park, CA 94025", "1 Hacker Way", "+1-650-308-7300");
    private static final ActemiumCustomer mark = new ActemiumCustomer("cust03Mark", "Passwd123&", "Mark", "Zuckerberg", facebook);
    
    private static Stream<Arguments> validContractAttributes02() {
        return Stream.of(
                Arguments.of(contractType01, mark, nextYear),
                Arguments.of(contractType02, mark, nextYear),
                Arguments.of(contractType02, mark, inTwoYears),
                Arguments.of(contractType02, mark, inThreeYears)
        		);
    }
    
    private static Stream<Arguments> invalidContractAttributes02() {
        return Stream.of(
                // endDate: cannot be before the startDate (today)
        		Arguments.of(contractType01, mark, yesterday),
        		Arguments.of(contractType02, mark, yesterday), 
        		Arguments.of(contractType02, mark, lastWeek), 
                // endDate: needs to be exactly 1, 2 or 3 years after startDate
        		Arguments.of(contractType01, mark, today),
        		Arguments.of(contractType01, mark, tomorrow),
        		Arguments.of(contractType02, mark, nextWeek), 
        		Arguments.of(contractType02, mark, nextYearTomorrow), 
        		Arguments.of(contractType02, mark, nextYearNextWeek), 
        		Arguments.of(contractType02, mark, inFourYears),     
        		Arguments.of(contractType02, mark, inFiveYears)     
        		);
    }
    
    private static Stream<Arguments> validContractAttributes03() {
    	return Stream.of(
                Arguments.of(contractType01, mark, today, nextYear),
                Arguments.of(contractType02, mark, today, nextYear),
                Arguments.of(contractType02, mark, today, inTwoYears),
                Arguments.of(contractType02, mark, today, inThreeYears),
                Arguments.of(contractType01, mark, tomorrow, nextYearTomorrow),
                Arguments.of(contractType01, mark, tomorrow, inTwoYearsTomorrow),
                Arguments.of(contractType01, mark, tomorrow, inThreeYearsTomorrow),
                Arguments.of(contractType02, mark, nextWeek, nextYearNextWeek),
                Arguments.of(contractType02, mark, nextWeek, inTwoYearsNextWeek),
                Arguments.of(contractType02, mark, nextWeek, inThreeYearsNextWeek)
    			);
    }
    
    private static Stream<Arguments> invalidContractAttributes03() {
    	return Stream.of(
    			// startDate: must be today or in the future
    			Arguments.of(contractType01, mark, yesterday, nextYear),
                Arguments.of(contractType02, mark, lastWeek, nextYear),
                // endDate: cannot be before the startDate
                Arguments.of(contractType01, mark, today, yesterday),
                Arguments.of(contractType02, mark, today, lastWeek),
                Arguments.of(contractType01, mark, tomorrow, today),
                // endDate: needs to be exactly 1, 2 or 3 years after startDate
                Arguments.of(contractType02, mark, today, today),
                Arguments.of(contractType02, mark, today, tomorrow),
                Arguments.of(contractType02, mark, today, nextYearTomorrow),
                Arguments.of(contractType01, mark, today, inTwoYearsTomorrow),
                Arguments.of(contractType01, mark, today, inThreeYearsTomorrow),
        		Arguments.of(contractType02, mark, today, inFourYears),     
        		Arguments.of(contractType02, mark, today, inFiveYears),
                Arguments.of(contractType02, mark, tomorrow, inTwoYearsNextWeek),
                Arguments.of(contractType02, mark, tomorrow, inThreeYearsNextWeek)
    			);
    }

    // Tests for consturctor with 2 parameters
	@ParameterizedTest
	@MethodSource("validContractAttributes02")
	public void createContract_ValidAttributes02_DoesNotThrowException(ActemiumContractType contractType, ActemiumCustomer customer, LocalDate endDate) {
		Assertions.assertDoesNotThrow(() -> new ActemiumContract(contractType, customer, endDate));
	}

    // Tests for consturctor with 2 parameters
	@ParameterizedTest
	@MethodSource("invalidContractAttributes02")
	public void createContract_InValidAttributes02_ThrowsIllegalArgumentException(ActemiumContractType contractType, ActemiumCustomer customer, LocalDate endDate) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new ActemiumContract(contractType, customer, endDate));
	}

    // Tests for consturctor with 3 parameters
	@ParameterizedTest
	@MethodSource("validContractAttributes03")
	public void createContract_ValidAttributes03_DoesNotThrowException(ActemiumContractType contractType, ActemiumCustomer customer, LocalDate startDate, LocalDate endDate) {
		Assertions.assertDoesNotThrow(() -> new ActemiumContract(contractType, customer, startDate, endDate));
	}

    // Tests for consturctor with 3 parameters
    @ParameterizedTest
    @MethodSource("invalidContractAttributes03")
    public void createContract_InValidAttributes03_ThrowsIllegalArgumentException(ActemiumContractType contractType, ActemiumCustomer customer, LocalDate startDate, LocalDate endDate) {
    	Assertions.assertThrows(IllegalArgumentException.class, () -> new ActemiumContract(contractType, customer, startDate, endDate));
    }
    
    @Test
    public void contractCreation_WithoutStartDate_HasContractStatusCURRENT() {
    	ActemiumContract contract = new ActemiumContract(contractType01, mark, nextYear);
        Assertions.assertEquals(ContractStatus.CURRENT, contract.getStatusAsEnum());
    }
    
    @Test
    public void contractCreation_WithStartDateNow_HasContractStatusCURRENT() {
    	ActemiumContract contract = new ActemiumContract(contractType01, mark, today, nextYear);
    	Assertions.assertEquals(ContractStatus.CURRENT, contract.getStatusAsEnum());
    }
    
    @Test
    public void contractCreation_WithStartDateInFuture_HasContractStatusIN_REQUEST() {
    	ActemiumContract contract = new ActemiumContract(contractType01, mark, nextWeek, nextYearNextWeek);
    	Assertions.assertEquals(ContractStatus.IN_REQUEST, contract.getStatusAsEnum());
    }
    
}
