package org.kkonoplev.bali.classifyreport;

import java.lang.reflect.Type;
import java.util.ArrayList;
import java.util.List;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.google.gson.reflect.TypeToken;
import com.google.gson.typeadapters.RuntimeTypeAdapterFactory;


public class GsonReports {
	
	public static void main(String[] args) throws Exception {
		
		List<Animal> animals = new ArrayList<>();
		animals.add(new Dog("dog1", true));
		animals.add(new Dog("dog2", false));
		animals.add(new Cat("cat1", false));
		Box box = new Box();
		box.setAnimals(animals);
		
		RuntimeTypeAdapterFactory<Animal> runtimeTypeAdapterFactory = RuntimeTypeAdapterFactory
		    .of(Animal.class, "typeparam")
		    .registerSubtype(Dog.class, "dog")
		    .registerSubtype(Cat.class, "cat");
		Gson gson = new GsonBuilder().setPrettyPrinting().registerTypeAdapterFactory(runtimeTypeAdapterFactory).create();
		
	
		String json = gson.toJson(box);
		Box fromJson = gson.fromJson(json, Box.class);
		System.out.println(gson.toJson(fromJson));
		
		for (Animal animal : fromJson.getAnimals()) {
		    if (animal instanceof Dog) {
		        System.out.println(animal + " DOG");
		    } else if (animal instanceof Cat) {
		        System.out.println(animal + " CAT");
		    } else if (animal instanceof Animal) {
		        System.out.println(animal + " ANIMAL");
		    } else {
		        System.out.println("Class not found");
		    }
		}
		
		
	}
		

}
