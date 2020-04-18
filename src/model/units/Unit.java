package model.units;

import model.events.SOSResponder;
import model.events.WorldListener;
import model.infrastructure.ResidentialBuilding;
import model.people.Citizen;
import model.people.CitizenState;
import simulation.Address;
import simulation.Rescuable;
import simulation.Simulatable;
import simulation.Simulator;

abstract public class Unit implements Simulatable,SOSResponder{
	
	private String unitID;
	private UnitState state;
	private Address location;
	private Rescuable target;
	private int distanceToTarget;
	private int stepsPerCycle;
	private WorldListener worldListener;
	
	public Unit(String id, Address location, int stepsPerCycle,WorldListener wl){
		state = UnitState.IDLE;
		unitID=id;
		this.location=location;
		this.stepsPerCycle=stepsPerCycle;
		worldListener=wl;
	}
	public void respond(Rescuable r) {
		if(this.target==null || (this.target instanceof Citizen && ((Citizen)this.target).getState()==CitizenState.RESCUED)) {
			this.target=r;
			this.distanceToTarget=Math.abs(r.getLocation().getX()-this.location.getX())+Math.abs(r.getLocation().getY()-this.location.getY());
		}
		else {
			this.target.getDisaster().setActive(true);
			this.target=r;
			this.distanceToTarget=Math.abs(r.getLocation().getX()-this.location.getX())+Math.abs(r.getLocation().getY()-this.location.getY());
		}
		this.state=UnitState.RESPONDING;
	}
	public UnitState getState() {
		return state;
	}

	public void setState(UnitState state) {
		this.state = state;
	}

	public Address getLocation() {
		return location;
	}

	public void setLocation(Address location) {
		this.location = location;
	}

	public String getUnitID() {
		return unitID;
	}

	public Rescuable getTarget() {
		return target;
	}

	public int getStepsPerCycle() {
		return stepsPerCycle;
	}

	public WorldListener getWorldListener() {
		return worldListener;
	}

	public void setWorldListener(WorldListener worldListener) {
		this.worldListener = worldListener;
	}

	public void setDistanceToTarget(int distanceToTarget) {
		this.distanceToTarget = distanceToTarget;
	}
	public void cycleStep(){
		if(this instanceof Evacuator) {
			Evacuator x=(Evacuator)this;
			if(x.getPassengers().size()==x.getMaxCapacity()) {
				if(x.getDistanceToBase()==0) {
					x.getWorldListener().assignAddress(this,0,0);
					x.treat();
					this.distanceToTarget=this.target.getLocation().getX()+this.target.getLocation().getY();
					this.state=UnitState.RESPONDING;		
				}
				else {
					x.setDistanceToBase(Math.max(0,x.getDistanceToBase()-x.getStepsPerCycle()));
					if(x.getDistanceToBase()==0) {
						x.getWorldListener().assignAddress(this,0,0);
					}
				}
			}
			else {
				if(this.state==UnitState.RESPONDING) {
					if(this.distanceToTarget==0) {
						this.worldListener.assignAddress(this,this.target.getLocation().getX(), this.target.getLocation().getY());
						this.state=UnitState.TREATING;this.treat();
						x.setDistanceToBase(x.getTarget().getLocation().getX()+x.getTarget().getLocation().getY());
						
					}
					else {
						this.distanceToTarget-=Math.min(this.stepsPerCycle,this.distanceToTarget);
						if(this.distanceToTarget==0) {
							this.worldListener.assignAddress(this,this.target.getLocation().getX(),this.target.getLocation().getY());
						}
					}
					if(this.target instanceof ResidentialBuilding && ((ResidentialBuilding)this.target).getStructuralIntegrity()==0) {
						this.jobsDone();
					}
				}
				else {
					if(this.state==UnitState.TREATING) {
						if(x.getDistanceToBase()==0) {
							x.treat();
							this.distanceToTarget=this.target.getLocation().getX()+this.target.getLocation().getY();
							this.state=UnitState.RESPONDING;		
						}
						else {
							x.setDistanceToBase(Math.max(0,x.getDistanceToBase()-x.getStepsPerCycle()));
							if(x.getDistanceToBase()==0) {
								this.worldListener.assignAddress(this,0,0);
							}
						}
					}
				}
				
			}
		}
		else {
			if(this.state==UnitState.RESPONDING) {
				if(this.distanceToTarget==0) {
					this.worldListener.assignAddress(this,this.target.getLocation().getX(), this.target.getLocation().getY());
					this.state=UnitState.TREATING;
					this.treat();
				}
				else {
					this.distanceToTarget-=Math.min(this.stepsPerCycle,this.distanceToTarget);
					if(this.distanceToTarget==0) {
						this.worldListener.assignAddress(this,this.target.getLocation().getX(),this.target.getLocation().getY());
					}
					
				}
			}
			else {
				if(this.state==UnitState.TREATING) {
					this.treat();
					
				}
			}
		}
	}
	public void treat(){
		target.getDisaster().setActive(false);
		
	}
	public void jobsDone(){
		state = UnitState.IDLE;this.target=null;
	}
}
