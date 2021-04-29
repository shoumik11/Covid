
package gui;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import org.jdatepicker.impl.DateComponentFormatter;
import org.jdatepicker.impl.JDatePanelImpl;
import org.jdatepicker.impl.JDatePickerImpl;
import org.jdatepicker.impl.UtilDateModel;

import database.Query;
import object.Patient;

import java.awt.event.ActionListener;
import java.util.Calendar;
import java.util.Date;
import java.util.Properties;
import java.awt.event.ActionEvent;
import javax.swing.JTextField;
import javax.swing.JLabel;
import javax.swing.SwingConstants;
import java.awt.Font;
import java.awt.Color;

public class PatientInfoView extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtLastName;
	private JTextField txtFirstName;
	private JTextField textField;
	private JLabel lblError;
	/**
	 * Create the dialog.
	 */
	public PatientInfoView() {
		setResizable(false);
		setBounds(100, 100, 450, 300);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 444, 228);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		
		Properties p = new Properties();
		p.put("text.today", "today");
		p.put("text.month", "month");
		p.put("text.year", "year");

		Calendar now = Calendar.getInstance();
		now.setTime(new Date());
		int year = now.get(Calendar.YEAR);
		int month = now.get(Calendar.MONTH); // Note: zero based!
		int day = now.get(Calendar.DAY_OF_MONTH);

		UtilDateModel model1 = new UtilDateModel();
		model1.setDate(year, month, day);
		model1.setSelected(true);
		
		JLabel lblDateLabel = new JLabel("Test date:");
		lblDateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDateLabel.setBounds(10, 163, 145, 23);
		contentPanel.add(lblDateLabel);
		JDatePickerImpl datePicker1 = new JDatePickerImpl(new JDatePanelImpl(model1, p), new DateComponentFormatter());
		datePicker1.setBounds(165, 163, 202, 23);

		contentPanel.add(datePicker1);
		
		txtLastName = new JTextField();
		txtLastName.setBounds(165, 101, 269, 20);
		contentPanel.add(txtLastName);
		txtLastName.setColumns(10);
		
		txtFirstName = new JTextField();
		txtFirstName.setColumns(10);
		txtFirstName.setBounds(165, 132, 269, 20);
		contentPanel.add(txtFirstName);
		
		textField = new JTextField();
		textField.setEditable(false);
		textField.setBounds(165, 197, 269, 20);
		contentPanel.add(textField);
		textField.setColumns(10);
		try {
			textField.setText(""+Query.getNextIDForPatient());
		} catch (Exception e1) {
			// TODO Auto-generated catch block
			e1.printStackTrace();
		}
		
		JLabel lblLastName = new JLabel("Last Name:");
		lblLastName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblLastName.setBounds(10, 104, 145, 14);
		contentPanel.add(lblLastName);
		
		JLabel lblFirstName = new JLabel("First Name:");
		lblFirstName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblFirstName.setBounds(10, 135, 145, 14);
		contentPanel.add(lblFirstName);
		
		JLabel lblId = new JLabel("ID:");
		lblId.setHorizontalAlignment(SwingConstants.TRAILING);
		lblId.setBounds(10, 200, 145, 14);
		contentPanel.add(lblId);
		
		JLabel lblMainText = new JLabel("Enter Patient Information");
		lblMainText.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMainText.setHorizontalAlignment(SwingConstants.CENTER);
		lblMainText.setBounds(0, 11, 444, 44);
		contentPanel.add(lblMainText);
		
		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setBounds(10, 66, 424, 14);
		contentPanel.add(lblError);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(0, 228, 444, 33);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			getContentPane().add(buttonPane);
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						if(txtFirstName.getText().trim().isEmpty() || txtLastName.getText().trim().isEmpty())
						{
							lblError.setText("Please enter First name & Last name");
						}
						else
						{
							lblError.setText("");
							Date d = (Date) datePicker1.getModel().getValue();
							try {
								Query.insertPatientData(new Patient(Integer.parseInt(textField.getText()),
										txtFirstName.getText().trim() + " " +txtLastName.getText().trim(), 
										(int)(d.getTime()/1000)));
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							PatientInfoView.this.dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(okButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						PatientInfoView.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		this.setLocationRelativeTo(null);
	}
}
