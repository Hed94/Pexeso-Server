/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testserver;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Random;
import hra.Server;

/**
 *
 * @author Hed94
 */
public class InstanceHry 
{

    private String hrac1;
    private String hrac2;
    private String aktualniHrac;
    private int tah;
    private int body1;
    private int body2;
    private Lobby lobby;
    private Policko prvni;
    private Policko druhe;

    
    ArrayList<Policko> policka = new ArrayList<Policko>();
    
    public InstanceHry(String hrac1,String hrac2,Lobby lobby)
    {
        this.lobby = lobby;
        this.hrac1 = hrac1;
        this.hrac2 = hrac2;
        this.aktualniHrac = hrac1;
        Server.LOGGER.info("Hraje "+hrac1+" proti "+hrac2);
        Server.LOGGER.info("Zacina "+aktualniHrac);
        this.tah = 0;
        // Vytvoření hracího plánu
        Server.LOGGER.info("Server vytváří herní plán");
        for(int i = 1; i<=12;i++)
        {
            int pomocneCislo=i;
            if(pomocneCislo>6)
            {
                pomocneCislo=pomocneCislo-6;
            }
            Policko pomocne = new Policko(i,pomocneCislo);
            policka.add(pomocne);
        }
        // Zamýchání obrázků u políček aby každá hra byla jiná
        Server.LOGGER.info("Server míchá políčka");
        for(int i = 0;i<100;i++)
        {
            Random rn = new Random();

            int random1 = rn.nextInt(11 - 0 + 1) + 0;
            int random2 = rn.nextInt(11 - 0 + 1) + 0;
            int pomocneCislo = 1;

            Policko pomocne = policka.get(random1);
            Policko pomocne2 = policka.get(random2);

            pomocneCislo = pomocne2.getObrazek();
            pomocne2.setObrazek(pomocne.getObrazek());
            pomocne.setObrazek(pomocneCislo);
        }

    }
    
    public void tahni(hra.Message zprava) throws IOException, InterruptedException
    {
        Server.LOGGER.info(zprava.getHrac()+" klepl na políčko a chce hrát");
        if(zprava.getHrac().equals(aktualniHrac))
        {
            int pozice = Integer.parseInt(zprava.getHodnota());
            Policko pomocne = new Policko(100,100);
             for(int i = 0; i<=11;i++)
            {
                if(policka.get(i).getPozice() == pozice)
                pomocne = policka.get(i);
            }
            if(pomocne.isOtoceno())
            {
            Server.LOGGER.info(zprava.getHrac()+" klepl na již otočené políčko");
            lobby.vysilac.PosliChybu(zprava.getHrac(), "error", "Toto pole už je otočené!");    
            }
            else
            {
                tah++; 
                if(tah==2)
                {
                    lobby.vysilac.PosliZpravu(String.valueOf(pomocne.getObrazek()), "otoc", String.valueOf(pomocne.getPozice()));
                    this.druhe=pomocne;
                    if(this.prvni.getObrazek() == this.druhe.getObrazek())
                    {
                            if(zprava.getHrac().equals(this.hrac1))
                            {
                                body1++;
                                lobby.vysilac.PosliZpravu(hrac1, "body", String.valueOf(body1));
                                if(konecHry())
                                {
                                    if(body1 >= body2)
                                    {
                                      lobby.vysilac.PosliZpravu(hrac1, "konec", "");
                                      Server.LOGGER.info("Nastal konec hry - vyhrál hráč "+hrac1);
                                      lobby.konec=true;
                                    }
                                    else
                                    {
                                      lobby.vysilac.PosliZpravu(hrac2, "konec", "");
                                      Server.LOGGER.info("Nastal konec hry - vyhrál hráč "+hrac2);
                                      lobby.konec=true;
                                    }
                                }
                            }
                            if(zprava.getHrac().equals(this.hrac2))
                            {
                                body2++;
                                lobby.vysilac.PosliZpravu(hrac2, "body", String.valueOf(body2));
                                if(konecHry())
                                {
                                    if(body2 >= body1)
                                    {
                                      lobby.vysilac.PosliZpravu(hrac2, "konec", "");
                                      Server.LOGGER.info("Nastal konec hry - vyhrál hráč "+hrac2);
                                      lobby.konec=true;
                                    }
                                    else
                                    {
                                     lobby.vysilac.PosliZpravu(hrac1, "konec", "");  
                                     Server.LOGGER.info("Nastal konec hry - vyhrál hráč "+hrac1);
                                     lobby.konec=true;
                                    }
                                }
                            }
                 }
                 else
                 {
                  Server.LOGGER.info("Byla otočena špatná políčka, nyní se otočí zpět");
                  Thread.sleep(3000);
                  lobby.vysilac.PosliZpravu("xx", "otoc", String.valueOf(prvni.getPozice()));
                  prvni.setOtoceno(false);
                  lobby.vysilac.PosliZpravu("xx", "otoc", String.valueOf(druhe.getPozice()));
                  druhe.setOtoceno(false);
                  if(zprava.getHrac().equals(this.hrac1))
                     {
                         aktualniHrac = hrac2;
                         lobby.vysilac.PosliZpravu(aktualniHrac, "zmenaTahu", aktualniHrac);
                     }
                     else
                     {
                         aktualniHrac = hrac1;
                         lobby.vysilac.PosliZpravu(aktualniHrac, "zmenaTahu", aktualniHrac);
                     }
                 }
                 tah = 0;
                }
                else
                {
                this.prvni=pomocne;
                lobby.vysilac.PosliZpravu(String.valueOf(pomocne.getObrazek()), "otoc", String.valueOf(pomocne.getPozice()));    // obrazek / prikaz / co se ma otočit
                pomocne.setOtoceno(true);
                }
            }
        }
        else
        {
         Server.LOGGER.info(zprava.getHrac()+" není na tahu, ale zkouší hrát");
         lobby.vysilac.PosliChybu(zprava.getHrac(), "error", "Nejsi na tahu!");   
        }    
    }
    
    public boolean konecHry()
    {
        int pomocna = 0;
        for(int i = 0; i<=11;i++)
        {
            if(policka.get(i).isOtoceno())
            {
                pomocna++;
            }
        }
        System.out.println(pomocna);
        
        if(pomocna == 6)
        {
            Server.LOGGER.info("Konec hry");
            return true;
        }
        else
        {
            Server.LOGGER.info("Hra pokračuje - všechna políčka nejsou otočená");
            return false;
        }
    }

    public Policko getPrvni() {
        return prvni;
    }

    public void setPrvni(Policko prvni) {
        this.prvni = prvni;
    }

    public Policko getDruhe() {
        return druhe;
    }

    public void setDruhe(Policko druhe) {
        this.druhe = druhe;
    }
    
    

    public String getHrac1() {
        return hrac1;
    }

    public void setHrac1(String hrac1) {
        this.hrac1 = hrac1;
    }

    public String getHrac2() {
        return hrac2;
    }

    public void setHrac2(String hrac2) {
        this.hrac2 = hrac2;
    }

    public String getAktualniHrac() {
        return aktualniHrac;
    }

    public void setAktualniHrac(String aktualniHrac) {
        this.aktualniHrac = aktualniHrac;
    }

    public int getTah() {
        return tah;
    }

    public void setTah(int tah) {
        this.tah = tah;
    }

    public int getBody1() {
        return body1;
    }

    public void setBody1(int body1) {
        this.body1 = body1;
    }

    public int getBody2() {
        return body2;
    }

    public void setBody2(int body2) {
        this.body2 = body2;
    }

    public ArrayList<Policko> getPolicka() {
        return policka;
    }

    public void setPolicka(ArrayList<Policko> policka) {
        this.policka = policka;
    }
    
    
}
