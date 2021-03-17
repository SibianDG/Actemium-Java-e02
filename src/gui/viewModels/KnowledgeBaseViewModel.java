package gui.viewModels;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;

import domain.ActemiumKbItem;
import domain.KbItem;
import domain.enums.KbItemType;
import domain.facades.KnowledgeBaseFacade;
import exceptions.InformationRequiredException;
import gui.GUIEnum;
import javafx.collections.ObservableList;

public class KnowledgeBaseViewModel extends ViewModel {

	private GUIEnum currentState;
	private KbItem selectedKbItem;
	private final KnowledgeBaseFacade knowledgeBaseFacade;

	public KnowledgeBaseViewModel(KnowledgeBaseFacade knowledgeBaseFacade) {
		super();
		this.knowledgeBaseFacade = knowledgeBaseFacade;
		setCurrentState(GUIEnum.KNOWLEDGEBASE);
	}

	public ObservableList<KbItem> giveKbItems() {
		return knowledgeBaseFacade.giveActemiumKbItems();
	}

	public KbItem getSelectedKbItem() {
		return selectedKbItem;
	}

	public void setSelectedKbItem(KbItem kbItem) {
		this.selectedKbItem = kbItem;
		if (kbItem != null) {
			// substring(8) to remove ACTEMIUM
			setCurrentState(GUIEnum.KNOWLEDGEBASE);
		}
		fireInvalidationEvent();
	}

	public ArrayList<String> getDetailsNewKbItem() {
		return new ArrayList<String>(Arrays.asList("Title", "Type", "Text"));
	}

	public Map<String, Map<Boolean, Object>> getDetails() {
		KbItem kbItem = selectedKbItem;
		Map<String, Map<Boolean, Object>> details = new LinkedHashMap<>();
		details.put("Title", Collections.singletonMap(true, kbItem.getTitle()));
		details.put("Type", Collections.singletonMap(true, kbItem.getTypeAsEnum()));
		details.put("Text", Collections.singletonMap(true, kbItem.getText()));

		return details;
	}

	public void registerKbItem(String title, KbItemType type, String text) throws InformationRequiredException {
		knowledgeBaseFacade.registerKbItem(title, type, text);
		setSelectedKbItem(knowledgeBaseFacade.getLastAddedKbItem());
	}

	public void modifyKbItem(String title, KbItemType type, String text) throws InformationRequiredException {
		knowledgeBaseFacade.modifyKbItem((ActemiumKbItem) selectedKbItem, title, type, text);
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
}
