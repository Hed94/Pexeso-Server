
package testserver;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;

import hra.Server;

public class Vysilac 
{
    private Socket socket1;
    private Socket socket2;
    private ObjectOutputStream output;
    private ObjectOutputStream output2;
    private Lobby lobby;
    
    Vysilac(Socket socket1,Socket socket2,Lobby lobby) throws IOException
    {
        this.socket1 = socket1;
        this.socket2 = socket2;
        output = new ObjectOutputStream(socket1.getOutputStream());
        output2 = new ObjectOutputStream(socket2.getOutputStream());
        this.lobby = lobby;
    }

   
    
    public synchronized void PosliZpravu(String a,String b,String c) throws IOException
    {
        if(b.equals("zacatek")) 
        {
        Server.LOGGER.info("Server začal hru");
        hra.Message message = new hra.Message(a,b,c);
        output.writeObject(message);
        output2.writeObject(message);
        }
        
        if(b.equals("otoc")) 
        {
        Server.LOGGER.info("Otáčí se karta "+c);
        hra.Message message = new hra.Message(a,b,c);
        output.writeObject(message);
        output2.writeObject(message);
        }
        
        if(b.equals("zmenaTahu")) 
        {
        Server.LOGGER.info("Na tahu je nyní hráč "+c);
        hra.Message message = new hra.Message(a,b,c);
        output.writeObject(message);
        output2.writeObject(message);
        }
        
        if(b.equals("body")) 
        {
        Server.LOGGER.info("bod získal hráč "+a);
        hra.Message message = new hra.Message(a,b,c);
        output.writeObject(message);
        output2.writeObject(message);
        }
        
        if(b.equals("konec")) 
        {
        Server.LOGGER.info("Hra končí, server posílá oboum hráčům že hra skončila a kdo vyhrál ");
        hra.Message message = new hra.Message(a,b,c);
        output.writeObject(message);
        output2.writeObject(message);
        }
    }
    
    public synchronized void PosliChybu(String nick,String b,String c) throws IOException
    {
        Server.LOGGER.info("Odesílá se chyba hráči "+nick+" - "+c);
        if(nick.equals(lobby.nicky.get(0)))
        {
         hra.Message message = new hra.Message(nick,b,c);
         output.writeObject(message);
        }
        if(nick.equals(lobby.nicky.get(1)))
        {
         hra.Message message = new hra.Message(nick,b,c); 
         output2.writeObject(message);
        }  
    }
    
    public synchronized void PosliProtihrace(String nick) throws IOException
    {
        String protihrac = "";
        if(nick.equals(lobby.nicky.get(0)))
        {
         protihrac = lobby.nicky.get(1);
         hra.Message message = new hra.Message("2", "protihrac", protihrac);
         Server.LOGGER.info("Hráč " + nick +" hraje proti "+lobby.nicky.get(1));
         output.writeObject(message);
        }
        if(nick.equals(lobby.nicky.get(1)))
        {
         protihrac = lobby.nicky.get(0);
         hra.Message message = new hra.Message("1", "protihrac", protihrac); 
         Server.LOGGER.info("Hráč " + nick +" hraje proti "+lobby.nicky.get(0));
         output2.writeObject(message);
        }  
    }
    
    
    
    
}