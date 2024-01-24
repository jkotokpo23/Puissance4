package composants;

public class Pion{

    private int x;
    private int y;
    private char type;

    public Pion(int x, int y, char type) {
        this.x = x;
        this.y = y;
        this.type = type;
    }
    public int getX() {
        return x;
    }
    public void setX(int x) {
        this.x = x;
    }
    
    public int getY() {
        return y;
    }
    public void setY(int y) {
        this.y = y;
    }
    
    public void update(int x, int y){
        this.x = x;
        this.y = y;
    }

    public char getCouleur() {
        return type;
    }
    public void setCouleur(char type) {
        this.type = type;
    }

    @Override
    public String toString(){
        return "X : " + getX() + ", Y : " + getY();
    }
    boolean equals(Pion pion2){
        return (pion2.x == getX() && pion2.y == getY() && getCouleur() == pion2.type);
    }
}