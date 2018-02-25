package org.kkonoplev.bali.classifyreport;

import java.util.ArrayList;
import java.util.List;

public class Box {
	
	List<Animal> animals = new ArrayList<>();
	private String str = "";
	
	public List<Animal> getAnimals() {
		return animals;
	}
	public void setAnimals(List<Animal> animals) {
		this.animals = animals;
	}
	public String getStr() {
		return str;
	}
	public void setStr(String str) {
		this.str = str;
	}
	
	
}
