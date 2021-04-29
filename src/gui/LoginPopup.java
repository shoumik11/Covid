
package gui;

import java.awt.FlowLayout;

import javax.swing.JButton;
import javax.swing.JDialog;
import javax.swing.JPanel;
import javax.swing.border.EmptyBorder;

import database.Query;
import utils.Utils;

import java.awt.event.ActionListener;
import java.awt.event.ActionEvent;
import javax.swing.JLabel;
import java.awt.Font;
import javax.swing.JTextField;
import javax.swing.JPasswordField;
import java.awt.Color;
import javax.swing.SwingConstants;

public class LoginPopup extends JDialog {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	private final JPanel contentPanel = new JPanel();
	private JTextField textField;
	private JPasswordField passwordField;
	private JPasswordField retypePasswordField;
	private JLabel lblPass, lblRetypePass, lblError;
	private JLabel lblMainText;
	private static boolean isLaunchingToUpdatePassword = false;

	public void launchToUpdatePassword()
	{
		lblMainText.setText("Update password");
		lblPass.setText("Enter current password:");
		retypePasswordField.setVisible(true);
		lblRetypePass.setVisible(true);
		LoginPopup.isLaunchingToUpdatePassword = true;
		contentPanel.revalidate();
	}

	/**
	 * Create the dialog.
	 */
	public LoginPopup() {
		setResizable(false);
		setBounds(100, 100, 367, 234);
		getContentPane().setLayout(null);
		contentPanel.setBounds(0, 0, 361, 205);
		contentPanel.setBorder(new EmptyBorder(5, 5, 5, 5));
		getContentPane().add(contentPanel);
		contentPanel.setLayout(null);
		{
			lblMainText = new JLabel("Please login to update");
			lblMainText.setHorizontalAlignment(SwingConstants.CENTER);
			lblMainText.setBounds(0, 10, 358, 29);
			lblMainText.setFont(new Font("Tahoma", Font.PLAIN, 24));
			contentPanel.add(lblMainText);
		}
		{
			textField = new JTextField();
			textField.setBounds(148, 75, 158, 20);
			contentPanel.add(textField);
			textField.setColumns(10);
		}

		JLabel lblNewLabel_1 = new JLabel("Username:");
		lblNewLabel_1.setHorizontalAlignment(SwingConstants.TRAILING);
		lblNewLabel_1.setBounds(0, 78, 138, 14);
		contentPanel.add(lblNewLabel_1);

		lblPass = new JLabel("Password:");
		lblPass.setHorizontalAlignment(SwingConstants.TRAILING);
		lblPass.setBounds(0, 115, 138, 14);
		contentPanel.add(lblPass);

		passwordField = new JPasswordField();
		passwordField.setBounds(148, 112, 158, 20);
		contentPanel.add(passwordField);
		{
			JPanel buttonPane = new JPanel();
			buttonPane.setBounds(10, 169, 348, 33);
			contentPanel.add(buttonPane);
			buttonPane.setLayout(new FlowLayout(FlowLayout.RIGHT));
			{
				JButton okButton = new JButton("OK");
				okButton.addActionListener(new ActionListener() {
					@SuppressWarnings("deprecation")
					public void actionPerformed(ActionEvent e) {
						if(isLaunchingToUpdatePassword)
						{
							String user = textField.getText();
							String hash = Utils.encryptString(passwordField.getText());
							String newHash = Utils.encryptString(retypePasswordField.getText().trim());
							//System.out.println(user +"_"+ passwordField.getText());
							try {
								if(Query.isValidUserCredential(user, hash) && !retypePasswordField.getText().trim().isEmpty())
								{
									lblError.setText("");
									Query.updateUserCredential(user, newHash);
									isLaunchingToUpdatePassword = false;
									LoginPopup.this.dispose();
									MainWindow.getInstance().setLoginState(true);
								}
								else
								{
									lblError.setText("user/pass mismatch, try again!");
									textField.setText("");
									passwordField.setText("");
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
						}
						else
						{
							String user = textField.getText();
							String hash = Utils.encryptString(passwordField.getText());
							//System.out.println(user +"_"+ passwordField.getText());
							try {
								if(Query.isValidUserCredential(user, hash))
								{
									lblError.setText("");
									isLaunchingToUpdatePassword = false;
									LoginPopup.this.dispose();
									MainWindow.getInstance().setLoginState(true);
								}
								else
								{
									lblError.setText("user/pass mismatch, try again!");
									textField.setText("");
									passwordField.setText("");
								}
							} catch (Exception e1) {
								// TODO Auto-generated catch block
								e1.printStackTrace();
							}
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
						isLaunchingToUpdatePassword = false;
						LoginPopup.this.dispose();
					}
				});
				cancelButton.setActionCommand("Cancel");
				buttonPane.add(cancelButton);
			}
		}
		{
			lblError = new JLabel("");
			lblError.setHorizontalAlignment(SwingConstants.CENTER);
			lblError.setForeground(Color.RED);
			lblError.setBounds(10, 50, 348, 14);
			contentPanel.add(lblError);
		}
		{
			lblRetypePass = new JLabel("Enter new password:");
			lblRetypePass.setHorizontalAlignment(SwingConstants.TRAILING);
			lblRetypePass.setBounds(0, 138, 138, 14);
			contentPanel.add(lblRetypePass);
		}
		{
			retypePasswordField = new JPasswordField();
			retypePasswordField.setBounds(148, 138, 158, 20);
			contentPanel.add(retypePasswordField);
		}
		if(isLaunchingToUpdatePassword)
		{
			retypePasswordField.setVisible(true);
			lblRetypePass.setVisible(true);
		}
		else
		{
			retypePasswordField.setVisible(false);
			lblRetypePass.setVisible(false);
		}
		this.setLocationRelativeTo(null);
		contentPanel.revalidate();
	}
}
