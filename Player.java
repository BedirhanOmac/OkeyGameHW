
public class Player {
    String playerName;
    Tile[] playerTiles;
    int numberOfTiles;

    public Player(String name) {
        setName(name);
        playerTiles = new Tile[15]; // there are at most 15 tiles a player owns at any time
        numberOfTiles = 0; // currently this player owns 0 tiles, will pick tiles at the beggining of the
                           // game
    }

    /*
     * This method calculates the longest chain per tile to be used when checking
     * the win condition
     */
    public int[] calculateLongestChainPerTile() {
        // keep a seperate copy of the tiles since findLongestChainOf sorts them
        Tile[] tilesCopy = new Tile[numberOfTiles];
        for (int i = 0; i < numberOfTiles; i++) {
            tilesCopy[i] = playerTiles[i];
        }

        // make the calculations
        int[] chainLengths = new int[numberOfTiles];
        for (int i = 0; i < numberOfTiles; i++) {
            chainLengths[i] = findLongestChainOf(tilesCopy[i]);
        }

        // revert the playerTiles to its original form
        for (int i = 0; i < numberOfTiles; i++) {
            playerTiles[i] = tilesCopy[i];
        }

        return chainLengths;
    }

    /*
     * DONE: finds and returns the longest chain of tiles that can be formed
     * using the given tile. a chain of tiles is either consecutive numbers
     * that have the same color or the same number with different colors
     * some chain examples are as follows:
     * 1B 2B 3B
     * 5Y 5B 5R 5K
     * 4Y 5Y 6Y 7Y 8Y
     * You can use canFormChainWith method in Tile class to check if two tiles can
     * make a chain
     * based on color order and value order. Use sortTilesColorFirst() and
     * sortTilesValueFirst()
     * methods to sort the tiles of this player then find the position of the given
     * tile t.
     * check how many adjacent tiles there are starting from the tile poisition.
     * Note that if you start a chain with matching colors it should continue with
     * the same type of match
     * and if you start a chain with matching values it should continue with the
     * same type of match
     * use the different values canFormChainWith method returns.
     */

    public int findLongestChainOf(Tile t) {
        int tilePosition;
        int longestChainColorFirst = 0;

        // DONE: find the longest chain starting from tilePosition going left and right
        sortTilesColorFirst();
        tilePosition = findPositionOfTile(t);
        int m = tilePosition;
        int n = tilePosition;

        if (tilePosition >= 1) {
            while (m >= 1 && playerTiles[m].canFormChainWith(playerTiles[m - 1]) == 1) {
                longestChainColorFirst++;
                m--;
            }
        }
        if (tilePosition < playerTiles.length - 1) {
            while (n < playerTiles.length - 1 && playerTiles[n].canFormChainWith(playerTiles[n + 1]) == 1) {
                longestChainColorFirst++;
                n++;
            }
        }

        longestChainColorFirst += 1;

        // DONE: find the longest chain starting from tilePosition going left and right
        sortTilesValueFirst();
        tilePosition = findPositionOfTile(t);
        int y = tilePosition;
        int z = tilePosition;
        int longestChainValueFirst = 0;
        if (tilePosition >= 1) {
            while (y >= 1 && playerTiles[y].canFormChainWith(playerTiles[y - 1]) == 2) {
                longestChainValueFirst++;
                y--;
            }
        }
        if (tilePosition < playerTiles.length - 1) {
            while (z < playerTiles.length - 1 && playerTiles[z].canFormChainWith(playerTiles[z + 1]) == 2) {
                longestChainValueFirst++;
                z++;
            }
        }
        longestChainValueFirst += 1;

        if (longestChainColorFirst > longestChainValueFirst) {
            return longestChainColorFirst;
        } else {
            return longestChainValueFirst;
        }
    }

    /*
     * DONE: adds the given tile at the end of playerTiles array, should also
     * update numberOfTiles accordingly. Make sure the player does not try to
     * have more than 15 tiles at a time
     */
    public void addTile(Tile t) {
        if (playerTiles[14] == null) {
            playerTiles[playerTiles.length - 1] = t;
            numberOfTiles = 15;
        }

        else {
            System.out.println("You cannot have more than 15 tiles");
        }

    }

    /*
     * DONE: uses bubble sort to sort playerTiles in increasing color and value
     * value order: 1 < 2 < ... < 12 < 13
     * color order: Y < B < R < K
     * color is more important in this ordering, a sorted example:
     * 3Y 3Y 6Y 7Y 1B 2B 3B 3B 10R 11R 12R 2K 4K 5K
     * you can use compareToColorFirst method in Tile class for comparing
     * you are allowed to use Collections.sort method
     */

    public void sortTilesColorFirst() {
        int n = playerTiles.length;
        for (int i = 0; i < n - 1; i++) {
            for (int m = 0; m < n - i - 1; m++) {
                if (playerTiles[m + 1] != null && playerTiles[m].compareToColorFirst(playerTiles[m + 1]) == 1) {
                    Tile temp = playerTiles[m + 1];
                    playerTiles[m + 1] = playerTiles[m];
                    playerTiles[m] = temp;
                }
            }
        }
    }

    /*
     * DONE: uses bubble sort to sort playerTiles in increasing value and color
     * value order: 1 < 2 < ... < 12 < 13
     * color order: Y < B < R < K
     * value is more important in this ordering, a sorted example:
     * 1B 2B 2K 3Y 3Y 3B 3B 4K 5K 6Y 7Y 10R 11R 12R
     * you can use compareToValueFirst method in Tile class for comparing
     * you are allowed to use Collections.sort method
     */

    public void sortTilesValueFirst() {

        int n = playerTiles.length;
        for (int i = 0; i < n - 1; i++) {
            for (int m = 0; m < n - i - 1; m++) {
                if (playerTiles[m + 1] != null && playerTiles[m].compareToValueFirst(playerTiles[m + 1]) == 1) {
                    Tile temp = playerTiles[m + 1];
                    playerTiles[m + 1] = playerTiles[m];
                    playerTiles[m] = temp;
                }
            }
        }
    }

    public int findPositionOfTile(Tile t) {
        int tilePosition = -1;
        for (int i = 0; i < numberOfTiles; i++) {
            if (playerTiles[i].matchingTiles(t)) {
                tilePosition = i;
            }
        }
        return tilePosition;
    }

    public void displayTiles() {
        System.out.println(playerName + "'s Tiles:");
        for (int i = 0; i < numberOfTiles; i++) {
            if (playerTiles[i] != null) {
                System.out.print(playerTiles[i].toString() + " ");
            }
        }
        System.out.println();
    }

    public Tile[] getTiles() {
        return playerTiles;
    }

    public void setName(String name) {
        playerName = name;
    }

    public String getName() {
        return playerName;
    }
}
