/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package byui.cit260.treeOfLife.control;

import byui.cit260.treeOfLife.model.Game;
import byui.cit260.treeOfLife.model.GameInventoryItems;
import byui.cit260.treeOfLife.model.Location;
import byui.cit260.treeOfLife.model.Map;
import byui.cit260.treeOfLife.control.MapControl.SceneType;
import byui.cit260.treeOfLife.model.Players;
import byui.cit260.treeOfLife.model.ProgressMeter;
import byui.cit260.treeOfLife.model.QuestionArray;
import byui.cit260.treeOfLife.model.Scene;
import byui.cit260.treeOfLife.view.ArmorShopMenuView;
import byui.cit260.treeOfLife.view.CharacterMenuView;
import citbyui.cit260.treeOfLife.exceptions.GameControlException;
import citbyui.cit260.treeOfLife.exceptions.MapControlException;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.util.ArrayList;
import treeoflife.TreeOfLife;

/**
 *
 * @author Chuck
 */
public class GameControl {

    public static void createNewGame(Players player) throws MapControlException {

        Game game = new Game();//create a new game
        TreeOfLife.setCurrentGame(game);//save the game in TreeOfLife

        game.setPlayer(player);//save player in game

        ArrayList<GameInventoryItems> forSale = GameControl.createInventoryList();//create items for sale in game
        game.setForSale(forSale);

        Map map = MapControl.createMap();//create the Map
        game.setMap(map);//save map in the game

        //create progress Meter
        ProgressMeter progress = new ProgressMeter();
        TreeOfLife.getCurrentGame().setProgressMeter(progress);
        
         
       
         //choose Character 
        CharacterMenuView character = new CharacterMenuView();
        character.display();
        System.out.println("Please select a character for your game.");
               
        //move characters to starting position in the map - moved to StartNewGame
        MapControl.moveCharactersToStartingLocation(map);

    }
    
 

    //@todo - instructions said to have this private, but it caused issues in Map.createMap()
    public static void assignScenesToLocations(Map map, Scene[] scenes) {
        //setting 3 locations to visited - these are accessible always 
        Location[][] locations = map.getLocations();
        locations[0][0].setScene(scenes[SceneType.armorShop.ordinal()]);
        locations[0][0].setVisited(true);
        locations[1][0].setScene(scenes[SceneType.temple.ordinal()]);
        locations[1][0].setVisited(true); 
        locations[1][0].setBlocked(false);
        locations[2][0].setScene(scenes[SceneType.mantle.ordinal()]);
        locations[2][0].setVisited(true); 
        locations[2][0].setBlocked(false);
        
        //start point
        locations[0][1].setScene(scenes[SceneType.cottage.ordinal()]);
        locations[0][1].setVisited(true); 
        
        //Levels - this is where the user will be able to answer Level Questions to gain faith, knowledge and obedience points
        locations[0][2].setScene(scenes[SceneType.levelOne.ordinal()]);
        locations[0][2].setVisited(true);
        locations[1][2].setScene(scenes[SceneType.levelTwo.ordinal()]);
            locations[1][2].setVisited(false);
            locations[1][2].setBlocked(true);
        locations[1][1].setScene(scenes[SceneType.levelThree.ordinal()]);
            locations[1][1].setVisited(false);
            locations[1][1].setBlocked(true);
        locations[2][1].setScene(scenes[SceneType.levelFour.ordinal()]);
            locations[2][1].setVisited(false);
            locations[2][1].setBlocked(true);
        locations[2][2].setScene(scenes[SceneType.levelFive.ordinal()]);       
            locations[2][2].setVisited(false);
            locations[2][2].setBlocked(true);
        //End Scenes - these are the scenes that the user will achieve based on their faith, knowledge and obedience points
        locations[0][3].setScene(scenes[SceneType.treeOfLife.ordinal()]);   
            locations[0][3].setVisited(false);
            locations[0][3].setBlocked(true);
        locations[1][3].setScene(scenes[SceneType.treeOfKnowledge.ordinal()]);
            locations[1][3].setVisited(false);
            locations[1][3].setBlocked(true);
        locations[2][3].setScene(scenes[SceneType.spaciousBuilding.ordinal()]);
            locations[2][3].setVisited(false);
            locations[2][3].setBlocked(true);
        
    }   

    public static ArrayList<GameInventoryItems> createInventoryList() {

        ArrayList<GameInventoryItems> forSaleList = new ArrayList<>();
        //add each armor piece to the forSaleList
        forSaleList.add(GameInventoryItems.Helmet);
        forSaleList.add(GameInventoryItems.ProtectiveShield);
        forSaleList.add(GameInventoryItems.TruthBelt);
        forSaleList.add(GameInventoryItems.Boots);
        forSaleList.add(GameInventoryItems.Sword);
        forSaleList.add(GameInventoryItems.IronBreastPlate);
 
        return forSaleList;

    }

    public static ArrayList<GameInventoryItems> getSortedPurchasedItems() {

        ArrayList<GameInventoryItems> originalPurchasedItemsList = new ArrayList<>();

        //get list of items purchased for current game
        originalPurchasedItemsList = TreeOfLife.getCurrentGame().getPurchasedItems();
        if (originalPurchasedItemsList.size() <= 1) { //if only 1 item in list return list
            return originalPurchasedItemsList;
        } else { //if more than one item in arrayList, sort alphabetically
            ArrayList<GameInventoryItems> purchasedItemsList = new ArrayList<>();
            // make a copy (clone) original
            purchasedItemsList = (ArrayList<GameInventoryItems>) originalPurchasedItemsList.clone();
            //use bubble sort to sort list of purchaseItems by name
            GameInventoryItems tempGameInventoryItem;
            //loop over every item in the list
            for (int i = 0; i < purchasedItemsList.size() - 1; i++) {

                for (int j = 0; j < purchasedItemsList.size() - 1 - i; j++) {
                    int k = j + 1;
//            for( GameInventoryItems index : purchasedItemsList ){

                    if (purchasedItemsList.get(j).compareTo(purchasedItemsList.get(k)) > 0) {

                        tempGameInventoryItem = purchasedItemsList.get(j);
                        purchasedItemsList.set(j, purchasedItemsList.get(k));
                        purchasedItemsList.set(k, tempGameInventoryItem);

                    }
                }
            }
            return purchasedItemsList;

        }
    }

    public static void displayCharacterInventory() throws GameControlException {
        ArrayList<GameInventoryItems> purchasedItemsList = new ArrayList<>();
//         purchasedItemsList = GameControl.getSortedPurchasedItems();
        purchasedItemsList = GameControl.getCharacterInventoryByHighestStats();
        if (purchasedItemsList.size() < 1) {
            throw new GameControlException("You have not earned any Armor Pieces yet");

        } else {
            for (GameInventoryItems armor : purchasedItemsList) {
                String armorPiece = armor.name();
                String armorDescr = armor.getItemDescription();
                int armorFaithPoints = armor.getFaithPoints();
                int armorObedPoints = armor.getObedPoints();
                int armorKnowPoints = armor.getKnowPoints();
                System.out.println("Armor Piece - " + armorPiece + "  or  " + armorDescr);
                System.out.println("\tFaith pts = " + armorFaithPoints);
                System.out.println("\tObedience pts = " + armorObedPoints);
                System.out.println("\tKnowledge pts =" + armorKnowPoints);
            }

        }

    }

    public static ArrayList<GameInventoryItems> getCharacterInventoryByHighestStats() throws GameControlException {
        //return items in CharacterInventory sorted by highest Faith points
        ArrayList<GameInventoryItems> originalPurchasedItemsList = new ArrayList<>();

        //get list of items purchased for current game
        originalPurchasedItemsList = TreeOfLife.getCurrentGame().getPurchasedItems();
      
        if (originalPurchasedItemsList == null) { //if only 1 item in list return list
            throw new GameControlException("You have not earned any Armor Pieces yet");
        } else { //if more than one item in arrayList, sort by lowest Faith points
            ArrayList<GameInventoryItems> purchasedItemsList = new ArrayList<>();
            // make a copy (clone) original
            purchasedItemsList = (ArrayList<GameInventoryItems>) originalPurchasedItemsList.clone();
            GameInventoryItems tempGameInventoryItem; //set temp variable for switching purposes
            for (int i = 0; i < purchasedItemsList.size() - 1; i++) {
                int index = i;
                for (int j = i + 1; j < purchasedItemsList.size(); j++) {
                    if (purchasedItemsList.get(j).getFaithPoints() < purchasedItemsList.get(index).getFaithPoints()) {
                        index = j;
                    }
                    tempGameInventoryItem = purchasedItemsList.get(j);
                    purchasedItemsList.set(j, purchasedItemsList.get(index));
                    purchasedItemsList.set(index, tempGameInventoryItem);
                }
            }
            return purchasedItemsList;
        }

    }

    public static GameInventoryItems[] sortFaithPoints() {
        //sorting faithPoints along with name
        GameInventoryItems[] inventoryItems = GameInventoryItems.values();
        for (int i = 0; i < inventoryItems.length; i++) { //sorting faithPoints
            System.out.println(inventoryItems[i].name()); //sorting name in GameInventoryItems enum
            System.out.println("Faith Points = " + inventoryItems[i].getFaithPoints() + "\n");  //Display both returned name with connected faithpoints.

        }
        ArmorShopMenuView armorMenu = new ArmorShopMenuView();
        armorMenu.display();
        return inventoryItems;
    }

    public static void saveGame(Game currentGame, String filePath) 
       throws GameControlException{
        
        try( FileOutputStream fops = new FileOutputStream(filePath)){
            ObjectOutputStream output = new ObjectOutputStream(fops);
            
            output.writeObject(currentGame); //write the game object out to file
//            output.writeObject(QuestionArray.getNumLevelQuestionsAnswered());
//            output.writeObject(QuestionArray.getNumSetsOfLevelQuestionsAsked());
        }
        catch(IOException e) {
            throw new GameControlException(e.getMessage());
        } 
    }

    public static void getExistingGame(String filePath) throws GameControlException{
        Game game = null;
        
        try( FileInputStream fips = new FileInputStream(filePath)){
            ObjectInputStream output = new ObjectInputStream(fips);
            
           game = (Game) output.readObject(); //read the game object out to file
        }
        catch(FileNotFoundException fnfe) {
            throw new GameControlException(fnfe.getMessage());
    }
        catch(Exception e) {
            throw new GameControlException(e.getMessage());
        }
        
        //close the output file
        TreeOfLife.setCurrentGame(game); // save in TreeOfLife
    }

   

    public void display() {
        GameControl.sortFaithPoints();//calling the sortFaithPoints() function

    }

}
