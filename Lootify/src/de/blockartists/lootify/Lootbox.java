package de.blockartists.lootify;

public class Lootbox {
	private String name;
	private String prefix;
	private String textOnOpening;
	
	public Lootbox(String prefix, String name) {
		this.prefix = prefix;
		this.name = name;
	}
	
	public Lootbox(String prefix, String name, String textOnOpening) {
		this(prefix, name);
		this.textOnOpening = textOnOpening;
	}
	
	public void setName(String name) {
		this.name = name;
	}
	
	public String getName() {
		return this.name;
	}
	
	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}
	
	public String getPrefix() {
		return this.prefix;
	}
	
	public void setTextOnOpening(String textOnOpening) {
		this.textOnOpening = textOnOpening;
	}
	
	public String getTextOnOpening() {
		return this.textOnOpening;
	}
}
