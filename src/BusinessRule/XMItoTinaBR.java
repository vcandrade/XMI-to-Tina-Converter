/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package BusinessRule;

import VO.Place;
import VO.Transition;
import View.XMItoTinaView;
import jaco.mp3.player.MP3Player;
import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.util.Vector;
import java.util.regex.Pattern;
import javax.swing.JOptionPane;

/**
 *
 * @author Vinicius
 */
public class XMItoTinaBR {

    XMItoTinaView xMItoTinaView;
    private MP3Player player;
    private Vector <String> tags = new Vector();
    private Vector <Place> places = new Vector();
    private Vector <Transition> transitions = new Vector();
    private String netName;
    private int minDefault;
    private int maxDefault;
    
    
    public XMItoTinaBR() {
        
    }

    public XMItoTinaBR(XMItoTinaView xMItoTinaView, int minDefault, int maxDefault) {
        
        this.xMItoTinaView = xMItoTinaView;
        this.minDefault = minDefault;
        this.maxDefault = maxDefault;
    }
    
    public boolean checkExistence(String outputDirectory) {
        
        File file = new File(outputDirectory);
        
        if(file.exists()) {
            
            String [] fileName;
            
            fileName = outputDirectory.split(Pattern.quote("/"));
            
            player = new MP3Player();
            player.addToPlayList(new File("src\\Media\\Windows Exclamation.mp3"));
            player.play();
                
            if(JOptionPane.showConfirmDialog(null, fileName[fileName.length - 1] + " already exists.\nWant to replace it?", "Existing File", JOptionPane.YES_NO_OPTION) == 0) {
                
                return false;
            }
            else {
                
                return true;
            }
        }
        else {
            return false;
        }
    }
    
    public void openFile(String inputDirectory) {
        
        try {

            BufferedReader xmi = new BufferedReader(new FileReader(inputDirectory));

            String line = "";
            line = xmi.readLine();

            while(line != null) {

                line = line.replace(" ", "<");
                setTags(line);
                line = xmi.readLine();
            }
            
            xmi.close();
            
            xMItoTinaView.setValueJProgressBar(25);
        }
        catch(IOException ioe) {

            player = new MP3Player();
            player.addToPlayList(new File("src\\Media\\Windows Critical Stop.mp3"));
            player.play();
            
            JOptionPane.showMessageDialog(null, "Error reading file: " + ioe.getMessage(), "Error reading file", JOptionPane.ERROR_MESSAGE);
        }
        catch(Exception e) {

            player = new MP3Player();
            player.addToPlayList(new File("src\\Media\\Windows Critical Stop.mp3"));
            player.play();
            
            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }
    }
    
    public void convertFile() {
        
        String name [] = null;
        
        try {
         
            for(int cont = 0; cont <= getTags().size() - 2; cont++) {

                String e = getTags().elementAt(cont);
                String fragment [] = e.split(Pattern.quote("<"));
                
                String nameFragment [] = xMItoTinaView.jTargetFile.getText().split(Pattern.quote("\\"));
                String nameFragmentAux [] = nameFragment[nameFragment.length - 1].split(Pattern.quote("."));

                if(fragment[1].equals("xmi:XMI")) {

                    name = fragment[4].split(Pattern.quote("\""));
                    setNetName(nameFragmentAux[0]);
                }
                else if(fragment[3].equals("Place")) {
                    
                    name = fragment[4].split(Pattern.quote("\""));
                    
                    if(name[1].contains("&lt;")) {
                        
                        name[1] = name[1].replace("&lt;", "<");
                    }
                    
                    Place place = new Place();
                    
                    place.setCode(cont - 2);
                    place.setName(name[1]);
                    
                    if(e.toString().contains("tokensNb")) {
                        place.setTokensNb(1);
                    }
                    else {
                        place.setTokensNb(0);
                    }
                    
                    setPlaces(place);
                }
                else if(fragment[3].equals("Transition")) {
                    
                    String constraints [] = null;
                    
                    name = fragment[4].split(Pattern.quote("\""));
                    
                    Transition transition = new Transition(getMinDefault(), getMaxDefault());
                    
                    transition.setCode(cont - 2);
                    
                    if(name[1].contains("&lt;")) {
                        
                        name[1] = name[1].replace("&lt;", "<");
                    }
                    
                    if(name[1].contains("[")) {
                    
                        name[1] = name[1].replace("]", "[");
                        constraints = name[1].split(Pattern.quote("["));
                        
                        transition.setName(constraints[0]);
                        transition.setMinTime(Integer.parseInt(constraints[1]));
                        transition.setMaxTime(Integer.parseInt(constraints[3]));
                        transition.setMinEnergy(Integer.parseInt(constraints[5]));
                        transition.setMaxEnergy(Integer.parseInt(constraints[7]));
                    }
                    else {
                        
                        transition.setName(name[1]);
                    }
                    
                    setTransitions(transition);
                }
            }
            
            for(int cont = 0; cont <= getTags().size() - 2; cont++) {
                
                String e = getTags().elementAt(cont);
                String fragment [] = e.split(Pattern.quote("<"));
                
                if(fragment[3].equals("Arc")) {
                    
                    int source = Integer.parseInt(fragment[5].split(Pattern.quote("\""))[1].replace("/", ""));
                    int target = Integer.parseInt(fragment[6].split(Pattern.quote("\""))[1].replace("/", ""));
                    
                    for (Transition transition: this.getTransitions()) {
                        
                        if(source == transition.getCode()) {
                            
                            for (Place place : this.getPlaces()) {
                                
                                if(target == place.getCode()) {
                                    
                                    transition.setDestinationPlaces(place);
                                }
                            }                            
                        }
                        else if(target == transition.getCode()) {
                            
                            for (Place place : this.getPlaces()) {
                                
                                if(source == place.getCode()) {
                                    
                                    transition.setOriginPlaces(place);
                                }
                            }
                        }
                    }
                }
            }
            
            xMItoTinaView.setValueJProgressBar(35);
        }
        catch(ArrayIndexOutOfBoundsException aioobe) {
    
        }
        catch(Exception e) {
            
            player = new MP3Player();
            player.addToPlayList(new File("src\\Media\\Windows Critical Stop.mp3"));
            player.play();
            
            JOptionPane.showMessageDialog(null, "Error in file conversion: " + e.getMessage(), "Error in file conversion", JOptionPane.ERROR_MESSAGE);
        }
        
    }
    
    public void netCreateFile(String outputDirectory, String constraints) {
        
        try {

            BufferedWriter net = new BufferedWriter(new FileWriter(outputDirectory));

            net.write("net " + getNetName());
            net.newLine();

            for (Transition transition: this.getTransitions()) {

                String originPlaces = "";
                String destinationPlaces = "";

                for(int cont = 0; cont < transition.getOriginPlaces().size(); cont++) {

                    Place place = (Place) transition.getOriginPlaces().elementAt(cont);                    
                    originPlaces = originPlaces + " " + place.getName();
                }

                for(int cont = 0; cont < transition.getDestinationPlaces().size(); cont++) {

                    Place place = (Place) transition.getDestinationPlaces().elementAt(cont);
                    destinationPlaces = destinationPlaces + " " + place.getName();
                }

                if(constraints.equals("Time")) {

                    net.write("tr " + transition.getName() + " [" + transition.getMinTime() + "," + transition.getMaxTime() + "]" + originPlaces + " ->" + destinationPlaces);
                    net.newLine();
                }
                else {

                    net.write("tr " + transition.getName() + " [" + transition.getMinEnergy() + "," + transition.getMaxEnergy() + "]" + originPlaces + " ->" + destinationPlaces);
                    net.newLine();
                }
            }

            xMItoTinaView.setValueJProgressBar(20);

            net.write("");
            net.newLine();

            for(int cont = 0; cont < this.getPlaces().size(); cont++) {

                Place place = this.getPlaces().elementAt(cont);

                if(cont != 0) {

                    Place placeAux = this.getPlaces().elementAt(cont - 1);

                    if(!place.getName().equals(placeAux.getName())) {

                        net.write("pl " + place.getName() + " (" + place.getTokensNb() + ")");
                        net.newLine();
                    }                
                }
                else {

                    net.write("pl " + place.getName() + " (" + place.getTokensNb() + ")");
                    net.newLine();
                }
            }

            xMItoTinaView.setValueJProgressBar(20);

            net.write("");
            net.close();

            player = new MP3Player();
            player.addToPlayList(new File("src\\Media\\Windows Error.mp3"));
            player.play();

            JOptionPane.showMessageDialog(null, "Conversion completed!", "XMI to TINA Converter", JOptionPane.INFORMATION_MESSAGE);

            xMItoTinaView.clearFields();
        } 
        catch(IOException ioe) {

            player = new MP3Player();
            player.addToPlayList(new File("src\\Media\\Windows Critical Stop.mp3"));
            player.play();

            JOptionPane.showMessageDialog(null, "Error creating file: " + ioe.getMessage(), "Error creating file", JOptionPane.ERROR_MESSAGE);
        }
        catch(Exception e) {

            player = new MP3Player();
            player.addToPlayList(new File("src\\Media\\Windows Critical Stop.mp3"));
            player.play();

            JOptionPane.showMessageDialog(null, "Error: " + e.getMessage(), "Error", JOptionPane.ERROR_MESSAGE);
        }

    }

    /**
     * @return the tags
     */
    public Vector <String> getTags() {
        return tags;
    }

    /**
     * @param tags the tags to set
     */
    public void setTags(String tag) {
        this.tags.add(tag);
    }

    /**
     * @return the places
     */
    public Vector <Place> getPlaces() {
        return places;
    }

    /**
     * @param places the places to set
     */
    public void setPlaces(Place place) {
        this.places.add(place);
    }

    /**
     * @return the transitions
     */
    public Vector <Transition> getTransitions() {
        return transitions;
    }

    /**
     * @param transitions the transitions to set
     */
    public void setTransitions(Transition transition) {
        this.transitions.add(transition);
    }

    /**
     * @return the netName
     */
    public String getNetName() {
        return netName;
    }

    /**
     * @param netName the netName to set
     */
    public void setNetName(String netName) {
        this.netName = netName;
    }    

    /**
     * @return the minDefault
     */
    public int getMinDefault() {
        return minDefault;
    }

    /**
     * @param minDefault the minDefault to set
     */
    public void setMinDefault(int minDefault) {
        this.minDefault = minDefault;
    }

    /**
     * @return the maxDefault
     */
    public int getMaxDefault() {
        return maxDefault;
    }

    /**
     * @param maxDefault the maxDefault to set
     */
    public void setMaxDefault(int maxDefault) {
        this.maxDefault = maxDefault;
    }
}
