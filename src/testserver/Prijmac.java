
package testserver;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.Socket;

import hra.Server;


public class Prijmac extends Thread
{
    private Socket socket;
    private Lobby lobby;
    private String nick;
    
    Prijmac (Socket socket,Lobby lobby)
    {
     this.socket = socket;
     this.lobby = lobby;
     this.nick = nick;
    }
    
    @Override
    public void run()
    {
      try
      {
            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            while(!lobby.konec)
            {
            hra.Message returnMessage = (hra.Message)input.readObject();
            zpracujZpravu(returnMessage);
            }
      } 
      catch (IOException ex) 
      {
            Server.LOGGER.error(nick + " " + ex.toString());
      } 
      catch (ClassNotFoundException ex) 
      {
            Server.LOGGER.error(nick + " " + ex.toString());
      } catch (InterruptedException ex) {
            Server.LOGGER.error(nick + " " + ex.toString());
        }
    }
    
    public synchronized void zpracujZpravu(hra.Message message) throws IOException, InterruptedException
    {
        if(message.getOperace().equals("zacatek"))
        {
            this.nick=message.getHrac();
            lobby.nicky.add(message.getHrac());
            Server.LOGGER.info("Hráč " + message.getHrac()+" byl zaregistrován ke hře. ");
        }
        if(message.getOperace().equals("tah"))
        {
            InstanceHry hra = lobby.instance;
            Server.LOGGER.info("Hráč " + message.getHrac()+" klepl na "+message.getHodnota());
            hra.tahni(message);
        }
        if(message.getOperace().equals("protihrac"))
        {
            Server.LOGGER.info("Hráč " + message.getHrac()+ " chce znát svého protihráče ");
            lobby.vysilac.PosliProtihrace(message.getHrac());
        }
    }

    public String getNick() {
        return nick;
    }
    
}