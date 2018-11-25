/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package systems.tech247.endeavorclockin;

/**
 *
 * @author Admin
 */
public class TADZKLibCommand {
    int commandID;
    String commandString;
    Boolean shouldDisconnect;
    String resultFilterString;
    Boolean booleanString;

    public TADZKLibCommand(int commandID, String commandString, Boolean shouldDisconnect, String resultFilterString) {
        this.commandID = commandID;
        this.commandString = commandString;
        this.shouldDisconnect = shouldDisconnect;
        this.resultFilterString = resultFilterString;
    }

    public TADZKLibCommand(int commandID, String commandString, Boolean shouldDisconnect, Boolean booleanString) {
        this.commandID = commandID;
        this.commandString = commandString;
        this.shouldDisconnect = shouldDisconnect;
        this.booleanString = booleanString;
    }

    public Boolean getBooleanString() {
        return booleanString;
    }
    
    

    public int getCommandID() {
        return commandID;
    }

    public String getCommandString() {
        return commandString;
    }

    public Boolean getShouldDisconnect() {
        return shouldDisconnect;
    }

    public String getResultFilterString() {
        return resultFilterString;
    }
    
}
