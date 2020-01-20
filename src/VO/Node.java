/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package VO;

/**
 *
 * @author Vinicius
 */
abstract class Node {
    
    private String name;
    private int code;

    /**
     * @return the name
     */
    public String getName() {
        return name;
    }

    /**
     * @param name the name to set
     */
    public void setName(String name) {
        this.name = name;
    }

    /**
     * @return the code
     */
    public int getCode() {
        return code;
    }

    /**
     * @param code the code to set
     */
    public void setCode(int code) {
        this.code = code;
    }
}
