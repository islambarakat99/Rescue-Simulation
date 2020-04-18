package model.disasters;

import model.people.Citizen;

public class Infection extends Disaster {
	
	public Infection (int cycle, Citizen target){
		super(cycle,target);
	}
	public void strike(){
		super.strike();
		((Citizen)getTarget()).setToxicity(25);
	}
	public void cycleStep(){
		((Citizen)getTarget()).setToxicity(((Citizen)getTarget()).getToxicity()+15);
	}
}
