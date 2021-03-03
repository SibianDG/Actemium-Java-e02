package tests;

import java.time.LocalDate;
import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domain.Contract;
import domain.enums.ContractStatus;
import domain.ContractType;
import domain.enums.ContractTypeStatus;
import domain.enums.Timestamp;

public class ContractTest {
	
	private static LocalDate lastWeek = LocalDate.now().minusWeeks(1);
	private static LocalDate yesterday = LocalDate.now().minusDays(1);
	private static LocalDate today = LocalDate.now();
	private static LocalDate tomorrow = LocalDate.now().plusDays(1);
	private static LocalDate nextWeek = LocalDate.now().plusWeeks(1);
	
	private static LocalDate nextYear = LocalDate.now().plusYears(1);
	private static LocalDate inTwoYears = LocalDate.now().plusYears(2);
	private static LocalDate inThreeYears = LocalDate.now().plusYears(3);
	private static LocalDate inFourYears = LocalDate.now().plusYears(4);
	private static LocalDate inFiveYears = LocalDate.now().plusYears(5);
	
	private static LocalDate nextYearTomorrow = LocalDate.now().plusYears(1).plusDays(1);
	private static LocalDate inTwoYearsTomorrow = LocalDate.now().plusYears(2).plusDays(1);
	private static LocalDate inThreeYearsTomorrow = LocalDate.now().plusYears(3).plusDays(1);
	
	private static LocalDate nextYearNextWeek = LocalDate.now().plusYears(1).plusWeeks(1);
	private static LocalDate inTwoYearsNextWeek = LocalDate.now().plusYears(2).plusWeeks(1);
	private static LocalDate inThreeYearsNextWeek = LocalDate.now().plusYears(3).plusWeeks(1);
	
    private static ContractType contractType01 = new ContractType("BasisEmailSupport", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, 2, 999.99);
    private static ContractType contractType02 = new ContractType("FullAllSupport", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.ALWAYS, 3, 1, 3999.99);

    private static Stream<Arguments> validContractAttributes02() {
        return Stream.of(
                Arguments.of(contractType01, nextYear),
                Arguments.of(contractType02, nextYear),
                Arguments.of(contractType02, inTwoYears),
                Arguments.of(contractType02, inThreeYears)
        		);
    }
    
    private static Stream<Arguments> invalidContractAttributes02() {
        return Stream.of(
                // endDate: cannot be before the startDate (today)
        		Arguments.of(contractType01, yesterday),
        		Arguments.of(contractType02, yesterday), 
        		Arguments.of(contractType02, lastWeek), 
                // endDate: needs to be exactly 1, 2 or 3 years after startDate
        		Arguments.of(contractType01, today),
        		Arguments.of(contractType01, tomorrow),
        		Arguments.of(contractType02, nextWeek), 
        		Arguments.of(contractType02, nextYearTomorrow), 
        		Arguments.of(contractType02, nextYearNextWeek), 
        		Arguments.of(contractType02, inFourYears),     
        		Arguments.of(contractType02, inFiveYears)     
        		);
    }
    
    private static Stream<Arguments> validContractAttributes03() {
    	return Stream.of(
                Arguments.of(contractType01, today, nextYear),
                Arguments.of(contractType02, today, nextYear),
                Arguments.of(contractType02, today, inTwoYears),
                Arguments.of(contractType02, today, inThreeYears),
                Arguments.of(contractType01, tomorrow, nextYearTomorrow),
                Arguments.of(contractType01, tomorrow, inTwoYearsTomorrow),
                Arguments.of(contractType01, tomorrow, inThreeYearsTomorrow),
                Arguments.of(contractType02, nextWeek, nextYearNextWeek),
                Arguments.of(contractType02, nextWeek, inTwoYearsNextWeek),
                Arguments.of(contractType02, nextWeek, inThreeYearsNextWeek)
    			);
    }
    
    private static Stream<Arguments> invalidContractAttributes03() {
    	return Stream.of(
    			// startDate: must be today or in the future
    			Arguments.of(contractType01, yesterday, nextYear),
                Arguments.of(contractType02, lastWeek, nextYear),
                // endDate: cannot be before the startDate
                Arguments.of(contractType01, today, yesterday),
                Arguments.of(contractType02, today, lastWeek),
                Arguments.of(contractType01, tomorrow, today),
                // endDate: needs to be exactly 1, 2 or 3 years after startDate
                Arguments.of(contractType02, today, today),
                Arguments.of(contractType02, today, tomorrow),
                Arguments.of(contractType02, today, nextYearTomorrow),
                Arguments.of(contractType01, today, inTwoYearsTomorrow),
                Arguments.of(contractType01, today, inThreeYearsTomorrow),
        		Arguments.of(contractType02, today, inFourYears),     
        		Arguments.of(contractType02, today, inFiveYears),
                Arguments.of(contractType02, tomorrow, inTwoYearsNextWeek),
                Arguments.of(contractType02, tomorrow, inThreeYearsNextWeek)
    			);
    }

    // Tests for consturctor with 2 parameters
	@ParameterizedTest
	@MethodSource("validContractAttributes02")
	public void createContract_ValidAttributes02_DoesNotThrowException(ContractType contractType, LocalDate endDate) {
		Assertions.assertDoesNotThrow(() -> new Contract(contractType, endDate));
	}

    // Tests for consturctor with 2 parameters
	@ParameterizedTest
	@MethodSource("invalidContractAttributes02")
	public void createContract_InValidAttributes02_ThrowsIllegalArgumentException(ContractType contractType, LocalDate endDate) {
		Assertions.assertThrows(IllegalArgumentException.class, () -> new Contract(contractType, endDate));
	}

    // Tests for consturctor with 3 parameters
	@ParameterizedTest
	@MethodSource("validContractAttributes03")
	public void createContract_ValidAttributes03_DoesNotThrowException(ContractType contractType, LocalDate startDate, LocalDate endDate) {
		Assertions.assertDoesNotThrow(() -> new Contract(contractType, startDate, endDate));
	}

    // Tests for consturctor with 3 parameters
    @ParameterizedTest
    @MethodSource("invalidContractAttributes03")
    public void createContract_InValidAttributes03_ThrowsIllegalArgumentException(ContractType contractType, LocalDate startDate, LocalDate endDate) {
    	Assertions.assertThrows(IllegalArgumentException.class, () -> new Contract(contractType, startDate, endDate));
    }
    
    @Test
    public void contractCreation_WithoutStartDate_HasContractStatusCURRENT() {
    	Contract contract = new Contract(contractType01, nextYear);
        Assertions.assertEquals(ContractStatus.CURRENT, contract.getStatus());
    }
    
    @Test
    public void contractCreation_WithStartDateNow_HasContractStatusCURRENT() {
    	Contract contract = new Contract(contractType01, today, nextYear);
    	Assertions.assertEquals(ContractStatus.CURRENT, contract.getStatus());
    }
    
    @Test
    public void contractCreation_WithStartDateInFuture_HasContractStatusIN_REQUEST() {
    	Contract contract = new Contract(contractType01, nextWeek, nextYearNextWeek);
    	Assertions.assertEquals(ContractStatus.IN_REQUEST, contract.getStatus());
    }
    
}
