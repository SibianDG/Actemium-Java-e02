package tests;

import java.util.stream.Stream;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.MethodSource;

import domain.ActemiumKbItem.KbItemBuilder;
import domain.enums.KbItemType;
import exceptions.InformationRequiredException;

public class KbItemTest {
	
    private static Stream<Arguments> validKbItemAttributes() {
        return Stream.of(
				Arguments.of("How to fix a router", KbItemType.NETWORK, "SampleKeyword, Router, Internet", "Step 01\nPut the router in the trash can\nStep 02\nBuy a new router"),
				Arguments.of("How to fix a Database", KbItemType.DATABASE, "SampleKeyword, Router, Internet", "Step 01\nPut the router in the trash can\nStep 02\nBuy a new router"),
				Arguments.of("How to fix a robot arm", KbItemType.INFRASTRUCTURE, "SampleKeyword, Router, Internet", "Step 01\nPut the router in the trash can\nStep 02\nBuy a new router"),
				Arguments.of("How to fix a coffe machine", KbItemType.OTHER, "SampleKeyword, Router, Internet", "Step 01\nPut the router in the trash can\nStep 02\nBuy a new router"),
				Arguments.of("How to fix blue screen of death", KbItemType.SOFTWARE, "SampleKeyword, Router, Internet", "Step 01\nPut the router in the trash can\nStep 02\nBuy a new router"),
				Arguments.of("How to replace a cpu", KbItemType.HARDWARE, "SampleKeyword, Router, Internet", "Step 01\nPut the router in the trash can\nStep 02\nBuy a new router")				
		);
    }

    private static Stream<Arguments> invalidKbItemAttributes() {
        return Stream.of(
                // title: not null or blank
        		Arguments.of(null, KbItemType.NETWORK, "SampleKeyword, Router, Internet", "Step 01\nPut the router in the trash can\nStep 02\nBuy a new router"),
        		Arguments.of("    ", KbItemType.NETWORK, "SampleKeyword, Router, Internet", "Step 01\nPut the router in the trash can\nStep 02\nBuy a new router"),
                // type: not null
        		Arguments.of("How to fix a Database", null, "SampleKeyword, Router, Internet", "Step 01\nPut the router in the trash can\nStep 02\nBuy a new router"),
                // keywords: not null or blank
				Arguments.of("How to fix a Database", KbItemType.DATABASE, null, "Step 01\nPut the router in the trash can\nStep 02\nBuy a new router"),
				Arguments.of("How to fix a Database", KbItemType.DATABASE, "   ", "Step 01\nPut the router in the trash can\nStep 02\nBuy a new router"),
                // text: not null or blank
				Arguments.of("How to fix a robot arm", KbItemType.INFRASTRUCTURE, "SampleKeyword, Router, Internet", null),
				Arguments.of("How to fix a coffe machine", KbItemType.OTHER, "SampleKeyword, Router, Internet", "   ")		
		);
    }

    @ParameterizedTest
    @MethodSource("validKbItemAttributes")
    public void createContractType_ValidAttributes_DoesNotThrowException(String title, KbItemType type, String keywords, String text) {
        Assertions.assertDoesNotThrow(() -> new KbItemBuilder()
    			.title(title)
    			.type(type)
    			.keywords(keywords)
    			.text(text)
    			.build());
    }

	@ParameterizedTest
	@MethodSource("invalidKbItemAttributes")
	public void createContractType_InValidAttributes_ThrowsIllegalArgumentException(String title, KbItemType type, String keywords, String text) {
		Assertions.assertThrows(InformationRequiredException.class, () -> new KbItemBuilder()
    			.title(title)
    			.type(type)
    			.keywords(keywords)
    			.text(text)
    			.build());
    }
        
}
