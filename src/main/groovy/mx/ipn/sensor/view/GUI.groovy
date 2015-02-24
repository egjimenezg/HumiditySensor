package mx.ipn.sensor.view
import mx.ipn.sensor.utils.ArduinoConnection
import groovy.swing.SwingBuilder
import javax.swing.WindowConstants as WC
import org.jfree.chart.JFreeChart
import org.jfree.chart.ChartFactory
import org.jfree.chart.ChartPanel
import org.jfree.data.category.DefaultCategoryDataset
import org.jfree.chart.plot.PlotOrientation as Orientation
import org.jfree.data.general.SeriesException
import org.jfree.data.xy.XYDataset;
import org.jfree.data.time.TimeSeries
import org.jfree.data.time.TimeSeriesCollection 

class GUI{
  ArduinoConnection arduinoConnection
  TimeSeries series
  XYDataset dataSet
  JFreeChart chart

  def GUI(){
    initComponents(); 
    arduinoConnection = new ArduinoConnection(series);
  }

  def initComponents(){    
    def labels = ["Sensor de humedad", "", "HR%"]
    def options = [true, true, true]
    series = new TimeSeries("Sensor Data")
    dataSet = new TimeSeriesCollection(series)
    chart = ChartFactory.createTimeSeriesChart(*labels, dataSet, *options)
    def swing = new SwingBuilder()
    def frame = swing.frame(title:"Sensor de humedad",
                            defaultCloseOperation:WC.EXIT_ON_CLOSE){
      panel(id:'canvas'){ widget(new ChartPanel(chart))}
    } 

    frame.pack()
    frame.show()
  }

}
