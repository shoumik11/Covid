
package gui;
import java.awt.BorderLayout;
import javax.swing.JFrame;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.knowm.xchart.XChartPanel;
import org.knowm.xchart.internal.chartpart.Chart;

public class ChartViewer extends JFrame {
	/**
	 * 
	 */
	private static final long serialVersionUID = -5765306970973799683L;
	private JPanel contentPane;
	public ChartViewer()
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1024, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
	}
	public ChartViewer(@SuppressWarnings("rawtypes") Chart chart)
	{
		setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
		setBounds(100, 100, 1024, 720);
		contentPane = new JPanel();
		contentPane.setBorder(new EmptyBorder(5, 5, 5, 5));
		contentPane.setLayout(new BorderLayout(0, 0));
		setContentPane(contentPane);
		this.setVisible(true);
		this.createChart(chart);
	}

	public void createChart(@SuppressWarnings("rawtypes") Chart chart) {
		@SuppressWarnings("rawtypes")
		JPanel panelChart = new XChartPanel<Chart>(chart);
		contentPane.add(panelChart);
		contentPane.validate();
	}
}
