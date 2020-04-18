package model.disasters;

import model.infrastructure.ResidentialBuilding;

public class Fire extends Disaster{
	

	public Fire(int cycle, ResidentialBuilding target) {
		super(cycle,target);
	}
	public void strike(){
		super.strike();
		((ResidentialBuilding)getTarget()).setFireDamage(10);
	}
	public void cycleStep(){
		((ResidentialBuilding)getTarget()).setFireDamage(((ResidentialBuilding)getTarget()).getFireDamage()+10);
	}
}
