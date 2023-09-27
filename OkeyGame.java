package OKEYGAMEHW;

import java.sql.Array;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Random;

public class OkeyGame {

    Player[] players;
    Tile[] tiles;

    Tile lastDiscardedTile;

    int currentPlayerIndex = 0;

    public OkeyGame() {
        players = new Player[4];
    }

    public void createTiles() {
        tiles = new Tile[104];
        int currentTile = 0;

        // two copies of each color-value combination, no jokers
        for (int i = 1; i <= 13; i++) {
            for (int j = 0; j < 2; j++) {
                tiles[currentTile++] = new Tile(i,'Y');
                tiles[currentTile++] = new Tile(i,'B');
                tiles[currentTile++] = new Tile(i,'R');
                tiles[currentTile++] = new Tile(i,'K');
            }
        }
    }

    /*
     * TODO: distributes the starting tiles to the players
     * player at index 0 gets 15 tiles and starts first
     * other players get 14 tiles
     * this method assumes the tiles are already sorted
     */
    public void distributeTilesToPlayers() {
        Tile[] remainingTiles = new Tile[104];
        for(int i = 0; i < 104; i++){
            remainingTiles[i] = tiles[i];
        }

        players[0] = new Player("Human");
        players[1] = new Player("Bot1");
        players[2] = new Player("Bot2");
        players[3] = new Player("Bot3");

        Random rand = new Random();
        
        for(int i = 0; i <= 3; i++){

            int indexOfRand = rand.nextInt(104);
            while(remainingTiles[indexOfRand].equals(null)){
                indexOfRand = rand.nextInt(104);
            }
            if( i != 0){
                for(int m = 0; m < 14; m++){
                    players[i].playerTiles[m] = remainingTiles[indexOfRand];
                    remainingTiles[indexOfRand] = null;
                }
            }
            else if( i == 0){
                int k = 0;
                while( k < 15 ){
                players[i].playerTiles[k] = remainingTiles[indexOfRand];
                remainingTiles[indexOfRand] = null;
                }
            }

        }

    }

    /*
     * TODO: get the last discarded tile for the current player
     * (this simulates picking up the tile discarded by the previous player)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getLastDiscardedTile() {
        
        
        players[currentPlayerIndex].addTile(lastDiscardedTile);
        return lastDiscardedTile + "";
    }

    /*
     * TODO: get the top tile from tiles array for the current player
     * that tile is no longer in the tiles array (this simulates picking up the top tile)
     * it should return the toString method of the tile so that we can print what we picked
     */
    public String getTopTile() {
        Tile topTile = tiles[tiles.length-1];
        // or this
        // Tile topTile = tiles[0];
        
        return String.valueOf(topTile.getValue()) + topTile.getColor();
    }

    /*
     * TODO: should randomly shuffle the tiles array before game starts ---DONE---
     */
    public void shuffleTiles() {

        Random rand = new Random();
        for(int i = 0; i < tiles.length; i++) {
            int randomIndexToSwap = rand.nextInt(tiles.length);
            Tile temp = tiles[randomIndexToSwap];
            tiles[randomIndexToSwap] = tiles[i];
            tiles[i] = temp;
        }

        /*Alternative solution
         * 
         * List<Tile> tileList = Arrays.asList(tiles);
            Collections.shuffle(tileList);
            tileList.toArray();
         */

    }

    /*
     * TODO: check if game still continues, should return true if current player/ staging issues
     * finished the game. Use calculateLongestChainPerTile method to get the
     * longest chains per tile.
     * To win, you need one of the following cases to be true:
     * - 8 tiles have length >= 4 and remaining six tiles have length >= 3 the last one can be of any length
     * - 5 tiles have length >= 5 and remaining nine tiles have length >= 3 the last one can be of any length
     * These are assuming we check for the win condition before discarding a tile
     * The given cases do not cover all the winning hands based on the original
     * game and for some rare cases it may be erroneous but it will be enough
     * for this simplified version
     */
    public boolean didGameFinish() {
        int[] a1 = this.players[currentPlayerIndex].calculateLongestChainPerTile();
        int count4OrMore = 0;
        int count5OrMore = 0;
        int count3OrMore = 0;
        for (int i = 0;i<14;i++) {
            if (a1 [i]>= 5) {
                count5OrMore ++;
            }
        }
         for (int i = 0;i<14;i++) {
            if (a1 [i]>= 3) {
                count3OrMore ++;
            }
        }
         for (int i = 0;i<14;i++) {
            if (a1 [i]>= 4) {
                count4OrMore ++;
            }
        }
        if (count5OrMore == 5 && count3OrMore == 14) {
            return true;
        }
        if (count4OrMore == 8 && count3OrMore == 14) {
            return true;
        }

        return false;
        
        
    }

    /*
     * TODO: Pick a tile for the current computer player using one of the following:
     * - picking from the tiles array using getTopTile()
     * - picking from the lastDiscardedTile using getLastDiscardedTile()
     * You may choose randomly or consider if the discarded tile is useful for
     * the current status. Print whether computer picks from tiles or discarded ones.
     */
    public void pickTileForComputer() {
        double choiceMaker = Math.floor(Math.random()*2);
        if (choiceMaker==1) {
            getTopTile();
        }
        else {
            getLastDiscardedTile();
        }
    }

    /*
     * TODO: Current computer player will discard the least useful tile.
     * For this use the findLongestChainOf method in Player class to calculate
     * the longest chain length per tile of this player,
     * then choose the tile with the lowest chain length and discard it
     * this method should print what tile is discarded since it should be
     * known by other players
     */
    public void discardTileForComputer() {
        
        Tile[] arr = this.players[currentPlayerIndex].playerTiles;
        int[] chainsOfTiles = new int[arr.length];
        for ( int i = 0 ; i < arr.length; i ++) {
            chainsOfTiles[i] = this.players[currentPlayerIndex].findLongestChainOf(arr[i]);
        }
        int lowestChainTile = chainsOfTiles[0];
        for (int i = 1; i < chainsOfTiles.length; i++ ) {
            if (chainsOfTiles[i] > lowestChainTile) {
                lowestChainTile = chainsOfTiles[i];
            }
        }
        Tile discardedTile = chainsOfTiles[lowestChainTile];
        discardTile(lowestChainTile);
        discardedTile.toString();
    }

    /*
     * TODO: discards the current player's tile at given index
     * this should set lastDiscardedTile variable and remove that tile from
     * that player's tiles
     */
    public void discardTile(int tileIndex) {
        Tile[] tiles = this.players[currentPlayerIndex].playerTiles;
        Tile[] newArray = new Tile[tiles.length - 1];

        //remove the tile from he array
        for (int i = 0; i < tiles.length; i ++) {
            if (i != tileIndex) {
                if (i < tileIndex){
                    newArray[i] = tiles[i];
                }
                else {
                    newArray[i - 1] = tiles[i];
                }
            }
        } 
        lastDiscardedTile = tiles[tileIndex];
        tiles = newArray;
        
    }

    public void currentPlayerSortTilesColorFirst() {
        players[currentPlayerIndex].sortTilesColorFirst();
    }

    public void currentPlayerSortTilesValueFirst() {
        players[currentPlayerIndex].sortTilesValueFirst();
    }

    public void displayDiscardInformation() {
        if(lastDiscardedTile != null) {
            System.out.println("Last Discarded: " + lastDiscardedTile.toString());
        }
    }

    public void displayCurrentPlayersTiles() {
        players[currentPlayerIndex].displayTiles();
    }

    public int getCurrentPlayerIndex() {
        return currentPlayerIndex;
    }

      public String getCurrentPlayerName() {
        return players[currentPlayerIndex].getName();
    }

    public void passTurnToNextPlayer() {
        currentPlayerIndex = (currentPlayerIndex + 1) % 4;
    }

    public void setPlayerName(int index, String name) {
        if(index >= 0 && index <= 3) {
            players[index] = new Player(name);
        }
    }

}




// trialllll