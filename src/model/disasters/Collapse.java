package model.disasters;

import model.infrastructure.ResidentialBuilding;

public class Collapse extends Disaster {
	
	public Collapse(int cycle, ResidentialBuilding target){
		super(cycle,target);
	}
	public void strike(){
		super.strike();
		((ResidentialBuilding)getTarget()).setFoundationDamage(10);
	}
	public void cycleStep(){
		((ResidentialBuilding)getTarget()).setFoundationDamage(((ResidentialBuilding)getTarget()).getFoundationDamage()+10);
	}
}
