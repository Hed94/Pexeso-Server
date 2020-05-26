/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testserver;


public class Policko 
{
    private int pozice;
    private int obrazek;
    private boolean otoceno;

    public Policko(int pozice,int obrazek) {
        this.pozice = pozice;
        this.otoceno = false;
        this.obrazek = obrazek;
    }

    public int getObrazek() {
        return obrazek;
    }

    public void setObrazek(int obrazek) {
        this.obrazek = obrazek;
    }
    
    public int getPozice() {
        return pozice;
    }

    public void setPozice(int pozice) {
        this.pozice = pozice;
    }

    public boolean isOtoceno() {
        return otoceno;
    }

    public void setOtoceno(boolean otoceno) {
        this.otoceno = otoceno;
    }
    
    
}
