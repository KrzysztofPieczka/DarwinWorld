package agh.ics.oop.model;

import agh.ics.oop.model.util.Boundary;

public class Water implements WorldElement {

    private final Vector2d waterSource;
    private Boundary waterBounds;
    private boolean flow = true; // przyplyw lub odplyw
    private int flowCnt = 1;

    public Water(Vector2d waterSource) {
        this.waterSource = waterSource;
        this.waterBounds = new Boundary(waterSource, waterSource);
    }

    @Override
    public Vector2d getPosition() {
        return waterSource;
    }

    @Override
    public boolean isAt(Vector2d position) {
        return position.precedes(waterBounds.upperRight()) && position.follows(waterBounds.lowerLeft());
    }

    public Boundary getWaterBounds() {
        return waterBounds;
    }

    public void ebbOrFlow(int range){
        if(flow)
        {
            flow();
            flowCnt += 1;
            if(flowCnt==range){
                flow = false;
            }
        }
        else{
            ebb();
            flowCnt -= 1;
            if(flowCnt == 1){
                flow = true;
            }
        }

    }

    private void flow(){
        Vector2d newUpperRight = waterBounds.upperRight().add(new Vector2d(1, 1));
        Vector2d newLowerLeft = waterBounds.lowerLeft().add(new Vector2d(-1, -1));
        waterBounds = new Boundary(newLowerLeft, newUpperRight);
    }

    private void ebb(){
        Vector2d newUpperRight = waterBounds.upperRight().add(new Vector2d(-1, -1));
        Vector2d newLowerLeft = waterBounds.lowerLeft().add(new Vector2d(1, 1));
        waterBounds = new Boundary(newLowerLeft, newUpperRight);
    }

    @Override
    public String toString(){
        return "~";
    }

}
