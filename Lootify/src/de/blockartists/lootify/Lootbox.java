package de.blockartists.lootify;

import org.bukkit.Material;

public class Lootbox {
	private String name;
	private String prefix;
	private Material itemMaterial;
	private String textOnOpening;
	
	public Lootbox(String prefix, String name, Material material) {
		
	}
	
	public Lootbox(String prefix, String name, Material material, String textOnOpening) {
		this(prefix, name, material);
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
	
	public void setItemMaterial(Material itemMaterial) {
		this.itemMaterial = itemMaterial;
	}
	
	public Material getItemMaterial() {
		return this.itemMaterial;
	}
	
	public void setTextOnOpening(String textOnOpening) {
		this.textOnOpening = textOnOpening;
	}
	
	public String getTextOnOpening() {
		return this.textOnOpening;
	}
}
