package utilities;

import java.io.File;

import javax.swing.JFileChooser;
import javax.swing.JFrame;
import javax.swing.JOptionPane;
import javax.swing.filechooser.FileNameExtensionFilter;

import org.json.simple.JSONObject;

import utilities.ReadCfdi;

public class Main {

	public static void main(String[] args) {
		JFrame  window = new JFrame();
		JFileChooser cfdi = new JFileChooser();
        cfdi.setFileFilter(new FileNameExtensionFilter("*.xml","xml"));
        int resp=cfdi.showOpenDialog(window);
         if (resp==JFileChooser.APPROVE_OPTION) {
        	 File dir = cfdi.getSelectedFile();
        	 ReadCfdi xml = new ReadCfdi();
        	 JSONObject objeto = xml.readJson(dir);
        	 System.out.println(objeto.toJSONString());
         } else {
             JOptionPane.showMessageDialog(null,"Ha sido cancelada la lectura del CFDI");
         }

	}

}
