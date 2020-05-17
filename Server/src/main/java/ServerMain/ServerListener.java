package ServerMain;

import DataModelJSON.Data;

import java.io.*;
import java.net.Socket;


public class ServerListener extends Thread{
    private Socket socket;

    public ServerListener(Socket socket){
        this.socket = socket;
    }

    @Override
    public void run() {

        try(Socket socket = this.socket) {

            ObjectInputStream input = new ObjectInputStream(socket.getInputStream());
            ObjectOutputStream output = new ObjectOutputStream(socket.getOutputStream());

            while(true){

                    String clientRequest = (String)input.readObject();

                    if(clientRequest.equals("CONNECT")){
                        System.out.println("Client Request: " + clientRequest);
                        output.writeObject("CONNECTED");

                    }else if(clientRequest.equals("GET_CONFIG")){
                        System.out.println("Client Request: " + clientRequest);
                        output.writeObject(Data.getConfigStringObject());

                    }else if(clientRequest.equals("GET_LEVEL_MODEL_1")){
                        System.out.println("Client Request: " + clientRequest);
                        output.writeObject(Data.getLevelModel1StringObject());

                    }else if(clientRequest.equals("GET_LEVEL_MODEL_2")){
                        System.out.println("Client Request: " + clientRequest);
                        output.writeObject(Data.getLevelModel2StringObject());

                    }else if(clientRequest.equals("GET_LEVEL_MODEL_3")){
                        System.out.println("Client Request: " + clientRequest);
                        output.writeObject(Data.getLevelModel3StringObject());

                    }else if(clientRequest.equals("GET_RESULTS")){
                        System.out.println("Client Request: " + clientRequest);
                        output.writeObject(Data.getResultsStringObject());
                        break;

                    }else if(clientRequest.equals("SEND_NICK_RESULT")){
                        System.out.println("Client Request: " + clientRequest);
                        output.writeObject("WAITING");
                        clientRequest = (String)input.readObject(); // client przyslal odpowiedniego Stringa z wynikiem

                        Data.addResultToServer(clientRequest);

                        try{
                            Data.loadResults(Data.getConfig().getPlayerResultsFileName());
                            Data.readResultsAsString(Data.getConfig().getPlayerResultsFileName());
                        }catch (IOException e){
                            System.out.println("Blad serwera - nie udalo sie zaktualizowac listy wynikow: " + e.getMessage());
                        }

                        break;
                    }

            }


        }catch (IOException | ClassNotFoundException e){
            System.out.println("BLAD SERWERA: Nie udalo sie wczytac/przyjac socketu " + e.getMessage());
        }

    }

}