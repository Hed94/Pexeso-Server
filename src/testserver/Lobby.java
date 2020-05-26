/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package testserver;

import java.net.Socket;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import hra.Server;

public class Lobby extends Thread
{  
    public List<String> nicky = Collections.synchronizedList( new ArrayList<String>());
    public Prijmac prijmac1;
    public Prijmac prijmac2;
    public Vysilac vysilac;
    public InstanceHry instance;
    public boolean konec = false;

    public Lobby(Socket socket1, Socket socket2) throws IOException 
    {
            vysilac = new Vysilac(socket1,socket2,this);
            prijmac1 = new Prijmac(socket1,this);
            prijmac2 = new Prijmac(socket2,this);
            Server.LOGGER.info("Server vytvořil lobby pro "+socket1+"a"+socket2);
    }

    public void run()
   {
        try 
        {
            prijmac1.start();
            prijmac2.start();
            Server.LOGGER.info("Server vytvořil hráčům příjmače pro komunikaci.");
            while(nicky.size() < 2)
            {
             Server.LOGGER.info("Server čeká na nicky hráčů");   
             Thread.sleep(2000);
            }
            if(prijmac1.getNick().equals(nicky.get(1)))
            {
                String pomocny = nicky.get(0);
                String pomocny2 = nicky.get(1);
                nicky.clear();
                nicky.add(pomocny2);
                nicky.add(pomocny);
            }
            Server.LOGGER.info("Server začíná vytvářet hru");
            instance = new InstanceHry(nicky.get(0),nicky.get(1),this);
            Thread.sleep(1000);
            Server.LOGGER.info("Server začal hru a dává vědět kdo je aktuální hráč");
            vysilac.PosliZpravu(instance.getAktualniHrac(),"zacatek","");
            
        } 
        catch (IOException ex) 
        {
            Server.LOGGER.error("Lobby: " + ex.toString());
        } catch (InterruptedException ex) {
            Server.LOGGER.error("Lobby: " + ex.toString());
        }
   }
    
    
    
}
