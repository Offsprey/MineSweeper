import java.util.ArrayList;

/*
Shane Neal : SpreSoft
 */


/**
 *
 * @author Spre
 */
public class Cell {
    
    private int[] cellId;
    private boolean mine;
    private boolean flagged;    
    private boolean exposed;
    private int prox;

    
    public Cell(int[] cellId) {
        this.cellId = cellId;
        
        this.mine = false;        
        this.flagged = false;
        this.exposed = false;
    }
    
    public Cell(int[] cellId, boolean isMine) {
        this.cellId = cellId;
        this.mine = isMine;
        
        flagged = false;
        exposed = false;
    }    
    

    public int[] getCellId() {
        return cellId;
    }

    public void setCellId(int[] cellId) {
        this.cellId = cellId;
    }

    public int getProx() {
        //if (this.exposed)
            return prox;
        //else
        //    return -1;
    }

    public void setProx(int prox) {
        this.prox = prox;
    }
    
    public static ArrayList<int[]> getProxCoords(int x, int y)
    {
    	    	
    	//build neighbors
        int[] n1 = new int[]{x-1,y-1};
        int[] n2 = new int[]{x,y-1};
        int[] n3 = new int[]{x+1,y-1};
        int[] n4 = new int[]{x+1,y};
        int[] n5 = new int[]{x+1,y+1};
        int[] n6 = new int[]{x,y+1};
        int[] n7 = new int[]{x-1,y+1};
        int[] n8 = new int[]{x-1,y};
        
        ArrayList<int[]> nList = new ArrayList<>();
        
        nList.add(n1);
        nList.add(n2);
        nList.add(n3);
        nList.add(n4);
        nList.add(n5);
        nList.add(n6);
        nList.add(n7);
        nList.add(n8);
        
        return nList;
    }
    
    public boolean isMine() {
        return mine;
    }

    public void setMine(boolean isMine) {
        this.mine = isMine;
    }

    public boolean isFlagged() {
        return flagged;
    }

    public void setFlagged(boolean flagged) {
        this.flagged = flagged;
    }
    
    public void toggleFlagged() {
        if (this.flagged)
            this.flagged = false;
        else
            this.flagged = true;
    }

    public boolean isExposed() {
        return exposed;
    }

    public void setExposed(boolean exposed) {
        this.exposed = exposed;
    }
    
}
