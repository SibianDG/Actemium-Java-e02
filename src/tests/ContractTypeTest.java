package tests;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domain.ActemiumContractType;
import domain.enums.ContractTypeStatus;
import domain.enums.Timestamp;
import exceptions.InformationRequiredException;

public class ContractTypeTest {
	
    private static Stream<Arguments> validContractTypeAttributes() {
        return Stream.of(
				Arguments.of("BasisEmailSupport", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, 2, 999.99),
				Arguments.of("BasisPhoneSupport", ContractTypeStatus.ACTIVE, false, true, false, Timestamp.WORKINGHOURS, 5, 2, 1999.99),
				Arguments.of("BasisAppSupport", ContractTypeStatus.ACTIVE, false, false, true, Timestamp.WORKINGHOURS, 5, 2, 1299.99),
				Arguments.of("FullEmailSupport", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.ALWAYS, 4, 1, 1999.99),
				Arguments.of("FullPhoneSupport", ContractTypeStatus.ACTIVE, false, true, false, Timestamp.ALWAYS, 4, 1, 2999.99),
				Arguments.of("FullAppSupport", ContractTypeStatus.ACTIVE, false, false, true, Timestamp.ALWAYS, 4, 1, 2299.99),
				Arguments.of("BasisAllSupport", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.WORKINGHOURS, 4, 2, 2999.99),
				Arguments.of("FullAllSupport", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.ALWAYS, 3, 1, 3999.99),
				Arguments.of("ExperimentalSupport", ContractTypeStatus.INACTIVE, true, false, true, Timestamp.ALWAYS, 4, 1, 2999.99),

				Arguments.of("CT1", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.ALWAYS, 4, 2, 250.50),
				Arguments.of("ContractType", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.ALWAYS, 4, 2, 250.50),
				Arguments.of("ContractType123", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.ALWAYS, 4, 2, 250.50),
				Arguments.of("ContractType123&123_", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.ALWAYS, 4, 2, 250.50),
				Arguments.of("AContractTypeForTimeStampAlwaysAndAlsoOneThatReactsSuperFastAndThisIsAlsoAVeryLongName", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.ALWAYS, 4, 2, 250.50),
				Arguments.of("Contract Type", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.ALWAYS, 4, 2, 250.50),

				//contractTypeStatus
				Arguments.of("CT1", ContractTypeStatus.INACTIVE, true, true, true, Timestamp.ALWAYS, 4, 2, 250.50),

				//boolean combination false email
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, false, true, true, Timestamp.ALWAYS, 4, 2, 250.50),
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, false, false, true, Timestamp.ALWAYS, 4, 2, 250.50),
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, false, true, false, Timestamp.ALWAYS, 4, 2, 250.50),
				//boolean combination false phone
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, true, false, true, Timestamp.ALWAYS, 4, 2, 250.50),
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, false, false, true, Timestamp.ALWAYS, 4, 2, 250.50),
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.ALWAYS, 4, 2, 250.50),
				//boolean combination false application
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, true, true, false, Timestamp.ALWAYS, 4, 2, 250.50),
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, false, true, false, Timestamp.ALWAYS, 4, 2, 250.50),
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.ALWAYS, 4, 2, 250.50),

				//timestamps
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.WORKINGHOURS, 4, 2, 250.50),

				//maxHandlingTime
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.WORKINGHOURS, 1, 2, 250.50),
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.WORKINGHOURS, 10, 2, 250.50),
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.WORKINGHOURS, 900, 2, 250.50),

				//minThroughputTime
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.WORKINGHOURS, 4, 1, 250.50),
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.WORKINGHOURS, 4, 9, 250.50),

				//price
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.WORKINGHOURS, 4, 9, 0.01),
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.WORKINGHOURS, 4, 9, 1),
				Arguments.of("CT1", ContractTypeStatus.ACTIVE, true, true, true, Timestamp.WORKINGHOURS, 4, 9, 9999.99)
		);
    }

    private static Stream<Arguments> invalidContractTypeAttributes() {
        return Stream.of(
                // name: not null or empty, not duplicate, but (special chars)? and digits are ok
        		Arguments.of("   ", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, 2, 999.99),
        		Arguments.of("", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, 2, 999.99),
        		Arguments.of(null, ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, 2, 999.99),
                
                // hasEmail/hasPhone/hasApplication: at least 1 ticket creation method must be selected
                Arguments.of("NoSupportCreationMethod", ContractTypeStatus.ACTIVE, false, false, false, Timestamp.ALWAYS, 3, 1, 3999.99),
                
                // maxHandlingTime: greater than zero
                Arguments.of("maxHandlingTimeLessThanZero", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, -5, 2, 999.99),
                Arguments.of("maxHandlingTimeLessThanZero", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, -1, 2, 999.99),
                Arguments.of("maxHandlingTimeZero", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 0, 2, 999.99),
                
                // minThroughputTime: greater than zero
                Arguments.of("minThroughputTimeLessThanZero", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, -5, 999.99),
                Arguments.of("minThroughputTimeLessThanZero", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, -1, 999.99),
                Arguments.of("minThroughputTimeZero", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, 0, 999.99),
                                
                // price: greater than zero
                Arguments.of("priceLessThanZero", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, 1, -420.69),
                Arguments.of("priceLessThanZero", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, 1, -00.01),
				Arguments.of("priceLessThanZero", ContractTypeStatus.ACTIVE, true, false, false, Timestamp.WORKINGHOURS, 5, 1, 0),

				Arguments.of("priceLessThanZero", null, true, false, false, Timestamp.WORKINGHOURS, 5, 1, 0),
				Arguments.of("priceLessThanZero", ContractTypeStatus.ACTIVE, true, false, false, null, 5, 1, 0)

        		);
    }

    @ParameterizedTest
    @MethodSource("validContractTypeAttributes")
    public void createContractType_ValidAttributes_DoesNotThrowException(String name,
			ContractTypeStatus contractTypeStatus, boolean hasEmail, boolean hasPhone, boolean hasApplication,
			Timestamp timestamp, int maxHandlingTime, int minThroughputTime, double price) {

        Assertions.assertDoesNotThrow(() -> new ActemiumContractType.ContractTypeBuilder()
				.name(name)
				.status(contractTypeStatus)
				.hasEmail(hasEmail)
				.hasPhone(hasPhone)
				.hasApplication(hasApplication)
				.timestamp(timestamp)
				.maxHandlingTime(maxHandlingTime)
				.minThroughputTime(minThroughputTime)
				.price(price)
				.build());
    }

	@ParameterizedTest
	@MethodSource("invalidContractTypeAttributes")
	public void createContractType_InValidAttributes_ThrowsIllegalArgumentException(String name,
			ContractTypeStatus contractTypeStatus, boolean hasEmail, boolean hasPhone, boolean hasApplication,
			Timestamp timestamp, int maxHandlingTime, int minThroughputTime, double price) {
		Assertions.assertThrows(InformationRequiredException.class, () -> new ActemiumContractType.ContractTypeBuilder()
				.name(name)
				.status(contractTypeStatus)
				.hasEmail(hasEmail)
				.hasPhone(hasPhone)
				.hasApplication(hasApplication)
				.timestamp(timestamp)
				.maxHandlingTime(maxHandlingTime)
				.minThroughputTime(minThroughputTime)
				.price(price)
				.build());
	}
        
}
