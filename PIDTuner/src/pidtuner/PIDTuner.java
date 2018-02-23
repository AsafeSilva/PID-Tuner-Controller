/*
 * Interface for PID tuning
 * Created by Asafe Silva (Téc. Mecatrônica, Eng. Mecânica)
 * Last update: 02/06/2017
 */

package pidtuner;

import com.fazecast.jSerialComm.SerialPort;
import com.fazecast.jSerialComm.SerialPortDataListener;
import com.fazecast.jSerialComm.SerialPortEvent;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import javax.swing.JLabel;
import org.jfree.data.xy.XYSeries;

/**
 * @author Asafe
 * @data 03/06/2017
 */
public class PIDTuner implements SerialPortDataListener{
    
    private SerialPort serialPort;
    private InputStream inputStream;
    private OutputStream outputStream;
    
    private JLabel inputLabel, outputLabel, setPointLabel;
    
    private XYSeries inputChart, setPointChart, outputChart;
    
    private String inputSerial = "", outputSerial = "";
    private int checkValue = -1;
    private long xAxis = 0;
    
    
    public PIDTuner(XYSeries inputChart, XYSeries setPointChart, XYSeries outputChart){  
        this.inputChart     = inputChart;
        this.setPointChart  = setPointChart;
        this.outputChart    = outputChart;
    }

    @Override
    public int getListeningEvents() {
        return SerialPort.LISTENING_EVENT_DATA_AVAILABLE;
    }

    @Override
    public void serialEvent(SerialPortEvent event) {
        if (event.getEventType() != SerialPort.LISTENING_EVENT_DATA_AVAILABLE)
            return;

        if(serialPort.bytesAvailable() < 1) return;
        
        try {
            byte[] data = new byte[serialPort.bytesAvailable()];
            inputStream.read(data);
            
            for(int i = 0; i < data.length; i++){
                char inChar = (char) data[i];
                
                if(inChar == '$'){
                    
                    try{
                        float pidInput = Float.parseFloat(inputSerial);
                        inputChart.add(xAxis, pidInput);
                        inputLabel.setText(inputSerial);
                    } catch(NumberFormatException e){
                        // System.out.println("ERROR Format Number");
                    }
                    
                    try{
                        float pidOutput = Float.parseFloat(outputSerial);
                        outputChart.add(xAxis, pidOutput);
                        outputLabel.setText(outputSerial);
                    } catch(NumberFormatException e){
                        // System.out.println("ERROR Format Number");
                    }
                    
                    try{
                        setPointChart.add(xAxis, Float.parseFloat(setPointLabel.getText()));
                    } catch(NumberFormatException e){
                        // System.out.println("ERROR Format Number");
                    }
                    
                    xAxis++;
                    
                    inputSerial = "";
                    outputSerial = "";
                    checkValue = -1;
                }else{
                    if(inChar == 'I')      checkValue = 0;
                    else if(inChar == 'O') checkValue = 1;
                    else{
                        if(checkValue == 0)     inputSerial  += inChar;
                        else if(checkValue == 1)outputSerial += inChar;
                    }
                }
            }
            
        } catch (IOException e) {
        } 
    }

    public void setSerialPort(SerialPort commPort) {
        serialPort = commPort;
        inputStream = serialPort.getInputStream();
        outputStream = serialPort.getOutputStream();
    }
    
    public void addLabels(JLabel inputLabel, JLabel outputLabel, JLabel setPointLabel){
        this.inputLabel = inputLabel;
        this.outputLabel = outputLabel;
        this.setPointLabel = setPointLabel;
    }

    public synchronized boolean writeData(String data) {
        try {
            outputStream.write(data.getBytes());
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public synchronized boolean writeData(byte data) {
        try {
            outputStream.write(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

    public synchronized boolean writeData(int data) {
        try {
            outputStream.write(data);
            return true;
        } catch (Exception e) {
            return false;
        }
    }

}
