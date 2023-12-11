/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package GunGame;

/**
 *
 * @author lenovo
 */
public class BulletModel {

    public int x;
    public int y;
    public int xdirection, ydirection; 
    public BulletModel(int x, int y , int xdirection, int ydirection) {
        this.x = x;
        this.y = y;
        this.xdirection = xdirection;
        this.ydirection = ydirection;
    }

}
