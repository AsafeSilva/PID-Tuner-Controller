/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package utils;

import com.fazecast.jSerialComm.SerialPort;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

/**
 *
 * @author Asafe
 */
public class Serial {

    public static final String TAB = "\t";
    public static final String NEW_LINE = "\n";

    public static final String END = NEW_LINE;

    //   Computer  --->   Microcontroler
    public static final String KP = "P";
    public static final String KI = "I";
    public static final String KD = "D";
    public static final String SP = "S";

    //   Microcontroler  --->   Computer
    public static final String INPUT = "I";
    public static final String OUTPUT = "O";
    
    private SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;

    private Thread threadListener;

    private DataListener dataListener;
    
    private String parameter = "", data = "";
    private boolean enableListener;

    public Serial() {
    }

    public void setSerialPort(SerialPort commPort) {
        serialPort = commPort;
        inputStream = serialPort.getInputStream();
        outputStream = serialPort.getOutputStream();
    }

    public void initListener(DataListener dataListener) {

        this.dataListener = dataListener;
        this.enableListener = true;
        parameter = "";
        data = "";

        threadListener = new Thread(() -> {
            
            while(enableListener){
                
                while (serialPort.bytesAvailable() < 1){  if(!enableListener) break;  }
                if(!enableListener) break;
                
                byte[] readBuffer = new byte[serialPort.bytesAvailable()];
                serialPort.readBytes(readBuffer, readBuffer.length);
                
                for (int i = 0; i < readBuffer.length; i++) {
                    char inChar = (char) readBuffer[i];
                    
                    if (Character.toString(inChar).equals(Serial.END)) {
                        
                        float value = 0;
                        
                        try {
                            value = Float.parseFloat(data);
                        } catch (NumberFormatException e) {
                            System.err.print("[utils.Serial] - ERROR Format Number");
                            System.err.println("\tData: " + data);
                        }
                        
                        if (dataListener != null) {
                            dataListener.onDataListener(parameter, value);
                        }
                        
                        parameter = "";
                        data = "";
                    } else {
                        if (Character.isLetter(inChar)) {
                            parameter += inChar;
                        } else {
                            data += inChar;
                        }
                    }
                }
            }
            
        });

        threadListener.setPriority(Thread.MAX_PRIORITY);
        threadListener.start();
    }

    public synchronized void stopListener() {
        enableListener = false;
        parameter = "";
        data = "";
    }

    public synchronized boolean writeData(String data) {
        try {
            outputStream.write(data.getBytes());
            return true;
        } catch (IOException|NullPointerException e) {
            return false;
        }
    }

    public synchronized boolean writeData(byte data) {
        try {
            outputStream.write(data);
            return true;
        } catch (IOException|NullPointerException e) {
            return false;
        }
    }

    public synchronized boolean writeData(int data) {
        try {
            outputStream.write(data);
            return true;
        } catch (IOException|NullPointerException e) {
            return false;
        }
    }

    public interface DataListener {

        public void onDataListener(String parameter, float value);
    }

}
