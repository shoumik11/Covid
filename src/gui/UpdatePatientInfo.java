
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
import utils.Utils;

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

public class UpdatePatientInfo extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField txtLastName;
	private JTextField txtFirstName;
	private JTextField textField;
	private JLabel lblError;
	private JDatePickerImpl datePicker1;
	/**
	 * Create the dialog.
	 */
	public UpdatePatientInfo() {
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

		JLabel lblDateLabel = new JLabel("Last Test date:");
		lblDateLabel.setHorizontalAlignment(SwingConstants.TRAILING);
		lblDateLabel.setBounds(10, 194, 145, 23);
		contentPanel.add(lblDateLabel);
		datePicker1 = new JDatePickerImpl(new JDatePanelImpl(model1, p), new DateComponentFormatter());
		datePicker1.setBounds(165, 194, 202, 23);

		contentPanel.add(datePicker1);

		txtLastName = new JTextField();
		txtLastName.setEditable(false);
		txtLastName.setBounds(165, 132, 269, 20);
		contentPanel.add(txtLastName);
		txtLastName.setColumns(10);

		txtFirstName = new JTextField();
		txtFirstName.setEditable(false);
		txtFirstName.setColumns(10);
		txtFirstName.setBounds(165, 163, 269, 20);
		contentPanel.add(txtFirstName);

		textField = new JTextField();
		textField.setBounds(165, 101, 170, 20);
		contentPanel.add(textField);
		textField.setColumns(10);

		JLabel lblLastName = new JLabel("Last Name:");
		lblLastName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblLastName.setBounds(10, 135, 145, 14);
		contentPanel.add(lblLastName);

		JLabel lblFirstName = new JLabel("First Name:");
		lblFirstName.setHorizontalAlignment(SwingConstants.TRAILING);
		lblFirstName.setBounds(10, 166, 145, 14);
		contentPanel.add(lblFirstName);

		JLabel lblId = new JLabel("Enter ID:");
		lblId.setHorizontalAlignment(SwingConstants.TRAILING);
		lblId.setBounds(10, 104, 145, 14);
		contentPanel.add(lblId);

		JLabel lblMainText = new JLabel("Update Patient Information");
		lblMainText.setFont(new Font("Tahoma", Font.PLAIN, 20));
		lblMainText.setHorizontalAlignment(SwingConstants.CENTER);
		lblMainText.setBounds(0, 11, 444, 44);
		contentPanel.add(lblMainText);

		lblError = new JLabel("");
		lblError.setForeground(Color.RED);
		lblError.setHorizontalAlignment(SwingConstants.CENTER);
		lblError.setBounds(10, 66, 424, 14);
		contentPanel.add(lblError);

		JButton btnNewButton = new JButton("Search");
		btnNewButton.addActionListener(new ActionListener() {
			public void actionPerformed(ActionEvent e) {
				try {
					Patient p = Query.getPatientInfoByID(Integer.parseInt(textField.getText().trim()));
					if(p != null)
					{
						lblError.setText("");
						String [] names = p.getName().split(" ");
						if(names.length == 1)
							txtLastName.setText(names[0]);
						if(names.length == 2)
						{
							txtFirstName.setText(names[0]);
							txtLastName.setText(names[1]);
						}
						String [] dateParts = (Utils.getDateStringFromEpoch(p.getLastTestDate()).split("-"));
						datePicker1.getModel().setDate(Integer.parseInt(dateParts[0]), 
								Integer.parseInt(dateParts[1]) - 1, Integer.parseInt(dateParts[2]));
						txtFirstName.setEditable(true);
						txtLastName.setEditable(true);
						textField.setEnabled(false);
					}
					else
					{
						lblError.setText("Patient not found, enter correct ID!");
						txtFirstName.setEditable(false);
						txtLastName.setEditable(false);
						textField.setEnabled(true);
					}
					contentPanel.revalidate();
				} catch (Exception e1) {
					// TODO Auto-generated catch block
					e1.printStackTrace();
				}

			}
		});
		btnNewButton.setBounds(345, 100, 89, 23);
		contentPanel.add(btnNewButton);
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
								Patient p = new Patient(Integer.parseInt(textField.getText()),
										txtFirstName.getText().trim() + " " +txtLastName.getText().trim());
								p.setLastTestDate((int)(d.getTime()/1000));
								Query.updatePatientData(p);
								ConfirmPopup popup = new ConfirmPopup("Updated successfully", Color.BLACK);
								popup.setModalityType(ModalityType.APPLICATION_MODAL);
								popup.setDefaultCloseOperation(JDialog.DISPOSE_ON_CLOSE);
								popup.setVisible(true);
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
							UpdatePatientInfo.this.dispose();
						}
					}
				});
				okButton.setActionCommand("OK");
				buttonPane.add(okButton);
				getRootPane().setDefaultButton(btnNewButton);
			}
			{
				JButton cancelButton = new JButton("Cancel");
				cancelButton.addActionListener(new ActionListener() {
					public void actionPerformed(ActionEvent e) {
						UpdatePatientInfo.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		this.setLocationRelativeTo(null);
	}
}
