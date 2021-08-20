import javax.swing.*;
import java.awt.*;
import java.awt.event.ActionEvent;
import java.awt.event.ActionListener;
import java.awt.event.MouseEvent;
import java.awt.event.MouseListener;
import java.awt.image.BufferStrategy;
import java.util.ArrayList;

public class Main extends Canvas implements Runnable, MouseListener {

    private Node start;
    private Node target;
    private Node[][] nodeList;
    private boolean calced;
    private static Main running;


    public static void main(String[] args) {
        JFrame frame = new JFrame("Pathfinding");
        frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        frame.setSize(600,625);
        frame.setResizable(false);
        Main m = new Main();
        frame.setLayout(null);
        m.setBounds(0,25,600,600);
        frame.add(m);
        setUpMenu(frame);
        frame.setVisible(true);
        running = m;
        m.start();
    }



    public static void setUpMenu(JFrame frame){
        JMenuBar bar = new JMenuBar();
        bar.setBounds(0,0,600,25);
        frame.add(bar);
        JMenu file = new JMenu("File");
        bar.add(file);
        JMenu options = new JMenu("Options");
        bar.add(options);
        JMenuItem exit = new JMenuItem("Exit");
        JMenuItem newGrid = new JMenuItem("New Grid");
        JMenuItem calcPath = new JMenuItem("Calculate Path");


        exit.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                System.exit(0);

            }
        });
        newGrid.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                running.resetNodes();
                running.calced = false;
            }
        });
        calcPath.addActionListener(new ActionListener() {

            public void actionPerformed(ActionEvent e) {
                if(running.target == null || running.start == null){
                    return;
                }
                running.calced = true;
                running.calculatePath();
            }
        });

        file.add(exit);
        options.add(newGrid);
        options.add(calcPath);


    }



    public void run(){
        init();
        while (true){
            BufferStrategy bs = getBufferStrategy();
            if(bs == null){
                createBufferStrategy(2);
                continue;
            }

            Graphics2D g = (Graphics2D) bs.getDrawGraphics();
            render(g);
            bs.show();
            try{
                Thread.sleep(1);
            } catch (Exception e){
                e.printStackTrace();
            }
        }
    }

    public void init(){
        requestFocus();
        calced = false;
        nodeList = new Node[10][10];
//        for (int i = 0; i<nodeList.length; i++){
//            for (int j = 0; j<nodeList[i].length; j++){
//                nodeList[i][j] = new Node(i,j).setX(30+i*50).setY(30+j*50);
//            }
//        }
        resetNodes();
        addMouseListener(this);
    }

    public void resetNodes(){
        target = null;
        start = null;
        for (int i = 0; i<nodeList.length; i++){
            for (int j = 0; j<nodeList[i].length; j++){
                nodeList[i][j] = new Node(i,j).setX(30 + i*50).setY(30 + j*50);
            }
        }
        for (int i = 0; i<nodeList.length; i++){
            for (int j = 0; j<nodeList[i].length; j++){
                int u = j-1;
                int d = j+1;
                int l = i-1;
                int r = i+1;
                Node up = null, down = null, left = null, right = null;
                if(u >= 0 && u<nodeList[i].length){
                    up = nodeList[i][u];
                }
                if(d >= 0 && d<nodeList[i].length){
                    down = nodeList[i][d];
                }
                if(l >= 0 && l<nodeList.length){
                    left = nodeList[l][j];
                }
                if(r >= 0 && r<nodeList.length){
                    right = nodeList[r][j];
                }
                nodeList[i][j].setDirection(left,right,up,down);
            }
        }
    }

    public void mousePressed(MouseEvent e){
        if(calced) return;
        int c = e.getButton();
        Node n = getNodeAt(e.getX(), e.getY());
        if(n!=null) n.clicked(c);
        if(n.isTarget()){
            if(target != null){
                target.clear();
            }
            target = n;
        }
        if(n.isStart()){
            if(start != null){
                start.clear();
            }
            start = n;
        }
    }

    public void mouseReleased(MouseEvent e){

    }
    public void mouseClicked(MouseEvent e){


    }
    public Node getNodeAt(int x, int y){
        x -= 30;
        y -= 30;
        x /= 50;
        y /= 50;
        System.out.println(x+":"+y);
        if(x >= 0 && y >= 0 && x<nodeList.length && y<nodeList[x].length) return nodeList[x][y];
        return  null;
    }

    public void mouseEntered(MouseEvent e){

    }

    @Override
    public void mouseExited(MouseEvent e){

    }
    public void render(Graphics2D g){
        g.setColor(Color.WHITE);
        g.fillRect(0,0,800,600);
        for (int i = 0; i<nodeList.length; i++){
            for (int j = 0; j<nodeList[i].length; j++){
                nodeList[i][j].render(g);
            }
        }
    }

    public void start(){
        new Thread(this).start();
    }

    private void calculatePath() {
        boolean calculated = false;
        for (int i = 0; i<nodeList.length; i++){
            for (int j = 0; j<nodeList[i].length; j++){
                nodeList[i][j].calcDistance(target);
            }
        }

        ArrayList<Node> closed = new ArrayList<Node>();
        ArrayList<Node> open = new ArrayList<Node>();
        open.add(start);
        while (!calculated){
            for(int i = 0; i<open.size(); i++){
                if(calculated){
                    break;
                }
                Node s =open.get(i);
                Node up = s.getUp();
                Node down = s.getDown();
                Node right = s.getRight();
                Node left = s.getLeft();

                if(up != null && up.isTarget()){
                    calculated = true;
                }
                else if(down != null && down.isTarget()){
                    calculated = true;
                }
                else if(left != null && left.isTarget()){
                    calculated = true;
                }
                else if(right != null && right.isTarget()){
                    calculated = true;
                }
                else{
                    if(up != null && !closed.contains(up) && !up.isWall()){
                        up.setParent(s);
                        open.add(up);
                        up.used();
                    }
                    if(down != null && !closed.contains(down)  && !down.isWall()){
                        down.setParent(s);
                        open.add(down);
                        down.used();
                    }
                    if(right != null && !closed.contains(right)  && !right.isWall()){
                        right.setParent(s);
                        open.add(right);
                        right.used();
                    }
                    if(left != null && !closed.contains(left)  && !left.isWall()){
                        left.setParent(s);
                        open.add(left);
                        left.used();
                    }
                    closed.add(s);
                }


                if(calculated){
                    Node n = s.traceBack();
                    while(n!=null){
                        n = n.traceBack();
                    }
                }

            }
        }
    }
}
