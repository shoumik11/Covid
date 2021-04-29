
package gui;

import java.awt.FlowLayout;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.SwingUtilities;
import javax.swing.border.EmptyBorder;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import mlcore.Simulator;
import report.LineChart;
import report.Report;

import javax.swing.JLabel;
import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JCheckBox;

public class DateRangeSelector extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JPanel buttonPane;
	private boolean isCalledForChart = false;
	private JCheckBox chkbox;
	public void setLaunchMode(boolean forChart)
	{
		isCalledForChart = forChart;
	}

	/**
	 * Launch the application.
	 */
	//	public static void main(String[] args) {
	//		try {
	//			DateRangeSelector dialog = new DateRangeSelector();
	//			dialog.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
	//			dialog.setVisible(true);
	//		} catch (Exception e) {
	//			e.printStackTrace();
	//		}
	//	}

	/**
	 * Create the dialog.
	 */
	public DateRangeSelector() {
		setResizable(false);
		isCalledForChart = false;
		setBounds(100, 100, 340, 203);
		contentPanel.setBounds(0, 0, 307, 89);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		Properties p = new Properties();
		p.put("text.today", "today");
		p.put("text.month", "month");
		p.put("text.year", "year");

		UtilDateModel model1 = new UtilDateModel();
		model1.setDate(2020, 0, 23); //Jan 23, 2020
		model1.setSelected(true);
		contentPanel.setLayout(null);

		JLabel lblNewLabel = new JLabel("From date: ");
		lblNewLabel.setBounds(28, 14, 56, 23);
		contentPanel.add(lblNewLabel);
		JDatePickerImpl datePicker1 = new JDatePickerImpl(new JDatePanelImpl(model1, p), new DateComponentFormatter());
		datePicker1.setBounds(93, 14, 202, 23);

		contentPanel.add(datePicker1);


		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH); // Note: zero based!
		int day = now.get(Calendar.DAY_OF_MONTH);


		UtilDateModel model2 = new UtilDateModel();
		model2.setDate(year+1, month, day);
		
		//model2.setDate(year, month, day);
		model2.setSelected(true);
		JLabel lblNewLabel_1 = new JLabel("To date: ");
		lblNewLabel_1.setBounds(38, 48, 44, 19);
		contentPanel.add(lblNewLabel_1);
		JDatePickerImpl datePicker2 = new JDatePickerImpl(new JDatePanelImpl(model2, p), new DateComponentFormatter());
		datePicker2.setBounds(93, 44, 202, 23);

		contentPanel.add(datePicker2);

		buttonPane = new JPanel();
		buttonPane.setBounds(10, 127, 314, 36);
		buttonPane.setLayout(new FlowLayout(FlowLayout.TRAILING));


		JButton okButton = new JButton("OK");
		okButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent arg0) {
				Date fromDate = (Date) datePicker1.getModel().getValue();
				Date toDate = (Date) datePicker2.getModel().getValue();
				Simulator.setModelOnly(chkbox.isSelected());
				if(isCalledForChart)
				{
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								LineChart.draw(fromDate, toDate);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							DateRangeSelector.this.dispose();
						}
					});					
				}
				else
				{
					SwingUtilities.invokeLater(new Runnable() {

						@Override
						public void run() {
							// TODO Auto-generated method stub
							try {
								new Report().displaySimulatedReportBetweenDates(fromDate, toDate);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							DateRangeSelector.this.dispose();
						}
					});
				}
			}
		});
		okButton.setActionCommand("OK");
		buttonPane.add(okButton);
		getRootPane().setDefaultButton(okButton);

		JButton cancelButton = new JButton("Cancel");
		cancelButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				DateRangeSelector.this.dispose();
			}
		});
		cancelButton.setActionCommand("Cancel");
		buttonPane.add(cancelButton);
		getContentPane().setLayout(null);

		getContentPane().add(contentPanel);
		getContentPane().add(buttonPane);
		
		chkbox = new JCheckBox("Apply only data model");
		chkbox.setBounds(93, 96, 214, 23);
		getContentPane().add(chkbox);
		this.setLocationRelativeTo(null);
	}
}
