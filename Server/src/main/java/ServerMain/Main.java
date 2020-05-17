package ServerMain;

import ConfigClasses.Player;
import DataModelJSON.Data;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.URISyntaxException;
import java.util.LinkedHashSet;

public class Main {
    private final static String mainConfigFileName = "configFile.json";

    public static void main(String[] args) throws Exception {

        try(ServerSocket serverSocket = new ServerSocket(14623)){
            while(true){
                new ServerListener(serverSocket.accept()).start();   // funkcja accept blokuje dzialanie petli do momentu przyjscia informacji od klienta
            }

        }catch (IOException e){
            System.out.println("Server exception: " + e.getMessage());
        }

    }


    static {
        Data.receiveString(mainConfigFileName);
        Data.receiveString(Data.getConfig().getLevelModel1FileName());
        Data.receiveString(Data.getConfig().getLevelModel2FileName());
        Data.receiveString(Data.getConfig().getLevelModel3FileName());

        try{
            Data.loadResults(Data.getConfig().getPlayerResultsFileName());
            Data.readResultsAsString(Data.getConfig().getPlayerResultsFileName());
        }catch (IOException e){
            System.out.println("Blad serwera - nie udalo sie wczytac plikow: " + e.getMessage());
        }
    }



}
