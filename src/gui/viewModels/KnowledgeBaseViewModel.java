package gui.viewModels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import domain.ActemiumKbItem;
import domain.KbItem;
import domain.User;
import domain.enums.KbItemType;
import domain.facades.KnowledgeBaseFacade;
import domain.facades.TicketFacade;
import domain.facades.UserFacade;
import exceptions.InformationRequiredException;
import gui.GUIEnum;
import javafx.collections.ObservableList;
import languages.LanguageResource;

public class KnowledgeBaseViewModel extends ViewModel {

	private GUIEnum currentState;
	private KbItem selectedKbItem;
	private final KnowledgeBaseFacade knowledgeBaseFacade;
	private final TicketFacade ticketFacade;
	private final UserFacade userFacade;

	public KnowledgeBaseViewModel(KnowledgeBaseFacade knowledgeBaseFacade, TicketFacade ticketFacade, UserFacade userFacade) {
		super();
		this.knowledgeBaseFacade = knowledgeBaseFacade;
		this.ticketFacade = ticketFacade;
		this.userFacade = userFacade;
		setCurrentState(GUIEnum.KNOWLEDGEBASE);
	}

	public ObservableList<KbItem> giveKbItems() {
		return knowledgeBaseFacade.giveActemiumKbItems();
	}

	public void setSelectedKbItem(KbItem kbItem) {
		this.selectedKbItem = kbItem;
		if (kbItem != null) {
			setCurrentState(GUIEnum.KNOWLEDGEBASE);
		}
		fireInvalidationEvent();
	}

	public User getSignedInUser() {
		return userFacade.getSignedInUser();
	}

	public ArrayList<String> getDetailsNewKbItem() {
		return new ArrayList<String>(Arrays.asList(LanguageResource.getString("title"), LanguageResource.getString("type"), LanguageResource.getString("keywords"), LanguageResource.getString("text")));
	}

    public Map<String, Map<Boolean, Object>> getDetails() {
    	KbItem kbItem = selectedKbItem;
        Map<String,Map<Boolean, Object>> details = new LinkedHashMap<>();
        details.put(LanguageResource.getString("title"), Collections.singletonMap(true, kbItem.getTitle()));
        details.put(LanguageResource.getString("type"), Collections.singletonMap(true, kbItem.getType()));
        details.put(LanguageResource.getString("keywords"), Collections.singletonMap(true, kbItem.getKeywords()));
        details.put(LanguageResource.getString("text"), Collections.singletonMap(true, kbItem.getText()));
        details.put("CompletedTicketsOfSameType", Collections.singletonMap(true, ticketFacade.giveTicketsOfSameType(selectedKbItem.getType())));

		return details;
	}

	public void registerKbItem(String title, KbItemType type, String keywords, String text) throws InformationRequiredException {
		knowledgeBaseFacade.registerKbItem(title, type, keywords, text);
		setSelectedKbItem(knowledgeBaseFacade.getLastAddedKbItem());
	}

	public void modifyKbItem(String title, KbItemType type, String keywords, String text) throws InformationRequiredException {
		knowledgeBaseFacade.modifyKbItem((ActemiumKbItem) selectedKbItem, title, type, keywords, text);
	}

	public GUIEnum getCurrentState() {
		return currentState;
	}

	public void setCurrentState(GUIEnum currentState) {
		this.currentState = currentState;
	}

	public String getTitleSelectedKbItem() {
		return selectedKbItem.getTitle();
	}

	@Override
	public void delete() {
		knowledgeBaseFacade.delete((ActemiumKbItem) selectedKbItem);
	}

	public void refreshKbData() {
		knowledgeBaseFacade.refreshKbData();
	}
}
