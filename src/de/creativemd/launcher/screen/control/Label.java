package de.creativemd.launcher.screen.control;

import de.creativemd.launcher.screen.event.ControlEvent;

public class Label extends ScreenControl {
	
	protected String text;
	
	public Label(String id, String text) {
		super(id);
		this.text = text;
	}
	
	public void setText(String text) {
		this.text = text;
		updateText();
	}
	
	public String getText() {
		return text;
	}
	
	public void updateText() {
		runCode("$('#" + id + "').text('" + text + "');");
	}
	
	@Override
	public void initVariables() {
		super.initVariables();
		variables.put("text", text);
	}
	
	@Override
	public void onEvent(ControlEvent event) {
		event.success("Finally it worked!");
	}
	
}
