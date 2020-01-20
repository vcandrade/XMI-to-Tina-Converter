/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package VO;

import java.util.Vector;

/**
 *
 * @author Vinicius
 */
public class Transition extends Node {
    
    private int minTime;
    private int maxTime;
    private int minEnergy;
    private int maxEnergy;
    private Vector <Place> originPlaces = new Vector();
    private Vector <Place> destinationPlaces = new Vector();
//    private Place [] originPlace;
//    private Place [] destinationPlace;

    public Transition(int min, int max) {
        
        this.setMinTime(min);
        this.setMaxTime(max);
        this.setMinEnergy(min);
        this.setMaxEnergy(max);
    }


    /**
     * @return the originPlaces
     */
    public Vector getOriginPlaces() {
        return originPlaces;
    }

    /**
     * @param originPlaces the originPlaces to set
     */
    public void setOriginPlaces(Place originPlace) {
        this.originPlaces.add(originPlace);
    }

    /**
     * @return the destinationPlaces
     */
    public Vector getDestinationPlaces() {
        return destinationPlaces;
    }

    /**
     * @param destinationPlaces the destinationPlaces to set
     */
    public void setDestinationPlaces(Place destinationPlace) {
        this.destinationPlaces.add(destinationPlace);
    }

    /**
     * @return the minTime
     */
    public int getMinTime() {
        return minTime;
    }

    /**
     * @param minTime the minTime to set
     */
    public void setMinTime(int minTime) {
        this.minTime = minTime;
    }

    /**
     * @return the maxTime
     */
    public int getMaxTime() {
        return maxTime;
    }

    /**
     * @param maxTime the maxTime to set
     */
    public void setMaxTime(int maxTime) {
        this.maxTime = maxTime;
    }

    /**
     * @return the minEnergy
     */
    public int getMinEnergy() {
        return minEnergy;
    }

    /**
     * @param minEnergy the minEnergy to set
     */
    public void setMinEnergy(int minEnergy) {
        this.minEnergy = minEnergy;
    }

    /**
     * @return the maxEnergy
     */
    public int getMaxEnergy() {
        return maxEnergy;
    }

    /**
     * @param maxEnergy the maxEnergy to set
     */
    public void setMaxEnergy(int maxEnergy) {
        this.maxEnergy = maxEnergy;
    }
}
