package model.disasters;

import model.infrastructure.ResidentialBuilding;

public class GasLeak extends Disaster{
	
	
	public GasLeak (int cycle, ResidentialBuilding target){
		super(cycle,target);
	}
	public void strike(){
		super.strike();
		((ResidentialBuilding)getTarget()).setGasLevel(10);
	}
	public void cycleStep(){
		((ResidentialBuilding)getTarget()).setGasLevel(((ResidentialBuilding)getTarget()).getGasLevel()+15);
	}
}
