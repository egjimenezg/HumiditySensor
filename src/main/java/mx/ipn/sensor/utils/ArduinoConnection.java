package mx.ipn.sensor.utils;

import java.io.InputStream;
import java.io.OutputStream;
import java.util.Scanner;
import java.util.Enumeration;
import gnu.io.CommPortIdentifier;
import gnu.io.CommPort;
import gnu.io.SerialPort;
import gnu.io.SerialPortEvent;
import gnu.io.SerialPortEventListener;
import org.jfree.data.time.TimeSeries;
import org.jfree.data.time.Millisecond;

public class ArduinoConnection{
  
  private CommPortIdentifier portId;
  private SerialPort serialPort;
  private OutputStream output;
  private InputStream input;
  private final String PORT_NAME = "/dev/tty.usbmodem1411";
  private final int TIME_OUT = 200;
  private final int DATA_RATE = 9600;
  
  public ArduinoConnection(TimeSeries series){
    findPortId(); 
    connectAndReadFromArduino(series);
  }
  
  public void findPortId(){
    Enumeration portEnum = CommPortIdentifier.getPortIdentifiers();
    while(portEnum.hasMoreElements()){
      CommPortIdentifier currPortId = ((CommPortIdentifier) portEnum.nextElement());
      if(PORT_NAME.equals(currPortId.getName())){
        portId = currPortId;
        return;
      }
    }
  }

  public void connectAndReadFromArduino(TimeSeries series){
    try{
      CommPort puerto = portId.open("Conexión Arduino",2000);
      serialPort = (SerialPort) puerto;
      serialPort.setSerialPortParams(DATA_RATE,SerialPort.DATABITS_8,SerialPort.STOPBITS_1,SerialPort.PARITY_NONE);

      Scanner myScanner = new Scanner(serialPort.getInputStream());
      Millisecond current = new Millisecond();
      int value;
      int counter = 0;
      while(myScanner.hasNextInt()){
        try{
          value = myScanner.nextInt();
          series.add(current,new Double(value));
          current = (Millisecond)current.next(); 
          counter++;
          if(counter == 10000){
            series.clear(); 
            counter = 0;
          }
        }
        catch(Exception e){
          e.printStackTrace();
        }
      }
      
    } 
    catch(Exception e){
      e.printStackTrace(); 
    }
  }

  
}
