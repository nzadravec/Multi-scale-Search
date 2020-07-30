package hr.fer.zemris.zavrad;

import java.awt.BorderLayout;
import java.awt.Color;

import javax.swing.JPanel;

import org.jfree.chart.ChartFactory;
import org.jfree.chart.ChartPanel;
import org.jfree.chart.JFreeChart;
import org.jfree.chart.plot.PlotOrientation;
import org.jfree.chart.plot.XYPlot;
import org.jfree.ui.ApplicationFrame;
import org.jfree.ui.RefineryUtilities;

public class GUI extends ApplicationFrame {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	/** The plot. */
    private XYPlot plot;

	public GUI(String title) {
		super(title);

		final JFreeChart chart = ChartFactory.createXYLineChart(null, "timestep", "fitness", null,
				PlotOrientation.VERTICAL, true, true, false);

		chart.setBackgroundPaint(Color.white);
		
		this.plot = chart.getXYPlot();
        this.plot.setBackgroundPaint(Color.lightGray);
        this.plot.setDomainGridlinePaint(Color.white);
        this.plot.setRangeGridlinePaint(Color.white);

        final JPanel content = new JPanel(new BorderLayout());
        
        final ChartPanel chartPanel = new ChartPanel(chart);
        content.add(chartPanel);
        
        chartPanel.setPreferredSize(new java.awt.Dimension(500, 270));
        setContentPane(content);
        
	}
	
	public static void main(String[] args) {
		final GUI gui = new GUI(null);
		gui.pack();
        RefineryUtilities.centerFrameOnScreen(gui);
        gui.setVisible(true);
	}

}
