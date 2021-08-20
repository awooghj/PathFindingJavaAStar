import java.awt.*;

public class Node {
    private int xPos;
    private int yPos;
    private boolean start;
    private boolean target;
    private boolean wall;
    private Node parent;
    private int fx,fy;
    private boolean onPath;
    private Node left,right, up, down;
    private int distance;
    private int factor;
    private boolean used;

    public Node(int x, int y){
        start = false;
        target = false;
        wall = false;
        fx = x;
        fy = y;
    }

    public void setParent(Node n){
        parent = n;
    }
    public Node getParent(){
        return parent;
    }
    public Node setX(int x){
        xPos = x;
        return this;
    }

    public Node setY(int y){
        yPos = y;
        return this;
    }

    public int getFx(){
        return fx;
    }

    public int getFy(){
        return fy;
    }

    public boolean isTarget(){
        return target;
    }
    public boolean isStart(){
        return start;
    }
    public boolean isWall(){
        return wall;
    }

    public void render(Graphics2D g){
        g.setColor(Color.BLACK);
        g.drawRect(xPos,yPos,50,50);
        if(onPath) g.setColor(Color.MAGENTA);
        else if(used) g.setColor(Color.BLUE);
        else if (start) g.setColor(Color.GREEN);
        else if(target) g.setColor(Color.ORANGE);
        else if(wall) g.setColor(Color.RED);
        else g.setColor(Color.LIGHT_GRAY);
        g.fillRect(xPos+1,yPos+1,49,49);

    }

    public void used(){
        used = true;
    }

    public void clear(){
        start = false;
        target = false;
        wall = false;
    }

    public void clicked(int c){
        System.out.println("called");
        if (start||target||wall){
            clear();
            return;
        }
        if(c == 1) wall = true;
        if(c == 2) start = true;
        if(c == 3) target = true;
    }

    public void setDirection(Node l, Node r, Node u, Node d){
        left = l;
        right = r;
        up = u;
        down = d;
    }

    public Node getLeft(){
        return left;
    }

    public Node getRight(){
        return right;
    }

    public Node getUp(){
        return up;
    }
    public Node getDown(){
        return down;
    }

    public Node traceBack(){
        onPath = true;
        return parent;
    }

    public void calcDistance(Node target){
        distance = Math.abs(fx - target.fx) + Math.abs(fy - target.fy);
    }

    public int getFactor(){
        return factor;
    }

    public void setFactor(int x){
        factor = x;
    }
}
