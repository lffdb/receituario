package br.com.luis.receituario;

import javax.swing.UIManager;
import javax.swing.UnsupportedLookAndFeelException;
import javax.swing.UIManager.LookAndFeelInfo;

public class LookAndFeel {
	
	public void setLookAndFeel(int tipo){
		String visual="";
		
		switch (tipo) {
		case 1:visual = "Windows"; break;
		case 2:visual = "Nimbus"; break;
		case 3:visual = "Metal"; break;
		default:
			break;
		}
		
	    for (LookAndFeelInfo info : UIManager.getInstalledLookAndFeels()) {
	        if (visual.equals(info.getName())) {
	            try {
					UIManager.setLookAndFeel(info.getClassName());
				} catch (ClassNotFoundException | InstantiationException | IllegalAccessException| UnsupportedLookAndFeelException e) {
					e.printStackTrace();
				}
	            break;
	        }
	    }
	}
}
