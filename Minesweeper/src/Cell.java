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
